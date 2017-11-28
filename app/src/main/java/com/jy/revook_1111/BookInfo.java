package com.jy.revook_1111;

/**
 * Created by remna on 2017-11-27.
 */

public class BookInfo {
    String title = "";
    String link = "";
    String publisher = "";
    String price = "";
    String image = "";
    String author = "";
    String description = "";
    String isbn ="";

    @Override
    public String toString() {
        return "BookInfo{" +
                "title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", publisher='" + publisher + '\'' +
                ", price='" + price + '\'' +
                ", image='" + image + '\'' +
                ", author='" + author + '\'' +
                ", description='" + description + '\'' +
                ", isbn='" + isbn + '\'' +
                '}';
    }

    public BookInfo(String title, String link, String publisher, String price, String image, String author,
                    String description, String isbn){
        this.title = title;
        this.link = link;
        this.publisher = publisher;
        this.price = price;
        this.image = image;
        this.author = author;
        this.description = description;
        this.isbn = isbn;
    }
}