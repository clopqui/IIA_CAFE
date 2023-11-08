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
 * La clase PuertoEntrada es una subclase de Puerto que representa un puerto de entrada en un proceso.
 * Este tipo de puerto se utiliza para recibir información en forma de documentos XML y enviarla a través de un slot de salida.
 */
public class PuertoEntrada extends Puerto {

    /**
     * Constructor de la clase PuertoEntrada. Inicializa el puerto de entrada con el conector especificado.
     * @param conector El conector al que está asociado el puerto de entrada.
     */
    public PuertoEntrada(Conector conector) {
        super(conector);
    }

    /**
     * Establece el slot de salida asociado al puerto de entrada para la comunicación.
     * @param salida El slot de salida que se establecerá para la comunicación.
     */
    @Override
    public void setSlotSalida(Slot salida) {
        this.slotSalida = salida;
    }

    /**
     * Envía información en forma de documento XML a través del puerto de entrada.
     * @param d El documento XML que se enviará a través del puerto de entrada.
     */
    @Override
    public void enviarInformacionDocumento(Document d) {
        slotSalida.pushMensaje(new Mensaje(d));
    }

    /**
     * Envía información en forma de mensaje a través del puerto de entrada.
     * @param m El mensaje que se enviará a través del puerto de entrada.
     */
    @Override
    protected void enviarInformacionMensaje(Mensaje m) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Inicia el puerto de entrada. Esta operación no está soportada en esta clase y debe ser implementada en subclases específicas.
     */
    @Override
    public void iniciar() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setSlotEntrada(Slot s) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}


