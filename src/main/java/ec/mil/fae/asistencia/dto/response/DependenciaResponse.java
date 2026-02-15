package ec.mil.fae.asistencia.dto.response;

import lombok.Data;

@Data
public class DependenciaResponse {

    private Integer id;
    private String descripcion;

    private Integer unidadId;
    private String unidadDescripcion;
}
