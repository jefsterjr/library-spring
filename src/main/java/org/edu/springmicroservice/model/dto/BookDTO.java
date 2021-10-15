package org.edu.springmicroservice.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Schema(name = "Book", description = "Book")
public class BookDTO implements Serializable {

    @Schema(description = "Unique identifier of the Book.", example = "1")
    private Long id;

    @Schema(description = "Book name", example = "John Nohj", required = true)
    @NotBlank
    private String name;

    @Schema(description = "Number of pages", example = "45", required = true)
    @Min(value = 1L)
    private Integer size;

    @Schema(description = "Book edition", example = "1", required = true)
    @Min(value = 1L)
    @NotBlank
    private String edition;

    @Schema(description = "If the book is available for borrow", example = "true", defaultValue = "true")
    private Boolean isAvailable = true;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public Boolean getAvailable() {
        return isAvailable;
    }

    public void setAvailable(Boolean available) {
        isAvailable = available;
    }
}
