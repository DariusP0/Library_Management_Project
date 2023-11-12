package repository;

import model.Book;

import java.util.List;
import java.util.Optional;

public class BookRepositoryCacheDecorator<T extends Book> extends BookRepositoryDecorator<T> {
    private Cache<T> cache;

    public BookRepositoryCacheDecorator(BookRepository<T> bookRepository, Cache<T> cache) {
        super(bookRepository);
        this.cache = cache;
    }

    @Override
    public List<T> findAll() {
        if (cache.hasResult()) {
            return cache.load();
        }

        List<T> items = decoratedRepository.findAll();
        cache.save(items);

        return items;
    }

    @Override
    public Optional<T> findById(Long id) {
        if (cache.hasResult()) {
            return cache.load()
                    .stream()
                    .filter(it -> it.getId().equals(id))
                    .findFirst();
        }

        return decoratedRepository.findById(id);
    }
    @Override
    public boolean save(T item) {
        cache.invalidateCache();
        return decoratedRepository.save(item);
    }


    @Override
    public void removeAll() {
        cache.invalidateCache();
        decoratedRepository.removeAll();
    }
}
