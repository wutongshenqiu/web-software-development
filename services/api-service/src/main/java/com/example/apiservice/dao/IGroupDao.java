package com.example.apiservice.dao;

import com.example.apiservice.domain.entity.Group;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IGroupDao extends IBaseDao<Group, Long> {
    Optional<Group> findGroupByInviteCode(String inviteCode);

    // JPQL
    @Query(value = "FROM Group g WHERE g.creator.id = ?1 AND g.groupStatus = 0")
    Optional<Group> findActiveGroupByCreatorId(Long creatorId);
}
