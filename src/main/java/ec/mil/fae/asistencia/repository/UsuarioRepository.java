package ec.mil.fae.asistencia.repository;

import ec.mil.fae.asistencia.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByCedula(String cedula);

    boolean existsByCedula(String cedula);

    List<Usuario> findByDependenciaId(Integer dependenciaId);

    long countByEstadoTrue();

    @Query("""
        SELECT u
        FROM AuthUsuario a
        JOIN a.usuario u
        WHERE a.correo = :correo
    """)
    Optional<Usuario> findByAuthCorreo(@Param("correo") String correo);

    // ✅ CORRECTO: Role tiene atributo "nombre"
    List<Usuario> findByRole_NombreIn(List<String> roles);
}



