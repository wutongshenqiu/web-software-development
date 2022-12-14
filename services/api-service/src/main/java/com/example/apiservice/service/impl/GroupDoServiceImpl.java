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
            throw new QuitGroupException("???????????????????????????????????????");
        }

        return group.get();
    }

    @Override
    public Group findActiveGroupByUserId(Long userId) throws QueryGroupException {
        List<GroupMember> groupMembers = groupMemberService.findActiveGroupMembersByUserId(userId);
        if (groupMembers.isEmpty()) throw new QueryGroupException("?????????????????????");
        if (groupMembers.size() > 1) {
            log.error("?????????????????????????????????????????? 1");
            log.error(groupMembers.toString());
        }

        return groupMembers.get(0).getGroup();
    }

    @Override
    public Group createGroup(Long userId, String groupName, String groupDescription) throws CreateGroupException {
        User user = userService.findOrElseRaise(userId);

        List<GroupMember> groupMembers = groupMemberService.findActiveGroupMembersByUserId(user.getId());
        if (!groupMembers.isEmpty()) {
            throw new CreateGroupException("??????????????????");
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
        // ????????????????????????????????????
        // 1. ??????????????????
        Group group = findActiveGroupByCreatorId(StpUtil.getLoginIdAsLong());

        // ???????????????????????????????????????????????????????????? DELETED
        group.setGroupStatus(GroupStatus.DELETED);
        group.setUpdateTime(LocalDateTime.now());
        save(group);

        // ??????????????? group member ??????
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
        // ????????????????????????????????????
        // 1. ???????????????????????????
        List<GroupMember> groupMembers = groupMemberService.findActiveGroupMembersByUserId(user.getId());
        if (!groupMembers.isEmpty()) {
            throw new JoinGroupException("??????????????????");
        }
        // 2. ????????????????????????????????????
        Group group = findByInviteCode(inviteCode);
        if (group == null) {
            throw new JoinGroupException("???????????????");
        }
        if (!group.getGroupStatus().equals(GroupStatus.ACTIVE)) {
            throw new JoinGroupException("???????????????????????????");
        }
        // 3. ?????????????????????
        if (!group.getCreator().getGender().equals(user.getGender())) {
            throw new JoinGroupException("??????????????????????????????");
        }
        // 4. ??????????????????
        List<GroupMember> members = groupMemberService.findActiveGroupMembersByGroupId(group.getId());
        if (members.size() >= sysConfigService.getGroupNumber()) {
            throw new JoinGroupException("??????????????????");
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
        // ????????????????????????????????????
        // 1. ???????????????
        if (group == null) {
            throw new QuitGroupException("?????????????????????");
        }
        // 2. ?????????????????????????????? 1
        if (group.getCreator().getId().equals(userId)) {
            List<GroupMember> groupMembers = groupMemberService.findActiveGroupMembersByGroupId(group.getId());
            if (groupMembers.size() > 1) {
                throw new QuitGroupException("??????????????????1????????????????????????");
            }
            // ???????????? 1 ???????????????????????????
            else {
                group.setUpdateTime(LocalDateTime.now());
                group.setGroupStatus(GroupStatus.DELETED);
                save(group);
            }
        }

        List<GroupMember> groupMembers = groupMemberService.findActiveGroupMembersByUserId(userId);
        if (groupMembers.size() > 1) {
            log.error("?????????????????????????????????????????? 1");
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

        // ?????????????????????
        StudentInfo creatorStudentInfo = userService.getStudentInfo(oldCreatorId);
        if (creatorStudentInfo.getStudentId().equals(assigneeStudentId)) {
            throw new TransferGroupException("?????????????????????");
        }

        List<GroupMember> groupMembers = groupMemberService.findActiveGroupMembersByGroupId(group.getId());
        for (GroupMember groupMember : groupMembers) {
            User member = groupMember.getMember();
            StudentInfo studentInfo = userService.getStudentInfo(member.getId());
            // ????????????
            if (studentInfo.getStudentId().equals(assigneeStudentId)) {
                group.setCreator(member);
                save(group);
                return;
            }
        }

        throw new TransferGroupException("???????????????????????????????????????");
    }

    @Override
    public List<GroupMember> findGroupMembersByGroupId(Long groupId) {
        return groupMemberService.findGroupMembersByGroupId(groupId);
    }
}
