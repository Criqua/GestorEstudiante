package entities;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "Horario_Curso")
@Getter
@Setter
public class SemesterSchedule {
    @Id
    @Column(name = "ID_HoraSemestre")
    private String id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_ReserCurso", referencedColumnName = "ID_ReserCurso")
    private CourseDetails courseDetails;
}