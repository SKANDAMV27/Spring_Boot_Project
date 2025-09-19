package com.prapthi.demoWebApp.service;

import com.prapthi.demoWebApp.dto.WebAppDTO;
import com.prapthi.demoWebApp.entity.WebAppEntity;
import com.prapthi.demoWebApp.repositry.WebRepositry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WebAppservice {

    @Autowired
    private WebRepositry webRepositry;

    // Convert Entity -> DTO
    private WebAppDTO toDTO(WebAppEntity entity) {
        return new WebAppDTO(entity.getStudent_id(),
                entity.getStudent_name(),
                entity.getStudent_city());
    }

    // Convert DTO -> Entity
    private WebAppEntity toEntity(WebAppDTO dto) {
        WebAppEntity entity = new WebAppEntity();
        if (dto.getId() != 0) {
            entity.setStudent_id(dto.getId());
        }
        entity.setStudent_name(dto.getName());
        entity.setStudent_city(dto.getCity());
        return entity;
    }

    // Get all records
    public List<WebAppDTO> details() {
        return webRepositry.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Get record by ID
    public WebAppDTO display(int id) {
        return webRepositry.findById(id)
                .map(this::toDTO)
                .orElse(null);
    }



    // Save a new record
    public WebAppDTO save(WebAppDTO webAppDTO) {
        WebAppEntity entity = toEntity(webAppDTO);
        WebAppEntity saved = webRepositry.save(entity);
        return toDTO(saved);
    }

    // Update a record by ID
    public WebAppDTO update(int id, WebAppDTO webAppDTO) {
        return webRepositry.findById(id)
                .map(existing -> {
                    if (webAppDTO.getName() != null) existing.setStudent_name(webAppDTO.getName());
                    if (webAppDTO.getCity() != null) existing.setStudent_city(webAppDTO.getCity());
                    WebAppEntity updated = webRepositry.save(existing);
                    return toDTO(updated);
                })
                .orElse(null);
    }



    // Delete a record by ID
    public boolean delete(int id) {
        if (webRepositry.existsById(id)) {
            webRepositry.deleteById(id);
            return true;
        }
        return false;
    }
}
