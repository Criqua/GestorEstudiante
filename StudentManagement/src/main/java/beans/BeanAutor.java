package beans;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Named("autor")
@ViewScoped
@Getter
@Setter
public class BeanAutor implements Serializable {

    private String nombre;

    public void crearAutor() {
        System.out.println(this.nombre);
    }
}