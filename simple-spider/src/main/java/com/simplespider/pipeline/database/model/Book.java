package com.simplespider.pipeline.database.model;

import java.util.Date;

public class Book {
    
    private String bookISBN;
    private String bookName;
    private String bookAuthor;
    private String bookTranslator;
    private String bookPress;
    private Date bookDate;
    private Double bookPrice;
    private Integer bookStock;
    private Integer bookStatus;
    private String bookCategory;
    private Double bookRating;
    private Integer bookRatingSum;
    private String bookPic;
    private String bookHref;
    private Date crateTime;
    private Date updateTime;

    public String getBookISBN() {
        return bookISBN;
    }

    public void setBookISBN(String bookISBN) {
        this.bookISBN = bookISBN;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public String getBookTranslator() {
        return bookTranslator;
    }

    public void setBookTranslator(String bookTranslator) {
        this.bookTranslator = bookTranslator;
    }

    public String getBookPress() {
        return bookPress;
    }

    public void setBookPress(String bookPress) {
        this.bookPress = bookPress;
    }

    public Date getBookDate() {
        return bookDate;
    }

    public void setBookDate(Date bookDate) {
        this.bookDate = bookDate;
    }

    public Double getBookPrice() {
        return bookPrice;
    }

    public void setBookPrice(Double bookPrice) {
        this.bookPrice = bookPrice;
    }

    public Integer getBookStock() {
        return bookStock;
    }

    public void setBookStock(Integer bookStock) {
        this.bookStock = bookStock;
    }

    public Integer getBookStatus() {
        return bookStatus;
    }

    public void setBookStatus(Integer bookStatus) {
        this.bookStatus = bookStatus;
    }

    public String getBookCategory() {
        return bookCategory;
    }

    public void setBookCategory(String bookCategory) {
        this.bookCategory = bookCategory;
    }

    public Double getBookRating() {
        return bookRating;
    }

    public void setBookRating(Double bookRating) {
        this.bookRating = bookRating;
    }

    public Integer getBookRatingSum() {
        return bookRatingSum;
    }

    public void setBookRatingSum(Integer bookRatingSum) {
        this.bookRatingSum = bookRatingSum;
    }

    public String getBookPic() {
        return bookPic;
    }

    public void setBookPic(String bookPic) {
        this.bookPic = bookPic;
    }

    public String getBookHref() {
        return bookHref;
    }

    public void setBookHref(String bookHref) {
        this.bookHref = bookHref;
    }

    public Date getCrateTime() {
        return crateTime;
    }

    public void setCrateTime(Date crateTime) {
        this.crateTime = crateTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "Book{" +
                "bookISBN='" + bookISBN + '\'' +
                ", bookName='" + bookName + '\'' +
                ", bookAuthor='" + bookAuthor + '\'' +
                ", bookTranslator='" + bookTranslator + '\'' +
                ", bookPress='" + bookPress + '\'' +
                ", bookDate=" + bookDate +
                ", bookPrice=" + bookPrice +
                ", bookStock=" + bookStock +
                ", bookStatus=" + bookStatus +
                ", bookCategory='" + bookCategory + '\'' +
                ", crateTime=" + crateTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
