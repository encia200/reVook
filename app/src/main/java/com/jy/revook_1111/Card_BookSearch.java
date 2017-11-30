package com.jy.revook_1111;

/**
 * Created by remna on 2017-12-01.
 */

public class Card_BookSearch {
    private String imgURL;
    private String bookTitle;

    public Card_BookSearch(String bookTitle, String imgURL){
        this.imgURL = imgURL;
        this.bookTitle = bookTitle;
    }

    public String getImgURL(){return this.imgURL;}
    public void setImgURL(String imgURL){this.imgURL = imgURL;}

    public String getBookTitle(){return this.bookTitle;}
    public void setBookTitle(String bookTitle){this.bookTitle = bookTitle;}


}
