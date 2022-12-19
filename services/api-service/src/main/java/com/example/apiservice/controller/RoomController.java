package com.example.apiservice.controller;

import com.example.apiservice.domain.dto.ResponseDto;
import com.example.apiservice.domain.dto.room.BuildingDetailResponseDto;
import com.example.apiservice.domain.dto.room.BuildingResponseDto;
import com.example.apiservice.domain.dto.room.RoomDetailResponseDto;
import com.example.apiservice.domain.entity.Building;
import com.example.apiservice.domain.entity.Room;
import com.example.apiservice.service.IBuildingService;
import com.example.apiservice.service.IRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/room")
public class RoomController {
    @Autowired
    IBuildingService buildingService;

    @Autowired
    IRoomService roomService;

    @GetMapping("/buildinglist")
    public ResponseEntity<ResponseDto> getBuildingList() {
        List<Building> buildings = buildingService.findAll();

        List<BuildingResponseDto> buildingList = new ArrayList<>();
        for (Building building : buildings) {
            BuildingResponseDto buildingResponseDto = new BuildingResponseDto()
                    .setBuildingId(building.getId())
                    .setBuildingName(building.getName());
            buildingList.add(buildingResponseDto);
        }

        Map<String, Object> data = new HashMap<>();
        data.put("rows", buildingList);

        return ResponseEntity.ok(ResponseDto.ok().setMessage("宿舍楼信息").setData(data));
    }

    @GetMapping("/building")
    public ResponseEntity<ResponseDto> getBuildingDetail(@RequestParam Long id) {
        Building building = buildingService.findOrElseRaise(id);

        BuildingDetailResponseDto buildingDetailResponseDto = new BuildingDetailResponseDto()
                .setName(building.getName())
                .setImageUrl(building.getImageUrl())
                .setDescription(building.getDescription());

        return ResponseEntity.ok(ResponseDto.ok().setMessage("宿舍楼详细信息").setData(buildingDetailResponseDto));
    }

    @GetMapping("/room")
    public ResponseEntity<ResponseDto> getRoomDetail(@RequestParam Long id) {
        Room room = roomService.findOrElseRaise(id);

        RoomDetailResponseDto roomDetailResponseDto = new RoomDetailResponseDto()
                .setBuildingId(room.getBuilding().getId())
                .setGender(room.getGender())
                .setDescription(room.getDescription())
                .setName(room.getName())
                .setImageUrl(room.getImageUrl());

        return ResponseEntity.ok(ResponseDto.ok().setMessage("房间详情").setData(roomDetailResponseDto));
    }

    @GetMapping("/empty")
    public ResponseEntity<ResponseDto> getEmptyRoom() {
        Map<String, Object> data = new HashMap<>();
        data.put("row", buildingService.getAllAvailableBedNumbers());

        return ResponseEntity.ok(ResponseDto.ok().setMessage("空房间数量").setData(data));
    }
}
