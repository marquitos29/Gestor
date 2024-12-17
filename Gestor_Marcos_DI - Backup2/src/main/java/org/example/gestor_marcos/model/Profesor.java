package org.example.gestor_marcos.model;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Profesor {

    private String nombreProfesor, apellidoProfesor, correoProfesor, passProfesor, asignatura ;

    private int fk_id_perfil;

    public Profesor(String nombre, String apellido, String asignatura, String correo, String pass) {
        this.nombreProfesor = nombre;
        this.apellidoProfesor = apellido;
        this.asignatura = asignatura;
        this.correoProfesor = correo;
        this.passProfesor = pass;
        this.fk_id_perfil = 1;
    }
}
