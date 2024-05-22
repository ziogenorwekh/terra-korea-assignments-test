package com.terrakorea.assignment.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class RetrieveResources {

    public ResponseEntity<Void> retrieveAMinutesCpuStatus() {

        return ResponseEntity.noContent().build();
    }

    public void retrieveAHoursCpuStatus() {

    }

    public void retrieveADaysCpuStatus() {

    }

}
