package ec.mil.fae.asistencia.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponse {
    private Integer id;
    private String cedula;
    private String nombreCompleto;
    private String email;
    private String grado;
    private String sexo;
    private String rol;
    private String dependencia;
    private String unidad;
    private String funcion;

    // Opcionales si luego los mandas
    private Integer horIdBase;
    private String horario;
}


