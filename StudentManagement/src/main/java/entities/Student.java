package entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "Estudiante")
@Data
public class Student {
    @Id
    private String CIF;

    @Column(name = "Nombre")
    private String fullName;

    @Column(name = "Genero")
    private String email;

    @Column(name = "Contrasenia")
    protected String password;

    @Column(name = "FotoPerfil", nullable = true)
    private String profilePhoto;

    @Column(name = "Estado")
    private String status;

    @ManyToMany
    @JoinTable(
            name = "CarreraEstudiante",
            joinColumns = @JoinColumn(name = "CIF"),
            inverseJoinColumns = @JoinColumn(name = "ID_Carrera"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"CIF", "ID_Carrera"})
    )
    private List<Major> majorList;
}