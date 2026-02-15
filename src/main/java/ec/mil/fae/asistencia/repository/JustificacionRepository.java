package ec.mil.fae.asistencia.repository;

import ec.mil.fae.asistencia.entity.Justificacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JustificacionRepository extends JpaRepository<Justificacion, Integer> {

    List<Justificacion> findAllByOrderByFechaCreacionDesc();

    List<Justificacion> findByEstadoOrderByFechaCreacionDesc(String estado);

    List<Justificacion> findByUsuarioIdOrderByFechaCreacionDesc(Integer usuarioId);

    long countByEstado(String estado);
}

