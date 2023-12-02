
package beans;


// import entities.Agenda;
import entities.Student;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Data;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleModel;
import service.IDAO;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
@Named
@ViewScoped // import javax.faces.view.ViewScoped;
public class AgendaControl implements Serializable {
    private ScheduleModel eventModel;

    @PostConstruct
    public void init() {
        eventModel = new DefaultScheduleModel();

        DefaultScheduleEvent<?> event = DefaultScheduleEvent.builder()
                .title("Champions League Match")
                // as I tested, you NEED a start date and enddate
                .startDate(LocalDateTime.of(2023, 1, 18, 8, 0)) // change with tomorrow's date
                .endDate(LocalDateTime.of(2023, 1, 18, 12, 0)) // change with tomorrow's date
                .build();
        eventModel.addEvent(event);
    }

    public ScheduleModel getEventModel() {
        return eventModel;
    }
}

/*
@Named
@RequestScoped
public class AgendaControl implements Serializable {

    @Inject
    private IDAO dao;

    private ScheduleModel eventModel;

    @PostConstruct
    public void init() {
        eventModel = new DefaultScheduleModel();
        loadEventsFromDatabase();
    }

    private void loadEventsFromDatabase() {
        // Obtén el estudiante logueado desde la sesión
        Student student = (Student) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");

        if (student != null) {
            // Utiliza la interfaz IDAO para obtener los eventos del estudiante desde la base de datos
            List<Agenda> agendaList = dao.getAll("SELECT a FROM Agenda a WHERE a.student.cif = :cif", Agenda.class);

            // Convierte los eventos de la base de datos a objetos DefaultScheduleEvent y agrégales al modelo
            for (Agenda agenda : agendaList) {
                DefaultScheduleEvent<?> event = DefaultScheduleEvent.builder()
                        .title(agenda.getTitle())
                        .startDate(agenda.getStartDate().atTime(agenda.getStartTime()))
                        .endDate(agenda.getEndDate().atTime(agenda.getEndTime()))
                        .build();
                eventModel.addEvent(event);
            }
        }
    }

    public ScheduleModel getEventModel() {
        return eventModel;
    }
}
*/