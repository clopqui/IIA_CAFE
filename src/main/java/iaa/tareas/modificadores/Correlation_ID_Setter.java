/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package iaa.tareas.modificadores;

import java.util.UUID;
import iaa.tareas.Tarea;
import iaa.utilidades.Mensaje;

/**
 *
 * @author chris
 */

/**
 * La clase Correlation_ID_Setter es una subclase de Tarea que se encarga de establecer un ID de correlación único en los mensajes de entrada.
 * Esta tarea se utiliza para asignar un identificador de correlación a cada mensaje, que se utiliza para rastrear y relacionar mensajes en un proceso.
 */
public class Correlation_ID_Setter extends Tarea {

    /**
     * Inicia la tarea Correlation_ID_Setter. Asigna un ID de correlación único a cada mensaje de entrada y lo envía como salida.
     */
    @Override
    public void iniciar() {
        while (!entrada(0).colaVacia()) {
            Mensaje m = entrada(0).recuperarMensaje();
            m.setIDcorrelacion(UUID.randomUUID()); // Genera un ID de correlación único
            salida(0).pushMensaje(m); // Envía el mensaje con el ID de correlación asignado
        }
    }
}

