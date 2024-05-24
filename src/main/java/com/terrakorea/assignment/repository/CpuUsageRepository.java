package com.terrakorea.assignment.repository;

import com.terrakorea.assignment.entity.CpuUsageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CpuUsageRepository extends JpaRepository<CpuUsageEntity, Long> {


    @Query("select c from CpuUsageEntity c where c.createdDate = ?1")
    List<CpuUsageEntity> findByCreatedDate(Date createdDate);

    @Query("select c from CpuUsageEntity c where c.createdDate between ?1 and ?2")
    List<CpuUsageEntity> findByCreatedDateIsBetween(Date startDate, Date endDate);

    @Query("select c from CpuUsageEntity c where c.createdDate = ?1 and c.createdTime between ?2 and ?3")
    List<CpuUsageEntity> findByCreatedDateAndCreatedTimeIsBetween(Date createdDate, Date from, Date to);

}
