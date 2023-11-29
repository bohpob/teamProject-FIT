package cz.cvut.fit.sp.chipin.base.log;

import cz.cvut.fit.sp.chipin.authentication.useraccount.UserAccount;
import cz.cvut.fit.sp.chipin.base.usergroup.UserGroup;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@AllArgsConstructor
public class LogService {
    private final LogRepository logRepository;

    public void create(String action, UserGroup userGroup, UserAccount userAccount) throws Exception {
        LocalDateTime currentDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM yyyy, HH:mm");
        String date = currentDate.format(formatter);

        logRepository.save(LogConverter.fromDto(new LogDTO(action, date, userAccount.getName()), userGroup, userAccount));
    }

    public List<Log> readLogs(Long groupId) throws Exception {
        return logRepository.findLogsByUserGroupId(groupId);
    }
}