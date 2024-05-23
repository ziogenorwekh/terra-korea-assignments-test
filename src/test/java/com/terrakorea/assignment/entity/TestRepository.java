package com.terrakorea.assignment.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TestRepository extends JpaRepository<TestEntity, Long> {


    List<TestEntity> findByCreatedDate(Date createdDate);

    @Query("select t from TestEntity t where t.createdDate between ?1 and ?2")
    List<TestEntity> findByCreatedDateIsBetween(Date from, Date to);

}
