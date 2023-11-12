package service;


import model.*;
import repository.BookRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class BookServiceImpl<T extends Book> implements BookService<T>{

    final BookRepository<T> bookRepository;

    public BookServiceImpl(BookRepository<T> bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public List<T> findAll() {
        return (List <T>) bookRepository.findAll();
    }

    @Override
    public T findById(Long id) {
        return (T) bookRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Book with id: %d not found".formatted(id)));
    }

    @Override
    public boolean save(T book) {
        return bookRepository.save(book);
    }

    @Override
    public int getAgeOfBook(Long id) {
        Book book = this.findById(id);

        LocalDate now = LocalDate.now();

        return (int)ChronoUnit.YEARS.between(book.getPublishedDate(), now);
    }
}