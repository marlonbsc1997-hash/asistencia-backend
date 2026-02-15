package ec.mil.fae.asistencia.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActividadReciente {
    private Integer id;
    private String usuario;
    private String accion;
    private String hora;
    private String estado;
}
