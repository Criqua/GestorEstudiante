package CustomeIdentifiers;

import entities.Agenda;
import entities.Student;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import service.ImplDAO;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AgendaIdGenerator extends ImplDAO implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object obj)
            throws HibernateException {
        try {
            String CIF = ((Student) obj).getCIF();

            List<Agenda> studentEvents = getAll("SELECT a FROM Agenda a WHERE a.student.CIF = :CIF", Agenda.class);
            Integer count = studentEvents.size(); // Se obtiene la cantidad de registros que coinciden con el CIF del estudiante en sesión

            String num = (count + 1) + ""; // Se incrementa la cantidad de registros a + 1, por la inserción del nuevo registro

            // Se crea la estructura del nuevo ID
            String identifier = count + "-" + CIF + "-" + LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyyyy"));

            return identifier;
        } catch (HibernateException e) {
            throw new HibernateException("Error al generar id para Agenda: ", e);
        }
    }



}
