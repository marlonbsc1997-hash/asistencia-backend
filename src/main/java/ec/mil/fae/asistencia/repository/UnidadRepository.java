package ec.mil.fae.asistencia.repository;

import ec.mil.fae.asistencia.entity.Unidad;
import org.springframework.data.jpa.repository.JpaRepository;

// import java.util.List;

public interface UnidadRepository extends JpaRepository<Unidad, Integer> {
    // Eliminado findByActivoTrue() - la tabla no tiene columna activo
}

