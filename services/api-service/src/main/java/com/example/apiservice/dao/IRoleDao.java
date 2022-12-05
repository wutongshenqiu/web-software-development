package com.example.apiservice.dao;

import com.example.apiservice.domain.entity.Role;
import org.springframework.stereotype.Repository;

@Repository
public interface IRoleDao extends IBaseDao<Role, Long> {
}
