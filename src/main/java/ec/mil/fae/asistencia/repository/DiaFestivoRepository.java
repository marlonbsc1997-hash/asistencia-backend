// ===============================
// 2) REPOSITORY: DiaFestivoRepository.java
// ===============================
package ec.mil.fae.asistencia.repository;

import ec.mil.fae.asistencia.entity.DiaFestivo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaFestivoRepository extends JpaRepository<DiaFestivo, Integer> {
    // Igual a Unidad: sin métodos extra por ahora
}
