/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package iia.tareas;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import iia.utilidades.Slot;

/**
 *
 * @author chris
 */

/**
 * La clase abstracta Tarea representa una unidad de trabajo en un proceso que puede tener entradas y salidas
 * a través de objetos Slot. Las tareas pueden recibir información a través de sus entradas y enviar información
 * a través de sus salidas.
 */
abstract public class Tarea {
    private final List<Slot> entradas; // Lista de entradas de la tarea.
    private final List<Slot> salidas;  // Lista de salidas de la tarea.
    protected Slot slotEntrada;  // Slot de entrada de la tarea.
    protected Slot slotSalida;   // Slot de salida de la tarea.

    /**
     * Constructor de la clase Tarea. Inicializa las listas de entradas y salidas.
     */
    public Tarea() {
        this.entradas = new ArrayList<>();
        this.salidas = new ArrayList<>();
    }

    /**
     * Añade una entrada a la lista de entradas de la tarea.
     * @param entrada El objeto Slot que se añade como entrada.
     */
    public void añadirEntrada(Slot entrada) {
        this.entradas.add(entrada);
    }

    /**
     * Añade una salida a la lista de salidas de la tarea.
     * @param salida El objeto Slot que se añade como salida.
     */
    public void añadirSalida(Slot salida) {
        this.salidas.add(salida);
    }

    /**
     * Verifica si la tarea tiene al menos una entrada.
     * @return true si la tarea tiene al menos una entrada, false en caso contrario.
     */
    public final boolean tieneEntradas() {
        return !entradas.isEmpty();
    }

    /**
     * Verifica si la tarea tiene al menos una salida.
     * @return true si la tarea tiene al menos una salida, false en caso contrario.
     */
    public final boolean tieneSalidas() {
        return !salidas.isEmpty();
    }

    /**
     * Obtiene la entrada en la posición especificada.
     * @param n La posición de la entrada.
     * @return El objeto Slot de la entrada en la posición n, o null si la posición no es válida.
     */
    protected final Slot entrada(int n) {
        if (n >= 0 && n < entradas.size()) {
            return entradas.get(n);
        } else {
            return null;
        }
    }

    /**
     * Obtiene la salida en la posición especificada.
     * @param n La posición de la salida.
     * @return El objeto Slot de la salida en la posición n, o null si la posición no es válida.
     */
    protected final Slot salida(int n) {
        if (n >= 0 && n < salidas.size()) {
            return salidas.get(n);
        } else {
            return null;
        }
    }

    /**
     * Obtiene el número de entradas de la tarea.
     * @return El número de entradas de la tarea.
     */
    protected final int nEntradas() {
        return entradas.size();
    }

    /**
     * Obtiene el número de salidas de la tarea.
     * @return El número de salidas de la tarea.
     */
    protected final int nSalidas() {
        return salidas.size();
    }

    /**
     * Obtiene un iterador para recorrer las entradas de la tarea.
     * @return Un iterador de entradas.
     */
    public Iterator<Slot> entradas() {
        return entradas.iterator();
    }

    /**
     * Método abstracto que debe ser implementado por las subclases. Inicia la tarea.
     */
    public abstract void iniciar();
}
