package com.simplespider.worker;

import com.simplespider.downloader.Downloader;
import com.simplespider.downloader.HttpClientDownloader;
import com.simplespider.model.Page;
import com.simplespider.model.Request;
import com.simplespider.model.Site;
import com.simplespider.processor.PageProcessor;
import com.simplespider.scheduler.QueueScheduler;
import com.simplespider.scheduler.Scheduler;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.log4j.Logger;

/**
 * @author Jianshu
 * @time 20201102
 */
public class Spider implements Runnable{
    protected Logger logger=Logger.getLogger(Spider.class);
    //初始化调度器
    protected Scheduler scheduler=new QueueScheduler();
    //下载器，在initComponent函数中初始化
    protected Downloader downloader;
    //页面处理器
    protected PageProcessor pageProcessor;
    //线程池
    protected ThreadPoolExecutor threadPoolExecutor;
    //最大爬取线程数量
    protected Integer threadNum=1;
    //开始爬取URL队列
    protected List<Request> startRequest;
    //新页面加入重入锁
    private ReentrantLock newUrlLock=new ReentrantLock();
    private Condition newUrlCondition=newUrlLock.newCondition();
    //线程池为空等待时间
    private int emptySleepTime=30000;
    //爬取总站点信息
    protected Site site;
    //线程池运行状态
    protected final static int STAT_INIT=0;
    protected final static int STAT_RUNNING=1;
    protected final static int STAT_STOPPED=2;
    //线程池状态原子判断器
    protected AtomicInteger stat=new AtomicInteger(STAT_INIT);

    /**
     * 初始化爬虫Processor
     * @param pageProcessor
     */
    public Spider(PageProcessor pageProcessor){
        this.pageProcessor=pageProcessor;
        this.site=pageProcessor.getSite();
    }

    /**
     * 加入起始爬取url
     * @param url
     * @return
     */
    public Spider addUrl(String url){
        addRequest(new Request(url));
        signalNewUrl();
        return this;
    }

    /**
     * 设置下载器类型
     * @param downloader
     * @return
     */
    public Spider setDownloader(Downloader downloader){
        this.downloader = downloader;
        return this;
    }

    /**
     * 初始化下载器和线程池
     */
    protected void initComponent(){
        if(downloader==null){
            this.downloader=new HttpClientDownloader();
        }
        if(threadPoolExecutor==null||threadPoolExecutor.isShutdown()){
            threadPoolExecutor=new ThreadPoolExecutor(
                    threadNum,threadNum,0L,TimeUnit.MILLISECONDS,new LinkedBlockingQueue<Runnable>());
        }
    }

    @Override
    public void run() {
        stat.set(STAT_RUNNING);
        initComponent();
        logger.info("Spider started!");
        while(!Thread.currentThread().isInterrupted()&&stat.get()==STAT_RUNNING){
            final Request request=scheduler.poll();
            if(request==null){
                if (threadPoolExecutor.getActiveCount()==0) {
                    break;
                }
                //队列为空时等待一会以防有新URL加入
                waitNewUrl();
            }
            else{
                threadPoolExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            processRequest(request);
                            logger.info("process request "+request+" success");
                        } catch (Exception e){
                            logger.error("process request "+request+" error",e);
                        }finally {
                            signalNewUrl();
                        }
                    }
                });
            }
        }
        stat.set(STAT_STOPPED);
        close();
        logger.info("Spider closed!");
    }

    /**
     * 下载页面
     * @param request
     */
    private void processRequest(Request request){
        Page page=downloader.download(request,site);
        onDownloadSuccess(request,page);
    }

    /**
     * 下载成功的页面进行处理
     * @param request
     * @param page
     */
    private void onDownloadSuccess(Request request, Page page){
        pageProcessor.process(page);
        extractAndAddRequests(page);
    }

    /**
     * 将处理后的页面的预爬取url加入队列
     * @param page
     */
    protected void extractAndAddRequests(Page page){
        if(!page.getTargetRequests().isEmpty()){
            for (Request request:page.getTargetRequests()){
                addRequest(request);
            }
        }
    }

    public void close() {
        threadPoolExecutor.shutdown();
    }
    /**
     * 将Request类型加入队列
     * @param request
     */
    private void addRequest(Request request){
        scheduler.push(request);
    }

    private void waitNewUrl(){
        newUrlLock.lock();
        try{
            newUrlCondition.await(emptySleepTime, TimeUnit.MILLISECONDS);
        }catch(InterruptedException e){
            System.out.println();
        }finally {
            newUrlLock.unlock();
        }
    }

    private void signalNewUrl(){
        try{
            newUrlLock.lock();
            newUrlCondition.signalAll();
        }
        finally {
            newUrlLock.unlock();
        }
    }
}
