package com.example.dormmanagement.dao;

import com.example.dormmanagement.domain.entity.Role;
import org.springframework.stereotype.Repository;

@Repository
public interface IRoleDao extends IBaseDao<Role, Long> {
}
