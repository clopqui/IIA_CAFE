/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package iia.utilidades;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
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
public class Utilidades {
    
    
    
    public static String archivoXSLaString(String ruta){
       try {
            Path path = Paths.get(ruta);
            Charset charset = Charset.forName("UTF-8"); // Ajusta la codificación según la del archivo

            byte[] bytes = Files.readAllBytes(path);
            return new String(bytes, charset);
        } catch (IOException e) {
            return null;
        }
    }
    
    /**
     * Clona un documento XML.
     *
     * @param doc Documento XML a clonar.
     * @return Una copia clonada del documento XML original.
     */
    public static Document clonarDocumento(Document doc) {
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
    
    public static NodeList evaluarXpath(Document doc, String expresion) throws XPathExpressionException {
        XPathFactory factory = XPathFactory.newInstance();
        XPath xpath = factory.newXPath();
        XPathExpression expr = xpath.compile(expresion);
        return (NodeList) expr.evaluate(doc, javax.xml.xpath.XPathConstants.NODESET);
    }
    
    public static Document rsAdoc(ResultSet rs) throws ParserConfigurationException, SQLException, XMLStreamException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();

        ResultSetMetaData rsmd = rs.getMetaData();
        int colCount = rsmd.getColumnCount();

        Element results = doc.createElement("Resultados");
        doc.appendChild(results);

        while (rs.next()) {
            Element row = doc.createElement("Columnas");
            results.appendChild(row);

            for (int i = 1; i <= colCount; i++) {
                String columnName = rsmd.getColumnName(i);
                Object value = rs.getObject(i);

                Element node = doc.createElement(columnName);
                node.appendChild(doc.createTextNode(value.toString()));
                row.appendChild(node);
            }
        }
        return doc;
    }
}
