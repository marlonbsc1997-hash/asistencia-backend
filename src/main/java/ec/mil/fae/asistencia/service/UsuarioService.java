package ec.mil.fae.asistencia.service;

import ec.mil.fae.asistencia.dto.request.UsuarioRequest;
import ec.mil.fae.asistencia.dto.response.UsuarioResponse;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    List<UsuarioResponse> findAll();
    Optional<UsuarioResponse> findById(Integer id);
    Optional<UsuarioResponse> findByCedula(String cedula);
    List<UsuarioResponse> findByDependenciaId(Integer dependenciaId);

    UsuarioResponse create(UsuarioRequest request);
    UsuarioResponse update(Integer id, UsuarioRequest request);
    void delete(Integer id);
}
