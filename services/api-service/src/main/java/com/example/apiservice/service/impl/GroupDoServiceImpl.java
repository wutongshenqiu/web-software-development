package com.example.apiservice.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaFoxUtil;
import com.example.apiservice.dao.IBaseDao;
import com.example.apiservice.dao.IGroupDao;
import com.example.apiservice.domain.entity.Group;
import com.example.apiservice.domain.entity.GroupMember;
import com.example.apiservice.domain.entity.StudentInfo;
import com.example.apiservice.domain.entity.User;
import com.example.apiservice.exception.group.*;
import com.example.apiservice.service.IGroupMemberService;
import com.example.apiservice.service.IGroupService;
import com.example.apiservice.service.ISysConfigService;
import com.example.apiservice.service.IUserService;
import com.example.apiservice.type.enumration.GroupMemberStatus;
import com.example.apiservice.type.enumration.GroupStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class GroupDoServiceImpl extends BaseDoServiceImpl<Group, Long> implements IGroupService {
    private final IGroupDao groupDao;
    private final IGroupMemberService groupMemberService;
    private final IUserService userService;
    private final ISysConfigService sysConfigService;

    public GroupDoServiceImpl(final IGroupDao groupDao, final IGroupMemberService groupMemberService, final IUserService userService, final ISysConfigService sysConfigService) {
        this.groupDao = groupDao;
        this.groupMemberService = groupMemberService;
        this.userService = userService;
        this.sysConfigService = sysConfigService;
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
    public Group findActiveGroupByCreatorId(Long creatorId) throws QueryGroupException {
        Optional<Group> group = groupDao.findActiveGroupByCreatorId(creatorId);
        if (group.isEmpty()) {
            throw new QuitGroupException("未找到用户为队长的活跃队伍");
        }

        return group.get();
    }

    @Override
    public Group findActiveGroupByUserId(Long userId) throws QueryGroupException {
        List<GroupMember> groupMembers = groupMemberService.findActiveGroupMembersByUserId(userId);
        if (groupMembers.isEmpty()) throw new QueryGroupException("用户不存在队伍");
        if (groupMembers.size() > 1) {
            log.error("服务器错误，用户有效队伍大于 1");
            log.error(groupMembers.toString());
        }

        return groupMembers.get(0).getGroup();
    }

    @Override
    public Group createGroup(Long userId, String groupName, String groupDescription) throws CreateGroupException {
        User user = userService.findOrElseRaise(userId);

        List<GroupMember> groupMembers = groupMemberService.findActiveGroupMembersByUserId(user.getId());
        if (!groupMembers.isEmpty()) {
            throw new CreateGroupException("用户已有队伍");
        }

        Group group = Group.builder()
                .creator(user)
                .name(groupName)
                .inviteCode(generateInviteCode())
                .description(groupDescription)
                .groupStatus(GroupStatus.ACTIVE)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
        save(group);

        GroupMember groupMember = GroupMember.builder()
                .member(user)
                .group(group)
                .joinTime(LocalDateTime.now())
                .groupMemberStatus(GroupMemberStatus.JOINED)
                .build();
        groupMemberService.save(groupMember);

        return group;
    }

    @Override
    public void deleteGroup(Long creatorId) throws DeleteGroupException {
        // 删除队伍必须满足如下条件
        // 1. 必须是创建者
        Group group = findActiveGroupByCreatorId(StpUtil.getLoginIdAsLong());

        // 并非真的从数据库中删除，只是把状态设置为 DELETED
        group.setGroupStatus(GroupStatus.DELETED);
        group.setUpdateTime(LocalDateTime.now());
        save(group);

        // 删除相应的 group member 字段
        List<GroupMember> groupMembers = groupMemberService.findGroupMembersByGroupId(group.getId());
        for (GroupMember groupMember : groupMembers) {
            groupMember.setGroupMemberStatus(GroupMemberStatus.DELETED);
            groupMember.setUpdateTime(LocalDateTime.now());
//            groupMember.setLeaveTime(LocalDateTime.now());
            groupMemberService.save(groupMember);
        }
    }

    @Override
    public Group joinGroupByInviteCode(Long userId, String inviteCode) throws JoinGroupException {
        User user = userService.findOrElseRaise(userId);
        // 加入队伍必须满足以下条件
        // 1. 不在任何一个队伍中
        List<GroupMember> groupMembers = groupMemberService.findActiveGroupMembersByUserId(user.getId());
        if (!groupMembers.isEmpty()) {
            throw new JoinGroupException("用户已有队伍");
        }
        // 2. 邀请码正确且队伍状态正确
        Group group = findByInviteCode(inviteCode);
        if (group == null) {
            throw new JoinGroupException("邀请码错误");
        }
        if (!group.getGroupStatus().equals(GroupStatus.ACTIVE)) {
            throw new JoinGroupException("队伍不处于活跃状态");
        }
        // 3. 与队长性别一致
        if (!group.getCreator().getGender().equals(user.getGender())) {
            throw new JoinGroupException("用户与队长性别不一致");
        }
        // 4. 队伍人数未满
        List<GroupMember> members = groupMemberService.findActiveGroupMembersByGroupId(group.getId());
        if (members.size() >= sysConfigService.getGroupNumber()) {
            throw new JoinGroupException("对于人数已满");
        }

        GroupMember groupMember = GroupMember.builder()
                .member(user)
                .group(group)
                .joinTime(LocalDateTime.now())
                .groupMemberStatus(GroupMemberStatus.JOINED)
                .build();
        groupMemberService.save(groupMember);

        return group;
    }

    @Override
    public void quitGroup(Long userId) throws QuitGroupException {
        Group group = findActiveGroupByUserId(userId);
        // 退出队伍不符合条件的情况
        // 1. 未加入队伍
        if (group == null) {
            throw new QuitGroupException("用户未加入队伍");
        }
        // 2. 是队长或者且人数大于 1
        if (group.getCreator().getId().equals(userId)) {
            List<GroupMember> groupMembers = groupMemberService.findActiveGroupMembersByGroupId(group.getId());
            if (groupMembers.size() > 1) {
                throw new QuitGroupException("队伍人数大于1，必须先转让队长");
            }
            // 若人数为 1 则需要删除当前队伍
            else {
                group.setUpdateTime(LocalDateTime.now());
                group.setGroupStatus(GroupStatus.DELETED);
                save(group);
            }
        }

        List<GroupMember> groupMembers = groupMemberService.findActiveGroupMembersByUserId(userId);
        if (groupMembers.size() > 1) {
            log.error("服务器错误，用户有效队伍大于 1");
            log.error(groupMembers.toString());
        }
        GroupMember groupMember = groupMembers.get(0);
        groupMember.setLeaveTime(LocalDateTime.now());
        groupMember.setUpdateTime(LocalDateTime.now());
        groupMember.setGroupMemberStatus(GroupMemberStatus.LEFT);
        groupMemberService.save(groupMember);
    }

    @Override
    public void transferGroup(Long oldCreatorId, String assigneeStudentId) throws TransferGroupException {
        Group group = findActiveGroupByCreatorId(oldCreatorId);

        // 不能转让给自己
        StudentInfo creatorStudentInfo = userService.getStudentInfo(oldCreatorId);
        if (creatorStudentInfo.getStudentId().equals(assigneeStudentId)) {
            throw new TransferGroupException("不能转让给自己");
        }

        List<GroupMember> groupMembers = groupMemberService.findActiveGroupMembersByGroupId(group.getId());
        for (GroupMember groupMember : groupMembers) {
            User member = groupMember.getMember();
            StudentInfo studentInfo = userService.getStudentInfo(member.getId());
            // 转让队伍
            if (studentInfo.getStudentId().equals(assigneeStudentId)) {
                group.setCreator(member);
                save(group);
                return;
            }
        }

        throw new TransferGroupException("被转让的成员不属于当前队伍");
    }

    @Override
    public List<GroupMember> findGroupMembersByGroupId(Long groupId) {
        return groupMemberService.findGroupMembersByGroupId(groupId);
    }
}
