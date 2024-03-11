package beans;

import entities.DegreeCourses;

import jakarta.enterprise.inject.spi.CDI;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.ConverterException;
import jakarta.faces.convert.FacesConverter;

import jakarta.inject.Named;

@Named
@FacesConverter(value = "degreeCoursesConverter", managed = true)
public class DegreeCoursesConverter implements Converter<Object> {

    private final StudentScheduleService studentScheduleService;

    // Solución alternativa para utilizar caracteristicas de CDI dentro de instancias que no las permite
    public DegreeCoursesConverter() {
        this.studentScheduleService = CDI.current().select(StudentScheduleService.class).get();
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }

        try {
            return studentScheduleService.getSubjectsAsMap().get(value);
        } catch (NumberFormatException e) {
            throw new ConverterException(new FacesMessage(value + " no es un ID de curso válido"));
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return "";
        } else if (value instanceof DegreeCourses) {
            return String.valueOf(((DegreeCourses) value).getId());
        }

        return String.valueOf(value);
    }
}