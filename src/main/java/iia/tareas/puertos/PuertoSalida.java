/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package iia.tareas.puertos;

import iia.conector.Conector;
import org.w3c.dom.Document;
import iia.utilidades.Mensaje;
import iia.utilidades.Slot;

/**
 *
 * @author chris
 */

/**
 * La clase PuertoSalida es una subclase de Puerto que representa un puerto de salida en un proceso.
 * Este tipo de puerto se utiliza para enviar información en forma de mensajes a través de un conector.
 */
public class PuertoSalida extends Puerto {

    /**
     * Constructor de la clase PuertoSalida. Inicializa el puerto de salida con el conector especificado.
     * @param conector El conector al que está asociado el puerto de salida.
     */
    public PuertoSalida(Conector conector) {
        super(conector);
    }

    /**
     * Establece el slot de entrada asociado al puerto de salida para la comunicación.
     * @param entrada El slot de entrada que se establecerá para la comunicación.
     */
    @Override
    public void setSlotEntrada(Slot entrada) {
        this.slotEntrada = entrada;
    }

    /**
     * Envía información en forma de mensaje a través del puerto de salida.
     * @param m El mensaje que se enviará a través del puerto de salida.
     */
    @Override
    protected void enviarInformacionMensaje(Mensaje m) {
        conector.enviarInformacionSalida(m);
    }

    /**
     * Envía información en forma de documento XML a través del puerto de salida.
     * Esta operación no está soportada en esta clase.
     * @param d El documento XML que se intenta enviar a través del puerto de salida.
     */
    @Override
    protected void enviarInformacionDocumento(Document d) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Inicia el puerto de salida. Envía mensajes a través del conector de forma concurrente utilizando hilos.
     */
    @Override
    public void iniciar() {
        while (!slotEntrada.colaVacia()) {
            final Mensaje m = slotEntrada.recuperarMensaje();
            var hilo = new Thread(() -> {
                enviarInformacionMensaje(m);
            });
            hilo.start();
        }
    }

    @Override
    public void setSlotSalida(Slot s) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
