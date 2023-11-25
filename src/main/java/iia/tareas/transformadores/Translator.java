/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package iia.tareas.transformadores;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import iia.tareas.Tarea;
import iia.utilidades.Mensaje;

/**
 *
 * @author chris
 */

/**
 * La clase Translator es una subclase de Tarea que se encarga de aplicar una transformación XSLT a un mensaje de entrada
 * y enviar el mensaje transformado como salida. Esta tarea se utiliza para traducir un mensaje de un formato a otro
 * utilizando reglas definidas en un archivo XSLT.
 */
public class Translator extends Tarea {
    
    private final String condicion; // La condición XSLT utilizada para la traducción.
    private Mensaje m; // Mensaje a traducir.
    
    /**
     * Constructor de la clase Translator. Inicializa el Translator con la condición XSLT.
     * @param XSLT La condición XSLT utilizada para la traducción.
     */
    public Translator(String XSLT) {
        this.condicion = XSLT;
    }
    
    /**
     * Inicia la tarea Translator. Aplica la transformación XSLT al mensaje de entrada y envía el mensaje
     * transformado como salida.
     */
    @Override
    public void iniciar() {
        while (!this.entrada(0).colaVacia()) {
            m = entrada(0).recuperarMensaje();           
            m.setCuerpo(traductor(m, condicion));
            salida(0).pushMensaje(m);
        }
    }
    
    /**
     * Aplica una transformación XSLT a un mensaje.
     * @param m El mensaje que se va a traducir.
     * @param condicion La condición XSLT utilizada para la traducción.
     * @return El documento XML resultante después de la transformación.
     */
    public Document traductor(Mensaje m, String condicion) {
        try {
            // Crear un nuevo TransformerFactory
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            // Crear las fuentes XSLT y XML a partir de las cadenas
            StreamSource xsltSource = new StreamSource(new StringReader(condicion));
            StreamSource xmlSource = new StreamSource(new StringReader(m.getCadenaCuerpo()));

            // Crear un StringWriter para la salida
            StringWriter outputWriter = new StringWriter();

            // Crear un Transformer para realizar la transformación
            Transformer transformer = transformerFactory.newTransformer(xsltSource);

            // Realizar la transformación y escribir la salida en el StringWriter
            transformer.transform(xmlSource, new StreamResult(outputWriter));

            // Obtener la salida transformada como una cadena
            String transformedXML = outputWriter.toString();
           
            // Analizar la cadena transformada en un Documento XML
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            ///Problema en la conversión a documento
            return builder.parse(new InputSource(new StringReader(transformedXML)));
            
            
        } catch (IOException | ParserConfigurationException | TransformerException | SAXException ex) {
            return null; 
        }
    }
}

