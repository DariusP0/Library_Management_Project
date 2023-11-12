import database.DatabaseConnectionFactory;
import database.JDBConnectionWrapper;
import model.AudioBook;
import model.Book;
import model.EBook;
import model.builder.AudioBookBuilder;
import model.builder.BookBuilder;
import model.builder.EBookBuilder;
import repository.BookRepository;
import repository.BookRepositoryMySQL;
import service.BookServiceImpl;

import java.time.LocalDate;
import java.util.Date;

public class Main {
    public static void main(String[] args){
        System.out.println("Hello world!");


        BookRepository bookRepository = new BookRepositoryMySQL(DatabaseConnectionFactory.getConnectionWrapper(true).getConnection());

        Book book = new BookBuilder()
                .setTitle("Harry Potter")
                .setAuthor("J.K. Rowling")
                .setPublishedDate(LocalDate.of(2010, 7, 3))
                .build();

        Book book2 = new BookBuilder()
                .setTitle("La granita cu lumea interlopa")
                .setAuthor("Anghel Stoica")
                .setPublishedDate(LocalDate.of(2011, 3, 1))
                .build();


           BookServiceImpl bookServ = new BookServiceImpl(bookRepository);


           EBook ebook = new EBookBuilder()
                   .setId((long)3)
                   .setAuthor("Codin Maticiuc")
                   .setTitle("Viata lui Nutu Camataru - Dresor de lei si de fraieri")
                   .setPublishedDate(LocalDate.of(2021,11,24))
                   .setFormat("pdf")
                   .build();
            AudioBook audiobook = new AudioBookBuilder()
                .setLength("3:01:00")
                .setTitle("Harry Potter")
                .setAuthor("J.K. Rowling")
                .setPublishedDate(LocalDate.of(2010, 7, 3))
                .build();
        //bookServ.save(book);
        bookServ.save(book2);
        bookServ.save(ebook);
        //bookServ.save(audiobook);



    }
}
