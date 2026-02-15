package ec.mil.fae.asistencia.service;

import ec.mil.fae.asistencia.entity.Unidad;
import java.util.List;
import java.util.Optional;

public interface UnidadService {
    List<Unidad> findAll();
    Optional<Unidad> findById(Integer id);
    Unidad create(Unidad unidad);
    Unidad update(Integer id, Unidad unidad);
    void delete(Integer id);
}
