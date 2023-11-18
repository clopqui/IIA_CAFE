/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package TestConectores;

import iia.conector.Conector;
import iia.conector.ConectorSalida;
import iia.tareas.puertos.Puerto;
import iia.tareas.puertos.PuertoSalida;
import iia.utilidades.Mensaje;
import iia.utilidades.Slot;
import java.io.File;
import java.io.IOException;
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
public class TestConectorSalida {
    
    public TestConectorSalida() {
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
     * *
     *                      *** IMPORTANTE ***
     * Antes de realizar el test.
     * verifica que no exista ningún fichero en el directorio con el nombre de comanda1.xml
     * para evitar dar un test falso positivo.
     * 
     * En este test se verifica que el conector de salida es capaz de escribir
     * el documento XML en el directorio que le corresponde
     *
     * Como el conector de salida genera un fichero con un nombre, dependiendo
     * de su contador propio el nombre del fichero será nombre0 para las pruebas
     * en primera instancia, a menos que se modifique.
     *
     */
    @Test
    public void testConectorSalida() {
        ///Directorio donde se deja el fichero
        String pathSalida = ".\\ficheros\\test\\TestComandasSalida";
        
        ///Creamos un puerto, el conector y un slot de entrada al puerto
        Conector conectorSalida = new ConectorSalida(pathSalida);
        Puerto puertoSalida = new PuertoSalida(conectorSalida);

        Slot slotentrada = new Slot();

        try {
            //Establecemos las relaciones entre los distintos componentes
            conectorSalida.setPuerto(puertoSalida);
            puertoSalida.setSlotEntrada(slotentrada);

            ///Creamos un mensaje para que lo pueda leer el conector desde el slot
            File forder = new File(".\\ficheros\\test\\order1.xml");
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(forder);
            Mensaje mensajetest = new Mensaje(1, doc);

            ///Insertamos el mensaje en el slot
            slotentrada.pushMensaje(mensajetest);
            
            ///Iniciamos el proceso actual
            puertoSalida.iniciar();

            
            ///Esperamos unos 5 segundos para que genere el fichero de salida
            ///Porque se realizan de manera asíncrona
            Thread.sleep(5000);
            
            ///Verificamos los resultados
            if (slotentrada.colaVacia()) {
                ///Inicialización de variables para la búsqueda del fichero generado
                File[] files = new File(pathSalida).listFiles();
                boolean encontrado = false;
                int i = 0;
                
                ///Busqueda del fichero
                while (i < files.length && encontrado == false) {
                    if (files[i].getName().equals("comanda1.xml")) {
                        encontrado = true;
                    }else{i++;}
                }
                
                assertTrue(encontrado);
            }

        } catch (ParserConfigurationException | SAXException | IOException | InterruptedException ex) {
            System.out.println(ex.getMessage());
            fail();
        } 

    }
}
