package model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "Estudiante")
@Getter
@Setter
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

    @Column(name = "Estado")
    private String status;

    @Column(name = "FotoPerfil")
    private String profilePhoto;

    @OneToMany(mappedBy = "student")
    private List<StudentMajor> studentMajorList;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<Notes> notesList;

    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL)
    private Agenda agenda;

    public Student(String CIF, String fullName, String gender, String email, String password, String status, String profilePhoto) {
        this.CIF = CIF;
        this.fullName = fullName;
        this.gender = gender;
        this.email = email;
        this.password = password;
        this.status = status;
        this.profilePhoto = profilePhoto;

    }

    public Student() {
    }
}