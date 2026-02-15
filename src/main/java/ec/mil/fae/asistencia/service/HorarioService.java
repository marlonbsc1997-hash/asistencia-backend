package ec.mil.fae.asistencia.service;

import ec.mil.fae.asistencia.entity.HorarioLaboral;
import java.util.List;
import java.util.Optional;

public interface HorarioService {
    List<HorarioLaboral> findAll();
    Optional<HorarioLaboral> findById(Integer id);
    HorarioLaboral create(HorarioLaboral horario);
    HorarioLaboral update(Integer id, HorarioLaboral horario);
    void delete(Integer id);
}

