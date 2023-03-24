package ru.fateyev.library.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fateyev.library.models.Book;
import ru.fateyev.library.models.Person;
import ru.fateyev.library.repositories.BookRepository;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Book findOne(int id) {
        return bookRepository.findById(id).orElse(null);
    }

    @Transactional
    public void save(Book book) {
        bookRepository.save(book);
    }

    @Transactional
    public void update(int id, Book updatedBook) {
        updatedBook.setId(id);
        bookRepository.save(updatedBook);
    }

    @Transactional
    public void delete(int id){
        bookRepository.deleteById(id);
    }

    @Transactional
    public void realese(int id){
        Optional<Book> book = bookRepository.findById(id);

        book.get().setOwner(null);
        bookRepository.save(book.get());
    }

    @Transactional
    public void assign(int id, Person person) {
        Optional<Book> book = bookRepository.findById(id);

        book.get().setOwner(person);
        bookRepository.save(book.get());
    }

    public Optional<Person> getOwner(int id){
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()) {
            Hibernate.initialize(book.get().getOwner());
            return Optional.ofNullable(book.get().getOwner());
        } else {
            return null;
        }
    }

    public List<Book> findPaginated(int page, int size, boolean sort) {
        Pageable pageable;
        if (sort) {
            pageable = PageRequest.of(page, size, Sort.by("year").ascending());
        } else {
            pageable = PageRequest.of(page, size);
        }
        return bookRepository.findAll(pageable).getContent();
    }

    public List<Book> searchBoock(String title){
        return bookRepository.findByTitleStartingWithIgnoreCase(title);
    }
}
