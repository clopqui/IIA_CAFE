/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package iia.tareas.enrutadores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import iia.tareas.Tarea;
import iia.utilidades.Mensaje;
import iia.utilidades.Slot;

/**
 *
 * @author chris
 */

/**
 * La clase Correlator es una subclase de Tarea que se encarga de correlacionar y procesar mensajes relacionados por un ID de correlación.
 * Esta tarea se utiliza para agrupar y procesar mensajes que tienen el mismo ID de correlación y distribuirlos a las salidas correspondientes.
 */
public class Correlator extends Tarea {

    /**
     * Inicia la tarea Correlator. Correlaciona y procesa mensajes relacionados por un ID de correlación y los distribuye a las salidas correspondientes.
     */
    @Override
    public void iniciar() {
        Map<UUID, List<Mensaje>> gruposMensajes = agruparMensajesPorIdCorrelacion();

        for (Map.Entry<UUID, List<Mensaje>> gMensaje : gruposMensajes.entrySet()) {
            if (gMensaje.getValue().size() == nEntradas()) {
                procesarYdistribuirMensajes(gMensaje.getValue());
            }
        }
    }
    
    /**
     * Agrupa mensajes por su ID de correlación.
     * @return Un mapa que asocia un ID de correlación con una lista de mensajes relacionados.
     */
    private Map<UUID, List<Mensaje>> agruparMensajesPorIdCorrelacion() {
        Map<UUID, List<Mensaje>> gruposMensajes = new HashMap<>();
        
        Iterator<Slot> iterEntradas = entradas();
        while (iterEntradas.hasNext()) {
            Slot slotE = iterEntradas.next();
            
            Iterator<Mensaje> iterEntradas2 = slotE.getIterador();
            while (iterEntradas2.hasNext()) {
                Mensaje m = iterEntradas2.next();
                UUID correlationId = m.getIDcorrelacion();
                
                List<Mensaje> lista = gruposMensajes.get(correlationId);
                if (lista == null) {
                    lista = new ArrayList<>();
                    gruposMensajes.put(correlationId, lista);
                }
                lista.add(m);
            }
        }
        return gruposMensajes;
    }

    /**
     * Procesa y distribuye mensajes a las salidas correspondientes.
     * @param mensajes La lista de mensajes a procesar y distribuir.
     */
    private void procesarYdistribuirMensajes(List<Mensaje> mensajes) {
        for (int i = 0; i < mensajes.size(); i++) {
            Mensaje m = mensajes.get(i);
            entrada(i).borrarMensaje(m);
            salida(i).pushMensaje(m);
        }
    }
}

