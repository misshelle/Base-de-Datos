package models;

public class Aviso {
    private int id;
    private String titulo;
    private String contenido;
    private String fecha;
    private int idProfesor;

    public Aviso(int id, String titulo, String contenido, String fecha, int idProfesor) {
        this.id = id;
        this.titulo = titulo;
        this.contenido = contenido;
        this.fecha = fecha;
        this.idProfesor = idProfesor;
    }

    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getContenido() { return contenido; }
    public String getFecha() { return fecha; }
    public int getIdProfesor() { return idProfesor; }

    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setContenido(String contenido) { this.contenido = contenido; }
    public void setIdProfesor(int idProfesor) { this.idProfesor = idProfesor; }
}
