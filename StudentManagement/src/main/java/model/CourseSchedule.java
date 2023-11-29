package model;

import customidentifiers.Identificator;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "Horario_Curso")
@Getter
@Setter
public class CourseSchedule extends Identificator {

    @OneToMany(mappedBy = "CourseSchedule", fetch = FetchType.LAZY)
    @Column(name = "ID_ReserCurso")
    private List<CourseDetails> courseDetailsList;

    @Column(name = "Dia")
    private LocalDate weekday;

    @Column(name = "Hora_Apertura")
    private LocalTime startTime;

    @Column(name = "Hora_Cierre")
    private LocalTime endTime;
}
