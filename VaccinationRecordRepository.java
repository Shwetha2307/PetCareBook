package com.example.project.repository;

import com.example.project.entity.VaccinationRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VaccinationRecordRepository extends JpaRepository<VaccinationRecord, Long> {

    List<VaccinationRecord> findByPetIdOrderByDateAdministeredDesc(Long petId);

    List<VaccinationRecord> findByNextDueDateBetweenOrderByNextDueDateAsc(LocalDate startDate, LocalDate endDate);

    List<VaccinationRecord> findByNextDueDateBeforeOrderByNextDueDateAsc(LocalDate date);

    long countByNextDueDateBetween(LocalDate startDate, LocalDate endDate);

    long countByNextDueDateBefore(LocalDate date);

    @Query("SELECT DISTINCT r.pet.id FROM VaccinationRecord r WHERE r.nextDueDate BETWEEN :startDate AND :endDate")
    List<Long> findDistinctPetIdsWithVaccinationsDueBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
