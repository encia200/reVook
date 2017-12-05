package com.jy.revook_1111;

/**
 * Created by remna on 2017-12-01.
 */

public class Card_BookSearch {
    private String imgURL;
    private String bookTitle;
    private String bookAuthor;
    private String bookPrice;

    public Card_BookSearch(String bookTitle, String bookAuthor, String bookPrice, String imgURL){
        this.imgURL = imgURL;
        this.bookAuthor = bookAuthor;
        this.bookTitle = bookTitle;
        this.bookPrice = bookPrice;
    }

    public String getBookPrice(){return this.bookPrice;}
    public void setBookPrice(String bookPrice){this.bookPrice = bookPrice;}

    public String getBookAuthor(){return this.bookAuthor;}
    public void setBookAuthor(String bookAuthor){this.bookAuthor = bookAuthor;}

    public String getImgURL(){return this.imgURL;}
    public void setImgURL(String imgURL){this.imgURL = imgURL;}

    public String getBookTitle(){return this.bookTitle;}
    public void setBookTitle(String bookTitle){this.bookTitle = bookTitle;}


}
