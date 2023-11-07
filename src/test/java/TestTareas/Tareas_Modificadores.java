/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package TestTareas;

import iia.tareas.Tarea;
import iia.tareas.modificadores.Context_enricher;
import iia.tareas.modificadores.Correlation_ID_Setter;
import iia.utilidades.Mensaje;
import iia.utilidades.Slot;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
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
public class Tareas_Modificadores {

    public Tareas_Modificadores() {
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

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    
    /**
     * En este test se verifica que el Context_enricher funciona correctamente
     * Para ello se toma un pedido de una bebida fría y añadimos el estado de
     * que se ha realizado con éxito
     *
     * Primera entrada Mensaje normal Segunda entrada Contexto
     *
     * Para eliminar la incertidumbre de que ocurran fallos al comparar el
     * resultado esperado y el obtenido se le eliminan los espacios en blancos
     */
    @Test
    public void testContext_enricher() {
        ///Creamos las tareas los slots y los mensajes
        Tarea contextEnricher = new Context_enricher();

        Slot contexto = new Slot();
        Slot entrada = new Slot();
        Slot salida = new Slot();

        try {
            File fOrder = new File(".\\ficheros\\test\\drinkCold.xml");
            File fContext = new File(".\\ficheros\\test\\context.xml");
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(fOrder);
            Document docContext = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(fContext);
            Mensaje m1 = new Mensaje(0, doc);      ///Mensaje que se correlaciona
            Mensaje m2 = new Mensaje(0, docContext);

            ///Insertamos los mensajes en los slot
            entrada.pushMensaje(m1);
            contexto.pushMensaje(m2);

            ///Preparamos el correlator con dos slots de entradas y dos de salida
            contextEnricher.añadirEntrada(entrada);
            contextEnricher.añadirEntrada(contexto);
            contextEnricher.añadirSalida(salida);

            ///Iniciamos el contextEnricher
            contextEnricher.iniciar();

            ///Verificamos los resultados
            ///Verificamos que se han pasado los mensajes de una cola a otra
            if (entrada.colaVacia() && contexto.colaVacia()) {
                ///Cargamos el fichero esperado
                File fResultExpected = new File(".\\ficheros\\test\\contextResult.xml");
                Document docResult = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(fResultExpected);
                Mensaje resultadoEsperado = new Mensaje(5, docResult);

                ///Leemos el mensaje de salida
                Mensaje resultado1 = salida.recuperarMensaje();

                //System.out.println(resultado1.getCadenaCuerpo());
                //System.out.println(resultadoEsperado.getCadenaCuerpo());

                assertEquals(resultado1.getCadenaCuerpo().replaceAll("\\s", ""), resultadoEsperado.getCadenaCuerpo().replaceAll("\\s", ""));
            } else {
                System.out.println("Fallo en el paso de mensajes");
                fail();
            }
        } catch (IOException | ParserConfigurationException | SAXException e) {
            System.out.println(e.getCause());
            fail();
        }

    }

    /**
     * En este test se verifica si se inserta un ID de correlacion en el mensaje 
     * y si es diferente para cada mensaje
     */
    @Test
    public void testCorrelation_ID_Setter() {
        ///Creamos las tareas los slots y los mensajes
        Tarea correlationIDSetter = new Correlation_ID_Setter();

        Slot entrada = new Slot();
        Slot salida = new Slot();

        try {
            File fOrder = new File(".\\ficheros\\test\\drinkCold.xml");
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(fOrder);
            Mensaje m1 = new Mensaje(0, doc);      ///Mensaje que no tiene ID de correlacion
            Mensaje m2 = new Mensaje(0, doc);      ///Mensaje que no tiene ID de correlacion
            
            
            ///Insertamos el mensaje en el slot
            entrada.pushMensaje(m1);
            entrada.pushMensaje(m2);
            
            ///añadimos los slots a la tarea
            correlationIDSetter.añadirEntrada(entrada);
            correlationIDSetter.añadirSalida(salida);
            
            ///Iniciamos el Correlation id setter
            correlationIDSetter.iniciar();
            
            ///verificamos los resultados
            //Verificamos si se han pasado los mensajes de la entrada
            if(entrada.colaVacia()){
                //Leemos los resultados
                UUID resultado1 = salida.recuperarMensaje().getIDcorrelacion();
                UUID resultado2 = salida.recuperarMensaje().getIDcorrelacion();
                
                assertNotEquals(resultado1,null);
                assertNotEquals(resultado2,null);
               
                assertNotEquals(resultado1,resultado2);
            }else{
                fail();
            }

        } catch (IOException | ParserConfigurationException | SAXException e) {
            System.out.println("Fallo en el paso de mensajes");
            fail();
        }
    }
}
