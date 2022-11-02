package cz.cvut.fit.sp.chipin.base.log;

import cz.cvut.fit.sp.chipin.authentication.user.User;
import cz.cvut.fit.sp.chipin.base.group.Group;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;

@Service
@AllArgsConstructor
public class LogService {
    private final LogRepository logRepository;

    public void create(String action, Group group, User user) throws Exception {
        Calendar date = Calendar.getInstance();
        logRepository.save(LogConverter.fromDto(new LogDTO(action, date, user.getName()), group, user));
    }

    public ArrayList<LogDTO> readAllFromGroup(Group group) throws Exception {
        return LogConverter.toListDto(logRepository.findAllByGroup(group));
    }
}