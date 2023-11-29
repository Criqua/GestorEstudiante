package model;

import customidentifiers.Identificator;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Items_Checklist")
@Getter
@Setter
public class ChecklistItems extends Identificator {

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
