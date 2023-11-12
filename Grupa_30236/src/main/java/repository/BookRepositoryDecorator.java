package repository;
import model.*;
public abstract class BookRepositoryDecorator<T extends Book> implements BookRepository<T>{
    protected BookRepository decoratedRepository;

    public BookRepositoryDecorator(BookRepository bookRepository){
        this.decoratedRepository = bookRepository;
    }

}