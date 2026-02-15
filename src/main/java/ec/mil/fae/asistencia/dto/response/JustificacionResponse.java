package ec.mil.fae.asistencia.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class JustificacionResponse {
    private Integer id;

    private Integer usuarioId;
    private String usuarioNombres;
    private String usuarioCedula;

    private Integer marcacionId;

    private LocalDate fecha;
    private String motivo;

    private String estado; // PENDIENTE | APROBADO | RECHAZADO

    private String documento; // nombre archivo (si existe)

    private Integer aprobadoPorId;
    private String aprobadoPorNombres;
    private LocalDateTime fechaAprobacion;

    private LocalDateTime fechaCreacion;
}


