package ec.mil.fae.asistencia.service;

import ec.mil.fae.asistencia.entity.Marcacion;
import ec.mil.fae.asistencia.enums.TipoMarcacion;

import java.time.LocalDate;
import java.util.List;

public interface MarcacionService {

    Marcacion registrarMarcacion(
            String identificador,
            TipoMarcacion tipoMarcacion,
            Double latitud,
            Double longitud,
            String ip
    );

    List<Marcacion> findMisMarcaciones(
            String identificador,
            LocalDate inicio,
            LocalDate fin
    );

    List<Marcacion> findByUsuarioIdAndFechaRange(
            Integer usuarioId,
            LocalDate inicio,
            LocalDate fin
    );

    List<Marcacion> findByFechaRange(
            LocalDate inicio,
            LocalDate fin
    );
}

