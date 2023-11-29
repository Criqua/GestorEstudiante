package model;

import customidentifiers.Identificator;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "Horario_Alumno")
@Getter
@Setter
/**
 * Consulta para recuperar todos los registros de la tabla "Carrera_Estudiante"
 * que coincidan con el CIF de un estudiante, a fin de filtrar la(s) carrera(s) que está cursando
 */
@NamedQuery(
        name = "StudentSchedule.findByStudentId",
        query = "SELECT ss FROM StudentMajor ss WHERE ss.student.id = :studentId"
)
public class StudentSchedule extends Identificator {
    @ManyToOne
    @JoinColumn(name = "CIF", referencedColumnName = "CIF")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "ID_HoraSemestre", referencedColumnName = "ID_HoraSemestre")
    private CourseSchedule courseSchedule;

    /**
     * Forma de ejecutar la consulta de búsqueda:
     * TypedQuery<StudentSchedule> query = entityManager.createNamedQuery("StudentSchedule.findByStudentId", StudentSchedule.class);
     * query.setParameter("studentCIF", student.getCIF());
     * List<StudentSchedule> studentSchedules = query.getResultList(); // Retorno de los resultados
     */

    // NamedQuery para actualizar un registro
    /*@NamedQuery(
            name = "StudentSchedule.updateMajorById",
            query = "UPDATE Horario_Alumno ss SET ss.major = :major WHERE ss.id = :scheduleId"
    )*/
}