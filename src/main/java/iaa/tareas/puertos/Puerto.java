/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package iaa.tareas.puertos;

import iaa.conector.Conector;
import org.w3c.dom.Document;
import iaa.tareas.Tarea;
import iaa.utilidades.Mensaje;
import iaa.utilidades.Slot;

/**
 *
 * @author chris
 */

/**
 * La clase abstracta Puerto es una subclase de Tarea que representa un puerto de comunicación en un proceso.
 * Cada puerto está asociado a un conector y se utiliza para enviar información en forma de mensajes o documentos.
 */
public abstract class Puerto extends Tarea {
    protected final Conector conector; // El conector al que está asociado el puerto.

    /**
     * Constructor de la clase Puerto. Inicializa el puerto con el conector especificado y establece la referencia inversa
     * del conector al puerto.
     * @param conector El conector al que está asociado el puerto.
     */
    public Puerto(Conector conector) {
        this.conector = conector;
        conector.setPuerto(this);
    }

    /**
     * Establece el slot asociado al puerto para la comunicación.
     * @param s El slot que se establecerá para la comunicación.
     */
    public abstract void setSlot(Slot s);

    /**
     * Envía información en forma de mensaje a través del puerto.
     * @param m El mensaje que se enviará a través del puerto.
     */
    protected abstract void enviarInformacionMensaje(Mensaje m);

    /**
     * Envía información en forma de documento XML a través del puerto.
     * @param d El documento XML que se enviará a través del puerto.
     */
    protected abstract void enviarInformacionDocumento(Document d);

    /**
     * Inicia el puerto y establece las operaciones específicas relacionadas con la comunicación.
     */
    @Override
    public abstract void iniciar();
}

