package ec.mil.fae.asistencia.repository;

import ec.mil.fae.asistencia.entity.Marcacion;
import ec.mil.fae.asistencia.enums.TipoMarcacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface MarcacionRepository extends JpaRepository<Marcacion, Integer> {

    // ================= DASHBOARD =================

    @Query("""
        SELECT COUNT(m)
        FROM Marcacion m
        WHERE m.fechaHora BETWEEN :inicio AND :fin
          AND m.tipoMarcacion = :tipo
    """)
    long countByFechaAndTipoMarcacion(LocalDateTime inicio, LocalDateTime fin, TipoMarcacion tipo);

    @Query("""
        SELECT COUNT(m)
        FROM Marcacion m
        WHERE m.fechaHora BETWEEN :inicio AND :fin
    """)
    long countByFechaBetween(LocalDateTime inicio, LocalDateTime fin);

    @Query("""
        SELECT COUNT(m)
        FROM Marcacion m
        WHERE m.estado = :estado
    """)
    long countByEstado(String estado);

    List<Marcacion> findTop10ByOrderByFechaHoraDesc();

    // ================= REPORTES =================

    // ✅ nombre correcto por campo: fechaHora
    List<Marcacion> findByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin);

    // ✅ alias para tu ReporteServiceImpl (si usa este nombre)
    default List<Marcacion> findByFechaBetween(LocalDateTime inicio, LocalDateTime fin) {
        return findByFechaHoraBetween(inicio, fin);
    }

    @Query("""
        SELECT m
        FROM Marcacion m
        WHERE m.usuario.id = :usuarioId
          AND m.fechaHora BETWEEN :inicio AND :fin
        ORDER BY m.fechaHora ASC
    """)
    List<Marcacion> findByUsuarioIdAndFechaBetween(Integer usuarioId, LocalDateTime inicio, LocalDateTime fin);
}



