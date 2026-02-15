package ec.mil.fae.asistencia.dto.request;

import ec.mil.fae.asistencia.enums.TipoMarcacion;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MarcacionRequest {

    @NotNull(message = "El tipo de marcación es requerido")
    private TipoMarcacion tipoMarcacion;

    @NotNull(message = "La latitud es requerida")
    private Double latitud;

    @NotNull(message = "La longitud es requerida")
    private Double longitud;
}


