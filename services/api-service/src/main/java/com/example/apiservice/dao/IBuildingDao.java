package com.example.apiservice.dao;

import com.example.apiservice.domain.entity.Building;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IBuildingDao extends IBaseDao<Building, Long> {
    @Query(
            value = """
                    WITH valid_room AS 
                    (
                        SELECT tb_room.id as room_id
                        FROM tb_room
                        WHERE
                            building_id = ?1 AND
                            gender = ?2
                    )
                                        
                    SELECT COUNT(*)
                    FROM tb_bed
                    INNER JOIN valid_room
                    ON tb_bed.room_id = valid_room.room_id
                    WHERE bed_status = 0
                    """,
            nativeQuery = true)
    Integer countAvailableBedByBuildingIdAndGender(Long buildingId, Integer gender);
}
