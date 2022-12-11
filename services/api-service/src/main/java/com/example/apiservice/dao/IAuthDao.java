package com.example.apiservice.dao;

import com.example.apiservice.domain.entity.Auth;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IAuthDao extends IBaseDao<Auth, Long> {
    Optional<Auth> findAuthByUsername(String username);
}
