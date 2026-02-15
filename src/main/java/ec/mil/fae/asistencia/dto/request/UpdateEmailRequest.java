package ec.mil.fae.asistencia.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateEmailRequest {
    @NotBlank(message = "El correo es requerido")
    @Email(message = "El correo no es válido")
    private String correo;
}
