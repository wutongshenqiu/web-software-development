package com.example.apiservice.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.example.apiservice.util.AuthUtil;
import com.example.apiservice.util.HttpReqRespUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册 Sa-Token 拦截器
        registry.addInterceptor(new SaInterceptor(handle -> checkLoginAndOverrideIp()))
                .addPathPatterns("/**")
                .excludePathPatterns("/auth/login")
                .excludePathPatterns("/auth/refreshtoken");
    }

    private void checkLoginAndOverrideIp() {
        StpUtil.checkLogin();

        SaSession session = StpUtil.getSession();
        String device = StpUtil.getLoginDevice();
        String ip = HttpReqRespUtil.getClientIpAddress();
        String previousIp = AuthUtil.getClientPreviousIpAddress();

        if (previousIp == null || !previousIp.equals(ip)) {
            session.set(device, ip);
            StpUtil.logoutByTokenValue(StpUtil.getTokenValue());
            StpUtil.login(10001, device);
        }
    }
}
