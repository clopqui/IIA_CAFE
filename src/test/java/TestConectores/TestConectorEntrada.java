/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package TestConectores;

import iia.conector.Conector;
import iia.conector.ConectorEntrada;
import iia.tareas.puertos.Puerto;
import iia.tareas.puertos.PuertoEntrada;
import iia.utilidades.Mensaje;
import iia.utilidades.Slot;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


/**
 *
 * @author alejandro
 */
public class TestConectorEntrada {

    public TestConectorEntrada() {
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
     *                      *** IMPORTANTE *** 
     * Antes de realizar el test 
     * Verificar que tiene al menos un fichero para leer en el directorio seleccionado.
     *
     * Verifica que el conector de entrada funciona correctamente al coger una
     * orden Para ello se usara el directorio TestComandasEntrada para coger las
     * comandas.
     *
     *
     */
    @Test
    public void testConectorEntrada() {
        ///Directorio usado Para la lectura de ficheros
        String pathEntrada = ".\\ficheros\\test\\TestComandasEntrada";

        ///Creamos el puerto y el conector de entrada
        Conector conectorEntrada = new ConectorEntrada(pathEntrada);
        Puerto pEntrada = new PuertoEntrada(conectorEntrada);

        ///Un slot donde se cargará el mensaje del puerto
        Slot slotEntrada = new Slot();

        try {
            ///Establecemos la relacion entre ellos
            pEntrada.setSlotSalida(slotEntrada);     ///Solo tiene que escribir
            conectorEntrada.setPuerto(pEntrada);

            ///Iniciamos el hilo para que lea la comanda
            conectorEntrada.iniciar();

            ///Esperamos 5 segundos para ver si se ha leido la comanda
            Thread.sleep(5000);

            ///Lo detenemos para que no siga buscando comandas
            conectorEntrada.detener();

            ///Verificamos los resultados
            if (!slotEntrada.colaVacia()) {
                Mensaje resultado = slotEntrada.recuperarMensaje();

                System.out.println(resultado.getCadenaCuerpo());

                assertNotNull(resultado);
            }

        } catch (Exception e) {
            fail();
        }

    }

    
}
