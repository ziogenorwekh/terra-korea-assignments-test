package com.terrakorea.assignment.entity;

import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
public class TestHandler {


    public Double minCpuUsage(List<TestEntity> testEntities) {
        return testEntities.stream().min(Comparator.comparingDouble(TestEntity::getCpuUsage))
                .orElseThrow(NullPointerException::new).getCpuUsage();
    }

    public Double maxCpuUsage(List<TestEntity> testEntities) {
        return testEntities.stream().max(Comparator.comparingDouble(TestEntity::getCpuUsage))
                .orElseThrow(NullPointerException::new).getCpuUsage();
    }

    public Double avgCpuUsage(List<TestEntity> testEntities) {
        return testEntities.stream()
                .mapToDouble(TestEntity::getCpuUsage)
                .average().orElseThrow(NullPointerException::new);
    }
}
