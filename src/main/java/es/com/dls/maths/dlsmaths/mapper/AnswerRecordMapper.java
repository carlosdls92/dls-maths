package es.com.dls.maths.dlsmaths.mapper;

import es.com.dls.maths.dlsmaths.entity.AnswerRecord;
import es.com.dls.maths.dlsmaths.model.AnswerRecordDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AnswerRecordMapper {

    @Mapping(target = "userAnswer", source = "givenAnswer")
    @Mapping(target = "correct", source = "correct")
    AnswerRecord toEntity(AnswerRecordDto dto);
}
