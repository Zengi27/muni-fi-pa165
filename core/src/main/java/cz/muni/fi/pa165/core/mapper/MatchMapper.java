package cz.muni.fi.pa165.core.mapper;

import cz.muni.fi.pa165.core.entity.Match;
import cz.muni.fi.pa165.model.dto.MatchDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface MatchMapper {
    Match toEntity(MatchDto matchDto);

    MatchDto toDto(Match match);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Match partialUpdate(MatchDto matchDto, @MappingTarget Match match);
}