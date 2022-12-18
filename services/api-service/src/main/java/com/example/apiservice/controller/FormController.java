package com.example.apiservice.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.example.apiservice.domain.dto.ResponseDto;
import com.example.apiservice.domain.dto.form.*;
import com.example.apiservice.domain.entity.*;
import com.example.apiservice.service.*;
import com.example.apiservice.type.enumration.FormStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/order")
public class FormController {
    @Autowired
    IFormService formService;

    @Autowired
    IBuildingService buildingService;

    @Autowired
    IGroupService groupService;

    @Autowired
    IGroupMemberService groupMemberService;

    @Autowired
    IUserService userService;

    @PostMapping("/create")
    public ResponseEntity<ResponseDto> create(@Valid @RequestBody FormCreateDto formCreateDto) {
        // 1. 宿舍楼不存在
        Building building = buildingService.findOrElseRaise(formCreateDto.getBuildingId());

        // 2. 未组队
        User user = userService.findOrElseRaise(StpUtil.getLoginIdAsLong());
        Group group = groupService.findActiveGroupByUserId(user.getId());
        if (group == null) {
            return new ResponseEntity<>(
                    ResponseDto.ok().setMessage("请先组队"),
                    HttpStatus.FORBIDDEN
            );
        }

        // 3. 队伍号不匹配
        if (!group.getId().equals(formCreateDto.getGroupId())) {
            return new ResponseEntity<>(
                    ResponseDto.ok().setMessage("队伍号不匹配"),
                    HttpStatus.FORBIDDEN
            );
        }

        Form form = Form.builder()
                .submitter(user)
                .group(group)
                .building(building)
                .formStatus(FormStatus.CREATED)
                .build();
        formService.save(form);

        List<GroupMember> groupMembers = groupMemberService.findGroupMembersByGroupId(group.getId());
        List<User> users = new ArrayList<>();
        for (GroupMember groupMember : groupMembers) {
            users.add(groupMember.getMember());
        }

        FormInRabbitDto formInRabbitDto = new FormInRabbitDto()
                .setFormId(form.getId())
                .setGroupId(group.getId())
                .setBuildingId(building.getId())
                .setGroupMembersFromUsers(users);
        formService.sendFormToMQ(formInRabbitDto);
        log.info(formInRabbitDto.toString());

        FormCreateResponseDto formCreateResponseDto = new FormCreateResponseDto()
                .setFormId(form.getId());

        return ResponseEntity.ok(ResponseDto.ok().setMessage("操作成功").setData(formCreateResponseDto));
    }

    @GetMapping("/list")
    public ResponseEntity<ResponseDto> getFormList() {
        List<Form> forms = formService.findAllBySubmitterId(StpUtil.getLoginIdAsLong());

        List<FormDetailDto> formDetailDtoList = new ArrayList<>();
        for (Form form : forms) {
            FormDetailDto formDetailDto = new FormDetailDto()
                    .setFormId(form.getId())
                    .setStatus(form.getFormStatus())
                    .setBuildingName(form.getBuilding().getName())
                    .setGroupName(form.getGroup().getName())
                    .setSubmitTime(form.getCreateTime())
                    .setResultContent(form.getResultContent());
            formDetailDtoList.add(formDetailDto);
        }
        Map<String, Object> data = new HashMap<>();
        data.put("rows", formDetailDtoList);

        return ResponseEntity.ok(ResponseDto.ok().setMessage("订单列表").setData(data));
    }

    @GetMapping("/info")
    public ResponseEntity<ResponseDto> getFormInfo(@RequestParam Long order_id) {
        Form form = formService.findOrElseRaise(order_id);

        // 只能获取自身队伍的订单信息
        Group group = groupService.findActiveGroupByUserId(StpUtil.getLoginIdAsLong());
        if (!group.getId().equals(form.getGroup().getId())) {
            return new ResponseEntity<>(
                    ResponseDto.ok().setMessage("订单不存在"),
                    HttpStatus.NOT_FOUND
            );
        }

        // 0 作为没有房间的默认值
        Room room = form.getRoom();
        Long roomId = 0L;
        if (room != null) {
            roomId = room.getId();
        }
        FormInfoDto formInfoDto = new FormInfoDto()
                .setRoomId(roomId)
                .setStatus(form.getFormStatus());

        return ResponseEntity.ok(ResponseDto.ok().setMessage("订单信息").setData(formInfoDto));
    }
}
