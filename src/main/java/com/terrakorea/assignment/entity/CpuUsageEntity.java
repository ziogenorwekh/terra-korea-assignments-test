package com.terrakorea.assignment.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "CPU_USAGE_TABLE")
@Getter
@NoArgsConstructor
public class CpuUsageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CPU_USAGE")
    private Double cpuUsage;

    @Column(name = "CREATED_DATE")
    @Temporal(value = TemporalType.DATE)
    private Date createdDate;

    @Temporal(value = TemporalType.TIME)
    @Column(name = "CREATED_TIME")
    private Date createdTime;

    @Builder
    public CpuUsageEntity(Double cpuUsage, Date createdDate, Date createdTime) {
        this.cpuUsage = cpuUsage;
        this.createdDate = createdDate;
        this.createdTime = createdTime;
    }
}
