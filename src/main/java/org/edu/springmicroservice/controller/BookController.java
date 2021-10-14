package org.edu.springmicroservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.edu.springmicroservice.config.validation.exception.BookIsAlreadyBorrowedException;
import org.edu.springmicroservice.model.Book;
import org.edu.springmicroservice.model.dto.BookDTO;
import org.edu.springmicroservice.model.mapper.BookMapper;
import org.edu.springmicroservice.model.repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController("/books")
@Tag(name = "Book", description = "The Book API")
public class BookController {

    private final BookRepository repository;

    private final BookMapper mapper;

    public BookController(BookRepository repository, BookMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Operation(summary = "Get a paginated list of books")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Found the books", content = @Content(mediaType = "application/json"))})
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public ResponseEntity<?> getBooks(@Parameter(description = "Pagination properties") Pageable pageable) {
        Page<Book> all = repository.findAll(pageable);
        return ResponseEntity.ok(mapper.toDTO(all));
    }

    @Operation(summary = "Get a book by its id")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Found the book", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Id is null", content = @Content(mediaType = "application/json"))})
    @GetMapping("/{id}")
    public ResponseEntity<?> getBook(@PathVariable @Parameter(description = "Book id") Long id) {
        Optional<Book> optBook = repository.findById(id);
        if (optBook.isPresent()) return ResponseEntity.ok(mapper.toDTO(optBook.get()));
        return ResponseEntity.ok().body("Unable to find a book with the provided id");

    }

    @Operation(summary = "Update book information")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Found the books", content = @Content(mediaType = "application/json"))})
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long id, Book book) {
        Optional<Book> bookOptional = repository.findById(id);
        if (bookOptional.isPresent()) {

        }
        return ResponseEntity.accepted().build();
    }

    @PatchMapping("/{id}/borrow")
    public ResponseEntity<BookDTO> borrowBook(@PathVariable Long id) {
        Optional<Book> optionalBook = repository.findById(id);
        optionalBook.ifPresent(book -> {
            if (book.getAvailable()) {
                book.setAvailable(false);
                repository.save(book);
            } else {
                throw new BookIsAlreadyBorrowedException();
            }
        });
        return ResponseEntity.accepted().build();
    }

    @PatchMapping("/{id}/return")
    public ResponseEntity<BookDTO> returnBook(@PathVariable Long id) {

        return ResponseEntity.accepted().build();
    }

    @Operation(summary = "Create a new book")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Book created", content = @Content(mediaType = "application/json"))})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<BookDTO> createBook(@RequestBody @Valid BookDTO book, UriComponentsBuilder uriComponentsBuilder) {
        URI uri = uriComponentsBuilder.path("/books/{id}").buildAndExpand(book).toUri();
        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteBook(@PathVariable Long id) {
        Optional<Book> optionalBook = repository.findById(id);
        optionalBook.ifPresent(repository::delete);
        return ResponseEntity.noContent().build();
    }
}
