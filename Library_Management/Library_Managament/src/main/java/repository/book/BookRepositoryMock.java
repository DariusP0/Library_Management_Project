package repository.book;

import model.Book;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookRepositoryMock implements BookRepository{

    private List<Book> books;

    public BookRepositoryMock(){
        books = new ArrayList<>();
    }

    @Override
    public List<Book> findAll() {
        return books;
    }

    @Override
    public Optional<Book> findById(Long id) {
        return books.parallelStream()
                .filter(it -> it.getId().equals(id))
                .findFirst();
    }

    @Override
    public boolean save(Book book) {
        return books.add(book);
    }

    @Override
    public boolean updateQuantity(String author, String title, int newQuantity){
        for(Book book : books){
            if(author.equals(book.getAuthor()) && title.equals(book.getTitle())){
                book.setQuantity(book.getQuantity() + newQuantity);
                return true;
            }
        }
        return false;
    }
    @Override
    public void removeById(Long deleteId){
        books.remove(deleteId);
    }
    public boolean updateByAuthor(Long id, String author){
        return false;
    }
    public boolean updateByTitle(Long id, String title){
        return false;
    }

    public boolean updateByDate(Long id, LocalDate publishedDate){
        return false;
    }

    public boolean updatePrice(Long id, Long price){
        return false;
    }
    public boolean updateByQuantity(Long id,Long quantity){
        return false;
    }
    @Override
    public void removeAll() {
        books.clear();
    }
}