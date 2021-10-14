package org.edu.springmicroservice.model.mapper;

import org.edu.springmicroservice.model.Book;
import org.edu.springmicroservice.model.dto.BookDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public non-sealed interface BookMapper extends BaseMapper<Book, BookDTO> {

}
