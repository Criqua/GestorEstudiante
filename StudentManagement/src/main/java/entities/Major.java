package entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
@Entity
@Table(name = "Carrera")
@Data
public class Major {
    @Id
    private String id;

    @Column(name = "Nombre")
    private String name;

    @ManyToMany(mappedBy = "majorList")
    private List<Student> studentMajorList;
}