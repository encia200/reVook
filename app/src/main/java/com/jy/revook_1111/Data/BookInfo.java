package com.jy.revook_1111.Data;

import android.app.ProgressDialog;

/**
 * Created by remna on 2017-11-27.
 */

public class BookInfo {
    public String title = "";
    public String link = "";
    public String publisher = "";
    public String price = "";
    public String imageURL = "";
    public String author = "";
    public String description = "";
    public String isbn ="";

    @Override
    public String toString() {
        return "BookInfo{" +
                "title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", publisher='" + publisher + '\'' +
                ", price='" + price + '\'' +
                ", image='" + imageURL + '\'' +
                ", author='" + author + '\'' +
                ", description='" + description + '\'' +
                ", isbn='" + isbn + '\'' +
                '}';
    }

    public BookInfo(String title, String link, String publisher, String price, String imageURL, String author,
                    String description, String isbn){
        this.link = link;
        this.publisher = publisher;
        this.price = price;
        this.imageURL = imageURL;
        this.author = author;
        this.isbn = isbn;
        editBookImageURL();
        this.title = reomoveTagString(title);
        this.description = reomoveTagString(description);
    }

    public String reomoveTagString(String ab){
        ab = ab.replace("<b>", "");
        ab = ab.replace("</b>", "");
        return ab;
    }
    public void editBookImageURL(){
        this.imageURL = this.imageURL.replace("type=m1", "");
        System.out.println("***************************" + this.imageURL);
    }
}