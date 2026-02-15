package ec.mil.fae.asistencia.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "El identificador es requerido")
    private String identificador; // correo o cédula

    @NotBlank(message = "La contraseña es requerida")
    private String password;
}
