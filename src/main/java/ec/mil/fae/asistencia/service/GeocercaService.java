package ec.mil.fae.asistencia.service;

import ec.mil.fae.asistencia.dto.response.ValidarUbicacionResponse;
import ec.mil.fae.asistencia.entity.Geocerca;
import java.util.List;
import java.util.Optional;

public interface GeocercaService {
    List<Geocerca> findAll();
    List<Geocerca> findByEstadoTrue();
    Optional<Geocerca> findById(Integer id);
    Geocerca create(Geocerca geocerca);
    Geocerca update(Integer id, Geocerca geocerca);
    void delete(Integer id);
    ValidarUbicacionResponse verificarUbicacion(Double latitud, Double longitud);
}
