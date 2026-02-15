package ec.mil.fae.asistencia.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "notificaciones")
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "not_id")
    private Integer id;

    @Column(name = "not_tipo", nullable = false, length = 20)
    private String tipo; // tardanza, ausencia, justificacion, sistema

    @Column(name = "not_titulo", nullable = false, length = 120)
    private String titulo;

    @Column(name = "not_mensaje", nullable = false, length = 300)
    private String mensaje;

    @Column(name = "not_fecha_hora", nullable = false)
    private LocalDateTime fechaHora = LocalDateTime.now();

    @Column(name = "not_leido", nullable = false)
    private Boolean leido = false;

    // DESTINATARIO
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usu_id", nullable = false)
    private Usuario destinatario;

    // ORIGEN (quien generó el evento)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "not_origen_usu_id")
    private Usuario origen;

    @Column(name = "not_ref_tabla", length = 30)
    private String refTabla;

    @Column(name = "not_ref_id")
    private Integer refId;
}
