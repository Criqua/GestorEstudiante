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

    public List<DegreeCourses> getStudentsSubjects() {
        List<DegreeCourses> subjects = new ArrayList<>();

        // Verificar si la lista de semesterScheduleList está inicializada
        if (semesterScheduleList != null) {
            // Iterar sobre la lista de SemesterSchedule
            for (SemesterSchedule schedule : semesterScheduleList) {
                // Obtener el CourseDetails asociado al SemesterSchedule
                CourseDetails courseDetails = schedule.getCourseDetails();
                if (courseDetails != null) {
                    // Obtener el DegreeCourses asociado al CourseDetails
                    DegreeCourses degreeCourses = courseDetails.getDegreeCourses();
                    if (degreeCourses != null) {
                        // Verificar si ya se ha agregado este DegreeCourses a la lista
                        if (!subjects.contains(degreeCourses)) {
                            // Agregar el DegreeCourses a la lista de asignaturas si no está presente
                            subjects.add(degreeCourses);
                        }
                    }
                }
            }
        }

        return subjects;
    }

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
            // Extraer el periodo que sigue al prefijo "PER"
            int startIndex = "PER".length();
            int endIndex = id.indexOf("-", startIndex); // Buscar el guion que separa el periodo del semestre
            if (endIndex != -1) {
                int semestreIndex = id.indexOf("-", endIndex + 1); // Buscar el guion que separa el periodo del semestre
                if (semestreIndex != -1) {
                    return id.substring(startIndex, semestreIndex);
                }
            }
        }
        return null;
    }
}
