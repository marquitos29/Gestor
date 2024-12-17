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

    public static void actualizarNota(String asignatura, int nuevaNota) {
        notasAsignaturas.put(asignatura, nuevaNota);
    }
}
