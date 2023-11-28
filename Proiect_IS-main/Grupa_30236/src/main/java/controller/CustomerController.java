package controller;

import database.DatabaseConnectionFactory;
import database.JDBConnectionWrapper;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import model.Book;
import model.User;
import service.book.BookService;
import service.book.*;
import view.CustomerView;
import repository.book.*;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static database.Constants.Schemas.TEST;

public class CustomerController {
    private final CustomerView customerView;
    private final BookService bookService;
    public CustomerController(CustomerView customerView, BookService bookService) {
        this.customerView = customerView;
        this.bookService = bookService;

        this.customerView.addViewAllButtonListener(new ViewAllButtonListener());
        this.customerView.addBuyButtonListener(new CustomerController.BuyButtonListener());
    }

    private class ViewAllButtonListener implements EventHandler<ActionEvent> {

        private List<Book> books;
        @Override
        public void handle(ActionEvent event) {
            books = bookService.findAll();
            customerView.setBooksData(books);
        }
    }
    private class BuyButtonListener implements EventHandler<ActionEvent> {

        private String author;
        private String title;
        private List<Book> boughtBooks = new ArrayList<>();
        private Book selectedBook;
        private Integer price = 0;
        @Override
        public void handle(ActionEvent event) {

            selectedBook = customerView.getSelectedBook();
            author = selectedBook.getAuthor();
            title = selectedBook.getTitle();

            int selectedQuantity = Integer.parseInt(customerView.getQuantityText());
            int newQuantity = selectedBook.getQuantity() - selectedQuantity;

            if(newQuantity >= 0) {
                bookService.updateQuantity(author, title, newQuantity);
                selectedBook.setQuantity(selectedQuantity);
                boughtBooks.add(selectedBook);

                price += selectedBook.getPrice()*selectedQuantity;

                if(boughtBooks!=null) {
                    customerView.setSelectedBookData(boughtBooks);
                    customerView.setPriceText(price);
                    refreshView();
                }

            }else{
                System.out.println("Carti prea multe");
            }


        }
        public void refreshView(){
            List<Book> books = bookService.findAll();
            customerView.setBooksData(books);
        }
    }
}