package ec.mil.fae.asistencia.repository;

import ec.mil.fae.asistencia.entity.HorarioLaboral;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HorarioRepository extends JpaRepository<HorarioLaboral, Integer> {
}
