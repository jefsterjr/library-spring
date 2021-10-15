package org.edu.springmicroservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.edu.springmicroservice.controller.service.BookService;
import org.edu.springmicroservice.model.dto.BookDTO;
import org.edu.springmicroservice.model.repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController()
@RequestMapping("/books")
@Tag(name = "Book", description = "The Book API")
public class BookController {

    private final BookRepository repository;

    private final BookService service;

    public BookController(BookRepository repository, BookService service) {
        this.repository = repository;
        this.service = service;
    }

    @Operation(summary = "Get a paginated list of books")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Found the books", content = @Content(mediaType = "application/json"))})
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public ResponseEntity<Page<BookDTO>> getBooks(@Parameter(description = "Pagination properties") @PageableDefault Pageable pageable,
                                                  @Parameter(description = "Book name") @RequestParam(required = false, name = "name") String name,
                                                  @Parameter(description = "If the book is available to borrow") @RequestParam(required = false, name = "is-available") Boolean isAvailable) {
        return ResponseEntity.ok(service.getPageBook(pageable, name, isAvailable));
    }


    @Operation(summary = "Get a book by its id")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Found the book", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Id is null", content = @Content(mediaType = "application/json"))})
    @GetMapping("/{id}")
    public ResponseEntity<?> getBook(@PathVariable @Parameter(description = "Book id") Long id) {
        return ResponseEntity.ok(service.getBook(id));
    }

    @Operation(summary = "Update book information")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Found the books", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Book not found", content = @Content(mediaType = "application/json")),})
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long id, BookDTO book) {
        return ResponseEntity.accepted().body(service.updateBook(id, book));
    }

    @Operation(summary = "Borrow book")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Book borrowed", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Book not found", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Book is not available", content = @Content(mediaType = "application/json"))})
    @PatchMapping("/{id}/borrow")
    public ResponseEntity<BookDTO> borrowBook(@PathVariable Long id) {
        service.borrowBook(id);
        return ResponseEntity.accepted().build();
    }

    @Operation(summary = "Return book")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Book returned", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Book not found", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Book is not borrowed", content = @Content(mediaType = "application/json"))})
    @PatchMapping("/{id}/return")
    public ResponseEntity<BookDTO> returnBook(@PathVariable Long id) {
        service.returnBook(id);
        return ResponseEntity.accepted().build();
    }

    @Operation(summary = "Create a new book")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Book created!", content = @Content(mediaType = "application/json"))})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<BookDTO> createBook(@RequestBody @Valid BookDTO book, UriComponentsBuilder uriComponentsBuilder) {
        Long id = service.createBook(book);
        URI uri = uriComponentsBuilder.path("/books/{id}").buildAndExpand(id).toUri();
        return ResponseEntity.created(uri).build();
    }

    @Operation(summary = "Delete a book")
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Book deleted!", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Book not found", content = @Content(mediaType = "application/json")),})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public ResponseEntity deleteBook(@PathVariable Long id) {
        if (repository.existsById(id)) repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
