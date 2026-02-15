package ec.mil.fae.asistencia.service.impl;

import ec.mil.fae.asistencia.entity.Unidad;
import ec.mil.fae.asistencia.repository.UnidadRepository;
import ec.mil.fae.asistencia.service.UnidadService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UnidadServiceImpl implements UnidadService {

    private final UnidadRepository unidadRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Unidad> findAll() {
        return unidadRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Unidad> findById(Integer id) {
        return unidadRepository.findById(id);
    }

    @Override
    public Unidad create(Unidad unidad) {
        return unidadRepository.save(unidad);
    }

    @Override
    public Unidad update(Integer id, Unidad unidad) {
        Unidad existing = unidadRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Unidad no encontrada con id: " + id));
        existing.setDescripcion(unidad.getDescripcion());
        return unidadRepository.save(existing);
    }

    @Override
    public void delete(Integer id) {
        if (!unidadRepository.existsById(id)) {
            throw new EntityNotFoundException("Unidad no encontrada con id: " + id);
        }
        unidadRepository.deleteById(id);
    }
}
