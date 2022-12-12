package com.example.apiservice.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.example.apiservice.domain.dto.ResponseDto;
import com.example.apiservice.domain.dto.group.*;
import com.example.apiservice.domain.entity.Group;
import com.example.apiservice.domain.entity.GroupMember;
import com.example.apiservice.domain.entity.StudentInfo;
import com.example.apiservice.domain.entity.User;
import com.example.apiservice.service.IGroupMemberService;
import com.example.apiservice.service.IGroupService;
import com.example.apiservice.service.IUserService;
import com.example.apiservice.type.enumration.GroupMemberStatus;
import com.example.apiservice.type.enumration.GroupStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;

// TODO: 将复杂的处理逻辑移动到 service 中
// FIXME: 考虑并发问题
@Slf4j
@RestController
@RequestMapping("/team")
public class GroupController {
    @Autowired
    IGroupService groupService;

    @Autowired
    IGroupMemberService groupMemberService;

    @Autowired
    IUserService userService;

    @PostMapping("/create")
    public ResponseEntity<ResponseDto> create(@Valid @RequestBody GroupCreateDto groupCreateDto) {
        User user = userService.findOrElseRaise(StpUtil.getLoginIdAsLong());
        // 创建队伍需要满足用户不在任何一个队伍中
        List<GroupMember> groupMembers = groupMemberService.findActiveGroupMembersByUserId(user.getId());
        if (!groupMembers.isEmpty()) {
            return new ResponseEntity<>(
                    ResponseDto.ok().setMessage("已有队伍"),
                    HttpStatus.FORBIDDEN
            );
        }

        Group group = Group.builder()
                .creator(user)
                .name(groupCreateDto.getName())
                .inviteCode(groupService.generateInviteCode())
                .description(groupCreateDto.getDescription())
                .groupStatus(GroupStatus.ACTIVE)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
        groupService.save(group);

        GroupMember groupMember = GroupMember.builder()
                .member(user)
                .group(group)
                .joinTime(LocalDateTime.now())
                .groupMemberStatus(GroupMemberStatus.JOINED)
                .build();
        groupMemberService.save(groupMember);

        GroupCreateResponseDto groupResponseDto = new GroupCreateResponseDto()
                .setGroupId(group.getId())
                .setInviteCode(group.getInviteCode());

        return ResponseEntity.ok(ResponseDto.ok().setMessage("操作成功").setData(groupResponseDto));
    }

    @PostMapping("/del")
    public ResponseEntity<ResponseDto> delete(@Valid @RequestBody GroupDeleteDto groupDeleteDto) {
        Group group = groupService.findOrElseRaise(groupDeleteDto.getTeamId());

        // 删除队伍必须满足如下条件
        // 1. 必须是创建者
        if (!group.getCreator().getId().equals(StpUtil.getLoginIdAsLong())) {
            return new ResponseEntity<>(
                    ResponseDto.ok().setMessage("不是队伍创建者"),
                    HttpStatus.FORBIDDEN
            );
        }
        // 2. 队伍处于 active 状态
        if (!group.getGroupStatus().equals(GroupStatus.ACTIVE)) {
            return new ResponseEntity<>(
                    // TODO: update return message
                    ResponseDto.ok().setMessage("队伍状态非法"),
                    HttpStatus.FORBIDDEN
            );
        }

        // 并非真的从数据库中删除，只是把状态设置为 DELETED
        group.setGroupStatus(GroupStatus.DELETED);
        group.setUpdateTime(LocalDateTime.now());
        groupService.save(group);
        // 删除相应的 group member 字段
        List<GroupMember> groupMembers = groupMemberService.findGroupMembersByGroupId(group.getId());
        for (GroupMember groupMember : groupMembers) {
            groupMember.setGroupMemberStatus(GroupMemberStatus.DELETED);
            groupMember.setUpdateTime(LocalDateTime.now());
            groupMember.setLeaveTime(LocalDateTime.now());
            groupMemberService.save(groupMember);
        }

        return ResponseEntity.ok(ResponseDto.ok().setMessage("操作成功"));
    }

    @PostMapping("/join")
    public ResponseEntity<ResponseDto> join(@Valid @RequestBody GroupJoinDto groupJoinDto) {
        User user = userService.findOrElseRaise(StpUtil.getLoginIdAsLong());
        // 加入队伍必须满足以下条件
        // 1. 不在任何一个队伍中
        List<GroupMember> groupMembers = groupMemberService.findActiveGroupMembersByUserId(user.getId());
        if (!groupMembers.isEmpty()) {
            return new ResponseEntity<>(
                    ResponseDto.ok().setMessage("已有队伍"),
                    HttpStatus.FORBIDDEN
            );
        }
        // 2. 邀请码正确且队伍状态正确
        Group group = groupService.findByInviteCode(groupJoinDto.getInviteCode());
        if (group == null) {
            return new ResponseEntity<>(
                    ResponseDto.ok().setMessage("邀请码错误"),
                    HttpStatus.FORBIDDEN
            );
        }
        if (!group.getGroupStatus().equals(GroupStatus.ACTIVE)) {
            return new ResponseEntity<>(
                    ResponseDto.ok().setMessage("队伍状态不合法"),
                    HttpStatus.FORBIDDEN
            );
        }
        // 3. 性别一致
        if (!group.getCreator().getGender().equals(user.getGender())) {
            return new ResponseEntity<>(
                    ResponseDto.ok().setMessage("与队长性别不一致"),
                    HttpStatus.FORBIDDEN
            );
        }

        GroupMember groupMember = GroupMember.builder()
                .member(user)
                .group(group)
                .joinTime(LocalDateTime.now())
                .groupMemberStatus(GroupMemberStatus.JOINED)
                .build();
        groupMemberService.save(groupMember);

        GroupJoinResponseDto joinResponseDto = new GroupJoinResponseDto().setGroupId(group.getId());

        return ResponseEntity.ok(ResponseDto.ok().setMessage("加入队伍成功").setData(joinResponseDto));
    }

    @PostMapping("/quit")
    public ResponseEntity<ResponseDto> quit(@Valid @RequestBody GroupQuitDto groupQuitDto) {
        Long userId = StpUtil.getLoginIdAsLong();

        Group group = groupService.findActiveGroupByUserId(userId);
        // 退出队伍不符合条件的情况
        // 1. group id 和已加入的队伍 id 不相等
        if (!group.getId().equals(groupQuitDto.getGroupId())) {
            return new ResponseEntity<>(
                    ResponseDto.ok().setMessage("未加入队伍 " + groupQuitDto.getGroupId()),
                    HttpStatus.FORBIDDEN
            );
        }
        // 2. 是队长或者且人数不为 1
        if (group.getCreator().getId().equals(userId)) {
            List<GroupMember> groupMembers = groupMemberService.findActiveGroupMembersByGroupId(group.getId());
            if (groupMembers.size() > 1) {
                return new ResponseEntity<>(
                        ResponseDto.ok().setMessage("必须先转让队长"),
                        HttpStatus.FORBIDDEN
                );
            }
            // 若人数为 1 则需要删除当前队伍
            else {
                group.setUpdateTime(LocalDateTime.now());
                group.setGroupStatus(GroupStatus.DELETED);
                groupService.save(group);
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

        return ResponseEntity.ok(ResponseDto.ok().setMessage("操作成功"));
    }

    @GetMapping("/my")
    public ResponseEntity<ResponseDto> my() {
        ResponseDto responseDto = ResponseDto.ok().setMessage("操作成功");

        List<GroupMember> groupMembers = groupMemberService.findActiveGroupMembersByUserId(StpUtil.getLoginIdAsLong());
        if (!groupMembers.isEmpty()) {
            if (groupMembers.size() > 1) {
                log.error("服务器错误，用户有效队伍大于 1");
                log.error(groupMembers.toString());
            }
            GroupMember groupMember = groupMembers.get(0);
            Group group = groupMember.getGroup();
            List<GroupMember> members = groupMemberService.findActiveGroupMembersByGroupId(group.getId());

            List<GroupMemberDto> groupMemberDtos = new ArrayList<>();
            for (GroupMember gMember : members) {
                User member = gMember.getMember();
                StudentInfo studentInfo = userService.getStudentInfo(member.getId());
                GroupMemberDto groupMemberDto = new GroupMemberDto()
                        .setStudentId(studentInfo.getStudentId())
                        .setStudentName(member.getName());
                groupMemberDtos.add(groupMemberDto);
            }
            GroupDetailDto groupDetailDto = new GroupDetailDto()
                    .setGroupId(group.getId())
                    .setGroupName(group.getName())
                    .setInviteCode(group.getInviteCode())
                    .setMembers(groupMemberDtos);
            responseDto.setData(groupDetailDto);
        }

        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/transfer")
    public ResponseEntity<ResponseDto> transferGroup(@Valid @RequestBody GroupTransferDto groupTransferDto) {
        Long userId = StpUtil.getLoginIdAsLong();
        // 转让队伍包括以下操作
        // 1. 有队长的队伍
        Group group = groupService.findActiveGroupByCreatorId(userId);
        if (group == null) {
            return new ResponseEntity<>(
                    ResponseDto.ok().setMessage("没有队伍"),
                    HttpStatus.FORBIDDEN
            );
        }

        // 2. 不能转让给自己
        StudentInfo creatorStudentInfo = userService.getStudentInfo(userId);
        if (creatorStudentInfo.getStudentId().equals(groupTransferDto.getStudentId())) {
            return new ResponseEntity<>(
                    ResponseDto.ok().setMessage("不能转让给自己"),
                    HttpStatus.FORBIDDEN
            );
        }

        // 3. 转让的成员必须在队伍中
        List<GroupMember> groupMembers = groupMemberService.findActiveGroupMembersByGroupId(group.getId());
        for (GroupMember groupMember : groupMembers) {
            User member = groupMember.getMember();
            StudentInfo studentInfo = userService.getStudentInfo(member.getId());
            // 转让队伍
            if (studentInfo.getStudentId().equals(groupTransferDto.getStudentId())) {
                group.setCreator(member);
                groupService.save(group);

                return ResponseEntity.ok(ResponseDto.ok().setMessage("操作成功"));
            }
        }

        return new ResponseEntity<>(ResponseDto.ok().setMessage("不是当前队伍成员"), HttpStatus.FORBIDDEN);
    }
}
