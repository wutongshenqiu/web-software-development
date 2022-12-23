package com.example.apiservice.service.impl;

import com.example.apiservice.dao.IBaseDao;
import com.example.apiservice.dao.IStudentInfoDao;
import com.example.apiservice.domain.entity.StudentInfo;
import com.example.apiservice.service.IStudentService;
import org.springframework.stereotype.Service;

@Service
public class StudentInfoDoServiceImpl extends BaseDoServiceImpl<StudentInfo, Long> implements IStudentService {
    private final IStudentInfoDao studentInfoDao;

    public StudentInfoDoServiceImpl(final IStudentInfoDao studentInfoDao) {
        this.studentInfoDao = studentInfoDao;
    }

    @Override
    public IBaseDao<StudentInfo, Long> getBaseDao() {
        return this.studentInfoDao;
    }
}
