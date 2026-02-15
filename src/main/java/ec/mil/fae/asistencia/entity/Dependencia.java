package ec.mil.fae.asistencia.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "dependencias")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Dependencia {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dep_id")
    private Integer id;
      
    @Column(name = "dep_descripcion")
    private String descripcion;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uni_id", nullable = false)
    private Unidad unidad;
    
}
