package repository.book;

import model.Book;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class BookRepositoryCacheDecorator extends BookRepositoryDecorator{
    private Cache<Book> cache;

    public BookRepositoryCacheDecorator(BookRepository bookRepository, Cache<Book> cache){
        super(bookRepository);
        this.cache = cache;
    }

    @Override
    public List<Book> findAll() {
        if (cache.hasResult()){
            return cache.load();
        }

        List<Book> books = decoratedRepository.findAll();
        cache.save(books);

        return books;
    }

    @Override
    public Optional<Book> findById(Long id) {

        if (cache.hasResult()){
            return cache.load()
                    .stream()
                    .filter(it -> it.getId().equals(id))
                    .findFirst();
        }

        return decoratedRepository.findById(id);
    }

    @Override
    public boolean save(Book book) {
        cache.invalidateCache();
        return decoratedRepository.save(book);
    }
    @Override
    public boolean updateQuantity(String author, String title, int newQuantity){
        cache.invalidateCache();
        return decoratedRepository.updateQuantity(author, title, newQuantity);
    }
    @Override
    public void removeById(Long deleteId){
        cache.invalidateCache();
        decoratedRepository.removeById(deleteId);
    }
    @Override
    public boolean updateByAuthor(Long id, String author){
        return false;
    }
    @Override
    public boolean updateByTitle(Long id, String title){
        return decoratedRepository.updateByTitle(id, title);
    }
    @Override
    public boolean updateByDate(Long id, LocalDate publishedDate){
        return decoratedRepository.updateByDate(id, publishedDate);
    }
    @Override
    public boolean updatePrice(Long id, Long price){
        return decoratedRepository.updatePrice(id, price);
    }
    @Override
    public boolean updateByQuantity(Long id,Long quantity){
        return decoratedRepository.updateByQuantity(id, quantity);
    }
    @Override
    public void removeAll() {
        cache.invalidateCache();
        decoratedRepository.removeAll();
    }
}