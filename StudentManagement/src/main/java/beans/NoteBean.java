package beans;

import entities.DegreeCourses;
import entities.Notes;
import entities.Student;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.model.SelectItem;
import jakarta.faces.model.SelectItemGroup;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.PrimeFaces;
import service.IDAO;

import java.io.Serializable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;
import java.util.stream.Collectors;

@Named
@ViewScoped
@Getter
@Setter
public class NoteBean implements Serializable {
    @Inject
    @Named("implDAO")
    transient private IDAO dao;

    @Inject
    private StudentScheduleService studentScheduleService;

    private List<String> noteLists = new ArrayList<>(List.of("Nota 1", "Nota 2", "Nota 3"));
    private List<Notes> notesList;
    private List<Notes> filteredNotes;
    private List<Notes> selectedNotes;

    private List<String> checklistLists = new ArrayList<>(List.of("Checklist 1", "Checklist 2", "Checklist 3"));

    private Notes selectedNote;
    private String body;

    private String layout = "grid";

    private String typeFilter;
    private String timeFilter;

    private DegreeCourses selectedSubject;

    private boolean isExpanded = false;
    private boolean selected;

    private List<LocalDate> dateRange;
    private LocalDate startDateRange;
    private LocalDate endDateRange;

    private List<SelectItem> allPeriodCourses;

    @PostConstruct
    public void init(){
        Student student = (Student) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");

        if (student != null){
            this.notesList = new ArrayList<>();
            this.notesList = dao.get("Notes.FindByStudentAndStatus", Notes.class,
                    "cif", student.getCIF(), "status", false);
            this.filteredNotes = new ArrayList<>();
            this.filteredNotes.addAll(notesList);
            this.typeFilter = "all";
            this.timeFilter = "lastModified";
            this.applyFilters();
        }

        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();
        int currentMonthValue = currentDate.getMonthValue();
        int currentSemester = currentMonthValue <= 6 ? 1 : 2;

        List<String> uniquePeriods = studentScheduleService.getUniquePeriods();
        Map<String, List<DegreeCourses>> periodsWithCourses = studentScheduleService.associatePeriodsWCourses(uniquePeriods);

        allPeriodCourses = new ArrayList<>();

        // Grupo para el período actual
        SelectItemGroup currentPeriodGroup = new SelectItemGroup("Asignaturas en período actual");
        List<SelectItem> currentPeriodItems = new ArrayList<>();

        for (String period : uniquePeriods){
            List<SelectItem> subjectItems = new ArrayList<>();
            Set<String> courseIds = new HashSet<>();

            String[] parts = period.split("-");
            int periodYear = Integer.parseInt(parts[0]);
            int periodSemester = Integer.parseInt(parts[1]);

            List<DegreeCourses> courses = periodsWithCourses.get(period);

            if (courses != null){
                for (DegreeCourses course : courses){
                    String courseId = course.getId();

                    if (!courseIds.contains(courseId)) {
                        subjectItems.add(new SelectItem(courseId, course.getCourseName()));
                        courseIds.add(courseId);
                    }
                }
            }

            // Se crean grupos y se agregan items según el período dado (actual o pasado)
            if (periodYear == currentYear && periodSemester == currentSemester) {
                currentPeriodItems.addAll(subjectItems);
            } else {
                String periodHeader = "Período: " + periodYear + " - Semestre: " + periodSemester;
                SelectItemGroup periodGroup = new SelectItemGroup(periodHeader);
                periodGroup.setSelectItems(subjectItems.toArray(new SelectItem[0])); // Agregar items al grupo
                allPeriodCourses.add(periodGroup); // Agregar grupo a la lista general
            }
        }

        // Se agregan los items del periodo actual al grupo del periodo actual, moviendo el grupo al inicio de la lista
        currentPeriodGroup.setSelectItems(currentPeriodItems.toArray(new SelectItem[0]));
        allPeriodCourses.add(0, currentPeriodGroup);

        // Impresión de lista de cursos (por cuestiones de prueba)
        /*System.out.println("Contenido de allPeriodCourses:");
        for (SelectItem item : allPeriodCourses) {
            if (item instanceof SelectItemGroup) {
                System.out.println(((SelectItemGroup) item).getLabel());
                for (SelectItem subItem : ((SelectItemGroup) item).getSelectItems()) {
                    System.out.println("   " + subItem.getLabel() + ": " + subItem.getValue());
                }
            } else {
                System.out.println(item.getLabel() + ": " + item.getValue());
            }
        }*/
    }

    public void openNew() {
        this.selectedNote = new Notes();
    }

    public String getDeleteBtnMessage() {
        if(hasSelectedNotes()){
            int size = this.selectedNotes.size();
            return size > 1 ? size + " notas seleccionadas" : "nota seleccionada";
        }

        return "Eliminar";
    }

    /* Método que actúa como estado de selección múltiple para las notas,
       relacionadas a la eliminación de estas
    */
    public boolean hasSelectedNotes() {
        return this.selectedNotes != null && !this.selectedNotes.isEmpty();
    }

    public void toggleNoteSelection(Notes note) {
        if (this.selectedNotes == null) {
            this.selectedNotes = new ArrayList<>();
        }

        if (this.selectedNotes.contains(note)) {
            this.selectedNotes.add(note);
        } else {
            this.selectedNotes.remove(note);
        }
    }


    /* Método que filtra los registros de notas en base a
       todas las opciones de filtrado seleccionadas por los componentes de p:selectOneMenu
    */
    public void applyFilters() {
        filterByType();
        filterByTime();
    }

    /* Método que aplica a las notas opciones de filtro por tipo,
       dadas las selecciones en el componente de p:selectOneMenu
    */
    public void filterByType() {
        if (typeFilter != null && !timeFilter.isEmpty()) {
            switch (typeFilter) {
                case "all":
                    break;
                case "courses":
                    filteredNotes = filteredNotes.stream()
                            .filter(note -> !note.getDegreeCourses().getCourseName().equals("N/A"))
                            .collect(Collectors.toList());
                    break;
                case "other":
                    filteredNotes = filteredNotes.stream()
                            .filter(note -> note.getDegreeCourses().getCourseName().equals("N/A"))
                            .collect(Collectors.toList());
                    break;
            }
        }
    }

    /* Método que aplica a las notas opciones de filtro por tiempo,
       dadas las selecciones en el componente de p:selectOneMenu
    */
    public void filterByTime() {
        if (timeFilter != null && !timeFilter.isEmpty()) {
            switch (timeFilter) {
                case "lastModified":
                    sortByLastModifiedDate();
                    break;
                case "recentlyOpened":
                    sortByRecentlyOpenedDate();
                    break;
                case "range_ofDate":
                    sortByDateRange();
                    break;
            }
        }
    }

    /* Método que filtra la lista de notas por su última fecha de modificación en orden ascendente*/
    private void sortByLastModifiedDate() {
        filteredNotes.sort(Comparator.comparing(Notes::getLastModifiedDate));
    }

    /* Método que filtra la lista de notas por su última fecha de apertura en orden ascendente*/
    private void sortByRecentlyOpenedDate() {
        filteredNotes.sort(Comparator.comparing(Notes::getLastOpenedDate));
    }

    /* Método que filtra la lista de notas entre un rango de fechas dado */
    private void sortByDateRange() {
        if (startDateRange != null && endDateRange != null) {
            filteredNotes = filteredNotes.stream()
                    .filter(note -> note.getLastModifiedDate().isAfter(startDateRange.atStartOfDay()) &&
                            note.getLastModifiedDate().isBefore(endDateRange.atStartOfDay()))
                    .collect(Collectors.toList());
        }
    }

    public void onTimeOptionChange() {
        isExpanded = "range_ofDate".equals(timeFilter);
        if (isExpanded) {
            PrimeFaces.current().executeScript("expandOverlayPanel()");
        } else {
            PrimeFaces.current().executeScript("restoreOverlayPanel()");
        }
    }

    public void restartFilters() {
        isExpanded = "range_ofDate".equals(timeFilter);
        if (isExpanded){
            PrimeFaces.current().executeScript("restoreOverlayPanel()");
        }

        typeFilter = "all";
        timeFilter = "lastModified";
        startDateRange = null;
        endDateRange = null;
        dateRange = null;
    }

    public void verifyDateRange() {
        startDateRange = dateRange.get(0);
        endDateRange = dateRange.get(1);

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Rango de fechas", "Inicio (" + startDateRange + ") Fin (" + endDateRange + ")"));

        PrimeFaces.current().ajax().update("notesForm:messages");
    }

    public String getNoteTag(Notes note) {
        if (note != null) {
            DegreeCourses degreeCourses = note.getDegreeCourses();
            if (degreeCourses != null) {
                String courseName = degreeCourses.getCourseName();
                if (courseName != null) {
                    if ("N/A".equals(courseName)) {
                        return "Otros";
                    } else {
                        return courseName;
                    }
                }
            }
        }
        return null;
    }

    public String getDateType(Notes note) {
        LocalDateTime lastOpenedDate = note.getLastOpenedDate();
        // Fecha formateada en el formato (dd/mm/yyyy) con mes detallado
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);
        String formattedDate = lastOpenedDate.format(dateFormatter);

        // Hora formateada al formato del reloj de 24hrs
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        String formattedTime = lastOpenedDate.format(timeFormatter);

        if ("recentlyOpened".equals(timeFilter)) {
            return "Abierto el: " + formattedDate + " • " + formattedTime;
        } else {
            return formattedDate + " • " + formattedTime;
        }
    }

    public String createNote() {
        LocalDateTime now = LocalDateTime.now();
        this.selectedNote.setBody(null);
        this.selectedNote.setCreationDate(now);
        this.selectedNote.setLastOpenedDate(now);
        this.selectedNote.setInTrash(false);

        dao.save(this.selectedNote);

        return "/notesWYSIWYG.xhtml?faces-redirect=true";
    }

    public void updateNote() {
        String content = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("documentBody");

        if (!this.selectedNote.getBody().equals(content)) {
            this.selectedNote.setBody(content);
        }
        this.selectedNote.setLastModifiedDate(LocalDateTime.now());

        dao.update(this.selectedNote);
    }

    /* Método que actualiza el estado de una nota en base a la recuperación o eliminación de esta */
    public void toTrash(Notes note, boolean trashFlag) {
        note.setInTrash(trashFlag);
        dao.update(note);
    }

    /* Método que elimina definitivamente una nota seleccionada */

    public void deleteNote(Notes note) {
        dao.remove(note);
    }

    public void deleteSelectedNotes() {
        this.notesList.removeAll(this.selectedNotes);
        this.selectedNotes = null;

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Notas eliminadas"));
        PrimeFaces.current().ajax().update("notesForm:messages", "notesForm:dVnotes");
        PrimeFaces.current().executeScript("PF('dvNotes').clearFilters()");
    }
}

// Método para inicializar un cuadro que enlista la(s) carrera(s) que cursa el estudiante
    /*
    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensaje", "Se seleccionó 'Rango de fecha'"));

    public void CB_coursedPeriods(){
        List<StudentSemestralPeriod> coursedPeriods = dao.get("StudentSemestralPeriod.FindByCIF",
                StudentSemestralPeriod.class, student.getCIF());

    public void CB_assignedCourses(){

    }*/