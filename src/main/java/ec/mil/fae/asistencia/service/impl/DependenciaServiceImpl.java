package ec.mil.fae.asistencia.service.impl;

import ec.mil.fae.asistencia.dto.request.DependenciaRequest;
import ec.mil.fae.asistencia.entity.Dependencia;
import ec.mil.fae.asistencia.entity.Unidad;
import ec.mil.fae.asistencia.repository.DependenciaRepository;
import ec.mil.fae.asistencia.repository.UnidadRepository;
import ec.mil.fae.asistencia.service.DependenciaService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class DependenciaServiceImpl implements DependenciaService {

    private final DependenciaRepository dependenciaRepository;
    private final UnidadRepository unidadRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Dependencia> findAll() {
        return dependenciaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Dependencia> findById(Integer id) {
        return dependenciaRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Dependencia> findByUnidadId(Integer unidadId) {
        return dependenciaRepository.findByUnidadId(unidadId);
    }

    @Override
    public Dependencia create(DependenciaRequest request) {

        Unidad unidad = unidadRepository.findById(request.getUnidadId())
                .orElseThrow(() ->
                        new EntityNotFoundException("Unidad no encontrada con id: " + request.getUnidadId())
                );

        Dependencia dependencia = new Dependencia();
        dependencia.setDescripcion(request.getDescripcion());
        dependencia.setUnidad(unidad);

        return dependenciaRepository.save(dependencia);
    }

    @Override
    public Dependencia update(Integer id, DependenciaRequest request) {

        Dependencia dependencia = dependenciaRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Dependencia no encontrada con id: " + id)
                );

        Unidad unidad = unidadRepository.findById(request.getUnidadId())
                .orElseThrow(() ->
                        new EntityNotFoundException("Unidad no encontrada con id: " + request.getUnidadId())
                );

        dependencia.setDescripcion(request.getDescripcion());
        dependencia.setUnidad(unidad);

        return dependenciaRepository.save(dependencia);
    }

    @Override
    public void delete(Integer id) {
        if (!dependenciaRepository.existsById(id)) {
            throw new EntityNotFoundException("Dependencia no encontrada con id: " + id);
        }
        dependenciaRepository.deleteById(id);
    }
}

