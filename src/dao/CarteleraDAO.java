//Contendra metodos crud (Create, Read, Update, Delete) para la tabla Cartelera

package dao;

import model.Pelicula;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarteleraDAO {

    //Metodo para agregar pelicula a la cartelera(Create)
    public boolean agregarPelicula(Pelicula pelicula) {

        //Consulta SQL para insertar una nueva pelicula, los ? son placeholders para los valores
        String sql = "INSERT INTO Cartelera (titulo, director, anio, duracion, genero) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            //Asignar valores a los placeholders
            ps.setString(1, pelicula.getTitulo());
            ps.setString(2, pelicula.getDirector());
            ps.setInt(3, pelicula.getAnio());
            ps.setInt(4, pelicula.getDuracion());
            ps.setString(5, pelicula.getGenero());

            //Ejecutar la consulta, executeUpdate devuelve el numero de filas afectadas
            return ps.executeUpdate() == 1;
        }catch (SQLException e){
            System.out.println("Error agregarPelicula(): " + e.getMessage());
            return false;
        }

    }

    //Metodo para eliminar pelicula de la cartelera (Delete), recibe el id de la pelicula a eliminar
    public boolean eliminarPelicula(int id) {

        String sql = "DELETE FROM Cartelera WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setInt(1, id);
                return ps.executeUpdate() == 1;

            } catch (SQLException e) {
                System.out.println("Error eliminarPelicula(): " + e.getMessage());
                return false;
            }


    }

    //Metodo para Listar Peliculas
    public List<Pelicula> listarPeliculas() {

        List<Pelicula> peliculas = new ArrayList<>();

        String sql = "SELECT id, titulo, director, anio, duracion, genero FROM Cartelera ORDER BY id";
        try(Connection conn = DBConnection.getConnection();
        Statement st = conn.createStatement(); // En este caso no necesitamos PreparedStatement porque no hay parametros, no hay valores, es algo más simple
        ResultSet rs = st.executeQuery(sql)){

            while (rs.next()) {
                Pelicula pelicula = new Pelicula(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getString("director"),
                        rs.getInt("anio"),
                        rs.getInt("duracion"),
                        rs.getString("genero")
                );
                peliculas.add(pelicula);
            }

        }catch(SQLException e){
            System.out.println("Error listarPeliculas(): " + e.getMessage());
            return null;
        }

        return peliculas;
    }

    //Metodo para buscar pelicula por titulo
    public List<Pelicula> buscarPorTitulo(String titulo) {
        List<Pelicula> peliculas = new ArrayList<>();

        String sql = "SELECT * FROM Cartelera WHERE titulo LIKE ? ORDER BY id";

        try(Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + titulo + "%"); //El % es un comodin que permite buscar coincidencias

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Pelicula pelicula = new Pelicula(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getString("director"),
                        rs.getInt("anio"),
                        rs.getInt("duracion"),
                        rs.getString("genero")
                );
                peliculas.add(pelicula);

            }

        }catch(SQLException e){
            System.out.println("Error buscarPorTitulo(): " + e.getMessage());
        }

        return peliculas;
    }

    //Metodo para buscar pelicula por id
    public Pelicula buscarPorId(int id) {
        String sql = "SELECT * FROM Cartelera WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Pelicula(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getString("director"),
                        rs.getInt("anio"),
                        rs.getInt("duracion"),
                        rs.getString("genero")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error buscarPorId(): " + e.getMessage());
        }
        return null;
    }

    public boolean actualizarPelicula(Pelicula pelicula) {
        String sql = "UPDATE Cartelera SET titulo = ?, director = ?, anio = ?, duracion = ?, genero = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, pelicula.getTitulo());
            ps.setString(2, pelicula.getDirector());
            ps.setInt(3, pelicula.getAnio());
            ps.setInt(4, pelicula.getDuracion());
            ps.setString(5, pelicula.getGenero());
            ps.setInt(6, pelicula.getId());

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            System.out.println("Error actualizarPelicula(): " + e.getMessage());
            return false;
        }
    }
    
    // Método para buscar películas por rango de años
public List<Pelicula> buscarPorRangoAnios(int anioInicio, int anioFin) {
    List<Pelicula> peliculas = new ArrayList<>();
    
    String sql = "SELECT * FROM Cartelera WHERE anio BETWEEN ? AND ? ORDER BY anio, id";
    
    try(Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
        
        ps.setInt(1, anioInicio);
        ps.setInt(2, anioFin);
        ResultSet rs = ps.executeQuery();
        
        while (rs.next()) {
            Pelicula pelicula = new Pelicula(
                    rs.getInt("id"),
                    rs.getString("titulo"),
                    rs.getString("director"),
                    rs.getInt("anio"),
                    rs.getInt("duracion"),
                    rs.getString("genero")
            );
            peliculas.add(pelicula);
        }
        
    } catch(SQLException e) {
        System.out.println("Error buscarPorRangoAnios(): " + e.getMessage());
    }
    
    return peliculas;
}

// Método para buscar por género y rango de años
public List<Pelicula> buscarPorGeneroYRangoAnios(String genero, int anioInicio, int anioFin) {
    List<Pelicula> peliculas = new ArrayList<>();
    
    String sql = "SELECT * FROM Cartelera WHERE genero LIKE ? AND anio BETWEEN ? AND ? ORDER BY anio, id";
    
    try(Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
        
        ps.setString(1, "%" + genero + "%");
        ps.setInt(2, anioInicio);
        ps.setInt(3, anioFin);
        ResultSet rs = ps.executeQuery();
        
        while (rs.next()) {
            Pelicula pelicula = new Pelicula(
                    rs.getInt("id"),
                    rs.getString("titulo"),
                    rs.getString("director"),
                    rs.getInt("anio"),
                    rs.getInt("duracion"),
                    rs.getString("genero")
            );
            peliculas.add(pelicula);
        }
        
    } catch(SQLException e) {
        System.out.println("Error buscarPorGeneroYRangoAnios(): " + e.getMessage());
    }
    
    return peliculas;
}
}
