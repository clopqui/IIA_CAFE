/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package TestConectores;

import iia.conector.Conector;
import iia.conector.ConectorSolicitud;
import iia.tareas.puertos.Puerto;
import iia.tareas.puertos.PuertoSolicitud;
import iia.utilidades.H2DB;
import iia.utilidades.Mensaje;
import iia.utilidades.Slot;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;


/**
 *
 * @author alejandro
 */
public class TestConectorSolicitud {

    public TestConectorSolicitud() {
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
     * En este test se verifica que el conector de solicitud funcione correctamente para la realización de consultas
     * 
     */
    @Test
    public void testConectorSolicitud() {
            ///Crear base de datos si no existe
            H2DB BD = new H2DB();
            BD.crearBaseDatos();
            
            ///Creamos el conector, el puerto de solicitud y los slots de entrada y salida del puerto
            String userTest = "userTest";
            String passwordTest = "passwordTest";
            
            Conector conectorSolicitud = new ConectorSolicitud(userTest, passwordTest);
            Puerto puertoSolicitud = new PuertoSolicitud(conectorSolicitud);
            
            Slot slotEntrada = new Slot();
            Slot slotSalida = new Slot();
            

            
        
            
        try {
            ///Creamos mensaje ya traducido para que se pueda realizar la consulta
            File forder = new File(".\\ficheros\\test\\translatorSQLResult.xml");
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(forder);
            Mensaje mensajetest = new Mensaje(1, doc);
            
            ///Relacionamos los distintos componentes
            puertoSolicitud.setSlotEntrada(slotEntrada);
            puertoSolicitud.setSlotSalida(slotSalida);
            
            ///Insertamos el mensaje generado
            slotEntrada.pushMensaje(mensajetest);
            
            ///Iniciamos el proceso
            puertoSolicitud.iniciar();
            
            ///Como es un proceso asíncrono esperamos 5 segundos
            Thread.sleep(5000);
            
            ///Verificamos los resultados
            if(slotEntrada.colaVacia() && !slotSalida.colaVacia()){
                Mensaje resultado = slotSalida.recuperarMensaje();
                
                System.out.println("Me devuelve " + resultado.getCadenaCuerpo());
                assertNotNull(resultado);
            }
            
        } catch (ParserConfigurationException | SAXException | IOException | InterruptedException ex) {
            fail();
        }

    }
}
