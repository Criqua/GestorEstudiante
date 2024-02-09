package entities;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Entity
@Table(name = "Carrera")
@Getter
@Setter
public class Major {
    @Id
    @Column(name = "ID_Carrera")
    private String id;

    @Column(name = "Nombre")
    private String name;

    @ManyToMany(mappedBy = "majorList", fetch = FetchType.EAGER)
    private List<Student> studentMajorList;
}