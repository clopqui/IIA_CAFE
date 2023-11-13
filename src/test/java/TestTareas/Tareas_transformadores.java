/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package TestTareas;

import iia.tareas.Tarea;
import iia.tareas.transformadores.Aggregator;
import iia.tareas.transformadores.Splitter;
import iia.tareas.transformadores.Translator;
import iia.utilidades.Mensaje;
import iia.utilidades.Slot;
import iia.utilidades.Utilidades;
import java.io.File;
import java.io.IOException;
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
public class Tareas_transformadores {

    public Tareas_transformadores() {
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
     * En este test se verifica que se agregan todos los elementos dispersos
     *
     * Solo nos sirve para la realización de los pasos de manera secuencial
     */
    @Test
    public void testAggregator() {
        ///Creación de la tarea, los slot de entrada y salida y mensaje 
        Tarea aggregator = new Aggregator();
        Slot entrada = new Slot();
        Slot salida = new Slot();

        ///Creamos dos mensajes y establecemos el mismo idCorrelacion 
        try {
            File forder = new File(".\\ficheros\\test\\drinkCold.xml");
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(forder);
            Mensaje m1 = new Mensaje(0, doc);      ///Mensaje que se correlaciona
            Mensaje m2 = new Mensaje(0, doc);      ///Mensaje que se debe correlacionar

            ///Insertamos los mensajes en los slot
            entrada.pushMensaje(m1);
            entrada.pushMensaje(m2);

            ///Preparamos el correlator con dos slots de entradas y dos de salida
            aggregator.añadirEntrada(entrada);
            aggregator.añadirSalida(salida);

            ///Iniciamos el correlator
            aggregator.iniciar();

            ///Verificamos los resultados
            ///Verificamos que se han pasado los mensajes de una cola a otra
            if (!salida.colaVacia() && entrada.colaVacia()) {
                Mensaje resultado = salida.recuperarMensaje();

                //System.out.println(resultado.getCadenaCuerpo());
                assertNotEquals(resultado, null);

            } else {
                fail();
            }
        } catch (IOException | ParserConfigurationException | SAXException e) {
            System.out.println(e.getCause());
            fail();
        }
    }

    /**
     * En este test se verifica que los elementos se dividen a partir de la
     * expresion Xpath
     *
     */
    @Test
    public void testSplitter() {
        ///Creación de la tarea, los slot de entrada y salida y mensaje 
        Tarea splitter = new Splitter("/cafe_order/drinks/drink");
        Slot entrada = new Slot();
        Slot salida = new Slot();

        ///Creamos dos mensajes y establecemos el mismo idCorrelacion 
        try {
            File forder = new File(".\\ficheros\\test\\order1.xml");
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(forder);
            Mensaje m1 = new Mensaje(0, doc);      ///Mensaje que se correlaciona

            ///Insertamos los mensajes en los slot
            entrada.pushMensaje(m1);

            ///Preparamos el correlator con dos slots de entradas y dos de salida
            splitter.añadirEntrada(entrada);
            splitter.añadirSalida(salida);

            ///Iniciamos el correlator
            splitter.iniciar();

            ///Verificamos los resultados
            ///Verificamos que se han pasado los mensajes de una cola a otra
            if (!salida.colaVacia() && entrada.colaVacia()) {
                Mensaje resultado1 = salida.recuperarMensaje(); //lo divide en dos mensajes
                Mensaje resultado2 = salida.recuperarMensaje();

                //System.out.println(resultado1.getCadenaCuerpo());
                //System.out.println(resultado2.getCadenaCuerpo());
                assertNotEquals(resultado1, null);
                assertNotEquals(resultado2, null);

            } else {
                fail();
            }
        } catch (IOException | ParserConfigurationException | SAXException e) {
            System.out.println(e.getCause());
            fail();
        }

    }

    /**
     * En este test se verifica el funcionamiento de la tarea translator
     * Para ello se ha tomado el fichero de configuración entradaFrío para verificar su correcta conversión
     */
    @Test
    public void testTranslator() {
        ///Creación de la tarea, los slot de entrada y salida y mensaje 
        Tarea translator = new Translator(Utilidades.archivoXSLaString("ficheros/configuracion/translator_entradaCaliente.xslt"));
        Slot entrada = new Slot();
        Slot salida = new Slot();

        ///Creamos dos mensajes y establecemos el mismo idCorrelacion 
        try {
            File forder = new File(".\\ficheros\\test\\drinkCold.xml");
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(forder);
            Mensaje m1 = new Mensaje(0, doc);      ///Mensaje que se traduce

            ///Insertamos los mensajes en los slot
            entrada.pushMensaje(m1);

            ///Preparamos el translator con un slot de entrada y salida
            translator.añadirEntrada(entrada);
            translator.añadirSalida(salida);

            ///Iniciamos el translator
            translator.iniciar();

            ///Verificamos los resultados
            ///Verificamos que se han pasado los mensajes de una cola a otra
            if (!salida.colaVacia() && entrada.colaVacia()) {
                ///Mensaje resultante esperado
                File fResult = new File(".\\ficheros\\test\\translatorResult.xml");
                Document doc2 = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(fResult);
                Mensaje resultadoEsperado = new Mensaje(1,doc2);
                
                ///Mensaje transformado
                Mensaje resultado1 = salida.recuperarMensaje(); //Resultado obtenido
                

                System.out.println(resultadoEsperado.getCadenaCuerpo());
                System.out.println(resultado1.getCadenaCuerpo());
                
                assertEquals(resultadoEsperado.getCadenaCuerpo().replaceAll("\\s+", ""),resultado1.getCadenaCuerpo().replaceAll("\\s+",""));
                
            } else {
                fail();
            }
        } catch (IOException | ParserConfigurationException | SAXException e) {
            System.out.println(e.getCause());
            fail();
        }

    }
}
