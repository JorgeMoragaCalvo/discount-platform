package com.mygroup.discountplatform.repositories;

import com.mygroup.discountplatform.entities.BuildingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BuildingRepository extends JpaRepository<BuildingEntity, Long> {

    List<BuildingEntity> findByCity(String city);
}
