package beans;

import entities.Major;
import entities.Student;
import entities.StudentSemestralPeriod;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Data;
import service.IDAO;

import java.io.Serializable;
import java.util.List;

@Named
@RequestScoped
@Data
public class NoteBean implements Serializable {
    @Inject
    @Named("implDAO")
    private IDAO dao;

    private List<Student> majorList;

    Student student = (Student) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");

    // Método para inicializar un cuadro que enlista la(s) carrera(s) que cursa el estudiante
    // CB: ComboBox
    public void CB_Majors(){
        majorList = dao.get("Student.FindMajorsByCIF", Student.class, student.getCIF());
    }

    public void CB_coursedPeriods(){
        List<StudentSemestralPeriod> coursedPeriods = dao.get("StudentSemestralPeriod.FindByCIF",
                StudentSemestralPeriod.class, student.getCIF());
    }

    public void CB_assignedCourses(){

    }
}