package beans;

import entities.Agenda;
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
import service.ImplDAO;

import java.io.Serializable;
import java.util.List;
@Named
@RequestScoped
@Data
public class AgendaBean implements Serializable {
    @Inject
    @Named("implDAO")
    private IDAO dao;

    private ScheduleModel eventModel;

    private List<Agenda> agendaList;

    @PostConstruct
    public void init() {
        eventModel = new DefaultScheduleModel();

        Student student = (Student) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");

        // Obtén la lista de eventos de la agenda para el estudiante actual
        agendaList = dao.get("Agenda.FindEventsByCIF", Agenda.class, student.getCIF());
        System.out.println(student.getCIF());
        // Convierte los eventos de la base de datos a objetos DefaultScheduleEvent y agrégales al modelo
        for (Agenda agenda : agendaList) {

            DefaultScheduleEvent<?> event = DefaultScheduleEvent.builder()
                    .title(agenda.getTitle())
                    .description(agenda.getDescription())
                    .startDate(agenda.getStartDate())
                    .endDate(agenda.getEndDate())
                    .build();
            eventModel.addEvent(event);

            System.out.println(agenda);
        }
    }
}
