package com.example.apiservice.service;

import com.example.apiservice.domain.entity.Auth;

public interface IAuthService extends IBaseService<Auth, Long> {
    Auth findByUsername(String username);
}
