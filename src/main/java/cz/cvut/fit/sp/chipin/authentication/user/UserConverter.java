package cz.cvut.fit.sp.chipin.authentication.user;

import cz.cvut.fit.sp.chipin.base.member.Member;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class UserConverter {

    private static GroupResponse toGroupResponse(Member member) {
        return new GroupResponse(member.getUser().getId(), member.getUser().getName(), member.getBalance());
    }

    public static List<GroupResponse> toUsersGroupResponse(ArrayList<Member> members) {
        members.sort(Comparator.comparing(Member::getBalance).reversed());
        return members.stream().map(UserConverter::toGroupResponse).collect(Collectors.toList());
    }
}
