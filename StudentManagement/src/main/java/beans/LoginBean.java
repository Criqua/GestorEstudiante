package beans;

import jakarta.faces.context.ExternalContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.PrimeFaces;
import service.IDAO;
import service.ImplDAO;

import entities.Student;

import java.io.IOException;
import java.io.Serializable;

@Named
@ViewScoped
@Getter
@Setter
public class LoginBean implements Serializable {

    private String cif;
    private String password;

    public String login() {
        final IDAO dao = new ImplDAO();
        Student student = dao.findById(Student.class, cif);

        if (student != null && student.getPassword().equals(password)) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("user", student);

            return "/mainMenu.xhtml?faces-redirect=true";
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error al iniciar sesión", "CIF o contraseña incorrectos. Favor vuelva a intentarlo."));

            PrimeFaces.current().ajax().update("formLogin:growl");

            return null;
        }
    }

    public void logout() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();

        // Invalidar la sesión
        externalContext.invalidateSession();

        try {
            // Redireccionar a la página de inicio de sesión
            externalContext.redirect(externalContext.getRequestContextPath() + "/login.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}