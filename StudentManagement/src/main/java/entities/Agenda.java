package entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "Agenda")
@Data
public class Agenda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Evento")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "CIF")
    private Student student;

    @Column(name = "Titulo")
    private String title;

    @Column(name = "Descripcion")
    private String description;

    @Column(name = "Fecha_inicio")
    private LocalDateTime startDate;

    @Column(name = "Fecha_fin")
    private LocalDateTime endDate;
}
