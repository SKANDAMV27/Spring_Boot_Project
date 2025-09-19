package com.prapthi.demoWebApp.repositry;

import com.prapthi.demoWebApp.dto.WebAppDTO;
import com.prapthi.demoWebApp.entity.WebAppEntity;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebRepositry extends JpaRepository<WebAppEntity, Integer> {


}
