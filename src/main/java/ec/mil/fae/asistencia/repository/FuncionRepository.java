package ec.mil.fae.asistencia.repository;

import ec.mil.fae.asistencia.entity.Funcion;
import org.springframework.data.jpa.repository.JpaRepository;
//import java.util.List;

public interface FuncionRepository extends JpaRepository<Funcion, Integer> {
	
}
