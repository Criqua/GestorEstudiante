package model.courses;


import customidentifiers.Identificator;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import model.Major;

@Entity
@Table(name="Cursos_Universidad")
@Getter
@Setter
public class DegreeCourses extends Identificator {

    @ManyToOne
    @JoinColumn(name = "ID_Programa", referencedColumnName = "ID_Carrera")
    private Major majorProgram;

    @ManyToOne
    @JoinColumn(name = "ID_Programa", referencedColumnName = "ID_Crédito")
    private Credit creditProgram;

    @Column(name="Nombre_Curso")
    private String courseName;
}