package ec.mil.fae.asistencia.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rol_id")
    private Integer id;
    
    @Column(name = "rol_nombre", nullable = false, unique = true, length = 50)
    private String nombre;
    
}
