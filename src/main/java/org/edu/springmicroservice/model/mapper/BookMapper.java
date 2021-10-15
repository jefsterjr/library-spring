package org.edu.springmicroservice.model.mapper;

import org.edu.springmicroservice.model.Book;
import org.edu.springmicroservice.model.dto.BookDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public non-sealed interface BookMapper extends BaseMapper<Book, BookDTO> {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(BookDTO dto, @MappingTarget Book entity);
}
