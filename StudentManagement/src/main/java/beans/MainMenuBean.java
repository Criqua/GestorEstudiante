package beans;

import entities.Student;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Data;
import service.IDAO;

@Named
@ApplicationScoped
@Data
public class MainMenuBean {
    @Inject
    @Named("implDAO")
    private IDAO dao;

    private String Nombre;

    private String profilePhoto;

    @PostConstruct
    public void init() {
        Student student = (Student) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");

        Nombre = student.getFullName();
        profilePhoto = student.getProfilePhoto();
    }
}
