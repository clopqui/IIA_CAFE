/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package iia.cafe;

import iia.conector.Conector;
import iia.conector.ConectorSalida;
import iia.conector.ConectorEntrada;
import iia.conector.ConectorSolicitud;
import iia.tareas.Tarea;
import iia.tareas.puertos.Puerto;
import iia.utilidades.H2DB;
import iia.utilidades.Proceso;
import static iia.utilidades.Proceso.TAREAS.*;
import iia.utilidades.Utilidades;

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
        // TODO code application logic here
        H2DB db = new H2DB();
        db.crearBaseDatos();

        Proceso p = new Proceso();

        Conector lector = new ConectorEntrada("ficheros/comandasEntrada");
        Conector escritor = new ConectorSalida("ficheros/comandasSalidas");
        Conector frio = new ConectorSolicitud("barmanFrio", "barmanFrioPass");
        Conector calor = new ConectorSolicitud("barbanCalor", "barmanCalorPass");

        Puerto entrada = p.crearPuerto(lector, Proceso.PUERTOS.ENTRADAS);

        Tarea splitter = p.crearTarea(SPLITTER, "/cafe_order/drinks/drink");
        Tarea correlationID = p.crearTarea(CORRELATION_ID_SETTER);
        Tarea distributor = p.crearTarea(DISTRIBUTOR, new String[]{"/drink/type", "cold"});

        Tarea replicatorFrio = p.crearTarea(REPLICATOR);
        Tarea translatorFrioEntrada = p.crearTarea(TRANSLATOR, Utilidades.archivoXSLaString("ficheros/configuracion/translator_entradaFrio.xsl"));
        Puerto barbanFrio = p.crearPuerto(frio, Proceso.PUERTOS.SOLICITUD);
        Tarea translatorFrioSalida = p.crearTarea(TRANSLATOR, Utilidades.archivoXSLaString("ficheros/configuracion/translator_salidaFrio.xsl"));
        Tarea correlatorFrio = p.crearTarea(CORRELATOR);
        Tarea contextEnricherFrio = p.crearTarea(CONTEXT_ENRICHER);

        Tarea replicatorCalor = p.crearTarea(REPLICATOR);
        Tarea translatorCalorEntrada = p.crearTarea(TRANSLATOR, Utilidades.archivoXSLaString("ficheros/configuracion/translator_entradaCalor.xsl"));
        Puerto barbanCalor = p.crearPuerto(calor, Proceso.PUERTOS.SOLICITUD);
        Tarea translatorCalorSalida = p.crearTarea(TRANSLATOR, Utilidades.archivoXSLaString("ficheros/configuracion/translator_salidaCalor.xsl"));
        Tarea correlatorCalor = p.crearTarea(CORRELATOR);
        Tarea contextEnricherCalor = p.crearTarea(CONTEXT_ENRICHER);

        Tarea merger = p.crearTarea(MERGER);
        Tarea aggregator = p.crearTarea(AGGREGATOR);

        Puerto salida = p.crearPuerto(escritor, Proceso.PUERTOS.SALIDAS);

        p.conectarSlots(entrada, splitter);
        p.conectarSlots(splitter, correlationID);
        p.conectarSlots(correlationID, distributor);
        p.conectarSlots(distributor, replicatorFrio);
        p.conectarSlots(distributor, replicatorCalor);

        p.conectarSlots(replicatorFrio, translatorFrioEntrada);
        p.conectarSlots(translatorFrioEntrada, barbanFrio);
        p.conectarSlots(barbanFrio, translatorFrioSalida);
        p.conectarSlots(replicatorFrio, correlatorFrio);
        p.conectarSlots(translatorFrioSalida, correlatorFrio);
        p.conectarSlots(correlatorFrio, contextEnricherFrio);   ///Entrada principal
        p.conectarSlots(correlatorFrio, contextEnricherFrio);   ///Entrada adicional

        p.conectarSlots(replicatorCalor, translatorCalorEntrada);
        p.conectarSlots(translatorCalorEntrada, barbanCalor);
        p.conectarSlots(barbanCalor, translatorCalorSalida);
        p.conectarSlots(replicatorCalor, correlatorCalor);
        p.conectarSlots(translatorCalorSalida, correlatorCalor);
        p.conectarSlots(correlatorCalor, contextEnricherCalor);  ///Entrada principal
        p.conectarSlots(correlatorCalor, contextEnricherCalor);  ///Entrada adicional

        p.conectarSlots(contextEnricherFrio, merger);
        p.conectarSlots(contextEnricherCalor, merger);
        p.conectarSlots(merger, aggregator);
        p.conectarSlots(aggregator, salida);

        p.inicializarSecuencial();

    }
}
