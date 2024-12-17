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
public class Alumno {

    private String nombreAlumno, apellidoAlumno, correoAlumno, passAlumno, curso ;

    private int fk_id_perfil;

    public Alumno(String nombre, String apellido, String curso, String correo, String pass) {
        this.nombreAlumno = nombre;
        this.apellidoAlumno = apellido;
        this.curso = curso;
        this.correoAlumno = correo;
        this.passAlumno = pass;
        this.fk_id_perfil = 1;
    }

    @Override
    public String toString() {
        return this.nombreAlumno + this.apellidoAlumno;
    }
}
