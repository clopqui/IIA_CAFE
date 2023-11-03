/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package iia.tareas.transformadores;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import iia.tareas.Tarea;
import iia.utilidades.Mensaje;
import iia.utilidades.Slot;

/**
 *
 * @author chris
 */

/**
 * La clase Splitter es una subclase de Tarea que se encarga de dividir un mensaje de entrada en múltiples
 * mensajes de salida basándose en una expresión XPath. Esta tarea se utiliza para separar y enrutar partes
 * específicas de un mensaje a través de una expresión de filtro XPath.
 */
public class Splitter extends Tarea {
    private String xpathfiltro; // Expresión XPath utilizada para dividir el mensaje.

    /**
     * Constructor de la clase Splitter. Inicializa el Splitter con una expresión XPath de filtro.
     * @param xpath La expresión XPath utilizada para dividir el mensaje.
     */
    public Splitter(String xpath) {
        this.xpathfiltro = xpath;
    }

    /**
     * Inicia la tarea Splitter. Divide el mensaje de entrada en múltiples partes y las envía como mensajes de salida.
     */
    @Override
    public void iniciar() {
        while (!entrada(0).colaVacia()) {
            Mensaje mensaje = entrada(0).recuperarMensaje();
            Document[] partes = null;
            try {
                partes = split(mensaje, xpathfiltro);
            } catch (Exception ex) {
                // Manejar excepciones relacionadas con la división de mensajes
            }
            for (Document part : partes) {
                salida(0).pushMensaje(new Mensaje(part));
            }
        }
    }
    
    /**
     * Divide un mensaje en múltiples partes basándose en una expresión XPath.
     * @param mensaje El mensaje que se va a dividir.
     * @param xpathExpression La expresión XPath utilizada para la división.
     * @return Un arreglo de documentos XML que representan las partes divididas del mensaje.
     * @throws XPathExpressionException Si ocurre un error en la evaluación de la expresión XPath.
     * @throws Exception Si ocurre un error durante la división del mensaje.
     */
    protected Document[] split(Mensaje mensaje, String xpathExpression) throws XPathExpressionException, Exception {
        XPath xpath = XPathFactory.newInstance().newXPath();
        NodeList listaNodos = (NodeList) xpath.evaluate(xpathExpression, mensaje.getCuerpo(), XPathConstants.NODESET);
        Document[] partes = new Document[listaNodos.getLength()];
        for (int i = 0; i < listaNodos.getLength(); i++) {
            partes[i] = Mensaje.nodoADocumento(listaNodos.item(i));
        }
        return partes;
    }
}

