package ec.mil.fae.asistencia.service;

import ec.mil.fae.asistencia.dto.response.PerfilResponse;

public interface PerfilService {
    PerfilResponse me(String identificador);
    PerfilResponse updateCorreo(String identificador, String nuevoCorreo);
    void updatePassword(String identificador, String currentPassword, String newPassword);
}
