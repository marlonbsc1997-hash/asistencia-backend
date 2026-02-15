package ec.mil.fae.asistencia.dto.response;

import ec.mil.fae.asistencia.entity.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PerfilResponse {
    private String correo;   // AuthUsuario.correo
    private Usuario usuario; // info de Usuario
}
