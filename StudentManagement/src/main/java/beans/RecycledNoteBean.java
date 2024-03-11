package beans;

import entities.Notes;
import entities.Student;
import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import service.IDAO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
@Getter
@Setter
public class RecycledNoteBean implements Serializable {
    @Inject
    @Named("implDAO")
    private transient IDAO dao;

    @Inject
    private NoteBean noteBean;

    private List<Notes> recycledNotesList;
    private List<Notes> selectedNotes;

    private Student student;

    @PostConstruct
    public void init() {
        this.student = (Student) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");

        if (student != null) {
            this.recycledNotesList = new ArrayList<>();
            this.recycledNotesList = dao.get("Notes.FindByStudentAndStatus", Notes.class, "cif", student.getCIF(), "status", true);
        }
    }

    public String getBtnMessage() {
        noteBean.setSelectedNotes(this.selectedNotes);
        return noteBean.getDeleteBtnMessage();
    }
    
    public boolean hasSelectedNotes() {
        noteBean.setSelectedNotes(this.selectedNotes);
        return noteBean.hasSelectedNotes();
    }

    public void toggleNoteSelection(Notes note) {
        noteBean.setSelectedNotes(this.selectedNotes);
        noteBean.toggleNoteSelection(note);
    }
}
