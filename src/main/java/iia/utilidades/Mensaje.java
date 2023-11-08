/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package iia.utilidades;

import java.io.StringWriter;
import java.util.UUID;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;

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
        this.cuerpo = Utilidades.clonarDocumento(cuerpo);
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
    public int getID() {
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

    
}