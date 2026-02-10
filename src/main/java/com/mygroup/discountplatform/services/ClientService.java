package com.mygroup.discountplatform.services;

import com.mygroup.discountplatform.dtos.ClientCreateRequestDTO;
import com.mygroup.discountplatform.dtos.ClientListDTO;
import com.mygroup.discountplatform.dtos.ClientResponseDTO;
import com.mygroup.discountplatform.entities.BuildingEntity;
import com.mygroup.discountplatform.entities.ClientEntity;
import com.mygroup.discountplatform.entities.HobbyEntity;
import com.mygroup.discountplatform.entities.ProfessionEntity;
import com.mygroup.discountplatform.mappers.ClientMapper;
import com.mygroup.discountplatform.repositories.BuildingRepository;
import com.mygroup.discountplatform.repositories.ClientRepository;
import com.mygroup.discountplatform.repositories.HobbyRepository;
import com.mygroup.discountplatform.repositories.ProfessionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final BuildingRepository buildingRepository;
    private final HobbyRepository hobbyRepository;
    private final ProfessionRepository professionRepository;

    public ClientService(
            ClientRepository clientRepository,
            ClientMapper clientMapper,
            BuildingRepository buildingRepository,
            HobbyRepository hobbyRepository,
            ProfessionRepository professionRepository
    ) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
        this.buildingRepository = buildingRepository;
        this.hobbyRepository = hobbyRepository;
        this.professionRepository = professionRepository;
    }

    public List<ClientListDTO> findAll() {
        List<ClientEntity> clients = clientRepository.findAll();
        return clientMapper.toListDTOList(clients);
    }

    public ClientResponseDTO findById(Long id) {
        ClientEntity client = clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Client not found with id: " + id));
        return clientMapper.toResponseDTO(client);
    }

    public List<ClientListDTO> findByBuildingId(Long buildingId) {
        List<ClientEntity> clients = clientRepository.findByBuildingId(buildingId);
        return clientMapper.toListDTOList(clients);
    }

    /**
     * Creates client; enforces uniqueness, cardinality, and existence constraints
     */
    @Transactional
    public ClientResponseDTO createClient(ClientCreateRequestDTO dto) {
        if (clientRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Client with email " + dto.getEmail() + " already exists");
        }

        if (clientRepository.existsByRut(dto.getRut())) {
            throw new IllegalArgumentException("Client with RUT " + dto.getRut() + " already exists");
        }

        BuildingEntity building = buildingRepository.findById(dto.getBuildingId())
                .orElseThrow(() -> new EntityNotFoundException("Building not found with id: " + dto.getBuildingId()));

        ClientEntity entity = clientMapper.toEntity(dto);
        entity.setBuilding(building);

        // Sets hobbies on a client if IDs are provided
        if (dto.getHobbyIds() != null && !dto.getHobbyIds().isEmpty()) {
            Set<HobbyEntity> hobbies = new HashSet<>(hobbyRepository.findAllById(dto.getHobbyIds()));
            entity.setHobbies(hobbies);
        }

        // Sets professions on a client if IDs are provided
        if (dto.getProfessionIds() != null && !dto.getProfessionIds().isEmpty()) {
            if (dto.getProfessionIds().size() > 2) {
                throw new IllegalArgumentException("A client can have at most 2 professions");
            }
            Set<ProfessionEntity> professions = new HashSet<>(professionRepository.findAllById(dto.getProfessionIds()));
            entity.setProfessions(professions);
        }

        ClientEntity saved = clientRepository.save(entity);
        return clientMapper.toResponseDTO(saved);
    }
}
