package br.com.logap.logitrackpro.service;

import br.com.logap.logitrackpro.dto.DashboardResponse;

public interface DashboardService {

    DashboardResponse obterMetricas(Integer veiculoId);
}
