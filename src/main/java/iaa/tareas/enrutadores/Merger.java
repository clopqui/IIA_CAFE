/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package iaa.tareas.enrutadores;

import iaa.tareas.Tarea;

/**
 *
 * @author chris
 */

/**
 * La clase Merger es una subclase de Tarea que se encarga de fusionar mensajes de múltiples entradas en un único mensaje de salida.
 * Esta tarea se utiliza para combinar información de diferentes fuentes en un solo mensaje de salida.
 */
public class Merger extends Tarea {

    /**
     * Inicia la tarea Merger. Fusiona mensajes de múltiples entradas en un único mensaje de salida.
     */
    @Override
    public void iniciar() {
        for (int i = 0; i < nEntradas(); i++) {
            while (!entrada(i).colaVacia()) {
                salida(0).pushMensaje(entrada(i).recuperarMensaje());
            }
        }
    }
}

