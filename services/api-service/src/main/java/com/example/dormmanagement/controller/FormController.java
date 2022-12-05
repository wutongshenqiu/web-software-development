package com.example.dormmanagement.controller;

import com.example.dormmanagement.domain.dto.ResponseDto;
import com.example.dormmanagement.domain.dto.form.FormCreateDto;
import com.example.dormmanagement.domain.dto.form.FormCreateResponseDto;
import com.example.dormmanagement.domain.dto.form.FormDetailDto;
import com.example.dormmanagement.domain.dto.form.FormInfoDto;
import com.example.dormmanagement.service.IFormService;
import com.example.dormmanagement.type.enumration.FormStatus;
import com.example.dormmanagement.util.MockUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/order")
public class FormController {
    @Autowired
    IFormService formService;

    @PostMapping("/create")
    public ResponseEntity<ResponseDto> create(@Valid @RequestBody FormCreateDto formCreateDto) {
        formService.sendFormToMQ(formCreateDto);

        FormCreateResponseDto formCreateResponseDto = new FormCreateResponseDto()
                .setFormId(new Random().nextLong(1000));

        return ResponseEntity.ok(ResponseDto.ok().setMessage("Create order").setData(formCreateResponseDto));
    }

    @GetMapping("/list")
    public ResponseEntity<ResponseDto> getFormList() {
        List<FormDetailDto> formDetailDtoList = new ArrayList<>();

        for (int i = 0; i < new Random().nextInt(0, 5); i++) {
            FormDetailDto formDetailDto = new FormDetailDto()
                    .setFormId(new Random().nextLong(2000L))
                    .setStatus(FormStatus.CREATED)
                    .setBuildingName(new Random().nextInt(10) + "号楼")
                    .setGroupName(MockUtil.getRandomString(4))
                    .setSubmitTime(LocalDateTime.now())
                    .setResultContent("Created");
            formDetailDtoList.add(formDetailDto);
        }

        return ResponseEntity.ok(ResponseDto.ok().setMessage("My order").setData(formDetailDtoList));
    }

    @GetMapping("/info")
    public ResponseEntity<ResponseDto> getFormInfo(@RequestParam Long order_id) {
        FormInfoDto formInfoDto = new FormInfoDto().setRoomId(new Random().nextLong(1000L))
                .setStatus(FormStatus.SUCCESS);

        return ResponseEntity.ok(ResponseDto.ok().setMessage("Order info").setData(formInfoDto));
    }
}
