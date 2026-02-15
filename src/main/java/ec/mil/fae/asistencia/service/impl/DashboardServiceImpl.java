package ec.mil.fae.asistencia.service.impl;

import ec.mil.fae.asistencia.dto.response.ActividadReciente;
import ec.mil.fae.asistencia.dto.response.DashboardEstadisticas;
import ec.mil.fae.asistencia.dto.response.DashboardResumenSemanal;
import ec.mil.fae.asistencia.entity.Marcacion;
import ec.mil.fae.asistencia.enums.TipoMarcacion;
import ec.mil.fae.asistencia.repository.JustificacionRepository;
import ec.mil.fae.asistencia.repository.MarcacionRepository;
import ec.mil.fae.asistencia.repository.UsuarioRepository;
import ec.mil.fae.asistencia.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final UsuarioRepository usuarioRepository;
    private final MarcacionRepository marcacionRepository;
    private final JustificacionRepository justificacionRepository;

    @Override
    public DashboardEstadisticas getEstadisticas(String cedula) {

        DashboardEstadisticas stats = new DashboardEstadisticas();
        LocalDate hoy = LocalDate.now();

        LocalDateTime inicioDia = hoy.atStartOfDay();
        LocalDateTime finDia = hoy.plusDays(1).atStartOfDay();

        long totalEmpleados = usuarioRepository.countByEstadoTrue();
        stats.setTotalEmpleados(totalEmpleados);

        long presentesHoy = marcacionRepository
                .countByFechaAndTipoMarcacion(inicioDia, finDia, TipoMarcacion.ENTRADA_LABORAL);
        stats.setPresentesHoy(presentesHoy);

        long tardanzasMes = marcacionRepository
                .countByEstado("TARDANZA");
        stats.setTardanzasMes(tardanzasMes);

        stats.setAusenciasMes(0);

        long justificacionesPendientes = justificacionRepository.countByEstado("PENDIENTE");
        stats.setJustificacionesPendientes(justificacionesPendientes);

        if (totalEmpleados > 0) {
            double porcentaje = (presentesHoy * 100.0) / totalEmpleados;
            stats.setPorcentajeAsistencia(Math.round(porcentaje * 100.0) / 100.0);
        } else {
            stats.setPorcentajeAsistencia(0.0);
        }

        stats.setEstadisticasPorDependencia(new ArrayList<>());
        stats.setTendenciaMensual(new ArrayList<>());

        return stats;
    }

    @Override
    public DashboardResumenSemanal getResumenSemanal(String cedula) {

        DashboardResumenSemanal resumen = new DashboardResumenSemanal();

        LocalDate hoy = LocalDate.now();
        LocalDateTime inicio = hoy.minusDays(7).atStartOfDay();
        LocalDateTime fin = hoy.plusDays(1).atStartOfDay();

        long totalEmpleados = usuarioRepository.countByEstadoTrue();
        long marcacionesSemana = marcacionRepository.countByFechaBetween(inicio, fin);

        if (totalEmpleados > 0) {
            double promedio = (marcacionesSemana * 100.0) / (totalEmpleados * 7);
            resumen.setAsistenciaPromedio(Math.min(100, Math.round(promedio * 100.0) / 100.0));
        } else {
            resumen.setAsistenciaPromedio(0.0);
        }

        resumen.setPuntualidad(85.0);
        return resumen;
    }

    @Override
    public List<ActividadReciente> getActividadReciente(String cedula, Integer limite) {

        List<Marcacion> marcaciones = marcacionRepository.findTop10ByOrderByFechaHoraDesc();
        List<ActividadReciente> actividades = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        for (Marcacion m : marcaciones) {
            ActividadReciente a = new ActividadReciente();
            a.setId(m.getId());
            a.setUsuario(m.getUsuario().getApellidosNombres());
            a.setAccion(m.getTipoMarcacion().name());
            a.setHora(m.getFechaHora().format(formatter));

            String estado = switch (m.getTipoMarcacion()) {
                case ENTRADA_LABORAL -> "entrada";
                case SALIDA_ALMUERZO -> "salida_almuerzo";
                case ENTRADA_ALMUERZO -> "entrada_almuerzo";
                case SALIDA_LABORAL -> "salida";
            };

            a.setEstado(estado);
            actividades.add(a);
        }

        return actividades;
    }
}

