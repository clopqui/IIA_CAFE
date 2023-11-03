/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package iaa.tareas.enrutadores;

import org.w3c.dom.Document;
import iaa.tareas.Tarea;
import iaa.utilidades.Mensaje;
import iaa.utilidades.Slot;

/**
 *
 * @author chris
 */


/**
 * La clase Replicator es una subclase de Tarea que se encarga de replicar un mensaje de entrada en múltiples mensajes de salida.
 * Esta tarea se utiliza para duplicar un mensaje y enviar copias idénticas a múltiples destinos.
 */
public class Replicator extends Tarea {
    private Slot entrada; // Slot de entrada principal.
    private Mensaje m; // Mensaje de entrada a replicar.

    /**
     * Inicia la tarea Replicator. Replica un mensaje de entrada en múltiples mensajes de salida.
     */
    @Override
    public void iniciar() {
        this.entrada = entrada(0);
        while (!entrada.colaVacia()) {
            m = entrada.recuperarMensaje();
            for (int i = 0; i < nSalidas(); i++) {
                salida(i).pushMensaje(new Mensaje((Document) m));
            }
        }
    }
}
