package beans;

import entities.CourseDetails;
import entities.DegreeCourses;
import entities.SemesterSchedule;
import entities.Student;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

import java.util.*;

@Named
@ApplicationScoped
public class StudentScheduleService {
    protected List<SemesterSchedule> semesterScheduleList;

    @PostConstruct
    public void init(){
        Student student = (Student) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");

        semesterScheduleList = student.getSemesterScheduleList();
    }

    // Método que retorna todas las materias inscritas actualmente y previamente por el estudiante
    public List<DegreeCourses> getStudentsSubjects() {
        List<DegreeCourses> subjects = new ArrayList<>();

        if (semesterScheduleList != null) {
            for (SemesterSchedule schedule : semesterScheduleList) {
                CourseDetails courseDetails = schedule.getCourseDetails();
                if (courseDetails != null) {
                    // Se obtiene el curso asociado a los detalles de este
                    DegreeCourses degreeCourses = courseDetails.getDegreeCourses();
                    if (degreeCourses != null) {
                        // Se verifica si ya se ha agregado el curso a la lista
                        if (!subjects.contains(degreeCourses)) {
                            // Se agrega el curso a la lista de asignaturas si no está presente
                            subjects.add(degreeCourses);
                        }
                    }
                }
            }
        }

        return subjects;
    }

    // Método que asocia cada período único con una lista de cursos correspondientes, basándose en la lista de horarios de cada semestre
    public Map<String, List<DegreeCourses>> associatePeriodsWCourses(List<String> uniquePeriods){
        Map<String, List<DegreeCourses>> periodCoursesMap = new HashMap<>();

        // Se itera sobre cada período único
        for (String uniquePeriod : uniquePeriods){
            List<DegreeCourses> coursesForPeriod = new ArrayList<>();

            // Se itera sobre los horarios del estudiante
            for (SemesterSchedule sc : semesterScheduleList){
                String periodId = sc.getId();

                if (periodId != null && periodId.contains(uniquePeriod)){
                    CourseDetails courseDetails = sc.getCourseDetails();

                    if (courseDetails != null){
                        DegreeCourses degreeCourses = courseDetails.getDegreeCourses();

                        if (degreeCourses != null){
                            coursesForPeriod.add(degreeCourses);
                        }
                    }
                }
            }

            // Se asocia el período único con los cursos coincidentes
            periodCoursesMap.put(uniquePeriod, coursesForPeriod);
        }

        return  periodCoursesMap;
    }

    // Método que retorna una lista de períodos únicos cursados por el estudiante
    public List<String> getUniquePeriods(){
        Set<String> uniquePeriods = new HashSet<>();

        for (SemesterSchedule sc : semesterScheduleList){
            String period = extractPeriod(sc.getId());
            if (period != null){
                uniquePeriods.add(period);
            }
        }
        return new ArrayList<>(uniquePeriods);
    }

    // Método para extraer el periodo de la cadena de ID del SemesterSchedule
    private String extractPeriod(String id) {
        if (id != null && id.startsWith("PER")) {
            // El formato de la cadena de ID es "PER"YYYY"-"N° SEMESTRE"-XXXX"
            // Se extrae el periodo que sigue al prefijo "PER"
            int startIndex = "PER".length();
            int endIndex = id.indexOf("-", startIndex);
            if (endIndex != -1) {
                int semestreIndex = id.indexOf("-", endIndex + 1); // Se busca el guion que separa el periodo del semestre
                if (semestreIndex != -1) {
                    return id.substring(startIndex, semestreIndex); // Se retorna la subcadena resultante ("YYYY"-"N° SEMESTRE")
                }
            }
        }
        return null;
    }
}
