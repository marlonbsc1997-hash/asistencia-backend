package ec.mil.fae.asistencia.service.impl;

import ec.mil.fae.asistencia.dto.request.UsuarioRequest;
import ec.mil.fae.asistencia.dto.response.UsuarioResponse;
import ec.mil.fae.asistencia.entity.AuthUsuario;
import ec.mil.fae.asistencia.entity.Dependencia;
import ec.mil.fae.asistencia.entity.Funcion;
import ec.mil.fae.asistencia.entity.HorarioLaboral;
import ec.mil.fae.asistencia.entity.Role;
import ec.mil.fae.asistencia.entity.Usuario;
import ec.mil.fae.asistencia.repository.AuthUsuarioRepository;
import ec.mil.fae.asistencia.repository.DependenciaRepository;
import ec.mil.fae.asistencia.repository.FuncionRepository;
import ec.mil.fae.asistencia.repository.HorarioRepository;
import ec.mil.fae.asistencia.repository.RoleRepository;
import ec.mil.fae.asistencia.repository.UsuarioRepository;
import ec.mil.fae.asistencia.security.SecurityUtils;
import ec.mil.fae.asistencia.service.UsuarioService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

@Service("usuarioServiceImpl")
@RequiredArgsConstructor
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final DependenciaRepository dependenciaRepository;
    private final FuncionRepository funcionRepository;
    private final RoleRepository roleRepository;
    private final HorarioRepository horarioRepository;
    private final AuthUsuarioRepository authUsuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponse> findAll() {
        return usuarioRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UsuarioResponse> findById(Integer id) {
        return usuarioRepository.findById(id).map(this::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UsuarioResponse> findByCedula(String cedula) {
        return usuarioRepository.findByCedula(cedula).map(this::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponse> findByDependenciaId(Integer dependenciaId) {
        return usuarioRepository.findByDependenciaId(dependenciaId)
                .stream().map(this::toResponse).toList();
    }

    @Override
    public UsuarioResponse create(UsuarioRequest request) {
        if (usuarioRepository.existsByCedula(request.getCedula())) {
            throw new IllegalArgumentException("Ya existe un usuario con la cédula: " + request.getCedula());
        }

        Dependencia dep = dependenciaRepository.findById(request.getDependenciaId())
                .orElseThrow(() -> new EntityNotFoundException("Dependencia no encontrada: " + request.getDependenciaId()));

        Funcion fun = funcionRepository.findById(request.getFuncionId())
                .orElseThrow(() -> new EntityNotFoundException("Función no encontrada: " + request.getFuncionId()));

        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado: " + request.getRoleId()));

        Usuario u = new Usuario();
        u.setCedula(request.getCedula());
        u.setApellidosNombres(request.getApellidosNombres());
        u.setSexo(request.getSexo());
        u.setGrado(request.getGrado());
        u.setEstado(request.getEstado() != null ? request.getEstado() : true);

        u.setDependencia(dep);
        u.setFuncion(fun);
        u.setRole(role);

        if (request.getHorIdBase() != null) {
            HorarioLaboral hor = horarioRepository.findById(request.getHorIdBase())
                    .orElseThrow(() -> new EntityNotFoundException("Horario no encontrado: " + request.getHorIdBase()));
            u.setHorarioBase(hor);
        } else {
            u.setHorarioBase(null);
        }

        Usuario saved = usuarioRepository.save(u);

        String correo = (request.getCorreo() != null && !request.getCorreo().isBlank())
                ? request.getCorreo().trim()
                : (saved.getCedula() + "@fae.local");

        String rawPass = (request.getPassword() != null && !request.getPassword().isBlank())
                ? request.getPassword()
                : saved.getCedula();

        AuthUsuario auth = new AuthUsuario();
        auth.setUsuario(saved);
        auth.setCorreo(correo);
        auth.setRole(role);
        auth.setActivo(true);
        auth.setCuentaBloqueada(false);
        auth.setIntentosFallidos(0);
        auth.setContrasena(passwordEncoder.encode(rawPass));

        authUsuarioRepository.save(auth);

        return toResponse(saved);
    }

    @Override
    public UsuarioResponse update(Integer id, UsuarioRequest request) {

        Usuario u = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id: " + id));

        Dependencia dep = dependenciaRepository.findById(request.getDependenciaId())
                .orElseThrow(() -> new EntityNotFoundException("Dependencia no encontrada: " + request.getDependenciaId()));

        Funcion fun = funcionRepository.findById(request.getFuncionId())
                .orElseThrow(() -> new EntityNotFoundException("Función no encontrada: " + request.getFuncionId()));

        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado: " + request.getRoleId()));

        u.setCedula(request.getCedula());
        u.setApellidosNombres(request.getApellidosNombres());
        u.setSexo(request.getSexo());
        u.setGrado(request.getGrado());
        if (request.getEstado() != null) u.setEstado(request.getEstado());

        u.setDependencia(dep);
        u.setFuncion(fun);
        u.setRole(role);

        if (request.getHorIdBase() != null) {
            HorarioLaboral hor = horarioRepository.findById(request.getHorIdBase())
                    .orElseThrow(() -> new EntityNotFoundException("Horario no encontrado: " + request.getHorIdBase()));
            u.setHorarioBase(hor);
        } else {
            u.setHorarioBase(null);
        }

        Usuario saved = usuarioRepository.save(u);

        AuthUsuario auth = authUsuarioRepository.findByUsuarioId(saved.getId())
                .orElseThrow(() -> new EntityNotFoundException("AuthUsuario no encontrado para usuario id: " + saved.getId()));

        if (request.getCorreo() != null && !request.getCorreo().isBlank()) {
            auth.setCorreo(request.getCorreo().trim());
        }
        auth.setRole(role);

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            auth.setContrasena(passwordEncoder.encode(request.getPassword()));
        }

        authUsuarioRepository.save(auth);

        return toResponse(saved);
    }

    @Override
    public void delete(Integer id) {
        Usuario u = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id: " + id));
        u.setEstado(false);
        usuarioRepository.save(u);
    }

    private UsuarioResponse toResponse(Usuario u) {
        String unidadDesc = (u.getDependencia() != null && u.getDependencia().getUnidad() != null)
                ? u.getDependencia().getUnidad().getDescripcion()
                : null;

        String depDesc = (u.getDependencia() != null) ? u.getDependencia().getDescripcion() : null;
        String funDesc = (u.getFuncion() != null) ? u.getFuncion().getDescripcion() : null;
        String rolNombre = (u.getRole() != null) ? u.getRole().getNombre() : null;

        String correo = authUsuarioRepository.findByUsuarioId(u.getId())
                .map(AuthUsuario::getCorreo)
                .orElse(null);

        Integer horId = (u.getHorarioBase() != null) ? u.getHorarioBase().getId() : null;
        String horDesc = (u.getHorarioBase() != null) ? u.getHorarioBase().getDescripcion() : null;

        return UsuarioResponse.builder()
                .id(u.getId())
                .cedula(u.getCedula())
                .nombreCompleto(u.getApellidosNombres())
                .email(correo)
                .grado(u.getGrado())
                .sexo(u.getSexo())
                .rol(rolNombre)
                .dependencia(depDesc)
                .unidad(unidadDesc)
                .funcion(funDesc)
                .horIdBase(horId)
                .horario(horDesc)
                .build();
    }

    /** ✅ Opción B: true si el token pertenece al mismo usuario consultado */
    @Transactional(readOnly = true)
    public boolean isOwner(Integer id) {
        String current = SecurityUtils.currentUsername();
        if (current == null || current.isBlank()) return false;

        return authUsuarioRepository.findByUsuarioId(id)
                .map(a -> current.equalsIgnoreCase(a.getCorreo()))
                .orElse(false);
    }
}


