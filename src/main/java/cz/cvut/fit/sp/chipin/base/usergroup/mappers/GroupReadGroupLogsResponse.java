package cz.cvut.fit.sp.chipin.base.usergroup.mappers;

import cz.cvut.fit.sp.chipin.base.log.mapper.LogReadGroupLogsResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
public class GroupReadGroupLogsResponse {
    List<LogReadGroupLogsResponse> logs;
}
