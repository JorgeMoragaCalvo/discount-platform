package com.mygroup.discountplatform.repositories;

import com.mygroup.discountplatform.entities.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Long> {

    List<ClientEntity> findByBuildingId(Long buildingId);

    Optional<ClientEntity> findByEmail(String email);

    Optional<ClientEntity> findByRut(String rut);

    boolean existsByEmail(String email);

    boolean existsByRut(String rut);
}
