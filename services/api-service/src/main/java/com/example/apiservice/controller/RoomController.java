package com.example.apiservice.controller;

import com.example.apiservice.domain.dto.ResponseDto;
import com.example.apiservice.domain.dto.room.BuildingDetailResponseDto;
import com.example.apiservice.domain.dto.room.BuildingResponseDto;
import com.example.apiservice.domain.dto.room.RoomDetailResponseDto;
import com.example.apiservice.domain.entity.Building;
import com.example.apiservice.domain.entity.Room;
import com.example.apiservice.exception.ResourceNotFoundException;
import com.example.apiservice.service.IBuildingService;
import com.example.apiservice.service.IRoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/room")
public class RoomController {
    @Autowired
    IBuildingService buildingService;

    @Autowired
    IRoomService roomService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    // TODO: 这里可以用泛型吗
    private List<Building> getBuildingsFromRedis() {
        Object object = redisTemplate.opsForValue().get("Building");

        List<Building> buildings = new ArrayList<>();
        if (object instanceof ArrayList<?>) {
            List<?> objs = (List<?>) object;
            for (Object o : objs) {
                buildings.add((Building) o);
            }
        }

        return buildings;
    }

    private List<Building> getBuildingFromRedisOrElseDb() {
        List<Building> buildings = getBuildingsFromRedis();

        if (buildings.isEmpty()) {
            // 读取数据库需要加锁
            synchronized (this) {
                // 再次判断缓存中是否存在数据
                buildings = getBuildingsFromRedis();

                if (buildings.isEmpty()) {
                    buildings = buildingService.findAll();
                    redisTemplate.opsForValue().set("Building", buildings, 1, TimeUnit.HOURS);
                }
            }
        }

        return buildings;
    }

    private Map<String, Object> getEmptyRoomFromRedis() {
        Object object = redisTemplate.opsForValue().get("EmptyRoom");

        if (object instanceof HashMap<?,?>) {
            Map<?, ?> objs = (Map<?, ?>) object;
            return objs.entrySet()
                    .stream()
                    .collect(Collectors.toMap(e -> (String) e.getKey(), Map.Entry::getValue));
        }

        return new HashMap<>();
    }

    private Map<String, Object> getEmptyRoomFromRedisOrElseDb() {
        Map<String, Object> emptyRoom = getEmptyRoomFromRedis();

        if (emptyRoom.isEmpty()) {
            // 读取数据库需要加锁
            synchronized (this) {
                // 再次判断缓存中是否存在数据
                emptyRoom = getEmptyRoomFromRedis();

                if (emptyRoom.isEmpty()) {
                    emptyRoom.put("row", buildingService.getAllAvailableBedNumbers());
                    redisTemplate.opsForValue().set("EmptyRoom", emptyRoom, 5, TimeUnit.SECONDS);
                }
            }
        }

        return emptyRoom;
    }

    @GetMapping("/buildinglist")
    public ResponseEntity<ResponseDto> getBuildingList() {
        List<Building> buildings = getBuildingFromRedisOrElseDb();

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
    public ResponseEntity<ResponseDto> getBuildingDetail(@RequestParam Long id) throws ResourceNotFoundException {
        List<Building> buildings = getBuildingFromRedisOrElseDb();
        for (Building building : buildings) {
            if (building.getId().equals(id)) {
                BuildingDetailResponseDto buildingDetailResponseDto = new BuildingDetailResponseDto()
                        .setName(building.getName())
                        .setImageUrl(building.getImageUrl())
                        .setDescription(building.getDescription());

                return ResponseEntity.ok(ResponseDto.ok().setMessage("宿舍楼详细信息").setData(buildingDetailResponseDto));
            }
        }

        throw new ResourceNotFoundException("宿舍楼 `" + id + "` 不存在");
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
        Map<String, Object> data = getEmptyRoomFromRedisOrElseDb();

        return ResponseEntity.ok(ResponseDto.ok().setMessage("空房间数量").setData(data));
    }
}
