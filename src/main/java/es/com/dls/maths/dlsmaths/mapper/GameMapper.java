package es.com.dls.maths.dlsmaths.mapper;

import es.com.dls.maths.dlsmaths.entity.GameResult;
import es.com.dls.maths.dlsmaths.entity.User;
import es.com.dls.maths.dlsmaths.model.GameSession;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring", uses = {AnswerRecordMapper.class})
public interface GameMapper {

    @Mapping(target = "tableName", source = "table")
    @Mapping(target = "playedAt", expression = "java(parsePlayedAt())")
    @Mapping(target = "user", expression = "java(user)")
    GameResult toEntity(GameSession session, @Context User user);

    default LocalDateTime parsePlayedAt() {
        return LocalDateTime.now();
    }
}
