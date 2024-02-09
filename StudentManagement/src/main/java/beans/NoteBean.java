package beans;

import entities.Notes;
import entities.Student;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import service.IDAO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@RequestScoped
@Getter
@Setter
public class NoteBean implements Serializable {
    @Inject
    @Named("implDAO")
    private IDAO dao;

    private List<String> noteLists = new ArrayList<>(List.of("Nota 1", "Nota 2", "Nota 3"));

    private String layout = "grid";

    private List<String> checklistLists = new ArrayList<>(List.of("Checklist 1", "Checklist 2", "Checklist 3"));
    private List<String> selectedNotes;

    private List<Student> majorList;

    Student student = (Student) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");

    // Método para inicializar un cuadro que enlista la(s) carrera(s) que cursa el estudiante
    // CB: ComboBox
   /* public void CB_Majors(){
        majorList = dao.get("Student.FindMajorsByCIF", Student.class, student.getCIF());
    }

    public void CB_coursedPeriods(){
        List<StudentSemestralPeriod> coursedPeriods = dao.get("StudentSemestralPeriod.FindByCIF",
                StudentSemestralPeriod.class, student.getCIF());
    }

    public void CB_assignedCourses(){

    }*/

    public String getDeleteBtnMessage(){
        if(hasSelectedNotes()){
            int size = this.selectedNotes.size();
            return size > 1 ? size + " notas seleccionadas" : "nota seleccionada";
        }

        return "Delete";
    }

    public boolean hasSelectedNotes(){
        return this.selectedNotes != null && !this.selectedNotes.isEmpty();
    }
}

// margin right: 15px;