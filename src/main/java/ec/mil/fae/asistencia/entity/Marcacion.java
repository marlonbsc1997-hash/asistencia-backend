package ec.mil.fae.asistencia.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ec.mil.fae.asistencia.enums.TipoMarcacion;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "marcaciones_diarias")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Marcacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mar_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usu_id", nullable = false)
    private Usuario usuario;

    @Column(name = "mar_fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    // ✅ ahora enum, coincide con el CHECK de la BD
    @Enumerated(EnumType.STRING)
    @Column(name = "mar_tipo", nullable = false, length = 30)
    private TipoMarcacion tipoMarcacion;

    @Column(name = "mar_latitud", nullable = false)
    private Double latitud;

    @Column(name = "mar_longitud", nullable = false)
    private Double longitud;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "geo_id")
    private Geocerca geocerca;

    @Column(name = "mar_estado", length = 20)
    private String estado;

    @Column(name = "mar_minutos_tardanza")
    private Integer minutosTardanza = 0;

    @Column(name = "mar_observacion")
    private String observacion;

    @Column(name = "mar_ip", length = 50)
    private String ip;

    @Transient
    private Boolean dentroGeocerca;

    @Transient
    private String dispositivo;
}


