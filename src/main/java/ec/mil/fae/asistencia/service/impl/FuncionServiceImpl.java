package ec.mil.fae.asistencia.service.impl;

import ec.mil.fae.asistencia.entity.Funcion;
import ec.mil.fae.asistencia.repository.FuncionRepository;
import ec.mil.fae.asistencia.service.FuncionService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class FuncionServiceImpl implements FuncionService {

    private final FuncionRepository funcionRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Funcion> findAll() {
        return funcionRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Funcion> findById(Integer id) {
        return funcionRepository.findById(id);
    }

    @Override
    public Funcion create(Funcion funcion) {
        return funcionRepository.save(funcion);
    }

    @Override
    public Funcion update(Integer id, Funcion funcion) {
        Funcion existing = funcionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Función no encontrada con id: " + id));
        existing.setDescripcion(funcion.getDescripcion());
        return funcionRepository.save(existing);
    }

    @Override
    public void delete(Integer id) {
        if (!funcionRepository.existsById(id)) {
            throw new EntityNotFoundException("Función no encontrada con id: " + id);
        }
        funcionRepository.deleteById(id);
    }
}
