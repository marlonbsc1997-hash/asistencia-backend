package ec.mil.fae.asistencia.repository;

import ec.mil.fae.asistencia.entity.Dependencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DependenciaRepository extends JpaRepository<Dependencia, Integer> {

    List<Dependencia> findByUnidadId(Integer unidadId);

}
