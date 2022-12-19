package com.example.apiservice.service;

import java.time.LocalDateTime;

public interface ISysConfigService {
    LocalDateTime getStartTime();

    LocalDateTime getEndTime();

    Integer getClassLimit();

    Integer getGroupLimit();

    Integer getGroupNumber();
}
