package ec.mil.fae.asistencia.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usu_id")
    private Integer id;

    @Column(name = "usu_cedula", nullable = false, length = 10, unique = true)
    private String cedula;

    @Column(name = "usu_apellidos_nombres", nullable = false)
    private String apellidosNombres;

    @Column(name = "usu_sexo", nullable = false, length = 1)
    private String sexo; // "M" / "F"

    @Column(name = "usu_grado", nullable = false)
    private String grado;

    @Column(name = "usu_estado", nullable = false)
    private Boolean estado = true;

    @Column(name = "usu_creacion")
    private LocalDateTime creacion;

    @Column(name = "usu_modificacion")
    private LocalDateTime modificacion;

    // ===================== RELACIONES (FKS) =====================

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "dep_id", nullable = false)
    private Dependencia dependencia;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fun_id", nullable = false)
    private Funcion funcion;

    // ✅ ESTA ES LA QUE TE FALTABA (rol_id)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "rol_id", nullable = false)
    private Role role;

    // Si tienes horario base:
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hor_id_base")
    private HorarioLaboral horarioBase;

    @PrePersist
    public void prePersist() {
        this.creacion = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.modificacion = LocalDateTime.now();
    }
}

