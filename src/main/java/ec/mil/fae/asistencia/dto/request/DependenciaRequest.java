package ec.mil.fae.asistencia.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DependenciaRequest {

    @NotBlank
    private String descripcion;

    @NotNull
    private Integer unidadId;
}
