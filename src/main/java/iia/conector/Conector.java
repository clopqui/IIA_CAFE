/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package iia.conector;

import iia.tareas.puertos.Puerto;
import iia.utilidades.Mensaje;

/**
 *
 * @author chris
 */

/**
 * La clase Conector es una clase abstracta que define un componente que facilita la comunicación entre
 * diferentes partes del sistema a través de un puerto. Los conectores pueden enviar y recibir información
 * a través del puerto y gestionar su estado.
 */
public abstract class Conector {
    /**
     * El puerto a través del cual se enviará y recibirá la información.
     */
    protected Puerto puerto;  // El puerto asociado al conector.

    /**
     * Establece el puerto que utilizará el conector para la comunicación.
     * @param p El puerto que se asignará al conector.
     */
    public void setPuerto(Puerto p) {
        this.puerto = p;
    }

    /**
     * Envía un mensaje de salida a través del conector de manera específica según la implementación.
     * @param m El mensaje a enviar.
     */
    public abstract void enviarInformacionSalida(Mensaje m);

    /**
     * Envía información de entrada a través del conector en formato XML de manera específica según la implementación.
     * @param xml El XML que se envía como entrada.
     * @throws Exception Si se produce un error durante el envío de información.
     */
    public abstract void enviarInformacionEntrada(String xml) throws Exception;

    /**
     * Inicia el conector.
     */
    public abstract void iniciar();

    /**
     * Detiene el conector.
     */
    public abstract void detener();
}
