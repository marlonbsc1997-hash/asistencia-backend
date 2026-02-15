// ===============================
// 4) SERVICE IMPL: DiaFestivoServiceImpl.java
// ===============================
package ec.mil.fae.asistencia.service.impl;

import ec.mil.fae.asistencia.entity.DiaFestivo;
import ec.mil.fae.asistencia.repository.DiaFestivoRepository;
import ec.mil.fae.asistencia.service.DiaFestivoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class DiaFestivoServiceImpl implements DiaFestivoService {

    private final DiaFestivoRepository diaFestivoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<DiaFestivo> findAll() {
        return diaFestivoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DiaFestivo> findById(Integer id) {
        return diaFestivoRepository.findById(id);
    }

    @Override
    public DiaFestivo create(DiaFestivo diaFestivo) {
        // creacion se setea solo con @PrePersist
        return diaFestivoRepository.save(diaFestivo);
    }

    @Override
    public DiaFestivo update(Integer id, DiaFestivo diaFestivo) {
        DiaFestivo existing = diaFestivoRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Día festivo no encontrado con id: " + id));

        existing.setFecha(diaFestivo.getFecha());
        existing.setDescripcion(diaFestivo.getDescripcion());
        existing.setRecurrente(diaFestivo.getRecurrente());

        // NO tocamos "creacion" en update
        return diaFestivoRepository.save(existing);
    }

    @Override
    public void delete(Integer id) {
        if (!diaFestivoRepository.existsById(id)) {
            throw new EntityNotFoundException("Día festivo no encontrado con id: " + id);
        }
        diaFestivoRepository.deleteById(id);
    }
}
