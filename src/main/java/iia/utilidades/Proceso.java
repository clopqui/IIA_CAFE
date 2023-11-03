/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package iia.utilidades;

import iia.tareas.puertos.PuertoEntrada;
import iia.tareas.puertos.PuertoSalida;
import iia.tareas.puertos.Puerto;
import iia.tareas.puertos.PuertoSolicitud;
import iia.conector.Conector;
import java.util.ArrayList;
import java.util.List;
import iia.tareas.Tarea;
import iia.tareas.enrutadores.Correlator;
import iia.tareas.enrutadores.Distributor;
import iia.tareas.enrutadores.Merger;
import iia.tareas.enrutadores.Replicator;
import iia.tareas.modificadores.Context_enricher;
import iia.tareas.modificadores.Correlation_ID_Setter;
import iia.tareas.transformadores.Aggregator;
import iia.tareas.transformadores.Splitter;
import iia.tareas.transformadores.Translator;

/**
 *
 * @author chris
 */

/**
 * La clase Proceso representa un conjunto de tareas y conectores.
 * Permite crear tareas, puertos y establecer conexiones entre ellos para definir la lógica del proceso.
 */
public class Proceso {

    // Listas para almacenar tareas, conectores y hilos
    private final List<Tarea> tareas;
    private final List<Conector> conectores;
    private final List<Thread> hilos;

    // Enumeraciones para los tipos de puertos y tareas
    public enum PUERTOS {
        ENTRADAS,  // Puertos de entrada
        SALIDAS,   // Puertos de salida
        SOLICITUD  // Puertos de solicitud
    }

    public enum TAREAS {
        AGGREGATOR,               // Tarea Aggregator
        CONTEXT_ENRICHER,         // Tarea Context_enricher
        CORRELATOR,               // Tarea Correlator
        CORRELATION_ID_SETTER,    // Tarea Correlation_ID_Setter
        DISTRIBUTOR,              // Tarea Distributor
        MERGER,                   // Tarea Merger
        REPLICATOR,               // Tarea Replicator
        SPLITTER,                 // Tarea Splitter
        TRANSLATOR                // Tarea Translator
    }

    // Constructor de la clase Proceso
    public Proceso() {
        tareas = new ArrayList<>();
        hilos = new ArrayList<>();
        conectores = new ArrayList<>();
    }

    /**
     * Añade una tarea a la lista de tareas del proceso.
     * @param t La tarea a añadir.
     */
    public void añadirTarea(Tarea t) {
        tareas.add(t);
    }

    /**
     * Crea un puerto de un tipo específico y lo asocia a un conector.
     * @param conector El conector al que se asocia el puerto.
     * @param tipo El tipo de puerto a crear.
     * @return El puerto creado.
     */
    public Puerto crearPuerto(Conector conector, Proceso.PUERTOS tipo) {
        Puerto p;
        switch (tipo) {
            case ENTRADAS -> {
                p = new PuertoEntrada(conector);
            }
            case SALIDAS -> {
                p = new PuertoSalida(conector);
            }
            case SOLICITUD -> {
                p = new PuertoSolicitud(conector);
            }
            default -> {
                p = null;
            }
        }
        if (p != null) {
            añadirTarea(p);
            conectores.add(conector);
        }
        return p;
    }

    /**
     * Crea una tarea de un tipo específico.
     * @param tipo El tipo de tarea a crear.
     * @return La tarea creada.
     */
    public Tarea crearTarea(TAREAS tipo) {
        return crearTarea(tipo, null);
    }

    /**
     * Crea una tarea de un tipo específico con una configuración adicional.
     * @param tipo El tipo de tarea a crear.
     * @param configuracion La configuración adicional para la tarea.
     * @return La tarea creada.
     */
    public Tarea crearTarea(TAREAS tipo, Object configuracion) {
        Tarea t;
        switch (tipo) {
            case AGGREGATOR -> {
                t = new Aggregator();
            }
            case CONTEXT_ENRICHER -> {
                t = new Context_enricher();
            }
            case CORRELATOR -> {
                t = new Correlator();
            }
            case CORRELATION_ID_SETTER -> {
                t = new Correlation_ID_Setter();
            }
            case DISTRIBUTOR -> {
                t = new Distributor(configuracion);
            }
            case MERGER -> {
                t = new Merger();
            }
            case REPLICATOR -> {
                t = new Replicator();
            }
            case SPLITTER -> {
                t = new Splitter((String) configuracion);
            }
            case TRANSLATOR -> {
                t = new Translator((String) configuracion);
            }
            default -> {
                t = null;
            }
        }
        if (t != null) {
            añadirTarea(t);
        }
        return t;
    }

    /**
     * Conecta un puerto a una tarea, estableciendo un Slot entre ellos.
     * @param p El puerto a conectar.
     * @param t La tarea a conectar.
     */
    public void conectarSlots(Puerto p, Tarea t) {
        Slot slot = new Slot();
        p.setSlot(slot);
        t.añadirEntrada(slot);
    }

    /**
     * Conecta dos tareas, estableciendo un Slot entre ellas.
     * @param t1 La primera tarea a conectar.
     * @param t2 La segunda tarea a conectar.
     */
    public void conectarSlots(Tarea t1, Tarea t2) {
        Slot slot = new Slot();
        t2.añadirEntrada(slot);
        t1.añadirSalida(slot);
    }

    /**
     * Conecta una tarea a un puerto, estableciendo un Slot entre ellos.
     * @param t La tarea a conectar.
     * @param p El puerto a conectar.
     */
    public void conectarSlots(Tarea t, Puerto p) {
        Slot slot = new Slot();
        t.añadirSalida(slot);
        p.setSlot(slot);
    }

    /**
     * Inicializa el proceso, creando hilos para las tareas y comenzando su ejecución.
     * @throws InterruptedException Si se produce una interrupción durante la inicialización.
     */

    public void inicializar() throws InterruptedException {
        for (Tarea t : tareas) {
            Thread hilo = new Thread((Runnable) t);
            hilos.add(hilo);
            hilo.start();
        }

        for (Conector c : conectores) {
            c.iniciar();
        }
    }

    /**
     * Detiene el proceso, interrumpiendo todos los hilos y deteniendo los conectores.
     * @throws InterruptedException Si se produce una interrupción durante la detención.
     */
    public void parar() throws InterruptedException {
        for (Thread t : hilos) {
            t.interrupt();
        }

        for (Conector c : conectores) {
            c.detener();
        }
    }
}

    
