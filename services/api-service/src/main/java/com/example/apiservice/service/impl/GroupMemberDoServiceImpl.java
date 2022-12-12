package com.example.apiservice.service.impl;

import com.example.apiservice.dao.IBaseDao;
import com.example.apiservice.dao.IGroupMemberDao;
import com.example.apiservice.domain.entity.GroupMember;
import com.example.apiservice.service.IGroupMemberService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupMemberDoServiceImpl extends BaseDoServiceImpl<GroupMember, Long> implements IGroupMemberService {
    private final IGroupMemberDao groupMemberDao;

    public GroupMemberDoServiceImpl(final IGroupMemberDao groupMemberDao) {
        this.groupMemberDao = groupMemberDao;
    }

    @Override
    public IBaseDao<GroupMember, Long> getBaseDao() {
        return this.groupMemberDao;
    }

    @Override
    public List<GroupMember> findGroupMembersByUserId(Long userId) {
        return groupMemberDao.findGroupMembersByMemberId(userId);
    }

    @Override
    public List<GroupMember> findActiveGroupMembersByUserId(Long userId) {
        return groupMemberDao.findActiveGroupMembersByMemberId(userId);
    }

    @Override
    public List<GroupMember> findGroupMembersByGroupId(Long groupId) {
        return groupMemberDao.findGroupMembersByGroupId(groupId);
    }

    @Override
    public List<GroupMember> findActiveGroupMembersByGroupId(Long groupId) {
        return groupMemberDao.findActiveGroupMembersByGroupId(groupId);
    }
}
