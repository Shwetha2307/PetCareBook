package com.example.project.repository;

import com.example.project.entity.VaccineType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VaccineTypeRepository extends JpaRepository<VaccineType, Long> {
    Optional<VaccineType> findByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCase(String name);
    List<VaccineType> findByTargetSpeciesIgnoreCaseOrTargetSpeciesIgnoreCase(String species, String fallback);
}
