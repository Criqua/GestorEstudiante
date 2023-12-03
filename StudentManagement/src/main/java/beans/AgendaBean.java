
package beans;


import entities.Agenda;
import entities.Student;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleModel;
import service.IDAO;

import java.io.Serializable;
import java.util.List;
@Named
@RequestScoped
public class AgendaBean implements Serializable {
    @Inject
    @Named("implDAO")
    private IDAO dao;

    private ScheduleModel eventModel;

    @PostConstruct
    public void init() {
        eventModel = new DefaultScheduleModel();

        Student student = (Student) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
        String cifer = student.getCIF();
        if (student != null) {
            // Utiliza la interfaz IDAO para obtener los eventos del estudiante desde la base de datos
            List<Agenda> agendaList = dao.getAll("Agenda.findByCIF", Agenda.class);

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
    }
    public ScheduleModel getEventModel() {
        return eventModel;
    }
}
