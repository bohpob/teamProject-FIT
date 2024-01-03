package cz.cvut.fit.sp.chipin.base.log.mapper;

import cz.cvut.fit.sp.chipin.base.log.Log;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LogMapper {
    String DATETIME_FORMAT = "yyyy-MM-dd HH:mm";

    @Mapping(target = "dateTime", dateFormat = DATETIME_FORMAT)
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.firstName", target = "userName")
    LogReadLogResponse entityToReadLogResponse(Log log);
}
