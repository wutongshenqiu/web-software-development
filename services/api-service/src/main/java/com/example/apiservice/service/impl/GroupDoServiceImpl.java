package com.example.apiservice.service.impl;

import cn.dev33.satoken.util.SaFoxUtil;
import com.example.apiservice.dao.IBaseDao;
import com.example.apiservice.dao.IGroupDao;
import com.example.apiservice.domain.entity.Group;
import com.example.apiservice.domain.entity.GroupMember;
import com.example.apiservice.service.IGroupMemberService;
import com.example.apiservice.service.IGroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class GroupDoServiceImpl extends BaseDoServiceImpl<Group, Long> implements IGroupService {
    private final IGroupDao groupDao;
    private final IGroupMemberService groupMemberService;

    public GroupDoServiceImpl(final IGroupDao groupDao, final IGroupMemberService groupMemberService) {
        this.groupDao = groupDao;
        this.groupMemberService = groupMemberService;
    }

    @Override
    public IBaseDao<Group, Long> getBaseDao() {
        return this.groupDao;
    }

    @Override
    public boolean existsByInviteCode(String inviteCode) {
        return groupDao.findGroupByInviteCode(inviteCode).isPresent();
    }

    @Override
    public Group findByInviteCode(String inviteCode) {
        return groupDao.findGroupByInviteCode(inviteCode).orElse(null);
    }

    @Override
    public String generateInviteCode() {
        String inviteCode = SaFoxUtil.getRandomString(64);

        while (existsByInviteCode(inviteCode)) {
            inviteCode = SaFoxUtil.getRandomString(64);
        }

        return inviteCode;
    }

    @Override
    public Group findActiveGroupByCreatorId(Long creatorId) {
        return groupDao.findActiveGroupByCreatorId(creatorId).orElse(null);
    }

    @Override
    public Group findActiveGroupByUserId(Long userId) {
        List<GroupMember> groupMembers = groupMemberService.findActiveGroupMembersByUserId(userId);
        if (groupMembers.isEmpty()) return null;
        if (groupMembers.size() > 1) {
            log.error("服务器错误，用户有效队伍大于 1");
            log.error(groupMembers.toString());
        }

        return groupMembers.get(0).getGroup();
    }
}
