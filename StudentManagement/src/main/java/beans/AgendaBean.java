package beans;

import entities.Agenda;
import jakarta.annotation.PostConstruct;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

import service.IDAO;

import java.io.Serializable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

@Named
@ViewScoped
@Getter
@Setter
public class AgendaBean implements Serializable {
    protected Agenda event;

    private LocalDateTime selectedDate;

    @PostConstruct
    public void init() {
        event = new Agenda();
    }

    /**
     * Método que permite seleccionar la fecha de inicio de un evento.
     * @apiNote Se establece la fecha de inicio del evento en base a la fecha seleccionada en el p:datePicker o p:schedule.
     *          La hora se establece a las 00:00 (medianoche) y termina una hora después por defecto
     */
    public void onDateSelect(SelectEvent<LocalDateTime> event) {
        PrimeFaces.current().ajax().update("form:display");
        PrimeFaces.current().executeScript("PF('eventDialog').show()");

        selectedDate = event.getObject().with(LocalTime.MIDNIGHT);

        this.getEvent().setStartDate(selectedDate);
        this.getEvent().setEndDate(selectedDate.plusHours(1));
    }

    public String redirectToSchedule(){
        return "/scheduleChart.xhtml?faces-redirect=true";
    }
}