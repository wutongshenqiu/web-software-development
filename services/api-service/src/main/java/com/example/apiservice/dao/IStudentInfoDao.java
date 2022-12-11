package com.example.apiservice.dao;

import com.example.apiservice.domain.entity.StudentInfo;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IStudentInfoDao extends IBaseDao<StudentInfo, Long> {
    Optional<StudentInfo> findStudentInfoByUserId(Long id);
}
