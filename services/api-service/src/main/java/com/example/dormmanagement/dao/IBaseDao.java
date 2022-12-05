package com.example.dormmanagement.dao;

import com.example.dormmanagement.domain.entity.Base;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface IBaseDao<T extends Base, I extends Serializable> extends JpaRepository<T, I>, JpaSpecificationExecutor<T> {
}
