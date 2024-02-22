package service.book;

import model.Book;
import repository.book.BookRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class BookServiceImpl implements BookService{

    final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository){
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public Book findById(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Book with id: %d not found".formatted(id)));
    }

    @Override
    public boolean save(Book book) {
        return bookRepository.save(book);
    }
    @Override
    public boolean updateQuantity(String author, String title, int newQuantity){
        return bookRepository.updateQuantity(author, title, newQuantity);
    }
    @Override
    public void removeById(Long deleteId){
        bookRepository.removeById(deleteId);
    }
    @Override
    public boolean updateByAuthor(Long id, String author){
        return bookRepository.updateByAuthor(id, author);
    }
    @Override
    public boolean updateByTitle(Long id, String title){
        return bookRepository.updateByTitle(id, title);
    }

    @Override
    public boolean updateByDate(Long id, LocalDate publishedDate){
        return bookRepository.updateByDate(id, publishedDate);
    }
    @Override
    public boolean updatePrice(Long id, Long price){
        return bookRepository.updatePrice(id, price);
    }
    @Override
    public boolean updateByQuantity(Long id,Long quantity){
        return bookRepository.updateByQuantity(id, quantity);
    }
    @Override
    public int getAgeOfBook(Long id) {
        Book book = this.findById(id);

        LocalDate now = LocalDate.now();

        return (int)ChronoUnit.YEARS.between(book.getPublishedDate(), now);
    }
}