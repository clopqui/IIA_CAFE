/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package iia.tareas.modificadores;

import java.util.logging.Level;
import java.util.logging.Logger;
import iia.tareas.Tarea;
import iia.utilidades.Mensaje;
import iia.utilidades.Slot;
import iia.utilidades.Utilidades;

/**
 *
 * @author chris
 */


/**
 * La clase Context_enricher es una subclase de Tarea que se encarga de enriquecer un mensaje de entrada con información adicional
 * proveniente de otro mensaje. Esta tarea se utiliza para combinar dos mensajes y agregar información de contexto al mensaje principal.
 */
public class Context_enricher extends Tarea {
    private Slot entrada; // Slot de entrada principal.
    private Slot entradaAñadida; // Slot de entrada para información adicional.
    private Slot salida; // Slot de salida para el mensaje enriquecido.

    /**
     * Inicia la tarea Context_enricher. Combina dos mensajes de entrada y agrega información de contexto al mensaje principal.
     */
    @Override
    public void iniciar() {
        this.entrada = entrada(0);
        this.entradaAñadida = entrada(1);
        this.salida = salida(0);
        while (!entrada.colaVacia() && !entradaAñadida.colaVacia()) {
            try {
                Mensaje mentrada = entrada.recuperarMensaje();
                Mensaje mentradaAñadida = entradaAñadida.recuperarMensaje();
                mentrada.setCuerpo(Utilidades.juntarXML(mentrada.getCuerpo(), mentradaAñadida.getCuerpo()));
                salida.pushMensaje(mentrada);
            } catch (Exception ex) {
                Logger.getLogger(Context_enricher.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
