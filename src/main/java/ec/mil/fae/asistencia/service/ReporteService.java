package ec.mil.fae.asistencia.service;

import java.time.LocalDate;
import java.util.Map;

public interface ReporteService {
    Map<String, Object> generateAsistenciaReport(LocalDate inicio, LocalDate fin, Integer dependenciaId);
    Map<String, Object> generateUsuarioReport(Integer usuarioId, LocalDate inicio, LocalDate fin);
}
