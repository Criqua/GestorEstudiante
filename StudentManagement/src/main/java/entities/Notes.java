package entities;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "Apuntes")
@Getter
@Setter
@NamedStoredProcedureQuery(
        name = "Notes.apunteAPapelera",
        procedureName = "ApunteAPapelera",
        parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, type = String.class, name = "ID_Apunte")
        }
)
@NamedStoredProcedureQuery(
        name = "Notes.eliminarApunteEnLimite",
        procedureName = "EliminarApunteEnLimite"
)
@NamedStoredProcedureQuery(
        name = "Notes.recuperarApunte",
        procedureName = "RecuperarApunte",
        parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, type = String.class, name = "ID_Apunte")
        }
)
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

    @ManyToOne(fetch = FetchType.EAGER)
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