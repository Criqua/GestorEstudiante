package beans;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import lombok.Data;
import service.ImplDAO;

import entities.Student;

@Named
@RequestScoped
@Data
public class LoginBean {

    private String cif;

    protected String password;

    @Inject
    private ImplDAO dao;

    public String login() {
        // Se busca al estudiante por su cif
        Student student = dao.findById(Student.class, cif);

        if (student != null && student.getPassword().equals(password)) {
            // Si el inicio de sesión es exitoso, se almacena el CIF para futuras implementaciones
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("user", student);
            return "loginSuccess";  // Página a la que se redirige después del inicio de sesión exitoso
        } else {
            // Inicio de sesión fallido, muestra un mensaje de error
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de inicio de sesión", "CIF o contraseña incorrectos."));
            return null;  // Permanecer en la misma página de inicio de sesión
        }
    }
}