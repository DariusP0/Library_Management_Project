package repository.book;

import model.Book;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BookRepository {
    List<Book> findAll();

    Optional<Book> findById(Long id);

    boolean save(Book book);
    boolean updateQuantity(String author, String title, int newQuantity);
    void removeById(Long deleteId);
    void removeAll();
    boolean updateByAuthor(Long id, String author);
    boolean updateByTitle(Long id, String title);

    boolean updateByDate(Long id, LocalDate publishedDate);

    boolean updatePrice(Long id, Long price);
    boolean updateByQuantity(Long id,Long quantity);

}