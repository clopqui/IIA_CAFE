/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package iaa.cafe;

import iaa.conector.*;
import iaa.tareas.*;
import iaa.tareas.puertos.Puerto;
import iaa.utilidades.Proceso;
import static iaa.utilidades.Proceso.TAREAS.*;

/**
 *
 * @author chris
 */
public class Cafe {

    /**
     * @param args the command line arguments
     * @throws java.lang.InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        // TODO code application logic here
        Proceso p = new Proceso();
        
        Conector lector = new ConectorEntrada("Ruta del directorio");
        Conector escritor = new ConectorSalida("Ruta del directorio");
        Conector frio = new ConectorSolicitud();
        Conector calor = new ConectorSolicitud();
        
        Puerto entrada = p.crearPuerto(lector, Proceso.PUERTOS.ENTRADAS);
        Puerto salida = p.crearPuerto(escritor, Proceso.PUERTOS.SALIDAS);
        Puerto barbanFrio = p.crearPuerto(frio, Proceso.PUERTOS.SOLICITUD);
        Puerto barbanCalor = p.crearPuerto(calor, Proceso.PUERTOS.SOLICITUD);
        
        Tarea splitter = p.crearTarea(SPLITTER, "/cafe_order/drinks/drink");
        Tarea correlationID = p.crearTarea(CORRELATION_ID_SETTER);
        Tarea distributor = p.crearTarea(DISTRIBUTOR, new String[] {"/drink/type", "cold"});
        
        Tarea replicatorFrio = p.crearTarea(REPLICATOR);
        Tarea translatorFrio = p.crearTarea(TRANSLATOR, "XSL");
        Tarea correlatorFrio = p.crearTarea(CORRELATOR);
        Tarea contextEnricherFrio = p.crearTarea(CONTEXT_ENRICHER);
        
        Tarea replicatorCalor = p.crearTarea(REPLICATOR);
        Tarea translatorCalor = p.crearTarea(TRANSLATOR, "XSL");
        Tarea correlatorCalor = p.crearTarea(CORRELATOR);
        Tarea contextEnricherCalor = p.crearTarea(CONTEXT_ENRICHER);
        
        Tarea merger = p.crearTarea(MERGER);
        Tarea aggregator = p.crearTarea(AGGREGATOR);
        
        p.conectarSlots(entrada, splitter);
        p.conectarSlots(splitter, correlationID);
        p.conectarSlots(correlationID, distributor);
        p.conectarSlots(distributor, replicatorFrio);
        p.conectarSlots(distributor, replicatorCalor);
        
        p.conectarSlots(replicatorFrio, translatorFrio);
        p.conectarSlots(translatorFrio, barbanFrio);
        p.conectarSlots(barbanFrio, correlatorFrio);
        p.conectarSlots(replicatorFrio, correlatorFrio);
        p.conectarSlots(correlatorFrio, contextEnricherFrio);
        
        p.conectarSlots(replicatorCalor, translatorCalor);
        p.conectarSlots(translatorCalor, barbanCalor);
        p.conectarSlots(barbanCalor, correlatorCalor);
        p.conectarSlots(replicatorCalor, correlatorCalor);
        p.conectarSlots(correlatorCalor, contextEnricherCalor);
        
        p.conectarSlots(contextEnricherFrio, merger);
        p.conectarSlots(contextEnricherCalor, merger);
        p.conectarSlots(merger, aggregator);
        p.conectarSlots(aggregator, salida); 
        
        p.inicializar();
        
    }
    
}
