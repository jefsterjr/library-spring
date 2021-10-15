package org.edu.springmicroservice.model.repository;

import org.edu.springmicroservice.model.Book;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends PagingAndSortingRepository<Book, Long> {


    Page<Book> findAll(Example<Book> example, Pageable pageable);
}
