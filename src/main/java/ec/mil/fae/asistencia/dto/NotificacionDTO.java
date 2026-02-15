package ec.mil.fae.asistencia.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NotificacionDTO {
    private Integer id;
    private String type;
    private String title;
    private String message;
    private LocalDateTime timestamp;
    private boolean read;
    private Integer userId;
    private String userName;
}
