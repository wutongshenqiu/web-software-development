package com.example.apiservice.service;

import com.example.apiservice.domain.entity.Group;

public interface IGroupService extends IBaseService<Group, Long> {
    boolean existsByInviteCode(String inviteCode);
    Group findByInviteCode(String inviteCode);
    String generateInviteCode();
    Group findActiveGroupByCreatorId(Long creatorId);
    Group findActiveGroupByUserId(Long userId);
}
