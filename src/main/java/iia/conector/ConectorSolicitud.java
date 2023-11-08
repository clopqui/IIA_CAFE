/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package iia.conector;

import iia.utilidades.H2DB;
import iia.utilidades.Mensaje;
import iia.utilidades.Utilidades;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.Document;

/**
 *
 * @author clopq
 */
public class ConectorSolicitud extends Conector{
    
    private H2DB bd;
    
    public ConectorSolicitud(String usuario, String contraseña){
        bd = new H2DB(usuario,contraseña);
    }
   
    @Override
    public  Document interaccionBD(Document documento){
        try {
            String sql = Utilidades.evaluarXpath(documento, "/sql").item(0).getTextContent();
            Document respuesta;
            try (ResultSet resultado = bd.crearDeclaracion().executeQuery(sql)) {
                respuesta = Utilidades.rsAdoc(resultado);
            }
            return respuesta;
        } catch (XPathExpressionException | ParserConfigurationException | SQLException | XMLStreamException ex) {
            Logger.getLogger(ConectorSolicitud.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    @Override
    public void enviarInformacionSalida(Mensaje m) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void enviarInformacionEntrada(String xml) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void iniciar() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void detener() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
