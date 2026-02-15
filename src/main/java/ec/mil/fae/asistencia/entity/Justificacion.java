package ec.mil.fae.asistencia.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "justificaciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Justificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "jus_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mar_id")
    private Marcacion marcacion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usu_id", nullable = false)
    private Usuario usuario;

    @Column(name = "jus_fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "jus_motivo", nullable = false, columnDefinition = "TEXT")
    private String motivo;

    @Column(name = "jus_documento")
    private String documento;

    @Column(name = "jus_estado", nullable = false, length = 20)
    private String estado = "PENDIENTE"; // PENDIENTE | APROBADO | RECHAZADO

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jus_aprobado_por")
    private Usuario aprobadoPor;

    @Column(name = "jus_fecha_aprobacion")
    private LocalDateTime fechaAprobacion;

    @Column(name = "jus_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
        if (this.estado == null) this.estado = "PENDIENTE";
    }
}


