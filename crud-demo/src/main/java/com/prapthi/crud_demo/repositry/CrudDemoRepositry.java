package com.prapthi.crud_demo.repositry;

import com.prapthi.crud_demo.entity.CrudDemoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CrudDemoRepositry extends JpaRepository<CrudDemoEntity,Integer> {



}
