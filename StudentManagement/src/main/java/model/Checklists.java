package model;

import customidentifiers.Identificator;
import entities.Student;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

@Entity
@Table(name = "Checklists")
@Getter
@Setter
public class Checklists extends Identificator {

    @ManyToOne
    @JoinColumn(name = "CIF", referencedColumnName = "CIF")
    private Student student;

    @Column(name = "Titulo")
    private String title;
}