package beans;

import entities.Agenda;
import entities.Student;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.NormalScope;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.annotation.View;
import jakarta.faces.application.FacesMessage;
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
import java.time.ZoneId;
import java.util.Date;
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

    private String newEventTitle;
    private String newEventDescription;
    private java.util.Date newEventStartDate;
    private java.util.Date newEventEndDate;

    @PostConstruct
    public void init() {
        eventModel = new DefaultScheduleModel();

        Student student = (Student) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");

        // Obtén la lista de eventos de la agenda para el estudiante actual
        agendaList = dao.get("Agenda.FindEventsByCIF", Agenda.class, student.getCIF());

        // Convierte los eventos de la base de datos a objetos DefaultScheduleEvent y agrégales al modelo
        for (Agenda agenda : agendaList) {
            DefaultScheduleEvent<?> event = DefaultScheduleEvent.builder()
                    .title(agenda.getTitle())
                    .description(agenda.getDescription())
                    .startDate(agenda.getStartDate())
                    .endDate(agenda.getEndDate())
                    .build();
            eventModel.addEvent(event);
        }
    }
    public void addEvent() {
        DefaultScheduleEvent<?> newEvent = new DefaultScheduleEvent<>();
        newEvent.setTitle(newEventTitle);
        newEvent.setDescription(newEventDescription);
        LocalDateTime startDateTime = newEventStartDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime endDateTime = newEventEndDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        newEvent.setStartDate(startDateTime);
        newEvent.setEndDate(endDateTime);

        // Agrega el evento al modelo
        eventModel.addEvent(newEvent);

        // Guarda el nuevo evento en la base de datos
        Agenda agenda = new Agenda();
        agenda.setStudent((Student) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user"));
        agenda.setTitle(newEvent.getTitle());
        agenda.setDescription(newEvent.getDescription());
        agenda.setStartDate(newEvent.getStartDate());
        agenda.setEndDate(newEvent.getEndDate());

        dao.insert(agenda);
        System.out.println("ID asignado: " + agenda.getId());


        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Evento agregado con éxito", null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}
