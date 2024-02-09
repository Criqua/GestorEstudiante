package entities;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "Lista_Docente")
@Getter
@Setter
public class Professor {
    @Id
    @Column(name = "ID_Docente")
    private String id;

    @Column(name = "Docente")
    private String fullName;

    @OneToMany(mappedBy = "professor", fetch = FetchType.EAGER)
    private List<CourseDetails> courseDetailsList;
}
