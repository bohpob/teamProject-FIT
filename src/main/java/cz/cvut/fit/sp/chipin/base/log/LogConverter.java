package cz.cvut.fit.sp.chipin.base.log;

import cz.cvut.fit.sp.chipin.authentication.useraccount.UserAccount;
import cz.cvut.fit.sp.chipin.base.usergroup.UserGroup;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class LogConverter {
    public static Log fromDto(LogDTO logDTO, UserGroup userGroup, UserAccount userAccount) {
        return new Log(logDTO.getAction(), logDTO.getDate(), userGroup, userAccount);
    }

    public static LogDTO toDto(Log log) {
        return new LogDTO(log.getAction(), log.getDate(), log.getUserAccount().getName());
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