package cz.cvut.fit.sp.chipin.base.log;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Calendar;

@Getter
@AllArgsConstructor
public class LogDTO {
    private String action;
    private Calendar date;
    private String userName;
}