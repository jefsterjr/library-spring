package org.edu.springmicroservice.controller.service.impl;

import org.edu.springmicroservice.config.validation.exception.BookIsAlreadyBorrowedException;
import org.edu.springmicroservice.config.validation.exception.BookIsAlreadyReturnedException;
import org.edu.springmicroservice.config.validation.exception.BookNotFoundException;
import org.edu.springmicroservice.controller.service.BookService;
import org.edu.springmicroservice.model.Book;
import org.edu.springmicroservice.model.dto.BookDTO;
import org.edu.springmicroservice.model.mapper.BookMapper;
import org.edu.springmicroservice.model.repository.BookRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;

@Service
public class BookServiceImpl implements BookService {

    private final BookMapper mapper;

    private final BookRepository repository;

    public BookServiceImpl(BookMapper mapper, BookRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    @Override
    @Transactional
    public BookDTO borrowBook(Long id) {
        Book book = findBook(id);
        if (book.isAvailable()) {
            book.setAvailable(false);
            return mapper.toDTO(book);
        } else {
            throw new BookIsAlreadyBorrowedException();
        }
    }

    @Override
    public BookDTO getBook(Long id) {
        BookDTO bookDTO = mapper.toDTO(findBook(id));
        return bookDTO;
    }

    @Override
    @Transactional
    public Long createBook(BookDTO book) {
        Book saveBook = repository.save(mapper.toEntity(book));
        return saveBook.getId();
    }

    @Override
    @Transactional
    public BookDTO returnBook(Long id) {
        Book book = findBook(id);
        if (!book.isAvailable()) {
            book.setAvailable(true);
            return mapper.toDTO(book);
        } else {
            throw new BookIsAlreadyReturnedException();
        }
    }

    @Override
    @Transactional
    public BookDTO updateBook(Long id, BookDTO dto) {
        Book book = findBook(id);
        mapper.update(dto, book);
        repository.save(book);
        return mapper.toDTO(book);
    }

    @Override
    public Page<BookDTO> getPageBook(Pageable pageable, String name, Boolean isAvailable) {
        ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("name", contains().ignoreCase());
        Book bookExample = new Book();
        if (isAvailable != null) bookExample.setAvailable(isAvailable);
        bookExample.setName(name);
        return repository.findAll(Example.of(bookExample, matcher), pageable).map(mapper::toDTO);
    }


    private Book findBook(Long id) {
        return repository.findById(id).orElseThrow(BookNotFoundException::new);
    }
}
