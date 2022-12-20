package cz.cvut.fit.sp.chipin.base.log;

import cz.cvut.fit.sp.chipin.authentication.user.User;
import cz.cvut.fit.sp.chipin.base.group.Group;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class LogConverter {
    public static Log fromDto(LogDTO logDTO, Group group, User user) {
        return new Log(logDTO.getAction(), logDTO.getDate(), group, user);
    }

    public static LogDTO toDto(Log log) {
        return new LogDTO(log.getAction(), log.getDate(), log.getUser().getName());
    }

    public static List<LogDTO> toGroupResponse(List<Log> logs) {
        Collections.reverse(logs);
        if (logs.size() > 2) {
            return logs.subList(0, 3).stream().map(LogConverter::toDto).collect(Collectors.toList());
        } else {
            return logs.stream().map(LogConverter::toDto).collect(Collectors.toList());
        }
    }
}