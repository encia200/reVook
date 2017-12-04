package com.jy.revook_1111.Data;

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
        this.title = title;
        this.link = link;
        this.publisher = publisher;
        this.price = price;
        this.imageURL = imageURL;
        this.author = author;
        this.description = description;
        this.isbn = isbn;
        editBookImageURL();
        editBookTitle();
    }

    public void editBookTitle(){
        this.title = this.title.replace("<b>", "");
        this.title = this.title.replace("</b>", "");
    }
    public void editBookImageURL(){
        this.imageURL = this.imageURL.replace("type=m1", "");
        System.out.println("***************************" + this.imageURL);
    }
}