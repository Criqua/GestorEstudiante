package entities;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.ScheduleEvent;

import java.time.LocalDateTime;

@Entity
@Table(name = "Agenda")
@Getter
@Setter
@NamedQueries({
        @NamedQuery(
                name = "Agenda.FindEventByCIF",
                query = "SELECT a FROM Agenda a LEFT JOIN FETCH a.student WHERE a.student.CIF = :cif"
        )
})
public class Agenda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Evento")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CIF", referencedColumnName = "CIF")
    private Student student;

    @Column(name = "Titulo")
    private String title;

    @Column(name = "Descripcion")
    private String description;

    @Column(name = "Fecha_inicio")
    private LocalDateTime startDate;

    @Column(name = "Fecha_fin")
    private LocalDateTime endDate;
}