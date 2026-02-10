package com.mygroup.discountplatform.repositories;

import com.mygroup.discountplatform.entities.ProfessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfessionRepository extends JpaRepository<ProfessionEntity, Long> {
}
