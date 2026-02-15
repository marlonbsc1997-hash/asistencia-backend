package ec.mil.fae.asistencia.repository;

import ec.mil.fae.asistencia.entity.Geocerca;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GeocercaRepository extends JpaRepository<Geocerca, Integer> {

    // ✅ Para GeocercaServiceImpl (lista)
    List<Geocerca> findByActivoTrue();

    // ✅ Para MarcacionServiceImpl (una sola)
    Optional<Geocerca> findFirstByActivoTrue();
}
