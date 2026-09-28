package com.example.project.repository;

import com.example.project.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findByOwnerId(Long ownerId);

    List<Pet> findBySpeciesIgnoreCase(String species);

    @Query("SELECT p FROM Pet p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(p.breed) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Pet> searchByNameOrBreed(@Param("query") String query);
}
