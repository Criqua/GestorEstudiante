package entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "Apuntes")
@Data
public class Notes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Apunte")
    private Long id;

    @ManyToMany
    @JoinTable(
            name = "Apuntes",
            joinColumns = @JoinColumn(name = "ID_Apunte"),
            inverseJoinColumns = @JoinColumn(name = "ID_Curso")
    )
    private List<DegreeCourses> degreeCoursesList;

    @ManyToOne
    @JoinColumn(name = "CIF", referencedColumnName = "CIF")
    private Student student;

    @Column(name = "Titulo")
    private String title;

    @Column(name = "Contenido_texto", nullable = true)
    private String body;

    @Column(name = "Contenido_svg", nullable = true)
    private String svg_body;

    @Column(name = "Fecha_creacion")
    private LocalDate creationDate;

    @Column(name = "Fecha_modificacion")
    private LocalDate lastModifiedDate;

    @Column(name = "Hora_modificacion")
    private LocalTime lastModifiedTime;

    @Column(name = "EnPapelera")
    private boolean inTrash;
}