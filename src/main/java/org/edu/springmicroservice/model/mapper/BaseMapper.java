package org.edu.springmicroservice.model.mapper;

import java.util.List;

public sealed interface BaseMapper<ENTITY, DTO> permits BookMapper {

    DTO toDTO(ENTITY entity);

    ENTITY toEntity(DTO dto);

    List<DTO> toDTO(List<ENTITY> entity);

    List<ENTITY> toEntity(List<DTO> dto);
}
