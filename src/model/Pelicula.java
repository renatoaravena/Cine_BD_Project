package model;

public class Pelicula {

    private Integer id;
    private int duracion, anio;
    private String titulo, director, genero;


    // Constructor de pelicula vacio
    public Pelicula() {}

    //Constructor de pelicula con todos los atributos
    public Pelicula(Integer id, String titulo, String director, int anio, int duracion, String genero) {
        this.id = id;
        this.titulo = titulo;
        this.director = director;
        this.anio = anio;
        this.duracion = duracion;
        this.genero = genero;
    }

    //Constructor de pelicula sin id (para insertar en la base de datos)
    public Pelicula(String titulo, String director, int anio, int duracion, String genero) {
        this(null, titulo, director, anio, duracion, genero);

    }

    //Getters y Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    @Override
    public String toString() {
        return "Pelicula{" +
                "id=" + id +
                ", duracion=" + duracion +
                ", anio=" + anio +
                ", titulo='" + titulo + '\'' +
                ", director='" + director + '\'' +
                ", genero='" + genero + '\'' +
                '}';
    }
}