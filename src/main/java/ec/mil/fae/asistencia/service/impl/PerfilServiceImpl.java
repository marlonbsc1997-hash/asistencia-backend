package ec.mil.fae.asistencia.service.impl;

import ec.mil.fae.asistencia.dto.response.PerfilResponse;
import ec.mil.fae.asistencia.entity.AuthUsuario;
import ec.mil.fae.asistencia.repository.AuthUsuarioRepository;
import ec.mil.fae.asistencia.service.PerfilService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PerfilServiceImpl implements PerfilService {

    private final AuthUsuarioRepository authUsuarioRepository;
    private final PasswordEncoder passwordEncoder;

    private AuthUsuario authOrFail(String identificador) {
        return authUsuarioRepository
                .findByIdentificadorWithUsuarioAndRole(identificador)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
    }

    @Override
    @Transactional(readOnly = true)
    public PerfilResponse me(String identificador) {
        AuthUsuario auth = authOrFail(identificador);
        return new PerfilResponse(auth.getCorreo(), auth.getUsuario());
    }

    @Override
    public PerfilResponse updateCorreo(String identificador, String nuevoCorreo) {
        AuthUsuario auth = authOrFail(identificador);

        String nuevo = nuevoCorreo.trim().toLowerCase();

        if (nuevo.equalsIgnoreCase(auth.getCorreo())) {
            return new PerfilResponse(auth.getCorreo(), auth.getUsuario());
        }

        if (authUsuarioRepository.findByCorreo(nuevo).isPresent()) {
            throw new IllegalArgumentException("El correo ya está registrado");
        }

        auth.setCorreo(nuevo);
        authUsuarioRepository.save(auth);

        return new PerfilResponse(auth.getCorreo(), auth.getUsuario());
    }

    @Override
    public void updatePassword(String identificador, String currentPassword, String newPassword) {
        AuthUsuario auth = authOrFail(identificador);

        if (!passwordEncoder.matches(currentPassword, auth.getContrasena())) {
            throw new IllegalArgumentException("La contraseña actual es incorrecta");
        }

        auth.setContrasena(passwordEncoder.encode(newPassword));
        authUsuarioRepository.save(auth);
    }
}

