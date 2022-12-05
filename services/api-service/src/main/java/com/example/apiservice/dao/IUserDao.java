package com.example.apiservice.dao;

import com.example.apiservice.domain.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserDao extends IBaseDao<User, Long> {
}
