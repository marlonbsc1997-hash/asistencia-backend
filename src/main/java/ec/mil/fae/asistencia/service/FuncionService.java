package ec.mil.fae.asistencia.service;

import ec.mil.fae.asistencia.entity.Funcion;
import java.util.List;
import java.util.Optional;

public interface FuncionService {
    List<Funcion> findAll();
    Optional<Funcion> findById(Integer id);
    Funcion create(Funcion funcion);
    Funcion update(Integer id, Funcion funcion);
    void delete(Integer id);
}
