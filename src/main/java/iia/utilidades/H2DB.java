/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package iia.utilidades;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.h2.jdbcx.JdbcConnectionPool;

/**
 *
 * @author chris
 */
public class H2DB {

    ///Es una database embebed por lo tanto solo tiene un usuario disponible para usarla
    private static String url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"; // El sufijo ";DB_CLOSE_DELAY=-1" permite mantener la base de datos en memoria después de cerrar la conexión;
    private String usuario;
    private String contraseña;
    private Connection conexionUsuario;
    private Statement declaracionUsuario;

    public H2DB() {
    }

    public void crearBaseDatos() {
        try (Connection conexion = JdbcConnectionPool.create(url, "admin", "admin").getConnection(); Statement declaracion = conexion.createStatement()) {
            //Crear las tablas en la BD
            String crearTablaSQL = "CREATE TABLE IF NOT EXISTS bebidas_frias (id INT AUTO_INCREMENT, name VARCHAR(255))";
            declaracion.execute(crearTablaSQL);
            crearTablaSQL = "CREATE TABLE IF NOT EXISTS bebidas_calientes (id INT AUTO_INCREMENT, name VARCHAR(255))";
            declaracion.execute(crearTablaSQL);

            //Insertar datos en la BD
            conexion.setAutoCommit(false);

            String[] bebidasFrias = {"coca-cola", "tonica", "guarana", "cerveza"};
            String[] bebidasCalientes = {"cafe", "chocolate", "te", "tila"};

            String insertSQL = "INSERT INTO bebidas_frias (name) VALUES (?)";

            try (PreparedStatement prepararDeclaracion = conexion.prepareStatement(insertSQL)) {

                for (String nombre : bebidasFrias) {
                    prepararDeclaracion.setString(1, nombre);
                    prepararDeclaracion.addBatch();
                }

                // Ejecutar todas las operaciones de inserción en una transacción
                int[] columnasAfectadas = prepararDeclaracion.executeBatch();

                // Completar la transacción
                conexion.commit();

                System.out.println("Se han insertado " + columnasAfectadas.length + " registros en bebidas frias.");
            }

            insertSQL = "INSERT INTO bebidas_calientes (name) VALUES (?)";

            try (PreparedStatement prepararDeclaracion = conexion.prepareStatement(insertSQL)) {

                for (String nombre : bebidasCalientes) {
                    prepararDeclaracion.setString(1, nombre);
                    prepararDeclaracion.addBatch();
                }

                // Ejecutar todas las operaciones de inserción en una transacción
                int[] columnasAfectadas = prepararDeclaracion.executeBatch();

                // Completar la transacción
                conexion.commit();

                System.out.println("Se han insertado " + columnasAfectadas.length + " registros en bebidas calientes.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public H2DB(String usuario, String contraseña) {
        try {
            CrearNuevoUsuario(usuario,contraseña);
            conexionUsuario = DriverManager.getConnection(url, usuario, contraseña);
            declaracionUsuario = conexionUsuario.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void CrearNuevoUsuario(String user, String password) {
            Connection conn = null;
            Statement stm = null;
        try {
            conn = DriverManager.getConnection(url, "admin", "admin");

            stm = conn.createStatement();
            stm.executeUpdate("CREATE USER " + user + " PASSWORD \'" + password + "\' ADMIN" );

            // Otorgar privilegios al nuevo usuario (puedes ajustar los permisos según tus necesidades)
            stm.executeUpdate("GRANT SELECT, INSERT, UPDATE, DELETE ON SCHEMA PUBLIC TO " + user);


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
                try {
            if (stm != null) {
                    stm.close();
            }
            if (conn != null) {
                conn.close();
            }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
        }
    }

    public Statement crearDeclaracion() {
        return this.declaracionUsuario;
    }

}
