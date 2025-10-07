package controller;
import model.Pelicula;
import dao.CarteleraDAO;

import java.util.ArrayList;
import java.util.List;

import java.time.LocalDate;


public class CarteleraController {

    // Instancia del DAO para interactuar con la base de datos
    private final CarteleraDAO dao = new CarteleraDAO();

    // Metodo para agregar una película a la cartelera
    public boolean agregarPelicula(Pelicula pelicula) {
        
       
        return dao.agregarPelicula(pelicula); 
        
    }

    // Metodo para eliminar una película de la cartelera por su ID
    public boolean eliminarPelicula(Integer id) {
        // Validar que la película exista
        if (id == null) return false;
        
        return dao.eliminarPelicula(id);
    }

    //Metodo para modificar una pelicula
    public boolean modificarPelicula(Pelicula pelicula) {
        // Validar que la película exista
        //Pelicula peliculaExistente = dao.buscarPorId(pelicula.getId());
        if (dao.buscarPorId(pelicula.getId()) == null) return false;

        // Validar datos
        String validado = validarDatos(pelicula);
        if (validado != null) return false; 

        return dao.actualizarPelicula(pelicula);
        
    }

    //Metodos para obtener datos
    public Pelicula buscarPeliculaPorId(int id) {
        if (id <= 0) {
            System.out.println("Error: ID debe ser un número positivo");
            return null;
        }

        return dao.buscarPorId(id);
    }

    public List<Pelicula> listarTodasLasPeliculas() {
        return dao.listarPeliculas();
    }

    public List<Pelicula> buscarPeliculasPorTitulo(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            System.out.println("Error: El título no puede estar vacío");
            return new ArrayList<>(); // Devolver lista vacía en lugar de null
        }

        return dao.buscarPorTitulo(titulo.trim());

    }

    private String validarDatos(Pelicula pelicula) {
        if (pelicula.getTitulo() == null || pelicula.getTitulo().trim().isEmpty()) {
            return "El título no puede estar vacío.";
        }
        if (pelicula.getDirector() == null || pelicula.getDirector().trim().isEmpty()) {
            return "El director no puede estar vacío.";
        }
        if (pelicula.getAnio() < 1888 || pelicula.getAnio() > 2100) {
            return "El año debe estar entre 1888 y 2100.";
        }
        if (pelicula.getDuracion() <= 0 || pelicula.getDuracion() > 500) {
            return "La duración debe ser entre 1 y 500 minutos.";
        }
        if (pelicula.getGenero() == null || pelicula.getGenero().trim().isEmpty()) {
            return "El género no puede estar vacío.";
        }
        return null; // Datos válidos
    }
    
    // Método para buscar películas por rango de años
    public List<Pelicula> buscarPeliculasPorRangoAnios(int anioInicio, int anioFin) {
        if (anioInicio < 1894  || anioFin > LocalDate.now().getYear() || anioInicio > anioFin) {
            System.out.println("Error: El rango de años debe ser válido (1894 - Año Actual)");
            return new ArrayList<>();
        }
    
        return dao.buscarPorRangoAnios(anioInicio, anioFin);
    }

    // Método para buscar por género y rango de años
    public List<Pelicula> buscarPeliculasPorGeneroYRangoAnios(String genero, int anioInicio, int anioFin) {
        if (genero == null || genero.trim().isEmpty()) {
            System.out.println("Error: El género no puede estar vacío");
            return new ArrayList<>();
        }
    
        if (anioInicio < 1888 || anioFin > 2100 || anioInicio > anioFin) {
            System.out.println("Error: El rango de años debe ser válido (1888-2100)");
            return new ArrayList<>();
        }
    
        return dao.buscarPorGeneroYRangoAnios(genero.trim(), anioInicio, anioFin);
    }
}
