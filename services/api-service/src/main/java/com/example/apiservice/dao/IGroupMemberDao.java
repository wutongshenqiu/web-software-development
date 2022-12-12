package com.example.apiservice.dao;

import com.example.apiservice.domain.entity.GroupMember;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IGroupMemberDao extends IBaseDao<GroupMember, Long> {
    List<GroupMember> findGroupMembersByMemberId(Long memberId);

    @Query(value = "FROM GroupMember gm WHERE gm.member.id = ?1 AND gm.groupMemberStatus = 0")
    List<GroupMember> findActiveGroupMembersByMemberId(Long memberId);

    List<GroupMember> findGroupMembersByGroupId(Long groupId);

    @Query(value = "FROM GroupMember gm WHERE gm.group.id = ?1 AND gm.groupMemberStatus = 0")
    List<GroupMember> findActiveGroupMembersByGroupId(Long groupId);
}
