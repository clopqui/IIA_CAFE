/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package iia.conector;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import iia.tareas.puertos.PuertoEntrada;
import iia.utilidades.Mensaje;
import org.w3c.dom.Document;

/**
 *
 * @author chris
 */

/**
 * La clase ConectorEntrada es una subclase de Conector que se encarga de observar un directorio
 * y enviar información de entrada a través de un puerto. Esta clase se utiliza para recibir datos
 * desde archivos en un directorio y enviarlos a través del puerto de entrada.
 */
public class ConectorEntrada extends Conector {

    private ScheduledExecutorService executorService; // Servicio de ejecución programada para observar el directorio.
    private final String directorio; // Directorio que se observa para obtener los datos de entrada.

    /**
     * Constructor de la clase ConectorEntrada.
     * @param directorio El directorio que se observará para obtener datos de entrada.
     */
    public ConectorEntrada(String directorio) {
        this.directorio = directorio;
    }
    
    /**
     * Inicia el conector de entrada y comienza a observar el directorio en busca de datos de entrada.
     */
    @Override
    public void iniciar() {
        if (directorio != null && executorService == null) {
            executorService = Executors.newSingleThreadScheduledExecutor();
            executorService.scheduleAtFixedRate(this::observarDirectorio, 0, 1, TimeUnit.SECONDS);
        }
    }
    
    /**
     * Detiene el conector de entrada y deja de observar el directorio.
     */
    @Override
    public void detener() {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
            executorService = null;
        }
    }
    
    /**
     * Observa el directorio especificado en busca de archivos y envía su contenido como información de entrada.
     */
    private void observarDirectorio() {
        try {
            File carpeta = new File(directorio);
            for (File archivo : carpeta.listFiles()) {
                if (archivo.isFile()) {
                    String contenido = new String(Files.readAllBytes(archivo.toPath()), StandardCharsets.UTF_8);
                    enviarInformacionEntrada(contenido);
                    Files.delete(archivo.toPath()); // Por si hace falta borrar las comandas
                }
            }
        } catch (IOException ex) {
            System.out.println(ex);
        } catch (Exception ex) {
            Logger.getLogger(ConectorEntrada.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Envía información de entrada a través del puerto de entrada.
     * @param xml El XML que se enviará como información de entrada.
     * @throws Exception Si se produce un error durante el envío de información.
     */
    @Override
    public void enviarInformacionEntrada(String xml) throws Exception {
        ((PuertoEntrada) puerto).enviarInformacionDocumento(Mensaje.XMLaDocumento(xml));
    }

    /**
     * Este método no se admite en esta implementación y arroja una excepción.
     * @param m El mensaje que se intenta enviar como información de salida.
     * @throws UnsupportedOperationException Si se intenta utilizar este método no admitido.
     */
    @Override
    public void enviarInformacionSalida(Mensaje m) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Document interaccionBD(Document documento) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
