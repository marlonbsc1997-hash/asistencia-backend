package ec.mil.fae.asistencia.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "funcion")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Funcion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fun_id")
    private Integer id;
    
    @Column(name = "fun_descripcion")
    private String descripcion;
    
}
