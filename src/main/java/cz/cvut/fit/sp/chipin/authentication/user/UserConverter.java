package cz.cvut.fit.sp.chipin.authentication.user;

import cz.cvut.fit.sp.chipin.base.member.Member;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


public class UserConverter {

    private static UserGroupResponse toUserGroupResponse(Member member) {
        return new UserGroupResponse(member.getUser().getId(), member.getUser().getName(), member.getBalance());
    }

    public static List<UserGroupResponse> toUsersGroupResponse(ArrayList<Member> members) {
        members.sort(Comparator.comparing(Member::getBalance).reversed());
        return members.stream().map(UserConverter::toUserGroupResponse).collect(Collectors.toList());
    }
}
