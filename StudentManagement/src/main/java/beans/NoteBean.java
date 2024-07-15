package beans;

import entities.DegreeCourses;
import entities.Notes;
import entities.Student;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateless;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.model.SelectItem;
import jakarta.faces.model.SelectItemGroup;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import jakarta.inject.Qualifier;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.PrimeFaces;

import service.IDAO;

import java.io.IOException;
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
    private transient IDAO dao;

    @Inject
    private StudentScheduleService studentScheduleService;

    private Student student;

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
    private String dialogHeader;
    private String btnLabel;

    private DegreeCourses selectedSubject;

    private boolean isExpanded = false;
    private boolean selected;

    private List<LocalDate> dateRange;
    private LocalDate startDateRange;
    private LocalDate endDateRange;

    private List<SelectItem> allPeriodCourses;

    @PostConstruct
    public void init() {
        this.student = (Student) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");

        if (student != null) {
            this.notesList = new ArrayList<>();
            this.notesList = dao.get("Notes.FindByStudentAndStatus", Notes.class, "cif", student.getCIF(), "status", false);
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

        for (String period : uniquePeriods) {
            List<SelectItem> subjectItems = new ArrayList<>();
            Set<String> courseIds = new HashSet<>();

            String[] parts = period.split("-");
            int periodYear = Integer.parseInt(parts[0]);
            int periodSemester = Integer.parseInt(parts[1]);

            List<DegreeCourses> courses = periodsWithCourses.get(period);

            if (courses != null) {
                for (DegreeCourses course : courses){
                    String courseId = course.getId();

                    if (!courseIds.contains(courseId)) {
                        subjectItems.add(new SelectItem(course.getId(), course.getCourseName()));
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

        // Se agrega un item alterno a los cursos presentados como primer valor de la lista
        allPeriodCourses.add(0, new SelectItem("N/A", "Otros"));
        // Se agregan los items del periodo actual al grupo del periodo actual, moviendo el grupo al inicio de la lista
        currentPeriodGroup.setSelectItems(currentPeriodItems.toArray(new SelectItem[0]));
        allPeriodCourses.add(1, currentPeriodGroup);
    }

    /* Método para inicializar una nueva instancia de notas al momento de crear una*/
    public void openNew() {
        this.selectedNote = new Notes();
    }

    public void setDialogLabel() {
        this.dialogHeader = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("headerLabel");
        this.btnLabel = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("btnLabel");
    }

    /**
     * Método que retorna un mensaje estándar o indicante de la cantidad de notas
     * seleccionadas a mostrar en un botón de eliminación de notas
     **/
    public String getDeleteBtnMessage() {
        if (hasSelectedNotes()){
            int size = this.selectedNotes.size();
            return size > 1 ? size + " notas seleccionadas" : "1 nota seleccionada";
        }

        return "Eliminar";
    }

    /* Método que actúa como estado de selección múltiple para las notas,
       relacionadas a la eliminación de estas
    */
    public boolean hasSelectedNotes() {
        return this.selectedNotes != null && !this.selectedNotes.isEmpty();
    }

    /* Método que gestiona el cambio de estado de selección (múltiple) de una nota */
    public void toggleNoteSelection(Notes note) {
        if (this.selectedNotes == null) {
            this.selectedNotes = new ArrayList<>();
        }

        if (this.selectedNotes.contains(note)) {
            this.selectedNotes.remove(note); // Si la nota ya está seleccionada, la eliminamos de la lista
        } else {
            this.selectedNotes.add(note); // Si la nota no está seleccionada, la agregamos a la lista
        }
    }

    /* Método que filtra los registros de notas en base a
       todas las opciones de filtrado seleccionadas por los componentes de p:selectOneMenu
    */
    public void applyFilters() {
        this.filteredNotes = new ArrayList<>(this.notesList);
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
                            .filter(note -> !note.getDegreeCourses().getCourseName().equals("No aplica"))
                            .collect(Collectors.toList());
                    break;
                case "other":
                    filteredNotes = filteredNotes.stream()
                            .filter(note -> note.getDegreeCourses().getCourseName().equals("No aplica"))
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
        this.verifyDateRange();
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
                    if ("No aplica".equals(courseName)) {
                        return "Otros";
                    } else {
                        return courseName;
                    }
                }
            }
        }
        return null;
    }

    /* Método que formatea la fecha y hora en un formato específico de una nota,
       retornando una cadena que lo representa */
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

    /**
     * Método que verifica si hay duplicados en el título de una nota,
     * generando uno nuevo si es necesario
    */
    public String verifyDuplicates(Notes note) {
        String newTitle = note.getTitle();
        int counter = 1; // Se inicializa un contador para agregar un sufijo único en caso de duplicados

        if (titleExist(newTitle, note)) {
            // Mientras el título con el sufijo actual exista, se incrementa el contador hasta generar el título único
            while (titleExist(newTitle + " (" + counter + ")", note)) {
                counter++;
            }
            newTitle += " (" + counter + ") ";
        }
        return newTitle;
    }

    /* Método que verifica si un título y categoría de nota
       coincide en un listado de notas
    */
    public boolean titleExist(String title, Notes note) {
        for (Notes existingNote : this.notesList) {
            if (existingNote.getTitle().equals(title) &&
                    existingNote.getDegreeCourses().equals(note.getDegreeCourses())) {
                return true; // Se devuelve true al haber encontrado una coincidencia de título y curso/categoría
            }
        }
        return false;
    }

    /* Método que crea una nueva nota, la guarda en la base de datos
       y redirige a un procesador de texto para su modificación o bien, actualiza sus atributos
       en caso de editar una nota existente */
    public String saveNote() {
        this.selectedSubject = this.selectedNote.getDegreeCourses();
        if (selectedSubject != null && this.selectedNote.getId() == null) {
            LocalDateTime now = LocalDateTime.now();
            String title = verifyDuplicates(this.selectedNote);

            // Establecer los valores en selectedNote
            this.selectedNote.setStudent(student);
            this.selectedNote.setTitle(title);
            this.selectedNote.setBody(null);
            this.selectedNote.setCreationDate(now);
            this.selectedNote.setLastOpenedDate(now);
            this.selectedNote.setLastModifiedDate(now);
            this.selectedNote.setInTrash(false);
            this.selectedNote.setDegreeCourses(selectedSubject); // Asignar el curso seleccionado

            // Guardar la nota en la base de datos
            dao.save(this.selectedNote);

            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("selectedNote", this.selectedNote);
            return "/notesWYSIWYG.xhtml?faces-redirect=true&includeViewParams=true";
        } else {
            updateNote(this.selectedNote);
            // Manejar el caso donde no se seleccionó ningún curso
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Edición", "Nota editada correctamente."));
            PrimeFaces.current().executeScript("PF('newNoteDialog').hide()");
            PrimeFaces.current().ajax().update("notesForm:messages", "notesForm:dVnotes");
            return null;
        }
    }

    /* Método que abre en el procesador de texto una nota seleccionada, mostrando su contenido */
    public String openNotesDoc() {
        Long selectedNoteId = Long.valueOf(FacesContext.getCurrentInstance().getExternalContext()
                .getRequestParameterMap().get("selectedNoteDoc"));

        Optional<Notes> foundNote = this.notesList.stream()
                .filter(note -> note.getId().equals(selectedNoteId)).findFirst();

        if (foundNote.isPresent()) {
            this.selectedNote = foundNote.get();

            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("selectedNote", this.selectedNote);
            return "/notesWYSIWYG.xhtml?faces-redirect=true&includeViewParams=true";
        }

        System.out.println("Didn't work :(");
        return null;
    }

    /* Método que actualiza los atributos de una nota en base al contenido proporcionado */
    public void updateNote(Notes note) {
        String content = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("documentBody");

        if (!note.getBody().equals(content)) {
            note.setBody(content);
            note.setLastModifiedDate(LocalDateTime.now());
        }

        dao.update(note);

        //FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("selectedNote");
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

        /*
        // Impresión de lista de cursos (por cuestiones de prueba)
        for (SelectItem item : allPeriodCourses) {
            if (item instanceof SelectItemGroup) {
                System.out.println(((SelectItemGroup) item).getLabel());
                for (SelectItem subItem : ((SelectItemGroup) item).getSelectItems()) {
                    System.out.println("   " + subItem.getLabel() + ": " + subItem.getValue());
                }
            } else {
                System.out.println(item.getLabel() + ": " + item.getValue());
            }
        }
        */
