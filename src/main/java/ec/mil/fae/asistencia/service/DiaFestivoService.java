// ===============================
// 3) SERVICE: DiaFestivoService.java
// ===============================
package ec.mil.fae.asistencia.service;

import ec.mil.fae.asistencia.entity.DiaFestivo;

import java.util.List;
import java.util.Optional;

public interface DiaFestivoService {
    List<DiaFestivo> findAll();
    Optional<DiaFestivo> findById(Integer id);
    DiaFestivo create(DiaFestivo diaFestivo);
    DiaFestivo update(Integer id, DiaFestivo diaFestivo);
    void delete(Integer id);
}
