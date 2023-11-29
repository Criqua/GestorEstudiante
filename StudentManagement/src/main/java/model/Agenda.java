package model;


import customidentifiers.Identificator;
import entities.Student;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "Agenda")
@Getter
@Setter
public class Agenda extends Identificator {

    @OneToOne
    @JoinColumn(name = "CIF", referencedColumnName = "CIF")
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