package controller;

import database.DatabaseConnectionFactory;
import database.JDBConnectionWrapper;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import model.Book;
import model.User;
import model.builder.BookBuilder;
import service.book.BookService;
import service.book.*;
import repository.book.*;
import view.EmployeeView;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static database.Constants.Schemas.TEST;

public class EmployeeController {
    private final EmployeeView employeeView;
    private final BookService bookService;
    public EmployeeController(EmployeeView employeeView, BookService bookService) {
        this.employeeView = employeeView;
        this.bookService = bookService;

        this.employeeView.addViewAllButtonListener(new ViewAllButtonListener());
        this.employeeView.addCreateButtonListener(new CreateButtonListener());
        this.employeeView.addDeleteButtonListener(new DeleteButtonListener());
        this.employeeView.addRetrieveButtonListener(new RetrieveButtonListener());
        this.employeeView.addUpdateButtonListener(new UpdateButtonListener());
        this.employeeView.addSellButtonListener(new SellButtonListener());
        
    }

    private class ViewAllButtonListener implements EventHandler<ActionEvent> {

        private List<Book> books;
        @Override
        public void handle(ActionEvent event) {
            books = bookService.findAll();
            employeeView.setBooksData(books);
        }
    }
    public class SellButtonListener implements EventHandler<ActionEvent> {
        List<Book> books = new ArrayList<>();
        @Override
        public void handle(ActionEvent event) {
            Book selectedBook = employeeView.getSelectedBook();

            Long newQuantity = (long) selectedBook.getQuantity() - 1;
            bookService.updateByQuantity(selectedBook.getId(), newQuantity);
            books.add(selectedBook);
            refreshView();

            // Generate PDF report
            generatePdfReport(books);
        }

        private void generatePdfReport(List<Book> books) {
            try {
                // Create a PdfWriter object to write to a file
                PdfWriter writer = new PdfWriter(new FileOutputStream("report.pdf", true));

                // Create a PdfDocument object
                PdfDocument pdfDocument = new PdfDocument(writer);

                // Create a Document object to add elements to the PDF
                Document document = new Document(pdfDocument);

                // Add a title to the PDF
                document.add(new Paragraph("Sold Books Report"));

                // Create a table to display book information
                Table table = new Table(3);
                table.addCell("Book ID");
                table.addCell("Title");
                table.addCell("Quantity");

                // Add book information to the table
                for (Book book : books) {
                    table.addCell(String.valueOf(book.getId()));
                    table.addCell(book.getTitle());
                    table.addCell(String.valueOf(1));
                }

                // Add the table to the document
                document.add(table);

                // Close the document
                document.close();

                System.out.println("PDF report generated successfully.");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private class UpdateButtonListener implements EventHandler<ActionEvent> {


        @Override
        public void handle(ActionEvent event) {
            Book selectedBook = employeeView.getSelectedBook();
            Long id = selectedBook.getId();

            if(!employeeView.getAuthorText().isEmpty()){
                String author = employeeView.getAuthorText();
                bookService.updateByAuthor(id, author);
            }
            if(!employeeView.getTitleText().isEmpty()){
                String title = employeeView.getTitleText();
                bookService.updateByTitle(id, title);
            }
            if(!employeeView.getDate().isEmpty()){
                LocalDate publishedDate = LocalDate.parse(employeeView.getDate());
                bookService.updateByDate(id, publishedDate);
            }
            if(!employeeView.getPrice().isEmpty()){
                Long price = Long.parseLong(employeeView.getPrice());
                bookService.updatePrice(id, price);
            }
            if(!employeeView.getQuantityText().isEmpty()){
                Long quantity = Long.parseLong(employeeView.getQuantityText());
                bookService.updateByQuantity(id, quantity);
            }
            refreshView();
        }

    }
    private class DeleteButtonListener implements EventHandler<ActionEvent> {
        private Book selectedBook;
        @Override
        public void handle(ActionEvent event) {
            selectedBook = employeeView.getSelectedBook();
            Long idSelectedBook = selectedBook.getId();
            bookService.removeById(idSelectedBook);
            refreshView();
        }
    }
    private class RetrieveButtonListener implements EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent event){
            Long id = Long.parseLong(employeeView.getIdText());
            List<Book> book = new ArrayList<>();
            book.add(bookService.findById(id));
            employeeView.setBooksData(book);
        }
    }
    private class CreateButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            String author = employeeView.getAuthorText();
            String title = employeeView.getTitleText();
            String publishedDate = employeeView.getDate();
            int price = Integer.parseInt(employeeView.getPrice());
            int quantity = Integer.parseInt(employeeView.getQuantityText());
            String[] numbers = publishedDate.split(",");

            // Convert the string array to integers
            int[] intNumbers = new int[3];
                int year = Integer.parseInt(numbers[0].trim());
                int month = Integer.parseInt(numbers[1].trim());
                int dayOfMonth = Integer.parseInt(numbers[2].trim());
            Book book = new BookBuilder()
                    .setTitle(title)
                    .setAuthor(author)
                    .setPublishedDate(LocalDate.of(year, month ,dayOfMonth))
                    .setPrice(price)
                    .setQuantity(quantity)
                    .build();
            bookService.save(book);
            refreshView();
        }

    }
        public void refreshView(){
            List<Book> books = bookService.findAll();
            employeeView.setBooksData(books);
        }
}
