package beans;

import jakarta.faces.context.ExternalContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import lombok.Data;
import service.IDAO;
import service.ImplDAO;

import entities.Student;

import java.io.IOException;
import java.io.Serializable;
import jakarta.annotation.PostConstruct;

@Named
@ViewScoped
@Data
public class LoginBean implements Serializable {

    private String cif;
    private String password;
    private String nombreEstudiante; // Nueva propiedad para almacenar el nombre

    @PostConstruct
    public void init() {
        // Puedes realizar alguna inicialización aquí si es necesario
    }
    public String login() {
        final IDAO dao = new ImplDAO();
        // Se busca al estudiante por su cif
        Student student = dao.findById(Student.class, cif);

        if (student != null && student.getPassword().equals(password)) {
            // Almacenar el nombre del estudiante en la propiedad del bean
            nombreEstudiante = student.getFullName();

            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("user", student);
            return "loginAccess.xhtml";
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error de inicio de sesión", "CIF o contraseña incorrectos."));
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
