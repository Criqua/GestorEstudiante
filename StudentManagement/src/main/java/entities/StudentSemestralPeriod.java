package entities;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "vw_Historial_Periodos")
@Getter
@Setter
@NamedQueries({
        @NamedQuery(
                name = "StudentSemestralPeriod.FindByCIF",
                query = "SELECT h FROM StudentSemestralPeriod h WHERE h.cif = :cif"
        )
})
public class StudentSemestralPeriod {
    @Id
    @Column(name = "CIF")
    private String cif;

    @Column(name = "Periodo")
    private String period;
}