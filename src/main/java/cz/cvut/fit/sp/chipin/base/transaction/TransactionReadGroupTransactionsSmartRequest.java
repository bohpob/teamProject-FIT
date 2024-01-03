package cz.cvut.fit.sp.chipin.base.transaction;

import cz.cvut.fit.sp.chipin.base.member.mapper.MemberReadMemberResponse;
import lombok.Data;


import java.util.List;

@Data
public class TransactionReadGroupTransactionsSmartRequest {
    List<String> categories;
    String dateTimeFrom;
    String dateTimeTo;
    List<MemberReadMemberResponse> members;
}
