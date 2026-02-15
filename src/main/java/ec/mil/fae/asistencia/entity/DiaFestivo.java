// ===============================
// 1) ENTITY: DiaFestivo.java
// ===============================
package ec.mil.fae.asistencia.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "dia_festivo",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uq_dia_festivo_fecha_descripcion",
            columnNames = {"df_fecha", "df_descripcion"}
        )
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiaFestivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "df_id")
    private Integer id;

    @Column(name = "df_fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "df_descripcion", nullable = false, length = 150)
    private String descripcion;

    @Column(name = "df_recurrente", nullable = false)
    private Boolean recurrente = false;

    @Column(name = "df_creacion", nullable = false)
    private LocalDateTime creacion;

    // ✅ Recomendación: set automático al insertar
    @PrePersist
    public void prePersist() {
        if (creacion == null) {
            creacion = LocalDateTime.now();
        }
        if (recurrente == null) {
            recurrente = false;
        }
    }
}
