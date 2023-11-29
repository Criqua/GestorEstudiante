package model;

import customidentifiers.Identificator;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "Apuntes")
@Getter
@Setter
public class Notes extends Identificator {

    @ManyToOne
    @JoinColumn(name = "ID_Curso", referencedColumnName = "ID_Curso")
    private String course;

    @ManyToOne
    @JoinColumn(name = "CIF", referencedColumnName = "CIF")
    private Student student;

    @Column(name = "Titulo")
    private String title;

    @Column(name = "Contenido")
    private String body;

    @Column(name = "Contenido_svg")
    private String svg_body;

    @Column(name = "Fecha_creacion")
    private LocalDate creationDate;

    @Column(name = "Fecha_modificacion")
    private LocalDate lastModifiedDate;

    @Column(name = "Hora_modificacion")
    private LocalTime lastModifiedTime;
}