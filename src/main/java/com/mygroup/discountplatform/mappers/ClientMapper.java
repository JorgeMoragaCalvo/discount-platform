package com.mygroup.discountplatform.mappers;

import com.mygroup.discountplatform.dtos.ClientCreateRequestDTO;
import com.mygroup.discountplatform.dtos.ClientListDTO;
import com.mygroup.discountplatform.dtos.ClientResponseDTO;
import com.mygroup.discountplatform.dtos.HobbyDTO;
import com.mygroup.discountplatform.dtos.ProfessionDTO;
import com.mygroup.discountplatform.entities.ClientEntity;
import com.mygroup.discountplatform.entities.HobbyEntity;
import com.mygroup.discountplatform.entities.ProfessionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ClientMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "building", ignore = true)
    @Mapping(target = "hobbies", ignore = true)
    @Mapping(target = "professions", ignore = true)
    @Mapping(target = "redemptions", ignore = true)
    ClientEntity toEntity(ClientCreateRequestDTO dto);

    @Mapping(target = "buildingId", source = "building.id")
    @Mapping(target = "buildingName", source = "building.name")
    ClientResponseDTO toResponseDTO(ClientEntity entity);

    @Mapping(target = "buildingName", source = "building.name")
    ClientListDTO toListDTO(ClientEntity entity);

    List<ClientListDTO> toListDTOList(List<ClientEntity> entities);

    HobbyDTO toHobbyDTO(HobbyEntity entity);

    ProfessionDTO toProfessionDTO(ProfessionEntity entity);

    List<HobbyDTO> toHobbyDTOList(List<HobbyEntity> entities);

    List<ProfessionDTO> toProfessionDTOList(List<ProfessionEntity> entities);
}
