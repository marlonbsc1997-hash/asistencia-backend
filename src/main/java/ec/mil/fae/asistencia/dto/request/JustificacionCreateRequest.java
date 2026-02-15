package ec.mil.fae.asistencia.dto.request;

import lombok.Data;
import java.time.LocalDate;

@Data
public class JustificacionCreateRequest {
    private Integer usuarioId;   // ADMIN/SUP => a quién justifican; EMPLEADO => puede venir null
    private Integer marcacionId;  // opcional
    private LocalDate fecha;
    private String motivo;
}
