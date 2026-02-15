package ec.mil.fae.asistencia.service.impl;

import ec.mil.fae.asistencia.dto.request.JustificacionCreateRequest;
import ec.mil.fae.asistencia.dto.response.JustificacionResponse;
import ec.mil.fae.asistencia.entity.AuthUsuario;
import ec.mil.fae.asistencia.entity.Justificacion;
import ec.mil.fae.asistencia.entity.Marcacion;
import ec.mil.fae.asistencia.entity.Usuario;
import ec.mil.fae.asistencia.mapper.JustificacionMapper;
import ec.mil.fae.asistencia.repository.AuthUsuarioRepository;
import ec.mil.fae.asistencia.repository.JustificacionRepository;
import ec.mil.fae.asistencia.repository.MarcacionRepository;
import ec.mil.fae.asistencia.repository.UsuarioRepository;
import ec.mil.fae.asistencia.service.JustificacionService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class JustificacionServiceImpl implements JustificacionService {

    private final JustificacionRepository justificacionRepository;
    private final UsuarioRepository usuarioRepository;
    private final MarcacionRepository marcacionRepository;
    private final AuthUsuarioRepository authUsuarioRepository;

    private static final Path UPLOAD_DIR = Paths.get("uploads", "justificaciones");

    private Usuario getUsuarioFromCorreo(String correo) {
        AuthUsuario auth = authUsuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new EntityNotFoundException("AuthUsuario no encontrado con correo: " + correo));
        if (auth.getUsuario() == null) throw new EntityNotFoundException("AuthUsuario sin usuario asociado");
        return auth.getUsuario();
    }

    @Override
    @Transactional(readOnly = true)
    public List<JustificacionResponse> findAll() {
        return justificacionRepository.findAllByOrderByFechaCreacionDesc()
                .stream()
                .map(JustificacionMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<JustificacionResponse> findPendientes() {
        return justificacionRepository.findByEstadoOrderByFechaCreacionDesc("PENDIENTE")
                .stream()
                .map(JustificacionMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<JustificacionResponse> findMine(String correoPrincipal) {
        Usuario yo = getUsuarioFromCorreo(correoPrincipal);
        return justificacionRepository.findByUsuarioIdOrderByFechaCreacionDesc(yo.getId())
                .stream()
                .map(JustificacionMapper::toResponse)
                .toList();
    }

    @Override
    public JustificacionResponse create(String correoPrincipal, JustificacionCreateRequest req) {
        Usuario creador = getUsuarioFromCorreo(correoPrincipal);

        if (req.getFecha() == null) throw new IllegalArgumentException("fecha es requerida");
        if (req.getMotivo() == null || req.getMotivo().isBlank()) throw new IllegalArgumentException("motivo es requerido");

        // ✅ Admin/Sup puede mandar usuarioId; empleado puede mandar null
        final Integer afectadoId = (req.getUsuarioId() != null) ? req.getUsuarioId() : creador.getId();

        Usuario afectado = usuarioRepository.findById(afectadoId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado: " + afectadoId));

        Marcacion marcacion = null;
        if (req.getMarcacionId() != null) {
            marcacion = marcacionRepository.findById(req.getMarcacionId())
                    .orElseThrow(() -> new EntityNotFoundException("Marcación no encontrada: " + req.getMarcacionId()));
        }

        Justificacion j = new Justificacion();
        j.setUsuario(afectado);
        j.setMarcacion(marcacion);
        j.setFecha(req.getFecha());
        j.setMotivo(req.getMotivo());
        j.setEstado("PENDIENTE");

        return JustificacionMapper.toResponse(justificacionRepository.save(j));
    }

    @Override
    public JustificacionResponse createWithDocument(String correoPrincipal, JustificacionCreateRequest req, MultipartFile documento) {
        JustificacionResponse base = create(correoPrincipal, req);

        Justificacion j = justificacionRepository.findById(base.getId())
                .orElseThrow(() -> new EntityNotFoundException("Justificación no encontrada"));

        if (documento != null && !documento.isEmpty()) {
            try {
                Files.createDirectories(UPLOAD_DIR);

                String original = documento.getOriginalFilename() == null ? "documento" : documento.getOriginalFilename();
                String safeName = original.replaceAll("[^a-zA-Z0-9\\.\\-_]", "_");

                String filename = UUID.randomUUID() + "_" + safeName;
                Path target = UPLOAD_DIR.resolve(filename);

                Files.copy(documento.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
                j.setDocumento(filename);

            } catch (IOException e) {
                throw new RuntimeException("Error al guardar documento: " + e.getMessage(), e);
            }
        }

        return JustificacionMapper.toResponse(justificacionRepository.save(j));
    }

    @Override
    public JustificacionResponse aprobar(Integer id, String correoPrincipal) {
        Usuario aprobador = getUsuarioFromCorreo(correoPrincipal);

        Justificacion j = justificacionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Justificación no encontrada: " + id));

        j.setEstado("APROBADO");
        j.setAprobadoPor(aprobador);
        j.setFechaAprobacion(LocalDateTime.now());

        return JustificacionMapper.toResponse(justificacionRepository.save(j));
    }

    @Override
    public JustificacionResponse rechazar(Integer id, String correoPrincipal, String motivoRechazo) {
        Usuario aprobador = getUsuarioFromCorreo(correoPrincipal);

        Justificacion j = justificacionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Justificación no encontrada: " + id));

        j.setEstado("RECHAZADO");
        j.setAprobadoPor(aprobador);
        j.setFechaAprobacion(LocalDateTime.now());

        return JustificacionMapper.toResponse(justificacionRepository.save(j));
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<Resource> downloadDocumento(Integer id) {
        Justificacion j = justificacionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Justificación no encontrada: " + id));

        if (j.getDocumento() == null || j.getDocumento().isBlank()) {
            throw new EntityNotFoundException("La justificación no tiene documento");
        }

        Path filePath = UPLOAD_DIR.resolve(j.getDocumento());

        try {
            if (!Files.exists(filePath)) throw new EntityNotFoundException("Archivo no encontrado");

            byte[] data = Files.readAllBytes(filePath);
            Resource resource = new ByteArrayResource(data);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + j.getDocumento() + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(data.length)
                    .body(resource);

        } catch (IOException e) {
            throw new RuntimeException("No se pudo leer el archivo", e);
        }
    }
}

