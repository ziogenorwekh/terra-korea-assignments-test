package com.terrakorea.assignment.testcode;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class TestEntity {

    public TestEntity() {

    }

    public TestEntity(Double cpuUsage, Date createdDate, Date createdTime) {
        this.cpuUsage = cpuUsage;
        this.createdDate = createdDate;
        this.createdTime = createdTime;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cpu_usage")
    private Double cpuUsage;

    @Column(name = "created_date")
    @Temporal(value = TemporalType.DATE)
    private Date createdDate;

    @Temporal(value = TemporalType.TIME)
    @Column(name = "created_time")
    private Date createdTime;



    public Double getCpuUsage() {
        return cpuUsage;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public Date getCreatedDate() {
        return createdDate;
    }
}
