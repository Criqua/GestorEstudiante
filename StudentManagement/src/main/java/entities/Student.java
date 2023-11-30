package entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "Estudiante")
@Data
@NamedQueries({
        @NamedQuery(name = "Student.FindByCIF", query="select e from Student e where e.CIF =?1")
})
public class Student {
    @Id
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
    private String status;

  /*@ManyToMany
    @JoinTable(
            name = "Carrera_Estudiante",
            joinColumns = @JoinColumn(name = "CIF"),
            inverseJoinColumns = @JoinColumn(name = "ID_Carrera"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"CIF", "ID_Carrera"})
    )
    private List<Major> majorList;*/
}