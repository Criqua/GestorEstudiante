package entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;

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
}