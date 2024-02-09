package entities;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "Detalles_Clase")
@Getter
@Setter
public class CourseDetails {
    @Id
    @Column(name = "ID_ReserCurso")
    private String id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_Curso", referencedColumnName = "ID_Curso")
    private DegreeCourses degreeCourses;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_Docente", referencedColumnName = "ID_Docente")
    private Professor professor;

    @OneToMany(mappedBy = "courseDetails", fetch = FetchType.EAGER)
    private List<SemesterSchedule> semesterScheduleList;
}