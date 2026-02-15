package ec.mil.fae.asistencia.repository;

import ec.mil.fae.asistencia.entity.Notificacion;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificacionRepository extends JpaRepository<Notificacion, Integer> {

    List<Notificacion> findByDestinatarioIdOrderByFechaHoraDesc(Integer usuId, Pageable pageable);

    long countByDestinatarioIdAndLeidoFalse(Integer usuId);
}
