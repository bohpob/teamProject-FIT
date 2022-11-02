package cz.cvut.fit.sp.chipin.base.transaction;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Calendar;
import java.util.List;

@Getter
@AllArgsConstructor
public class TransactionDTO {
    private String name;
    private Float amount;
    private Calendar date;
    private String payerName;
    private Long payerId;
    private List<Long> userIds;
}
