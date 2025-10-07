package dao;// Importaciones necesarias para conexión a base de datos

import java.sql.Connection;    // Representa una conexión a la base de datos
import java.sql.DriverManager; // Clase que maneja los drivers de base de datos
import java.sql.SQLException;  // Excepción que puede ocurrir en operaciones SQL

/**
 * Clase DBConnection - Maneja la conexión a la base de datos
 * Esta clase implementa el patrón Singleton para centralizar las conexiones a la BD
 * Contiene la configuración de conexión y proporciona un metodo estático para obtener conexiones
 */


public class DBConnection {

    // Constantes de configuración para la conexión a la base de datos
    // static final = constantes que pertenecen a la clase, no a instancias específicas

    /**
     * URL de conexión a la base de datos MySQL
     * jdbc:mysql:// = protocolo para conectar a MySQL
     * localhost:3306 = servidor local en puerto 3306 (puerto por defecto de MySQL)
     * Agenda_DB = nombre de la base de datos
     */

    private static final String URL = "jdbc:mysql://localhost:3306/Cine_DB";

    /**
     * Usuario de la base de datos
     * Cambiar según la configuración de tu entorno
     */
    private static final String USER = "root";

    /**
     * Contraseña de la base de datos
     * Cambiar según la configuración de tu entorno
     * NOTA: En aplicaciones reales, las contraseñas no se hardcodean así
     */
    private static final String PASS = "12345678";

    /**
     * Metodo estático para obtener una conexión a la base de datos
     * Al ser estático, se puede llamar sin crear una instancia de la clase
     *
     * @return Connection - Objeto que representa la conexión a la base de datos
     * @throws SQLException - Se lanza si hay problemas al conectar (BD no disponible, credenciales incorrectas, etc.)
     */
    public static Connection getConnection() throws SQLException {
        // DriverManager.getConnection() establece la conexión usando la URL, usuario y contraseña
        return DriverManager.getConnection(URL, USER, PASS);

    }
}
