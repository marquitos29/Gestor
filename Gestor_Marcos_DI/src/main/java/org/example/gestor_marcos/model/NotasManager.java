package org.example.gestor_marcos.model;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class NotasManager {
    @Getter
    private static Map<String, Integer> notasAsignaturas = new HashMap<>();

    public static void inicializarNotasSiNoExisten(String asignatura) {
        if (!notasAsignaturas.containsKey(asignatura)) {
            notasAsignaturas.put(asignatura, (int) (Math.random() * 10) + 1);
        }
    }

    public static void actualizarNota(String asignatura, String alumno, int nuevaNota) {
        notasAsignaturas.put(asignatura, nuevaNota);
    }

    public static String generarClave(String asignatura, String alumno) {
        return asignatura + "_" + alumno; // Genera una clave única
    }

    public static void inicializarNotaSiNoExisteConClave(String clave) {
        if (!notasAsignaturas.containsKey(clave)) {
            notasAsignaturas.put(clave, (int) (Math.random() * 10) + 1);
        }
    }

    public static int obtenerNota(String asignatura, String alumno) {
        String clave = generarClave(asignatura, alumno);
        inicializarNotaSiNoExisteConClave(clave);
        return notasAsignaturas.get(clave);
    }



}
