package ec.mil.fae.asistencia.dto.request;

import lombok.Data;

@Data
public class UsuarioRequest {
    private String cedula;
    private String apellidosNombres;
    private String sexo;   // "M" o "F"
    private String grado;
    private Boolean estado;

    // FK obligatorias en tu tabla usuario:
    private Integer dependenciaId; // dep_id
    private Integer funcionId;     // fun_id
    private Integer roleId;        // rol_id

    // ✅ NUEVO: horario base (FK opcional en usuario.hor_id_base)
    private Integer horIdBase;     // hor_id_base

    // opcionales para auth_usuario
    private String correo;         // aut_correo (si no viene, se genera/conserva)
    private String password;       // si no viene, usamos cedula (solo en create)
}

