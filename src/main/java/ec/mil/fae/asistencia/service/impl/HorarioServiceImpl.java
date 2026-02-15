package ec.mil.fae.asistencia.service.impl;

import ec.mil.fae.asistencia.entity.HorarioLaboral;
import ec.mil.fae.asistencia.repository.HorarioRepository;
import ec.mil.fae.asistencia.service.HorarioService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class HorarioServiceImpl implements HorarioService {

    private final HorarioRepository horarioRepository;

    @Override
    @Transactional(readOnly = true)
    public List<HorarioLaboral> findAll() {
        return horarioRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<HorarioLaboral> findById(Integer id) {
        return horarioRepository.findById(id);
    }

    @Override
    public HorarioLaboral create(HorarioLaboral horario) {
        return horarioRepository.save(horario);
    }

    @Override
    public HorarioLaboral update(Integer id, HorarioLaboral horario) {
        HorarioLaboral existing = horarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Horario no encontrado con id: " + id));

        existing.setDescripcion(horario.getDescripcion());
        existing.setHoraEntradaJornada(horario.getHoraEntradaJornada());
        existing.setHoraSalidaJornada(horario.getHoraSalidaJornada());
        existing.setHoraSalidaAlmuerzo(horario.getHoraSalidaAlmuerzo());
        existing.setHoraRetornoAlmuerzo(horario.getHoraRetornoAlmuerzo());
        existing.setToleranciaMinutos(horario.getToleranciaMinutos());

        // ✅ ahora es LocalTime (TIME)
        existing.setHorasDiarias(horario.getHorasDiarias());

        existing.setDiasLaborables(horario.getDiasLaborables());

        return horarioRepository.save(existing);
    }

    @Override
    public void delete(Integer id) {
        if (!horarioRepository.existsById(id)) {
            throw new EntityNotFoundException("Horario no encontrado con id: " + id);
        }
        horarioRepository.deleteById(id);
    }
}


