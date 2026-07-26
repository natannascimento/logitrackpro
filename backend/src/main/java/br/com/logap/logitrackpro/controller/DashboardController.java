package br.com.logap.logitrackpro.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.logap.logitrackpro.dto.DashboardResponse;
import br.com.logap.logitrackpro.service.DashboardService;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public DashboardResponse obterMetricas(@RequestParam(required = false) Integer veiculoId) {
        return dashboardService.obterMetricas(veiculoId);
    }
}
