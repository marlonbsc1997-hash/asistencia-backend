package ec.mil.fae.asistencia.service.impl;

import ec.mil.fae.asistencia.repository.MarcacionRepository;
import ec.mil.fae.asistencia.repository.UsuarioRepository;
import ec.mil.fae.asistencia.service.ReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReporteServiceImpl implements ReporteService {

    private final MarcacionRepository marcacionRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    public Map<String, Object> generateAsistenciaReport(LocalDate inicio, LocalDate fin, Integer dependenciaId) {

        Map<String, Object> report = new HashMap<>();

        LocalDateTime desde = inicio.atStartOfDay();
        LocalDateTime hasta = fin.plusDays(1).atStartOfDay();

        report.put("fechaInicio", inicio);
        report.put("fechaFin", fin);
        report.put("marcaciones", marcacionRepository.findByFechaBetween(desde, hasta));

        return report;
    }

    @Override
    public Map<String, Object> generateUsuarioReport(Integer usuarioId, LocalDate inicio, LocalDate fin) {

        Map<String, Object> report = new HashMap<>();

        LocalDateTime desde = inicio.atStartOfDay();
        LocalDateTime hasta = fin.plusDays(1).atStartOfDay();

        report.put("usuario", usuarioRepository.findById(usuarioId).orElse(null));
        report.put("fechaInicio", inicio);
        report.put("fechaFin", fin);
        report.put("marcaciones",
                marcacionRepository.findByUsuarioIdAndFechaBetween(usuarioId, desde, hasta)
        );

        return report;
    }
}

