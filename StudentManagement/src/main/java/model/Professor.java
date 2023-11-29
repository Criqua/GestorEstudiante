package model;

import customidentifiers.Identificator;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
@Entity
@Table(name = "Lista_Docente")
@Getter
@Setter
public class Professor extends Identificator {

    @Column(name = "Docente")
    private String name;

    @Column(name = "Estado")
    private String status;
}