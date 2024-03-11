package beans;

import entities.Notes;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

import service.IDAO;

import java.io.Serializable;
import java.time.LocalDateTime;

@Named
@ViewScoped
@Getter
@Setter
public class DocumentBean implements Serializable {
    @Inject
    @Named("implDAO")
    private transient IDAO dao;

    @Inject
    private NoteBean noteBean;

    private Notes selectedNote;

    @PostConstruct
    public void init() {
        Object data = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("selectedNote");

        if (data != null) {
            this.selectedNote = (Notes) data;
            this.selectedNote.setLastOpenedDate(LocalDateTime.now());
        }
    }

    public void updateNote() {
        noteBean.updateNote(this.selectedNote);
    }
}