package entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;


@Entity
@Table(name = "Items_Checklist")
@Getter
@Setter
public class ChecklistItems implements Serializable {
    @Id
    @Column(name = "Elementos_Checklist")
    private String id;

    @ManyToOne
    @JoinColumn(name = "ID_Checklist", referencedColumnName = "ID_Checklist")
    private Checklists checklist;

    @Column(name = "Objetivo_Elemento")
    private String description;

    @Column(name = "Fecha_Expiracion")
    private LocalDate endDate;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "Estado")
    private ChecklistStatus status;

    public enum ChecklistStatus{
        FINALIZADO, EN_PROGRESO, RETRASADO, CANCELADO
    }
}