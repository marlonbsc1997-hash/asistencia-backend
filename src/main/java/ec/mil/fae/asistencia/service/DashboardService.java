package ec.mil.fae.asistencia.service;

import ec.mil.fae.asistencia.dto.response.ActividadReciente;
import ec.mil.fae.asistencia.dto.response.DashboardEstadisticas;
import ec.mil.fae.asistencia.dto.response.DashboardResumenSemanal;

import java.util.List;

public interface DashboardService {
    
    DashboardEstadisticas getEstadisticas(String cedula);
    
    DashboardResumenSemanal getResumenSemanal(String cedula);
    
    List<ActividadReciente> getActividadReciente(String cedula, Integer limite);
}

