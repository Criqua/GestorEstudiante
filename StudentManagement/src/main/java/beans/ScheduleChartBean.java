package beans;

import entities.Agenda;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.schedule.ScheduleEntryMoveEvent;
import org.primefaces.event.schedule.ScheduleEntryResizeEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Named
@ViewScoped
@Getter
@Setter
public class ScheduleChartBean extends AgendaBean implements Serializable {

    private ScheduleModel eventModel;

    private ScheduleEvent<?> scheduleEvent = new DefaultScheduleEvent<>();

    private boolean slotEventOverlap = true;
    private boolean showWeekNumbers = false;
    private boolean showHeader = true;
    private boolean draggable = true;
    private boolean resizable = true;
    private boolean selectable = false;
    private boolean showWeekends = true;
    private boolean tooltip = true;
    private boolean allDaySlot = true;
    private boolean rtl = false;

    private double aspectRatio = Double.MIN_VALUE;

    private String serverTimeZone = ZoneId.systemDefault().toString();
    private String clientTimeZone = "local";
    private String locale = "es";
    private String timeZone = "";
    private String view = "timeGridWeek";
    private String height = "auto";
    private String leftHeaderTemplate = "prev,next today";
    private String centerHeaderTemplate = "title";
    private String rightHeaderTemplate = "dayGridMonth,timeGridWeek,timeGridDay,listYear";
    private String nextDayThreshold = "09:00:00";
    private String weekNumberCalculation = "local";
    private String weekNumberCalculator = "date.getTime()";
    private String displayEventEnd;
    private String timeFormat;
    private String slotDuration = "00:30:00";
    private String slotLabelInterval;
    private String slotLabelFormat;
    private String scrollTime = "06:00:00";
    private String minTime = "04:00:00";
    private String maxTime = "20:00:00";

    @PostConstruct
    @Override
    public void init(){
        super.init();
        eventModel = new DefaultScheduleModel();
        // loadEvents();
    }

    @Override
    public void onDateSelect(SelectEvent<LocalDateTime> event) {
        super.onDateSelect(event);
    }

   /* private void loadEvents(){
        List<Agenda> agendaEventsList = dao.getAll("Agenda.FindEventsByCIF", Agenda.class);

        // Se agregan al modelo los eventos de la BD a objetos DefaultScheduleEvent
        for (Agenda agendaEvent : agendaEventsList){
            DefaultScheduleEvent<?> event = DefaultScheduleEvent.builder()
                    .title(agendaEvent.getTitle())
                    .description(agendaEvent.getDescription())
                    .startDate(agendaEvent.getStartDate())
                    .endDate(agendaEvent.getEndDate())
                    .build();
            eventModel.addEvent(event);
        }
    }*/

    private void addEventsToEventModel(){
        DefaultScheduleEvent<?> sEvent = DefaultScheduleEvent.builder()
                .title(event.getTitle())
                .description(event.getDescription())
                .startDate(event.getStartDate())
                .endDate(event.getEndDate())
                .build();
    }

    private void addEvent(ScheduleEvent<?> event){
        if (event.getId() == null){
            eventModel.addEvent(event);
        } else {
            eventModel.updateEvent(event);
        }
    }

    public void onViewChange(SelectEvent<String> selectEvent) {
        view = selectEvent.getObject();
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se ha cambiado la vista", "Vista:" + view);
        addMessage(message);
    }

    public void onEventSelect(SelectEvent<ScheduleEvent<?>> selectEvent) {
        scheduleEvent = selectEvent.getObject();
    }

    public void onEventMove(ScheduleEntryMoveEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Evento movido",
                "Delta:" + event.getDeltaAsDuration());
        addMessage(message);
    }

    public void onEventResize(ScheduleEntryResizeEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Evento redimensionado",
                "Start-Delta:" + event.getDeltaStartAsDuration() + ", End-Delta: " + event.getDeltaEndAsDuration());
        addMessage(message);
    }

    private void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    /*private void saveEvent(){
        dao.save(event);
    }*/
}
