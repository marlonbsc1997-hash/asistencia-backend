package ec.mil.fae.asistencia.service.impl;

import ec.mil.fae.asistencia.dto.response.ValidarUbicacionResponse;
import ec.mil.fae.asistencia.entity.Geocerca;
import ec.mil.fae.asistencia.repository.GeocercaRepository;
import ec.mil.fae.asistencia.service.GeocercaService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class GeocercaServiceImpl implements GeocercaService {

    private final GeocercaRepository geocercaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Geocerca> findAll() {
        return geocercaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Geocerca> findByEstadoTrue() {
        return geocercaRepository.findByActivoTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Geocerca> findById(Integer id) {
        return geocercaRepository.findById(id);
    }

    @Override
    public Geocerca create(Geocerca geocerca) {
        return geocercaRepository.save(geocerca);
    }

    @Override
    public Geocerca update(Integer id, Geocerca geocerca) {
        Geocerca existing = geocercaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Geocerca no encontrada con id: " + id));

        existing.setNombre(geocerca.getNombre());
        existing.setLatitud(geocerca.getLatitud());
        existing.setLongitud(geocerca.getLongitud());
        existing.setRadioMetros(geocerca.getRadioMetros());
        existing.setActivo(geocerca.getActivo());
        //existing.setUnidad(geocerca.getUnidad());

        return geocercaRepository.save(existing);
    }

    @Override
    public void delete(Integer id) {
        if (!geocercaRepository.existsById(id)) {
            throw new EntityNotFoundException("Geocerca no encontrada con id: " + id);
        }
        geocercaRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public ValidarUbicacionResponse verificarUbicacion(Double latitud, Double longitud) {
        List<Geocerca> geocercas = geocercaRepository.findByActivoTrue();

        for (Geocerca geocerca : geocercas) {
            double distance = calculateDistance(latitud, longitud, geocerca.getLatitud(), geocerca.getLongitud());
            if (distance <= geocerca.getRadioMetros()) {
                return new ValidarUbicacionResponse(true, geocerca, distance);
            }
        }
        return new ValidarUbicacionResponse(false, null, -1);
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371000; // metros
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}


