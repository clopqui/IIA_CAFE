/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package TestTareas;

import iia.tareas.Tarea;
import iia.tareas.enrutadores.Correlator;
import iia.tareas.enrutadores.Distributor;
import iia.tareas.enrutadores.Merger;
import iia.tareas.enrutadores.Replicator;
import iia.utilidades.Mensaje;
import iia.utilidades.Proceso;
import iia.utilidades.Slot;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author alejandro
 */
public class Tareas_Enrutadores {

    public Tareas_Enrutadores() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
    }

    /**
     * En este test se comprueba que la tarea Correlator correlaciona por ID
     * correlación y no tiene encuenta otros factores
     */
    @Test
    public void testCorrelatorPorID() {
        ///Creación de la tarea, los slot de entrada y salida y mensaje 
        Tarea correlator = new Correlator();
        Slot entrada1 = new Slot();
        Slot entrada2 = new Slot();
        Slot salida1 = new Slot();
        Slot salida2 = new Slot();

        ///Creamos dos mensajes y establecemos el mismo idCorrelacion 
        try {
            File forder = new File(".\\ficheros\\test\\order1.xml");
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(forder);
            Mensaje m1 = new Mensaje(0, doc);      ///Mensaje que se correlaciona
            Mensaje m2 = new Mensaje(0, doc);       ///Mensaje que se debe correlacionar
            UUID idCorrelacion1 = UUID.randomUUID();        ///ID de correlacion de los mensajes

            m1.setIDcorrelacion(idCorrelacion1);
            m2.setIDcorrelacion(idCorrelacion1);

            ///Se crea un tercer mensaje con el mismo ID pero distinto IDCorrelacion
            Mensaje m3 = new Mensaje(0, doc);
            m3.setIDcorrelacion(UUID.randomUUID());

            ///Insertamos los mensajes en los slot
            entrada1.pushMensaje(m1);
            entrada1.pushMensaje(m3);

            entrada2.pushMensaje(m2);

            ///Preparamos el correlator con dos slots de entradas y dos de salida
            correlator.añadirEntrada(entrada1);
            correlator.añadirEntrada(entrada2);

            correlator.añadirSalida(salida1);
            correlator.añadirSalida(salida2);

            ///Iniciamos el correlator
            correlator.iniciar();

            ///Verificamos los resultados
            
            ///Verificamos que se han pasado los mensajes de una cola a otra
            ///Debe quedar un mensaje en la cola 1
            if (!salida1.colaVacia() && !salida2.colaVacia() && !entrada1.colaVacia() && entrada2.colaVacia()) {
                Mensaje resultado1 = salida1.recuperarMensaje();
                Mensaje resultado2 = salida2.recuperarMensaje();

                System.out.println("El mensaje en la primera salida " + resultado1.getIDcorrelacion());
                System.out.println("El mensaje en la segunda salida " + resultado2.getIDcorrelacion());
                System.out.println("El mensaje no deseado es " + m3.getIDcorrelacion());
               
                assertEquals(resultado1.getIDcorrelacion(), resultado2.getIDcorrelacion());
                
            } else {
                System.out.println("Falla Aqui");
                fail();
            }
        } catch (IOException | ParserConfigurationException | SAXException e) {
            System.out.println(e.getCause());
            fail();
        }
    }

    /**
     * En este test dispondremos de un distributor con dos salidas y una
     * configuracion de forma que los separe por el tipo que sería cold y hot
     * como en el café
     *
     * Se dispone de dos mensajes uno caliente y otro frío,
     *
     * Se selecciona en la primera salida el tipo frío y el resto que no
     * corresponda irán a la otra salida
     */
    @Test
    public void testDistributor() {
        ///Creamos la tareas y los slots 
        String[] configuracion = new String[]{"/drink/type", "cold"};
        Tarea distributor = new Distributor(configuracion);

        Slot entrada = new Slot();
        Slot salidaCold = new Slot();   ///primera salida
        Slot salidaHot = new Slot();    ///segunda salida

        ///Creamos los mensajes
        Document docCold, docHot;
        try {
            File dCold = new File(".\\ficheros\\test\\drinkCold.xml");
            File dHot = new File(".\\ficheros\\test\\drinkHot.xml");

            docCold = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(dCold);
            docHot = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(dHot);

            Mensaje m1 = new Mensaje(1, docCold);      ///Mensaje de tipo cold con ID 1 para diferenciarlos
            Mensaje m2 = new Mensaje(2, docHot);       ///Mensaje de tipo hot  con ID 2 para diferenciarlos

            ///Insertamos los mensajes en el slot de entrada
            entrada.pushMensaje(m1);
            entrada.pushMensaje(m2);

            ///Preparamos el distributor con un slot de entradas y dos de salida según el orden especificado
            distributor.añadirEntrada(entrada);

            distributor.añadirSalida(salidaCold);
            distributor.añadirSalida(salidaHot);

            ///Iniciamos el distributor
            distributor.iniciar();

            ///Verificamos los resultados
            
            ///Verificamos que se han pasado los mensajes de una cola a otra
            if (!salidaCold.colaVacia() && !salidaHot.colaVacia() && entrada.colaVacia()) {
                Mensaje resultadoCold = salidaCold.recuperarMensaje();
                Mensaje resultadoHot = salidaHot.recuperarMensaje();

                System.out.println("ID mensaje cold " + resultadoCold.getID());
                System.out.println("ID mensaje hot " + resultadoHot.getID());
                assertEquals(resultadoCold.getID(), 1);
                assertEquals(resultadoHot.getID(), 2);

            } else {
                fail();
            }

        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(Tareas_Enrutadores.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }

    }

    /**
     * En este test verificamos la tarea Merger
     *
     */
    @Test
    public void testMerger() {
        ///Creamos la tarea, slots y  mensajes
        Tarea merger = new Merger();

        Slot entrada1 = new Slot();
        Slot entrada2 = new Slot();
        Slot entrada3 = new Slot();
        Slot sa1ida1 = new Slot();

        ///Creamos los mensajes
        try {
            File forder = new File(".\\ficheros\\test\\order1.xml");

            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(forder);
            Mensaje m1 = new Mensaje(1, doc);      ///Mensaje entrada 1
            Mensaje m2 = new Mensaje(2, doc);       ///Mensaje entrada 2
            Mensaje m3 = new Mensaje(3, doc);       ///Mensaje entrada 3

            ///insertamos los mensajes en los slot de entrada correspondientes
            entrada1.pushMensaje(m1);
            entrada2.pushMensaje(m2);
            entrada3.pushMensaje(m3);

            ///Añadimos los slots al merger
            merger.añadirEntrada(entrada1);
            merger.añadirEntrada(entrada2);
            merger.añadirEntrada(entrada3);

            merger.añadirSalida(sa1ida1);

            ///Iniciar merger
            merger.iniciar();

             ///Verificamos resultados.
             ///Verificamos que las colas de entrada no tengan los mensajes
           if(entrada1.colaVacia() && entrada2.colaVacia() && entrada3.colaVacia()){
                assertEquals(sa1ida1.obtenerTamaño(), 3);
           }else{
               fail();
           }

        } catch (IOException | ParserConfigurationException | SAXException ex) {
            Logger.getLogger(Tareas_Enrutadores.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * En este test verificamos la tarea Merger
     */
    @Test
    public void testReplicator() {
        ///Creamos la tarea, slots y  mensajes
        Tarea replicator = new Replicator();

        Slot entrada = new Slot();
        Slot salida1 = new Slot();
        Slot salida2 = new Slot();
        Slot salida3 = new Slot();

        ///Creamos los mensajes
        try {
            File forder = new File(".\\ficheros\\test\\order1.xml");

            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(forder);
            Mensaje m1 = new Mensaje(1, doc);      ///Mensaje

            ///insertamos los mensajes en los slot de entrada correspondientes
            entrada.pushMensaje(m1);

            ///añadimos los slots de entrada y salida del replicator
            replicator.añadirEntrada(entrada);
            replicator.añadirSalida(salida1);
            replicator.añadirSalida(salida2);
            replicator.añadirSalida(salida3);

            ///Iniciar merger
            replicator.iniciar();

            ///Verificamos resultados.
            
            ///Verificamos el paso de mensajes
            if (!salida1.colaVacia() && !salida2.colaVacia() && !salida3.colaVacia() && entrada.colaVacia()) {
                Mensaje resultado1 = salida1.recuperarMensaje();
                Mensaje resultado2 = salida2.recuperarMensaje();
                Mensaje resultado3 = salida3.recuperarMensaje();
                
                boolean isMismoObjeto1 = resultado1.hashCode() == resultado2.hashCode();
                boolean isMismoObjeto2 = resultado1.hashCode() == resultado3.hashCode();
                boolean isMismoObjeto3 = resultado2.hashCode() == resultado3.hashCode();
                
                System.out.println("Codigo hash objeto 1" + resultado1.hashCode());
                System.out.println("Codigo hash objeto 2" + resultado2.hashCode());
                System.out.println("Codigo hash objeto 3" + resultado3.hashCode());
                
                assertFalse(isMismoObjeto1);
                assertFalse(isMismoObjeto2);
                assertFalse(isMismoObjeto3);
                
            }else{
                fail();
            }

        } catch (IOException | ParserConfigurationException | SAXException ex) {
            Logger.getLogger(Tareas_Enrutadores.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

}
