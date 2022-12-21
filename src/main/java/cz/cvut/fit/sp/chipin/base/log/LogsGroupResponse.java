package cz.cvut.fit.sp.chipin.base.log;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class LogsGroupResponse {

    private List<LogDTO> logs;

}
