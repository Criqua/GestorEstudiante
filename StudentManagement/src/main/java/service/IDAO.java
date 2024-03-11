package service;

import entities.Agenda;
import entities.Test;

import java.util.List;

/**
 * Interfaz de definición de operaciones CRUD en la BD.
 * Se utilizan genéricos para operar con diferentes tipos de entidades
 */
public interface IDAO {
    // Recuperación de todos los registros de una entidad en la BD
    <T> List<T> getAll(String namedQuery, Class<T> clazz);

    // Inserción de un nuevo registro en la BD con la entidad como parámetro
    <T> void insert(T entity);

    // Actualización de un registro existente en la BD con la entidad como parámetro
    <T> T update(T entity);

    // Eliminación de un registro existente en la BD con la entidad como parámetro
    <T> void remove(T entity);

    // Búsqueda y retorno de un registro por su ID con la clase de una entidad como parámetros
    <T, ID> T findById(Class<T> clazz, ID id);

    // Ejecución de una consulta nombrada con parámetros cualquiera y devuelve una lista de resultados.
    <T> List<T> get(String namedQuery, Class<T> clazz, Object... params);

    <T> T save(T entity);
}