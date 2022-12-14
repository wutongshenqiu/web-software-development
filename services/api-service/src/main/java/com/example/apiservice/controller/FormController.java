package com.example.apiservice.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.example.apiservice.domain.dto.ResponseDto;
import com.example.apiservice.domain.dto.form.*;
import com.example.apiservice.domain.entity.*;
import com.example.apiservice.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    IGroupService groupService;

    @PostMapping("/create")
    public ResponseEntity<ResponseDto> create(@Valid @RequestBody FormCreateDto formCreateDto) {
        Form form = formService.submitForm(StpUtil.getLoginIdAsLong(), formCreateDto.getGroupId(), formCreateDto.getBuildingId());
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
        Form form = formService.findBySubmitterId(StpUtil.getLoginIdAsLong(), order_id);

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
