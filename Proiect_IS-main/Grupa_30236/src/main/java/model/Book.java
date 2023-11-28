package model;

// Java Bean -

// POJO - Plain Old Java Object


import java.time.LocalDate;
import java.util.Date;

public class Book{

    private Long id;

    private String author;

    private String title;

    private LocalDate publishedDate;
    private  int price;
    private  int quantity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getPublishedDate() {
        return publishedDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setPublishedDate(LocalDate publishedDate) {
        this.publishedDate = publishedDate;
    }

    @Override
    public String toString(){
        return String.format("Id: %d | Title: %s | Author: %s | Date: %s | Price: %d | Quantity: %d", this.id, this.title, this.author, this.publishedDate,this.price, this.quantity);
    }
}