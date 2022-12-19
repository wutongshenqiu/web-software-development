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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

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
        Group group = groupService.createGroup(StpUtil.getLoginIdAsLong(), groupCreateDto.getName(), groupCreateDto.getDescription());

        GroupCreateResponseDto groupResponseDto = new GroupCreateResponseDto()
                .setGroupId(group.getId())
                .setInviteCode(group.getInviteCode());

        return ResponseEntity.ok(ResponseDto.ok().setMessage("操作成功").setData(groupResponseDto));
    }

    @PostMapping("/del")
    public ResponseEntity<ResponseDto> delete() {
        groupService.deleteGroup(StpUtil.getLoginIdAsLong());

        return ResponseEntity.ok(ResponseDto.ok().setMessage("操作成功"));
    }

    @PostMapping("/join")
    public ResponseEntity<ResponseDto> join(@Valid @RequestBody GroupJoinDto groupJoinDto) {
        Group group = groupService.joinGroupByInviteCode(StpUtil.getLoginIdAsLong(), groupJoinDto.getInviteCode());
        GroupJoinResponseDto joinResponseDto = new GroupJoinResponseDto().setGroupId(group.getId());

        return ResponseEntity.ok(ResponseDto.ok().setMessage("加入队伍成功").setData(joinResponseDto));
    }

    @PostMapping("/quit")
    public ResponseEntity<ResponseDto> quit() {
        groupService.quitGroup(StpUtil.getLoginIdAsLong());

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

            List<GroupMemberDto> groupMemberDtoList = new ArrayList<>();
            for (GroupMember gMember : members) {
                User member = gMember.getMember();
                StudentInfo studentInfo = userService.getStudentInfo(member.getId());
                GroupMemberDto groupMemberDto = new GroupMemberDto()
                        .setStudentId(studentInfo.getStudentId())
                        .setStudentName(member.getName());
                groupMemberDtoList.add(groupMemberDto);
            }
            GroupDetailDto groupDetailDto = new GroupDetailDto()
                    .setGroupId(group.getId())
                    .setGroupName(group.getName())
                    .setInviteCode(group.getInviteCode())
                    .setMembers(groupMemberDtoList);
            responseDto.setData(groupDetailDto);
        }

        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/transfer")
    public ResponseEntity<ResponseDto> transferGroup(@Valid @RequestBody GroupTransferDto groupTransferDto) {
        groupService.transferGroup(StpUtil.getLoginIdAsLong(), groupTransferDto.getStudentId());

        return ResponseEntity.ok(ResponseDto.ok().setMessage("操作成功"));
    }
}
