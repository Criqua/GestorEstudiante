package entities;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "Apuntes")
@Getter
@Setter
@NamedQueries({
        @NamedQuery(
                name = "Notes.FindByStudentAndStatus",
                query = "SELECT n FROM Notes n " +
                        "LEFT JOIN FETCH n.student " +
                        "LEFT JOIN FETCH n.degreeCourses " +
                        "WHERE n.student.CIF = :cif AND n.inTrash = :status"
        )
})
public class Notes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Apunte")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_Curso", referencedColumnName = "ID_Curso")
    private DegreeCourses degreeCourses;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CIF", referencedColumnName = "CIF")
    private Student student;

    @Column(name = "Titulo")
    private String title;

    @Column(name = "Contenido", nullable = true)
    private String body;

    @Column(name = "Fecha_creacion")
    private LocalDateTime creationDate;

    @Column(name = "Fecha_modificacion", nullable = true)
    private LocalDateTime lastModifiedDate;

    @Column(name = "Fecha_abierto")
    private LocalDateTime lastOpenedDate;

    @Column(name = "EnPapelera")
    private boolean inTrash;
}