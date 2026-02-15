package ec.mil.fae.asistencia.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalTime;

@Entity
@Table(name = "horario_laboral")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HorarioLaboral {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hor_id")
    private Integer id;

    @Column(name = "hor_descripcion", nullable = false, length = 100)
    private String descripcion;

    @Column(name = "hor_entrada_jornada", nullable = false)
    private LocalTime horaEntradaJornada;

    @Column(name = "hor_salida_jornada", nullable = false)
    private LocalTime horaSalidaJornada;

    @Column(name = "hor_salida_almuerzo")
    private LocalTime horaSalidaAlmuerzo;

    @Column(name = "hor_retorno_almuerzo")
    private LocalTime horaRetornoAlmuerzo;

    @Column(name = "hor_tolerancia_minutos")
    private Integer toleranciaMinutos;

    // ✅ EN TU BD ES TIME (ej: 08:00:00) => debe ser LocalTime
    @Column(name = "hor_diarias")
    private LocalTime horasDiarias;

    @Column(name = "hor_dias_laborables", length = 20)
    private String diasLaborables;
}
