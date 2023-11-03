/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package iaa.utilidades;

import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author chris
 */

/**
 * La clase Slot representa una cola de mensajes sincronizada que se utiliza para la comunicación entre tareas.
 * Permite el almacenamiento y manipulación de mensajes de manera segura en entornos multi-hilo.
 */
public class Slot {
    private final LinkedList<Mensaje> cola; // Cola de mensajes en el slot.
    
    /**
     * Constructor de la clase Slot. Inicializa una nueva cola de mensajes.
     */
    public Slot() {
        cola = new LinkedList<>();
    }
    
    /**
     * Agrega un mensaje a la cola del slot de manera sincronizada.
     * @param mensaje El mensaje a agregar a la cola.
     */
    public synchronized void pushMensaje(Mensaje mensaje) {
            cola.add(mensaje);
    }
    
    /**
     * Elimina un mensaje de la cola del slot de manera sincronizada.
     * @param mensaje El mensaje a eliminar de la cola.
     */
    public synchronized void borrarMensaje(Mensaje mensaje) {
        cola.remove(mensaje);
    }
    
    /**
     * Recupera y elimina el primer mensaje de la cola del slot de manera sincronizada.
     * @return El primer mensaje en la cola, o null si la cola está vacía.
     */
    public synchronized Mensaje recuperarMensaje() {
        return cola.poll();
    }
    
    /**
     * Obtiene el número de mensajes en la cola del slot de manera sincronizada.
     * @return El tamaño de la cola de mensajes.
     */
    public synchronized int obtenerTamaño() {
            return cola.size();
    }
    
    /**
     * Verifica si la cola del slot está vacía de manera sincronizada.
     * @return true si la cola está vacía, false en caso contrario.
     */
    public synchronized boolean colaVacia() {
        return this.cola.isEmpty();
    }
    
    /**
     * Obtiene un iterador para recorrer los mensajes en la cola del slot de manera sincronizada.
     * @return Un iterador de mensajes en la cola.
     */
    public synchronized Iterator<Mensaje> getIterador() {
        return cola.iterator();
    }
}

