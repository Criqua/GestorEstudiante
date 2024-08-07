package entities;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "Estudiante")
@Getter
@Setter
@NamedQueries({
        @NamedQuery(
                name = "Student.FindByCIF",
                query="select e from Student e where e.CIF =?1"
        ),
        @NamedQuery(
                name = "Student.FindMajorsByCIF",
                query="select e.majorList from Student e where e.CIF = ?1"
        )
})
public class Student {
    @Id
    @Column(name = "CIF")
    private String CIF;

    @Column(name = "Nombre")
    private String fullName;

    @Column(name = "Genero")
    private String gender;

    @Column(name = "Correo")
    private String email;

    @Column(name = "Contrasenia")
    protected String password;

    @Column(name = "FotoPerfil", nullable = true)
    private String profilePhoto;

    @Column(name = "Estado")
    private boolean status;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "Carrera_Estudiante",
            joinColumns = @JoinColumn(name = "CIF_Estudiante"),
            inverseJoinColumns = @JoinColumn(name = "ID_Carrera")
    )
    private List<Major> majorList;

    @OneToMany(mappedBy = "student", fetch = FetchType.EAGER)
    private List<Notes> notesList;

    @OneToMany(mappedBy = "student", fetch = FetchType.EAGER)
    private List<Agenda> agendaList;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "Horario_Alumno",
            joinColumns = @JoinColumn(name = "CIF"),
            inverseJoinColumns = @JoinColumn(name = "ID_HoraSemestre")
    )
    private List<SemesterSchedule> semesterScheduleList;
}