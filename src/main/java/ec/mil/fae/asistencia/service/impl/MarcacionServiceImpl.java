package ec.mil.fae.asistencia.service.impl;

import ec.mil.fae.asistencia.entity.AuthUsuario;
import ec.mil.fae.asistencia.entity.Geocerca;
import ec.mil.fae.asistencia.entity.Marcacion;
import ec.mil.fae.asistencia.enums.TipoMarcacion;
import ec.mil.fae.asistencia.repository.AuthUsuarioRepository;
import ec.mil.fae.asistencia.repository.GeocercaRepository;
import ec.mil.fae.asistencia.repository.MarcacionRepository;
import ec.mil.fae.asistencia.service.MarcacionService;
import ec.mil.fae.asistencia.service.NotificacionService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MarcacionServiceImpl implements MarcacionService {

    private final MarcacionRepository marcacionRepository;
    private final AuthUsuarioRepository authUsuarioRepository;
    private final GeocercaRepository geocercaRepository;

    // ✅ NUEVO: para generar notificación de tardanza
    private final NotificacionService notificacionService;

    @Override
    public Marcacion registrarMarcacion(
            String identificador,
            TipoMarcacion tipoMarcacion,
            Double latitud,
            Double longitud,
            String ip
    ) {
        AuthUsuario auth = authUsuarioRepository
                .findByIdentificadorWithUsuarioAndRole(identificador)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        var usuario = auth.getUsuario();
        var horario = usuario.getHorarioBase();

        if (horario == null) {
            throw new IllegalStateException("El usuario no tiene horario asignado");
        }

        int tolerancia = horario.getToleranciaMinutos() != null ? horario.getToleranciaMinutos() : 5;

        // ✅ Hora actual (Ecuador)
        ZoneId zone = ZoneId.of("America/Guayaquil");
        LocalDateTime ahora = LocalDateTime.now(zone);
        LocalTime horaActual = ahora.toLocalTime();

        // ✅ Hora esperada según tipo
        LocalTime horaEsperada = switch (tipoMarcacion) {
            case ENTRADA_LABORAL -> horario.getHoraEntradaJornada();
            case SALIDA_ALMUERZO -> horario.getHoraSalidaAlmuerzo();
            case ENTRADA_ALMUERZO -> horario.getHoraRetornoAlmuerzo();
            case SALIDA_LABORAL -> horario.getHoraSalidaJornada();
        };

        if (horaEsperada == null) {
            throw new IllegalStateException("Horario incompleto: falta hora esperada para " + tipoMarcacion);
        }

        String estado = "PUNTUAL";
        int minutos = 0;
        String observacion = null;

        boolean esEntrada = (tipoMarcacion == TipoMarcacion.ENTRADA_LABORAL || tipoMarcacion == TipoMarcacion.ENTRADA_ALMUERZO);
        boolean esSalida  = (tipoMarcacion == TipoMarcacion.SALIDA_ALMUERZO  || tipoMarcacion == TipoMarcacion.SALIDA_LABORAL);

        // ✅ 1) Entradas: TARDANZA si pasa tolerancia
        if (esEntrada) {
            LocalTime limite = horaEsperada.plusMinutes(tolerancia);
            if (horaActual.isAfter(limite)) {
                estado = "TARDANZA";
                minutos = (int) Duration.between(limite, horaActual).toMinutes();
                observacion = "Ingreso fuera de hora. Límite: " + limite + " (tolerancia " + tolerancia + " min).";
            }
        }

        // ✅ 2) Salidas: ANTICIPADO si sale antes
        if (esSalida) {
            if (horaActual.isBefore(horaEsperada)) {
                estado = "ANTICIPADO";
                minutos = (int) Duration.between(horaActual, horaEsperada).toMinutes();
                observacion = "Salida anticipada: " + minutos + " min antes de la hora establecida (" + horaEsperada + ").";
            }
        }

        // ✅ 3) Ventana de almuerzo: si está muy fuera del rango, solo advertimos (no bloquea)
        if (tipoMarcacion == TipoMarcacion.SALIDA_ALMUERZO) {
            if (!estaEnVentana(horaActual, horaEsperada, 15, 30)) {
                observacion = buildFueraVentanaMsg(horaActual, horaEsperada, "salida a almuerzo");
            }
        }

        if (tipoMarcacion == TipoMarcacion.ENTRADA_ALMUERZO) {
            if (!estaEnVentana(horaActual, horaEsperada, 15, 30)) {
                observacion = buildFueraVentanaMsg(horaActual, horaEsperada, "regreso de almuerzo");
            }
        }

        // ✅ Geocerca (por ahora tomas la primera activa)
        Geocerca geocerca = geocercaRepository.findFirstByActivoTrue().orElse(null);

        Marcacion m = new Marcacion();
        m.setUsuario(usuario);
        m.setFechaHora(ahora);
        m.setTipoMarcacion(tipoMarcacion);
        m.setLatitud(latitud);
        m.setLongitud(longitud);
        m.setGeocerca(geocerca);
        m.setIp(ip);

        m.setEstado(estado);
        m.setMinutosTardanza(minutos); // Nota: también guarda anticipado (minutos diferencia)
        m.setObservacion(observacion);

        // ✅ Guardar primero
        Marcacion saved = marcacionRepository.save(m);

        // ✅ SOLO aquí generamos notificación de tardanza (no ausencia ni justificación)
        if ("TARDANZA".equals(saved.getEstado())) {
            notificacionService.generarTardanza(
                    usuario.getId(),
                    saved.getMinutosTardanza(),
                    saved.getId()
            );
        }

        return saved;
    }

    private boolean estaEnVentana(LocalTime actual, LocalTime target, int beforeMin, int afterMin) {
        LocalTime ini = target.minusMinutes(beforeMin);
        LocalTime fin = target.plusMinutes(afterMin);
        return !actual.isBefore(ini) && !actual.isAfter(fin);
    }

    private String buildFueraVentanaMsg(LocalTime actual, LocalTime target, String evento) {
        long diff = Duration.between(target, actual).toMinutes(); // + ya pasó, - falta
        if (diff < 0) {
            return "Aún no es hora de " + evento + ". Faltan " + Math.abs(diff) + " min (hora: " + target + ").";
        }
        return "La hora de " + evento + " ya pasó hace " + diff + " min (hora: " + target + ").";
    }

    @Override
    @Transactional(readOnly = true)
    public List<Marcacion> findMisMarcaciones(String identificador, LocalDate inicio, LocalDate fin) {

        AuthUsuario auth = authUsuarioRepository
                .findByIdentificadorWithUsuarioAndRole(identificador)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        return marcacionRepository.findByUsuarioIdAndFechaBetween(
                auth.getUsuario().getId(),
                inicio.atStartOfDay(),
                fin.plusDays(1).atStartOfDay()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<Marcacion> findByUsuarioIdAndFechaRange(Integer usuarioId, LocalDate inicio, LocalDate fin) {
        return marcacionRepository.findByUsuarioIdAndFechaBetween(
                usuarioId,
                inicio.atStartOfDay(),
                fin.plusDays(1).atStartOfDay()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<Marcacion> findByFechaRange(LocalDate inicio, LocalDate fin) {
        return marcacionRepository.findByFechaHoraBetween(
                inicio.atStartOfDay(),
                fin.plusDays(1).atStartOfDay()
        );
    }
}




