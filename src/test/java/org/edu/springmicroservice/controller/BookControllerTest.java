package org.edu.springmicroservice.controller;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.edu.springmicroservice.model.Book;
import org.edu.springmicroservice.model.dto.BookDTO;
import org.edu.springmicroservice.model.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookRepository repository;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @DisplayName("Get book id - Success")
    @Test
    void getBookById() throws Exception {
        Mockito.when(repository.findById(anyLong())).thenReturn(Optional.of(getBook()));
        MvcResult mvcResult = mockMvc.perform(get("/books/1")).andReturn();
        BookDTO bookDTO = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), BookDTO.class);
        assertEquals(bookDTO.getId(), Long.MAX_VALUE);
    }

    @DisplayName("Get book by id - Book not found")
    @Test
    void getBookByIdBookNotFound() throws Exception {
        Mockito.when(repository.findById(anyLong())).thenReturn(Optional.ofNullable(null));
        mockMvc.perform(get("/books/1")).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @DisplayName("Get a page of books with no filter and no pagination")
    @Test
    void getBooks_NoFilter_NoPagination() throws Exception {
        Page<Book> books = new PageImpl<>(Collections.singletonList(getBook()), Pageable.ofSize(1).withPage(0), 1);
        Mockito.when(repository.findAll(any(Example.class), any(Pageable.class))).thenReturn(books);
        MvcResult mvcResult = mockMvc.perform(get("/books"))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        PageImpl<BookDTO> bookDTOS = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<RestResponsePage<BookDTO>>() {
        });
        assertEquals(bookDTOS.getTotalElements(), 1);
    }

    @DisplayName("Get a page of books with all filters")
    @Test
    void getBooks_AllFilters() throws Exception {
        Page<Book> books = new PageImpl<>(Collections.singletonList(getBook()));
        Mockito.when(repository.findAll(any(Example.class), any(Pageable.class))).thenReturn(books);
        MvcResult mvcResult = mockMvc.perform(get("/books")
                        .queryParam("page", "1")
                        .queryParam("size", "1")
                        .queryParam("number", "1")
                        .queryParam("name", "Lord of Rings")
                        .queryParam("isAvailable", "true"))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        PageImpl<BookDTO> bookDTOS = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<RestResponsePage<BookDTO>>() {
        });
        assertEquals(bookDTOS.getTotalElements(), 1);
    }

    @DisplayName("Get a page of books with all filters")
    @Test
    void getBooks_OnlyFilters() throws Exception {
        Page<Book> books = new PageImpl<>(Collections.singletonList(getBook()));
        Mockito.when(repository.findAll(any(Example.class), any(Pageable.class))).thenReturn(books);
        MvcResult mvcResult = mockMvc.perform(get("/books")
                        .queryParam("name", "Lord of Rings")
                        .queryParam("isAvailable", "true"))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        PageImpl<BookDTO> bookDTOS = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<RestResponsePage<BookDTO>>() {
        });
        assertEquals(bookDTOS.getTotalElements(), 1);
    }

    @DisplayName("Borrow a book")
    @Test
    void borrowBook_Success() throws Exception {
        Mockito.when(repository.findById(anyLong())).thenReturn(Optional.of(getBook()));
        mockMvc.perform(patch("/books/1/borrow"))
                .andExpect(MockMvcResultMatchers.status().isAccepted());
    }

    @DisplayName("Try to borrow a book that is already borrowed")
    @Test
    void borrowBook_AlreadyBorrowed() throws Exception {
        Book book = getBook();
        book.setAvailable(false);
        Mockito.when(repository.findById(anyLong())).thenReturn(Optional.of(book));
        mockMvc.perform(patch("/books/1/borrow"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    private Book getBook() {
        Book book = new Book();
        book.setAvailable(true);
        book.setName("Lord of Rings");
        book.setSize(690);
        book.setEdition("1");
        book.setId(Long.MAX_VALUE);
        return book;
    }


}

@JsonIgnoreProperties(ignoreUnknown = true)
class RestResponsePage<T> extends PageImpl<T> {
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public RestResponsePage(@JsonProperty("content") List<T> content,
                            @JsonProperty("number") int number,
                            @JsonProperty("size") int size,
                            @JsonProperty("totalElements") Long totalElements,
                            @JsonProperty("pageable") JsonNode pageable,
                            @JsonProperty("last") boolean last,
                            @JsonProperty("totalPages") int totalPages,
                            @JsonProperty("sort") JsonNode sort,
                            @JsonProperty("first") boolean first,
                            @JsonProperty("numberOfElements") int numberOfElements) {

        super(content, PageRequest.of(number, size), totalElements);
    }

    public RestResponsePage(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public RestResponsePage(List<T> content) {
        super(content);
    }

    public RestResponsePage() {
        super(new ArrayList<>());
    }
}