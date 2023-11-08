/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package iia.utilidades;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import org.h2.jdbcx.JdbcConnectionPool;

/**
 *
 * @author chris
 */
public class H2DB {
    
    private static String url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"; // El sufijo ";DB_CLOSE_DELAY=-1" permite mantener la base de datos en memoria después de cerrar la conexión;
    private String usuario;
    private String contraseña;
    private Connection conexionUsuario;
    private Statement declaracionUsuario;
    
    public H2DB(){
        
    }
    
    public void crearBaseDatos(){
        try (Connection conexion = JdbcConnectionPool.create(url, "admin", "admin").getConnection();
            Statement declaracion = conexion.createStatement()) {
                //Crear las tablas en la BD
                String crearTablaSQL = "CREATE TABLE IF NOT EXISTS bebidas_frias (id INT AUTO_INCREMENT, name VARCHAR(255))";
                declaracion.execute(crearTablaSQL);
                crearTablaSQL = "CREATE TABLE IF NOT EXISTS bebidas_calientes (id INT AUTO_INCREMENT, name VARCHAR(255))";
                declaracion.execute(crearTablaSQL);
                
                //Insertar datos en la BD
                conexion.setAutoCommit(false);
                
                String[] bebidasFrias = {"coca-cola","tonica","guarana","cerveza"};
                String[] bebidasCalientes = {"cafe","chocolate","te","tila"};
                
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
    
    public H2DB(String usuario, String contraseña){
        try {
            conexionUsuario = JdbcConnectionPool.create(url, usuario, contraseña).getConnection();
            declaracionUsuario = conexionUsuario.createStatement();
        } catch (SQLException e) {
        }
    }
    
    public Statement crearDeclaracion(){
        return this.declaracionUsuario;
    }
    
    
    
}
