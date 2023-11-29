package cz.cvut.fit.sp.chipin.authentication.useraccount;

import cz.cvut.fit.sp.chipin.base.member.Member;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class UserAccountConverter {

    private static UserAccountGroupResponse toUserAccountGroupResponse(Member member) {
        return new UserAccountGroupResponse(member.getUserAccount().getId(), member.getUserAccount().getName(), member.getBalance());
    }

    public static List<UserAccountGroupResponse> toUserAccountsGroupResponse(ArrayList<Member> members) {
        members.sort(Comparator.comparing(Member::getBalance).reversed());
        return members.stream().map(UserAccountConverter::toUserAccountGroupResponse).collect(Collectors.toList());
    }
}
