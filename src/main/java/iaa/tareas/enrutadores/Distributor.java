/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package iaa.tareas.enrutadores;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import iaa.tareas.Tarea;
import iaa.utilidades.Mensaje;

/**
 *
 * @author chris
 */

/**
 * La clase Distributor es una subclase de Tarea que se encarga de distribuir mensajes de entrada en función de una condición dada.
 * Esta tarea se utiliza para dividir mensajes en dos grupos: los que cumplen con la condición y los que no.
 */
public class Distributor extends Tarea {
    private String xpath; // Expresión XPath para evaluar la condición.
    private String cadena; // Cadena de texto que se compara con el resultado de la expresión XPath.

    /**
     * Constructor de la clase Distributor. Inicializa la tarea de distribución con una configuración específica.
     * @param configuracion Un array de dos strings que contiene la expresión XPath y la cadena de texto de la condición.
     */
    public Distributor(Object configuracion) {
        String[] configuracionArray = (String[]) configuracion;
        this.xpath = configuracionArray[0];
        this.cadena = configuracionArray[1];
    }

    /**
     * Inicia la tarea Distributor. Distribuye mensajes de entrada en función de la condición especificada.
     */
    @Override
    public void iniciar() {
        while (!entrada(0).colaVacia()) {
            Mensaje m = entrada(0).recuperarMensaje();
            if (check(m, xpath, cadena)) {
                salida(0).pushMensaje(m); // Cumple con la condición
            } else {
                salida(1).pushMensaje(m); // No cumple con la condición
            }
        }
    }

    /**
     * Comprueba si un mensaje cumple con una condición dada, evaluando una expresión XPath en su contenido.
     * @param m El mensaje a evaluar.
     * @param cadenaxpath La expresión XPath que se evaluará en el contenido del mensaje.
     * @param cadena La cadena de texto con la que se comparará el resultado de la expresión XPath.
     * @return True si el mensaje cumple con la condición, False si no cumple.
     */
    public boolean check(Mensaje m, String cadenaxpath, String cadena) {
        try {
            Document doc = m.getCuerpo();
            XPathFactory factory = XPathFactory.newInstance();
            XPath xpath = factory.newXPath();
            NodeList nodeList = (NodeList) xpath.evaluate(cadenaxpath, doc, XPathConstants.NODESET);

            if (nodeList.getLength() > 0) {
                String contenidoMensaje = nodeList.item(0).getTextContent();
                return contenidoMensaje.equals(cadena);
            } else {
                return false;
            }
        } catch (XPathExpressionException ex) {
            return false;
        }
    }
}

