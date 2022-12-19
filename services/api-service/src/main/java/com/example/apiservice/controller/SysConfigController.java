package com.example.apiservice.controller;

import com.example.apiservice.domain.dto.ResponseDto;
import com.example.apiservice.service.ISysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/sys")
public class SysConfigController {
    @Autowired
    ISysConfigService sysConfigService;

    @GetMapping("/opentime")
    public ResponseEntity<?> openTime() {
        Map<String, Object> data = new HashMap<>();
        data.put("start_time", sysConfigService.getStartTime());
        data.put("end_time", sysConfigService.getEndTime());

        return ResponseEntity.ok(ResponseDto.ok().setMessage("开放时间").setData(data));
    }

    @GetMapping("/classlimit")
    public ResponseEntity<?> classLimit() {
        Map<String, Integer> data = new HashMap<>();
        data.put("class_limit", sysConfigService.getClassLimit());

        return ResponseEntity.ok(ResponseDto.ok().setMessage("班级限制").setData(data));
    }

    @GetMapping("/groupnum")
    public ResponseEntity<?> groupNumberLimit() {
        Map<String, Integer> data = new HashMap<>();
        data.put("group_limit", sysConfigService.getGroupLimit());
        data.put("group_num", sysConfigService.getGroupNumber());

        return ResponseEntity.ok(ResponseDto.ok().setMessage("队伍配置").setData(data));
    }
}
