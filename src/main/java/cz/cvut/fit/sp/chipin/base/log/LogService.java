package cz.cvut.fit.sp.chipin.base.log;

import cz.cvut.fit.sp.chipin.authentication.user.User;
import cz.cvut.fit.sp.chipin.base.group.Group;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@AllArgsConstructor
public class LogService {
    private final LogRepository logRepository;

    public void create(String action, Group group, User user) {
        LocalDateTime currentDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM yyyy, HH:mm");
        String date = currentDate.format(formatter);

        logRepository.save(new Log(action, date, group, user));
    }

    public List<Log> readLogs(Long groupId) {
        return logRepository.findLogsByGroupId(groupId);
    }
}