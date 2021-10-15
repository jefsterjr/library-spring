package org.edu.springmicroservice.controller.service;

import org.edu.springmicroservice.model.dto.BookDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {

    BookDTO borrowBook(Long id);

    BookDTO getBook(Long id);

    Long createBook(BookDTO book);

    BookDTO returnBook(Long id);

    BookDTO updateBook(Long id, BookDTO book);

    Page<BookDTO> getPageBook(Pageable pageable, String name, Boolean isAvailable);
}
