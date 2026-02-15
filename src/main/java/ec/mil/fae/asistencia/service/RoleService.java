package ec.mil.fae.asistencia.service;

import ec.mil.fae.asistencia.entity.Role;
import java.util.List;
import java.util.Optional;

public interface RoleService {
    List<Role> findAll();
    Optional<Role> findById(Integer id);
    Role create(Role role);
    Role update(Integer id, Role role);
    void delete(Integer id);
}
