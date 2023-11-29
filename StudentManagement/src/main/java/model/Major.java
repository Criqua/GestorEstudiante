package model;

import customidentifiers.Identificator;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "Carrera")
@Getter
@Setter
public class Major extends Identificator {

    @Column(name = "Nombre")
    private String name;

    @OneToMany(mappedBy = "Carrera")
    private List<StudentMajor> studentMajorList;
}