package com.terrakorea.assignment.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/")
public class CpuUsageResources {


    @RequestMapping(value = "/usage/{}", method = RequestMethod.GET)
    public ResponseEntity<Void> retrieveAMinutesCpuUsage() {

        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/usage/{}", method = RequestMethod.GET)
    public ResponseEntity<Void> retrieveAHoursCpuUsage() {

        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/usage/{}", method = RequestMethod.GET)
    public ResponseEntity<Void> retrieveADaysCpuUsage() {

        return ResponseEntity.noContent().build();
    }

}
