package entities;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "Cursos_Universidad")
@Getter
@Setter
public class DegreeCourses {
    @Id
    @Column(name = "ID_Curso")
    private String id;

    @Column(name = "ID_Programa")
    private String id_program;

    @Column(name = "Nombre_Curso")
    private String courseName;

    @OneToMany(mappedBy = "degreeCourses", fetch = FetchType.EAGER)
    private List<CourseDetails> courseDetailsList;
}