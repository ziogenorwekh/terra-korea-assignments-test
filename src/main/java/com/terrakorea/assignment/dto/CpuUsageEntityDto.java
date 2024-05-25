package com.terrakorea.assignment.dto;

import com.terrakorea.assignment.entity.CpuUsageEntity;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO for {@link CpuUsageEntity}
 */
@Getter
@Builder
@EqualsAndHashCode
public class CpuUsageEntityDto {

    private final Long id;
    private final Double cpuUsage;
    private final Date createdDate;
    private final Date createdTime;

}