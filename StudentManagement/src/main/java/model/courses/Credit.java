package model.courses;

import customidentifiers.Identificator;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
public class Credit extends Identificator {

    @Column(name = "Nombre_Credito")
    private String name;
}
