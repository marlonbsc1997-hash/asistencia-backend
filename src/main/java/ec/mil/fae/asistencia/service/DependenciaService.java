package ec.mil.fae.asistencia.service;

import ec.mil.fae.asistencia.dto.request.DependenciaRequest;
import ec.mil.fae.asistencia.entity.Dependencia;

import java.util.List;
import java.util.Optional;

public interface DependenciaService {

    List<Dependencia> findAll();
    Optional<Dependencia> findById(Integer id);
    List<Dependencia> findByUnidadId(Integer unidadId);

    Dependencia create(DependenciaRequest request);
    Dependencia update(Integer id, DependenciaRequest request);

    void delete(Integer id);
}
