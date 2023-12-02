package cz.cvut.fit.sp.chipin.base.usergroup.mapper;

import cz.cvut.fit.sp.chipin.base.log.mapper.LogReadLogResponse;
import lombok.Data;

import java.util.List;

@Data
public class GroupReadGroupLogsResponse {
    List<LogReadLogResponse> logs;
}
