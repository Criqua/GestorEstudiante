package entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "Checklists")
@Getter
@Setter
public class Checklists {
    @Id
    @Column(name = "ID_Checklist")
    private String id;

    @ManyToOne
    @JoinColumn(name = "CIF", referencedColumnName = "CIF")
    private Student student;

    @Column(name = "Titulo")
    private String title;
}