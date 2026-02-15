package ec.mil.fae.asistencia.security;

import ec.mil.fae.asistencia.entity.AuthUsuario;
import ec.mil.fae.asistencia.repository.AuthUsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("usuarioSecurity")
@RequiredArgsConstructor
public class UsuarioSecurity {

    private final AuthUsuarioRepository authUsuarioRepository;

    /** true si el usuario autenticado es el mismo del {id} */
    public boolean isSelf(Integer id, Authentication authentication) {
        if (id == null || authentication == null) return false;

        // OJO: authentication.getName() = subject del JWT (en tu caso correo)
        String correo = authentication.getName();

        AuthUsuario auth = authUsuarioRepository.findByCorreo(correo).orElse(null);
        if (auth == null || auth.getUsuario() == null) return false;

        return id.equals(auth.getUsuario().getId());
    }
}
