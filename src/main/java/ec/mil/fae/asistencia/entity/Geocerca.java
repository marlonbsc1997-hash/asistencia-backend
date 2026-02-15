package ec.mil.fae.asistencia.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "geocercos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Geocerca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "geo_id")
    private Integer id;

    @Column(name = "geo_nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "geo_latitud", nullable = false)
    private Double latitud;

    @Column(name = "geo_longitud", nullable = false)
    private Double longitud;

    // ✅ EN TU BD ES geo_radio (NO geo_radio_metros)
    @Column(name = "geo_radio", nullable = false)
    private Integer radioMetros;

    // ⚠️ En tu query de columnas NO aparece uni_id.
    // Si realmente no existe en la tabla, comenta este bloque o crea la columna.
    //@ManyToOne(fetch = FetchType.LAZY)
    //@JoinColumn(name = "uni_id")
    //private Unidad unidad;

    @Column(name = "geo_activo", nullable = false)
    private Boolean activo = true;

    @Column(name = "geo_fecha_creacion")
    private LocalDateTime fechaCreacion;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }
}

