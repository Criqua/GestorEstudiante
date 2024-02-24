package beans;

import entities.Student;
import jakarta.annotation.PostConstruct;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import lombok.Getter;
import lombok.Setter;
import service.IDAO;

import java.io.Serializable;

@Named
@RequestScoped
@Getter
@Setter
public class MainMenuBean implements Serializable {
    /*@Inject
    @Named("implDAO")
    private IDAO dao;*/

    private String name;

    private String profilePhoto;

    @PostConstruct
    public void init() {
        Student student = (Student) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");

        name = student.getFullName();
        profilePhoto = student.getProfilePhoto();
    }
}

/* Diseños a tomar en cuenta para la estructura del menú principal:
        - Las pestañas de "Mis apuntes" y "Mis checklists" estarán en un panel central,
        "Mis apuntes" estará encima de "Mis checklists" y cada uno tendrá una vista previa de las
tres notas modificadas recientemente. Dentro de "Mis apuntes" habrá un filtro por carrera, semestre
y materia.

        - La pestaña de "Mi agenda" estará en un panel izquierdo, mientras que la pestaña de "Mi horario actual"
estará en un panel derecho. "Mi agenda" tendrá una versión minimizada en menú que mostrará el mes actual,
pero que puede ser maximizado, lo mismo ocurrirá con la pestaña de "Mi horario actual".

        - Al tocar el usuario, se mostrará una ventana de diálogo que muestre datos como el nombre, foto de perfil,
correo, sexo y carreras cursadas
*/