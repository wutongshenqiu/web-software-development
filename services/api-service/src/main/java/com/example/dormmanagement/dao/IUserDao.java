package com.example.dormmanagement.dao;

import com.example.dormmanagement.domain.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserDao extends IBaseDao<User, Long> {
}
