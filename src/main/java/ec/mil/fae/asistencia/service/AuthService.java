package ec.mil.fae.asistencia.service;

import ec.mil.fae.asistencia.dto.request.LoginRequest;
import ec.mil.fae.asistencia.dto.response.LoginResponse;
import ec.mil.fae.asistencia.dto.response.UsuarioResponse;
import ec.mil.fae.asistencia.entity.AuthUsuario;
import ec.mil.fae.asistencia.entity.Usuario;
import ec.mil.fae.asistencia.repository.AuthUsuarioRepository;
import ec.mil.fae.asistencia.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthUsuarioRepository authUsuarioRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Transactional
    public LoginResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getIdentificador(),
                            request.getPassword()
                    )
            );
        } catch (AuthenticationException e) {
            throw new RuntimeException("Credenciales inválidas");
        }

        AuthUsuario authUsuario = authUsuarioRepository
                .findByIdentificadorWithUsuarioAndRole(request.getIdentificador())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        authUsuario.setUltimoAcceso(LocalDateTime.now());
        authUsuario.setIntentosFallidos(0);
        authUsuarioRepository.save(authUsuario);

        String token = jwtService.generateToken(authUsuario);

        Usuario u = authUsuario.getUsuario();

        UsuarioResponse usuarioResponse = UsuarioResponse.builder()
                .id(u.getId())
                .cedula(u.getCedula())
                .nombreCompleto(u.getApellidosNombres())
                .email(authUsuario.getCorreo())
                .grado(u.getGrado())
                .sexo(u.getSexo())
                .rol(authUsuario.getRole().getNombre())
                .dependencia(u.getDependencia() != null ? u.getDependencia().getDescripcion() : null)
                .unidad(
                        u.getDependencia() != null && u.getDependencia().getUnidad() != null
                                ? u.getDependencia().getUnidad().getDescripcion()
                                : null
                )
                .funcion(u.getFuncion() != null ? u.getFuncion().getDescripcion() : null)
                .horIdBase(u.getHorarioBase() != null ? u.getHorarioBase().getId() : null)
                .horario(u.getHorarioBase() != null ? u.getHorarioBase().getDescripcion() : null)
                .build();

        return LoginResponse.builder()
                .token(token)
                .usuario(usuarioResponse)
                .build();
    }
}




