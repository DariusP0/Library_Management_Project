package service.book;

import model.Book;

import java.time.LocalDate;
import java.util.List;

public interface BookService {

    List<Book> findAll();

    Book findById(Long id);

    boolean save(Book book);
    boolean updateQuantity(String author, String title, int newQuantity);
    void removeById(Long deleteId);
    boolean updateByAuthor(Long id, String author);
    boolean updateByTitle(Long id, String title);

    boolean updateByDate(Long id, LocalDate publishedDate);

    boolean updatePrice(Long id, Long price);
    boolean updateByQuantity(Long id,Long quantity);
    int getAgeOfBook(Long id);
}