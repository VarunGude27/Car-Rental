package com.mzp.carrental.controller;


import com.mzp.carrental.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/public/api/stats")
public class StatisticsController {



    @Autowired
    private StatisticsService statisticsService;

    @GetMapping
    public Map<String, Long> getStatistics() {
        return statisticsService.getStatistics();
    }
}
