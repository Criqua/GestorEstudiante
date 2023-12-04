package beans;

import entities.Major;
import entities.Student;
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

    //private List<Student>

    Student student = (Student) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");

    // Método para inicializar un cuadro que enlista la(s) carrera(s) que cursa el estudiante
    public void ComboBoxMajors(){
        majorList = dao.get("Student.FindMajorsByCIF", Student.class, student.getCIF());
    }

    public void ComboBoxMajorCourses(){

    }
}