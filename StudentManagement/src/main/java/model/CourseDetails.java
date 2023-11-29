package model;

import customidentifiers.Identificator;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import model.courses.DegreeCourses;

@Entity
@Table(name = "Detalles_Clase")
@Getter
@Setter
public class CourseDetails extends Identificator {

  /* @ManyToOne
    @JoinColumn(name = "ID_Curso", referencedColumnName = "ID_Curso")
    private DegreeCourses course;

    @OneToOne
    @JoinColumn(name = "ID_Docente", referencedColumnName = "ID_Docente")
    private Professor professor;

    @ElementCollection
    @CollectionTable(name = "CourseGroups", joinColumns=@JoinColumn(name="courseDetails_id"))
    @Column(name="Grupo")
    private Integer group;

    @ElementCollection
    @CollectionTable(name = "CourseClassrooms", joinColumns=@JoinColumn(name="courseDetails_id"))
    @Column(name="Aula")
    private String classroom;

   */
}