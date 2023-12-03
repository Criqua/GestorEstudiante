package entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "Agenda")
@Data
public class Agenda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Apunte")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "CIF")
    private Student student;

    @Column(name = "Titulo")
    private String title;

    @Column(name = "Descripcion")
    private String description;

    @Column(name = "Fecha_inicio")
    private LocalDate startDate;

    @Column(name = "Fecha_fin")
    private LocalDate endDate;

    @Column(name = "Hora_inicio")
    private LocalTime startTime;

    @Column(name = "Hora_fin")
    private LocalTime endTime;
}