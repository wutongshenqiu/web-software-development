package com.example.apiservice.controller;

import com.example.apiservice.domain.dto.ResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/sys")
public class SysConfigController {
    @GetMapping("/opentime")
    public ResponseEntity<?> openTime() {
        Map<String, String> data = new HashMap<>();
        data.put("start_time", "2022/12/10");
        data.put("end_time", "2022/12/13");

        return ResponseEntity.ok(ResponseDto.ok().setMessage("开放时间").setData(data));
    }

    @GetMapping("/classlimit")
    public ResponseEntity<?> classLimit() {
        Map<String, Integer> data = new HashMap<>();
        data.put("class_limit", 1);

        return ResponseEntity.ok(ResponseDto.ok().setMessage("班级限制").setData(data));
    }

    @GetMapping("/groupnum")
    public ResponseEntity<?> groupNumberLimit() {
        Map<String, Integer> data = new HashMap<>();
        data.put("group_limit", 0);
        data.put("group_num", 4);

        return ResponseEntity.ok(ResponseDto.ok().setMessage("队伍配置").setData(data));
    }
}
