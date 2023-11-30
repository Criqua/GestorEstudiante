package beans;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import lombok.Data;
import service.IDAO;
import service.ImplDAO;

import entities.Student;

import java.io.Serializable;

@Named
@ViewScoped
@Data
public class LoginBean implements Serializable {

    private String cif;

    protected String password;

    public String login() {
        final IDAO dao = new ImplDAO();
        // Se busca al estudiante por su cif
        Student student = dao.findById(Student.class, cif);

        if (student != null && student.getPassword().equals(password)) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("user", student);
            return "loginSuccess";
        } else{
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error de inicio de sesión", "CIF o contraseña incorrectos."));
            return null;
        }
    }
}