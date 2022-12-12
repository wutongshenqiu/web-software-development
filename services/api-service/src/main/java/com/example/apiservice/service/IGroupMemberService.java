package com.example.apiservice.service;

import com.example.apiservice.domain.entity.GroupMember;

import java.util.List;

public interface IGroupMemberService extends IBaseService<GroupMember, Long> {
    List<GroupMember> findGroupMembersByUserId(Long userId);

    List<GroupMember> findActiveGroupMembersByUserId(Long userId);

    List<GroupMember> findGroupMembersByGroupId(Long groupId);

    List<GroupMember> findActiveGroupMembersByGroupId(Long groupId);
}
