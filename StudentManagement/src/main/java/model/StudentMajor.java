package model;

import customidentifiers.Identificator;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;


@Entity
@Table(name = "Carrera_Estudiante")
@Getter
@Setter
@NamedQuery(
        name = "StudentMajor.findByStudentId",
        query = "SELECT sm FROM StudentMajor sm WHERE sm.student.id = :studentId"
)
public class StudentMajor extends Identificator {
    @ManyToOne
    @JoinColumn(name = "CIF_Estudiante", referencedColumnName = "CIF")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "ID_Carrera", referencedColumnName = "ID_Carrera")
    private Major majorList;

    @Column(name = "Estado")
    private String status; // "Cursando", "Finalizado", "Retirado"

    /*
     * Forma de ejecutar la consulta:
     * TypedQuery<StudentMajor> query = entityManager.createNamedQuery("StudentMajor.findByStudentId", StudentMajor.class);
     * query.setParameter("studentId", student.getCIF);
     * List<StudentMajor> studentMajors = query.getResultList(); // Retorno de los resultados
     */
}