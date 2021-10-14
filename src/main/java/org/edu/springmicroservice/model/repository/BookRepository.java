package org.edu.springmicroservice.model.repository;

import org.edu.springmicroservice.model.Book;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends PagingAndSortingRepository<Book, Long> {

}
