package com.example.apiservice.controller;

import com.example.apiservice.domain.dto.ResponseDto;
import com.example.apiservice.domain.dto.room.BuildingDetailResponseDto;
import com.example.apiservice.domain.dto.room.BuildingResponseDto;
import com.example.apiservice.domain.dto.room.EmptyRoomResponseDto;
import com.example.apiservice.domain.dto.room.RoomDetailResponseDto;
import com.example.apiservice.type.enumration.Gender;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/room")
public class RoomController {
    @GetMapping("/buildinglist")
    public ResponseEntity<ResponseDto> getBuildingList() {
        List<BuildingResponseDto> buildingList = new ArrayList<>();
        for (long i = 1; i <= 10; i++) {
            BuildingResponseDto buildingResponseDto = new BuildingResponseDto().setBuildingId(i)
                    .setBuildingName(i + "号楼");
            buildingList.add(buildingResponseDto);
        }

        return ResponseEntity.ok(ResponseDto.ok().setMessage("Building info").setData(buildingList));
    }

    @GetMapping("/building")
    public ResponseEntity<ResponseDto> getBuildingDetail(@RequestParam Long id) {
        BuildingDetailResponseDto buildingDetailResponseDto = new BuildingDetailResponseDto().setName(id + "号楼")
                .setImageUrl("http://xxx.xxx.com")
                .setDescription("这是" + id + "号楼");

        return ResponseEntity.ok(ResponseDto.ok().setMessage("Building detail").setData(buildingDetailResponseDto));
    }

    @GetMapping("/room")
    public ResponseEntity<ResponseDto> getRoomDetail(@RequestParam Long id) {
        RoomDetailResponseDto roomDetailResponseDto = new RoomDetailResponseDto().setBuildingId(5L)
                .setGender(Gender.FEMALE)
                .setDescription("这是" + id + "号房间")
                .setName(id + "号房间")
                .setImageUrl("https://xxx.xxx.com");

        return ResponseEntity.ok(ResponseDto.ok().setMessage("Room detail").setData(roomDetailResponseDto));
    }

    @GetMapping("/room/empty")
    public ResponseEntity<ResponseDto> getEmptyRoom(@RequestParam Gender gender) {
        List<EmptyRoomResponseDto> emptyRoomResponseDtoList = new ArrayList<>();

        for (long i = 1; i <= 10; i++) {
            EmptyRoomResponseDto emptyRoomResponseDto = new EmptyRoomResponseDto()
                    .setEmptyRoomNumber(new Random().nextLong(50))
                    .setBuildingId(i)
                    .setGender(gender);
            emptyRoomResponseDtoList.add(emptyRoomResponseDto);
        }

        return ResponseEntity.ok(ResponseDto.ok().setMessage("Empty rooms").setData(emptyRoomResponseDtoList));
    }
}
