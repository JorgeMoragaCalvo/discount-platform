package com.mygroup.discountplatform.services;

import com.mygroup.discountplatform.dtos.BuildingByCityDTO;
import com.mygroup.discountplatform.dtos.BuildingCreateRequestDTO;
import com.mygroup.discountplatform.dtos.BuildingListDTO;
import com.mygroup.discountplatform.dtos.BuildingResponseDTO;
import com.mygroup.discountplatform.entities.BuildingEntity;
import com.mygroup.discountplatform.mappers.BuildingMapper;
import com.mygroup.discountplatform.repositories.BuildingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BuildingService {

    private final BuildingRepository buildingRepository;
    private final BuildingMapper buildingMapper;

    public BuildingService(BuildingRepository buildingRepository, BuildingMapper buildingMapper) {
        this.buildingRepository = buildingRepository;
        this.buildingMapper = buildingMapper;
    }

    public List<BuildingListDTO> findAll() {
        List<BuildingEntity> buildings = buildingRepository.findAll();
        return buildingMapper.toListDTOList(buildings);
    }

    public BuildingResponseDTO createBuilding(BuildingCreateRequestDTO dto) {
        BuildingEntity entity = buildingMapper.toEntity(dto);
        BuildingEntity saved = buildingRepository.save(entity);
        return buildingMapper.toResponseDTO(saved);
    }

    public List<BuildingByCityDTO> findByCity(String city) {
        List<BuildingEntity> buildings = buildingRepository.findByCity(city);
        return buildingMapper.toByCityDTOList(buildings);
    }
}
