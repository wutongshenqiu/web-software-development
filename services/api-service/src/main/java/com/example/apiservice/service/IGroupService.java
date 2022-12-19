package com.example.apiservice.service;

import com.example.apiservice.domain.entity.Group;
import com.example.apiservice.domain.entity.GroupMember;

import java.util.List;

public interface IGroupService extends IBaseService<Group, Long> {
    boolean existsByInviteCode(String inviteCode);
    Group findByInviteCode(String inviteCode);
    String generateInviteCode();
    Group findActiveGroupByCreatorId(Long creatorId);
    Group findActiveGroupByUserId(Long userId);
    Group createGroup(Long userId, String groupName, String groupDescription);
    void deleteGroup(Long creatorId);
    Group joinGroupByInviteCode(Long userId, String inviteCode);
    void quitGroup(Long userId);
    void transferGroup(Long oldCreatorId, String assigneeStudentId);
    List<GroupMember> findGroupMembersByGroupId(Long groupId);
}
