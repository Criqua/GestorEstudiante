package beans;

import entities.Major;
import entities.Student;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.NavigationHandler;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Named
@SessionScoped
@Getter
public class NavbarBean implements Serializable {

    private String fullName;

    private String profilePhoto;

    private String email;

    private List<String> majorNames;

    @PostConstruct
    public void init(){
        Student student = (Student) FacesContext.getCurrentInstance().
                getExternalContext().getSessionMap().get("user");

        if (student != null){
            fullName = student.getFullName();
            profilePhoto = student.getProfilePhoto();
            email = student.getEmail();

            // Se asigna a la lista del Managed Bean la lista de objetos original de 'Major'
            majorNames = student.getMajorList().stream().map(Major::getName).collect(Collectors.toList());
        }
    }

    public String redirectToHome(){
        return "/mainMenu.xhtml?faces-redirect=true";
    }
}