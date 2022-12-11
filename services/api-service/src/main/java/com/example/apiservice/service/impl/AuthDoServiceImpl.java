package com.example.apiservice.service.impl;

import com.example.apiservice.dao.IAuthDao;
import com.example.apiservice.dao.IBaseDao;
import com.example.apiservice.domain.entity.Auth;
import com.example.apiservice.service.IAuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthDoServiceImpl extends BaseDoServiceImpl<Auth, Long> implements IAuthService {
    private final IAuthDao authDao;

    public AuthDoServiceImpl(final IAuthDao authDao) {
        this.authDao = authDao;
    }

    @Override
    public IBaseDao<Auth, Long> getBaseDao() {
        return this.authDao;
    }

    @Override
    public Auth findByUsername(String username) {
        return authDao.findAuthByUsername(username).orElse(null);
    }
}
