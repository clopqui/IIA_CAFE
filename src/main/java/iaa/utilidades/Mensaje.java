/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package iaa.utilidades;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.UUID;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSException;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author chris
 */

/**
 * La clase `Mensaje` representa un mensaje que contiene un identificador único, un identificador de correlación
 * y un cuerpo en forma de documento XML. Permite la manipulación y transformación de mensajes XML.
 */
public class Mensaje {
    
    private final int ID;
    private UUID IDcorrelacion;
    private Document cuerpo;
    private static int contador = 0;
    
    /**
     * Constructor de la clase Mensaje.
     *
     * @param ID            Identificador único del mensaje.
     * @param cuerpo        Documento XML que representa el cuerpo del mensaje.
     */
    public Mensaje(int ID, Document cuerpo){
        this.ID = ID;
        this.IDcorrelacion = null;
        this.cuerpo = clonarDocumento(cuerpo);
    }
    
    /**
     * Constructor de la clase Mensaje que autoincrementa los identificadores.
     *
     * @param cuerpo Documento XML que representa el cuerpo del mensaje.
     */
    public Mensaje(Document cuerpo){
        this(contador, cuerpo);
        contador++;
    }
    
    /**
     * Obtiene el identificador único del mensaje.
     *
     * @return Identificador único del mensaje.
     */
    public long getID() {
        return ID;
    }
    
    /**
     * Obtiene el identificador de correlación del mensaje.
     *
     * @return Identificador de correlación del mensaje.
     */
    public UUID getIDcorrelacion() {
        return IDcorrelacion;
    }
    
    /**
     * Obtiene el cuerpo del mensaje como un documento XML.
     *
     * @return Documento XML que representa el cuerpo del mensaje.
     */
    public Document getCuerpo() {
        return cuerpo;
    }
    
    /**
     * Obtiene una representación del cuerpo del mensaje como una cadena XML formateada.
     *
     * @return Cadena XML que representa el cuerpo del mensaje, o "El documento no está bien formado" en caso de error.
     */
    public String getCadenaCuerpo() {
        try {
            // Crea un objeto de escritura XML
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);

            // Crea un objeto Transformer
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            // Configura la salida para que sea "pretty-printed"
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            // Transforma el documento en la salida
            transformer.transform(new DOMSource(cuerpo), result);

            // Devuelve la representación XML como una cadena
            return writer.toString();
        } catch (TransformerException ex) {
            return "El documento no está bien formado";
        }
    }
    
    /**
     * Establece el cuerpo del mensaje como un documento XML.
     *
     * @param cuerpo Documento XML que representa el cuerpo del mensaje.
     */
    public void setCuerpo(Document cuerpo){
        this.cuerpo = cuerpo;
    }
    
    public void setIDcorrelacion(UUID id){
        this.IDcorrelacion = id;
    }

    /**
     * Clona un documento XML.
     *
     * @param doc Documento XML a clonar.
     * @return Una copia clonada del documento XML original.
     */
    private Document clonarDocumento(Document doc) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document newDocument = builder.newDocument();
            Node importedNode = newDocument.importNode(doc.getDocumentElement(), true);
            newDocument.appendChild(importedNode);
            return newDocument;
        } catch (ParserConfigurationException ex) {
            return null;
        }
    }
    
    /**
     * Convierte una cadena XML en un documento XML.
     *
     * @param xml Cadena XML a convertir.
     * @return Documento XML resultante.
     * @throws Exception Si ocurre un error durante la conversión.
     */
    public static Document XMLaDocumento(String xml) throws Exception {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource inputSource = new InputSource(new StringReader(xml));
            return builder.parse(inputSource);
        } catch (IOException | ParserConfigurationException | SAXException ex) {
            return null;
        }
    }
    
    /**
     * Combina dos documentos XML en uno solo.
     *
     * @param xmlprincipal    Documento XML principal.
     * @param xmlsecundario   Documento XML secundario a combinar.
     * @return Documento XML combinado.
     * @throws Exception Si ocurre un error durante la combinación.
     */
    public static Document juntarXML(Document xmlprincipal, Document xmlsecundario) throws Exception {
        try {
            Node root2 = xmlsecundario.getDocumentElement();
            Node importedNode = xmlprincipal.importNode(root2, true);
            xmlprincipal.getDocumentElement().appendChild(importedNode);
            return xmlprincipal;
        } catch (Exception ex) {
            return null;
        }   
    } 
    
    /**
     * Convierte un nodo XML en un documento XML independiente.
     *
     * @param node Nodo XML a convertir.
     * @return Documento XML resultante.
     * @throws Exception Si ocurre un error durante la conversión.
     */
    public static Document nodoADocumento(Node node) throws Exception {
        try {
            DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
            DOMImplementationLS domImplLS = (DOMImplementationLS) registry.getDOMImplementation("LS");
            LSSerializer serializer = domImplLS.createLSSerializer();
            String xmlString = serializer.writeToString(node);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(new InputSource(new StringReader(xmlString)));
        } catch (IOException | ClassCastException | ClassNotFoundException | IllegalAccessException | InstantiationException | ParserConfigurationException | DOMException | LSException | SAXException ex) {
            return null;
        }
    }
}