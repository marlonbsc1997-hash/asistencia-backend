package ec.mil.fae.asistencia.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "unidades")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Unidad {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uni_id")
    private Integer id;
   
    @Column(name = "uni_descripcion")
    private String descripcion;
    
}
