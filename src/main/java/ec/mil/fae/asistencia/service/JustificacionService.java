package ec.mil.fae.asistencia.service;

import ec.mil.fae.asistencia.dto.request.JustificacionCreateRequest;
import ec.mil.fae.asistencia.dto.response.JustificacionResponse;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface JustificacionService {

    List<JustificacionResponse> findAll();
    List<JustificacionResponse> findPendientes();
    List<JustificacionResponse> findMine(String correoPrincipal);

    JustificacionResponse create(String correoPrincipal, JustificacionCreateRequest req);
    JustificacionResponse createWithDocument(String correoPrincipal, JustificacionCreateRequest req, MultipartFile documento);

    JustificacionResponse aprobar(Integer id, String correoPrincipal);
    JustificacionResponse rechazar(Integer id, String correoPrincipal, String motivoRechazo);

    ResponseEntity<Resource> downloadDocumento(Integer id);
}
