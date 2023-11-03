/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package iaa.tareas.transformadores;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import iaa.tareas.Tarea;
import iaa.utilidades.Mensaje;

/**
 *
 * @author chris
 */

/**
 * La clase Aggregator es una subclase de Tarea que se encarga de recopilar mensajes de entrada, fusionarlos
 * y enviar un mensaje de salida que contiene la fusión de los mensajes. Esta tarea se utiliza para combinar
 * información de entrada de varias fuentes en un solo mensaje de salida.
 */
public class Aggregator extends Tarea {

    /**
     * Inicia la tarea Aggregator. Recopila los mensajes de entrada, los fusiona en un solo mensaje y los envía como salida.
     */
    @Override
    public void iniciar() {
        try {
            List<Mensaje> mensajes = recopilarMensajes(entrada(0).getIterador());
            Document fusionarDocu = juntarMensajesXML(mensajes);
            salida(0).pushMensaje(new Mensaje(fusionarDocu));
        }catch (Exception ex) {
            // Manejar excepciones relacionadas con la recopilación y fusión de mensajes

        }
    }
    
    /**
     * Recopila los mensajes de entrada y los almacena en una lista.
     * @param mensajeIterator El iterador que proporciona los mensajes de entrada.
     * @return Una lista de mensajes recopilados.
     */
    protected List<Mensaje> recopilarMensajes(Iterator<Mensaje> mensajeIterator) {
        List<Mensaje> mensajes = new ArrayList<>();
        while (mensajeIterator.hasNext()) {
            mensajes.add(mensajeIterator.next());
        }
        return mensajes;
    }
    
    /**
     * Fusiona varios mensajes XML en un solo documento XML.
     * @param mensajes La lista de mensajes a fusionar.
     * @return El documento XML que contiene la fusión de los mensajes.
     */
    protected Document juntarMensajesXML(List<Mensaje> mensajes) {
        try {
            Document documentoAgregado = crearDocumentoVacio();
            Element elementoRaiz = documentoAgregado.getDocumentElement();

            for (Mensaje mensaje : mensajes) {
                Document cuerpoMensaje = mensaje.getCuerpo();
                Node nodoCuerpo = documentoAgregado.importNode(cuerpoMensaje.getDocumentElement(), true);
                elementoRaiz.appendChild(nodoCuerpo);
            }
            return documentoAgregado;
        } catch (DOMException ex) {
            return null;
        }
    }
    
    /**
     * Crea un documento XML vacío con un elemento raíz.
     * @return El documento XML vacío con el elemento raíz.
     */
    protected Document crearDocumentoVacio() {
        // Crea un documento XML vacío con un elemento raíz
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.newDocument();
            Element root = doc.createElement("lista"); // Personalizar el nombre del elemento raíz aquí
            doc.appendChild(root);
            return doc;
        } catch (ParserConfigurationException | DOMException ex) {
            return null;
        }
    }
}

