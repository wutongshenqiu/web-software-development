package com.example.dormmanagement.controller;

import com.example.dormmanagement.domain.dto.ResponseDto;
import com.example.dormmanagement.domain.dto.group.*;
import com.example.dormmanagement.util.MockUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/team")
public class GroupController {
    @PostMapping("/create")
    public ResponseEntity<ResponseDto> create(@Valid @RequestBody GroupCreateDto groupCreateDto) {
        GroupCreateResponseDto groupResponseDto = new GroupCreateResponseDto()
                .setGroupId(new Random().nextLong(1000))
                .setInviteCode(MockUtil.getRandomString(10));

        return ResponseEntity.ok(ResponseDto.ok().setMessage("Create team").setData(groupResponseDto));
    }

    @PostMapping("/del")
    public ResponseEntity<ResponseDto> delete(@Valid @RequestBody GroupDeleteDto groupDeleteDto) {
        return ResponseEntity.ok(ResponseDto.ok().setMessage("Delete team"));
    }

    @PostMapping("/join")
    public ResponseEntity<ResponseDto> join(@Valid @RequestBody GroupJoinDto groupJoinDto) {
        GroupJoinResponseDto joinResponseDto = new GroupJoinResponseDto().setGroupId(new Random().nextLong(1000));

        return ResponseEntity.ok(ResponseDto.ok().setMessage("Join team").setData(joinResponseDto));
    }

    @PostMapping("/quit")
    public ResponseEntity<ResponseDto> quit(@Valid @RequestBody GroupQuitDto groupQuitDto) {
        return ResponseEntity.ok(ResponseDto.ok().setMessage("Quit team"));
    }

    @GetMapping("/my")
    public ResponseEntity<ResponseDto> my() {
        List<GroupMemberDto> members = new ArrayList<>();
        for (int i = 0; i < new Random().nextInt(2, 4); i++) {
            members.add(new GroupMemberDto().setStudentId(
                    new Random().nextLong(1000)).setStudentName(MockUtil.getFakeName()));
        }

        GroupDetailDto groupDetailDto = new GroupDetailDto().setGroupId(new Random().nextLong(1000))
                .setGroupName("qiufeng")
                .setInviteCode(MockUtil.getRandomString(10))
                .setMembers(members);

        return ResponseEntity.ok(ResponseDto.ok().setMessage("My team").setData(groupDetailDto));
    }

    @PostMapping("/transfer")
    public ResponseEntity<ResponseDto> transferGroup(@Valid @RequestBody GroupTransferDto groupTransferDto) {
        return ResponseEntity.ok(ResponseDto.ok().setMessage("Transfer team"));
    }
}
