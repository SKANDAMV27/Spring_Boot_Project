package com.prapthi.crud_demo.repositry;

import com.prapthi.crud_demo.entity.CrudDemoEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CrudDemoRepositry extends JpaRepository<CrudDemoEntity,Integer> {

    List<CrudDemoEntity> findByName(String name);

    List<CrudDemoEntity> findByEmailId(String emailId);

    @Transactional
    @Modifying
    long deleteByName(String name);

    @Transactional
    @Modifying
    long deleteByEmailId(String emailId);


//    List<CrudDemoEntity> updateByName(String name);
}

