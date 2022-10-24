package cz.cvut.fit.sp.chipin.base.log;

import cz.cvut.fit.sp.chipin.authentication.user.User;
import cz.cvut.fit.sp.chipin.base.group.Group;

import java.util.ArrayList;

public class LogConverter {
    public static Log fromDto(LogDTO logDTO, Group group, User user) {
        return new Log(logDTO.getAction(), logDTO.getDate(), group, user);
    }

    public static LogDTO toDto(Log log) {
        return new LogDTO(log.getAction(), log.getDate(), log.getUser().getName());
    }

    public static ArrayList<LogDTO> toListDto(ArrayList<Log> logList) {
        ArrayList<LogDTO> listDto = new ArrayList<>();
        for (int i = logList.size() - 1; i >= 0; i--) {
            listDto.add(LogConverter.toDto(logList.get(i)));
        }
        return listDto;
    }
}