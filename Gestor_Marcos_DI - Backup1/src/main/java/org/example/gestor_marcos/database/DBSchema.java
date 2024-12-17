package org.example.gestor_marcos.database;

public interface DBSchema {

    String HOST = "localhost";
    String PORT = "3306";
    String DATABASE_NAME = "marcos_gestor_di_4";


    String TAB_THE = "profesores";
    String COL_ID_THE = "id_profesor";
    String COL_NAME_THE = "nombre_profesor";
    String COL_SNAME_THE = "apellido_profesor";
    String COL_MAIL_THE = "correo_profesor";
    String COL_PASS_THE = "contrasenia_profesor";
    String COL_ID_PRO_FK_THE = "id_perfil_fk_profesor";


    String TAB_STU = "alumnos";
    String COL_ID_STU = "id_alumno";
    String COL_NAME_STU = "nombre_alumno";
    String COL_SNAME_STU = "apellido_alumno";
    String COL_MAIL_STU = "correo_alumno";
    String COL_COURSE_STU = "curso_alumno";
    String COL_PASS_STU = "contrasenia_alumno";
    String COL_ID_PRO_FK_STU = "id_perfil_fk_alumno";


    String TAB_COURSE = "cursos";
    String COL_ID_COURSE = "id_curso";
    String COL_NAME_COURSE = "nombre_curso";
    String COL_FK_TEACHER_USER_COURSE = "id_profesor_fk_curso";
    String COL_FK_STUDENT_USER_COURSE = "id_alumno_fk_curso";


    String TAB_PROFILE = "perfiles";
    String COL_ID_PRO = "id_perfiles";
    String COL_TYPE_PRO = "tipo_perfiles";


    String TAB_SUBJECT = "asignaturas";
    String COL_ID_SUBJECT = "id_asignatura";
    String COL_NAME_SUBJECT = "nombre_asignatura";
    String COL_FK_TEACHER_USER_SUBJECT = "id_profesor_fk_asignatura";
    String COL_FK_COURSE_SUBJECT = "id_curso_fk_asignatura"; // Relaciona asignatura con curso
}
