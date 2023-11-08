/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package iia.conector;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import iia.utilidades.Mensaje;
import org.w3c.dom.Document;

/**
 *
 * @author chris
 */

/**
 * La clase ConectorSalida es una subclase de Conector que se encarga de enviar información de salida
 * a través de archivos en un directorio final. Esta clase se utiliza para guardar mensajes en archivos
 * en un directorio de salida.
 */
public class ConectorSalida extends Conector {

    private final String directorioFinal; // Directorio final donde se guardan los archivos de salida.
    private int contadorComandas; // Contador para los nombres de archivos.

    /**
     * Constructor de la clase ConectorSalida.
     * @param directorioFinal El directorio final donde se guardarán los archivos de salida.
     */
    public ConectorSalida(String directorioFinal) {
        this.directorioFinal = directorioFinal;
        this.contadorComandas = 0;
    }

    /**
     * Envía un mensaje de salida guardándolo en un archivo en el directorio final.
     * @param m El mensaje a enviar como información de salida.
     */
    @Override
    public void enviarInformacionSalida(Mensaje m) {
        synchronized (this) {
            Path outputFile = Paths.get(directorioFinal,"comanda" + ++contadorComandas + ".xml");
            try {
                Files.write(outputFile, m.getCadenaCuerpo().getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
            } catch (IOException ex) {
                // Manejar posibles excepciones de E/S
            }
        }
    }

    /**
     * Este método no se admite en esta implementación y arroja una excepción.
     * @param xml El XML que se intenta enviar como información de entrada.
     * @throws UnsupportedOperationException Si se intenta utilizar este método no admitido.
     */
    @Override
    public void enviarInformacionEntrada(String xml) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    /**
     * Este método no se admite en esta implementación y arroja una excepción.
     * @throws UnsupportedOperationException Si se intenta utilizar este método no admitido.
     */
    @Override
    public void iniciar() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    /**
     * Este método no se admite en esta implementación y arroja una excepción.
     * @throws UnsupportedOperationException Si se intenta utilizar este método no admitido.
     */
    @Override
    public void detener() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Document interaccionBD(Document documento) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}

