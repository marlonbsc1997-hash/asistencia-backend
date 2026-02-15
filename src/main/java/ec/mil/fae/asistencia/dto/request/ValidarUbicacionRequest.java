package ec.mil.fae.asistencia.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ValidarUbicacionRequest {
    @NotNull(message = "La latitud es requerida")
    private Double latitud;
    
    @NotNull(message = "La longitud es requerida")
    private Double longitud;
}
