package com.simplespider.worker;

import com.simplespider.downloader.Downloader;
import com.simplespider.downloader.HttpClientDownloader;
import com.simplespider.model.Page;
import com.simplespider.model.Request;
import com.simplespider.model.Site;
import com.simplespider.pipeline.ConsolePipeline;
import com.simplespider.pipeline.Pipeline;
import com.simplespider.processor.PageProcessor;
import com.simplespider.scheduler.QueueScheduler;
import com.simplespider.scheduler.Scheduler;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.log4j.Logger;

/**
 * **以下set函数都使用了builder设计模式**
 * @author Jianshu
 * @since 20201102
 */
public class Spider implements Runnable{
    //日志记录
    protected Logger logger=Logger.getLogger(Spider.class);
    //初始化调度器
    protected Scheduler scheduler=new QueueScheduler();
    //下载器
    protected Downloader downloader;
    //页面处理器
    protected PageProcessor pageProcessor;
    //管道pipelines
    protected List<Pipeline> pipelines=new ArrayList<>();
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
    //监视器
    protected List<SpiderListener> spiderListeners;

    /**
     * 初始化爬虫Processor
     * @param pageProcessor
     */
    public Spider(PageProcessor pageProcessor){
        this.pageProcessor=pageProcessor;
        this.site=pageProcessor.getSite();
    }

    /**
     * 加入起始爬取urls
     * @param urls
     * @return
     */
    public Spider addUrl(List<String> urls){
        for (String url:urls) {
            addRequest(new Request(url));
        }
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
     * 设置多个pipelines
     * @param pipelines
     * @return
     */
    public Spider setPipelines(List<Pipeline> pipelines) {
        this.pipelines=pipelines;
        return this;
    }

    /**
     * 加入单个pipeline
     * @param pipeline
     * @return
     */
    public Spider addPipeline(Pipeline pipeline){
        pipelines.add(pipeline);
        return this;
    }

    /**
     * 设置线程池数量
     * @param threadNum
     * @return
     */
    public Spider setThreadNum(int threadNum) {
        this.threadNum=threadNum;
        if (threadNum<=0) {
            throw new IllegalArgumentException("threadNum should be more than one!");
        }
        return this;
    }

    /**
     * 初始化
     */
    protected void initComponent(){
        if(downloader==null){
            this.downloader=new HttpClientDownloader();
        }
        if(pipelines.isEmpty()){
            pipelines.add(new ConsolePipeline());
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
                            onSuccess(request);
                            logger.info("process request "+request+" success");
                        } catch (Exception e){
                            onError(request);
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
        for (Pipeline pipeline:pipelines) {
            pipeline.process(page.getResultItems());
        }
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

    /**
     * run函数结束清理
     */
    public void close(){
        destroyEach(downloader);
        destroyEach(pageProcessor);
        for (Pipeline pipeline:pipelines){
            destroyEach(pipeline);
        }
        threadPoolExecutor.shutdown();
    }

    /**
     * 关闭资源
     * @param object
     */
    private void destroyEach(Object object){
        if(object instanceof Closeable) {
            try {
                ((Closeable)object).close();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 调用监视器error函数回调
     * @param request
     */
    protected void onError(Request request) {
        if (!spiderListeners.isEmpty()) {
            for (SpiderListener spiderListener : spiderListeners) {
                spiderListener.onError(request);
            }
        }
    }

    /**
     * 调用监视器success函数回调
     * @param request
     */
    protected void onSuccess(Request request) {
        if (!spiderListeners.isEmpty()) {
            for (SpiderListener spiderListener : spiderListeners) {
                spiderListener.onSuccess(request);
            }
        }
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
