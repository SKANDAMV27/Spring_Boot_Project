package com.prapthi.crud_demo.repositry;

import com.prapthi.crud_demo.entity.CrudDemoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CrudDemoRepositry extends JpaRepository<CrudDemoEntity,Integer> {

    List<CrudDemoEntity> findByName(String name);
    List<CrudDemoEntity> findByEmail(String email);

//    List<CrudDemoEntity> readByName(String name);


}
