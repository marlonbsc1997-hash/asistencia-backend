package ec.mil.fae.asistencia.repository;

import ec.mil.fae.asistencia.entity.AuthUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AuthUsuarioRepository extends JpaRepository<AuthUsuario, Integer> {

    @Query("""
        SELECT a
        FROM AuthUsuario a
        JOIN FETCH a.usuario u
        JOIN FETCH a.role r
        WHERE a.correo = :identificador OR u.cedula = :identificador
    """)
    Optional<AuthUsuario> findByIdentificadorWithUsuarioAndRole(String identificador);

    Optional<AuthUsuario> findByUsuarioId(Integer usuarioId);

    Optional<AuthUsuario> findByCorreo(String correo);
}

