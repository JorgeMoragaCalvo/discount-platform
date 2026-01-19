package com.mygroup.discountplatform.services;

import com.mygroup.discountplatform.entities.BuildingEntity;
import com.mygroup.discountplatform.repositories.BuildingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BuildingService {

    private final BuildingRepository buildingRepository;

    public BuildingService(BuildingRepository buildingRepository) {
        this.buildingRepository = buildingRepository;
    }

    public List<BuildingEntity> findAll() {
        return buildingRepository.findAll();
    }

    public BuildingEntity createBuilding(BuildingEntity building) {
        return buildingRepository.save(building);
    }

    public List<BuildingEntity> findByCity(String city){
        return buildingRepository.findByCity(city);
    }
}
