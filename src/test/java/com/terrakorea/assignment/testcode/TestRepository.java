package com.terrakorea.assignment.testcode;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TestRepository extends JpaRepository<TestEntity, Long> {

    // 일단된 일자의 시간 당, 분 당 조회
    @Query("select t from TestEntity t where t.createdDate = ?1")
    List<TestEntity> findByCreatedDate(Date createdDate);

    // 지정된 일자 구간의 사용량 조회
    @Query("select t from TestEntity t where t.createdDate between ?1 and ?2")
    List<TestEntity> findByCreatedDateIsBetween(Date from, Date to);

    @Query("select t from TestEntity t where t.createdDate = ?1 and t.createdTime between ?2 and ?3")
    List<TestEntity> findByCreatedDateAndCreatedTimeIsBetween(Date createdDate, Date from, Date to);

}
