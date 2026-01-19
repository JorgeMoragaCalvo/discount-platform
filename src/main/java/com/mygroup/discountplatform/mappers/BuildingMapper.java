package com.mygroup.discountplatform.mappers;

import com.mygroup.discountplatform.dtos.BuildingByCityDTO;
import com.mygroup.discountplatform.dtos.BuildingCreateRequestDTO;
import com.mygroup.discountplatform.dtos.BuildingListDTO;
import com.mygroup.discountplatform.dtos.BuildingResponseDTO;
import com.mygroup.discountplatform.entities.BuildingEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BuildingMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "clients", ignore = true)
    @Mapping(target = "buildingSuppliers", ignore = true)
    BuildingEntity toEntity(BuildingCreateRequestDTO dto);

    BuildingResponseDTO toResponseDTO(BuildingEntity entity);

    BuildingListDTO toListDTO(BuildingEntity entity);

    BuildingByCityDTO toByCityDTO(BuildingEntity entity);

    List<BuildingListDTO> toListDTOList(List<BuildingEntity> entities);

    List<BuildingByCityDTO> toByCityDTOList(List<BuildingEntity> entities);
}
