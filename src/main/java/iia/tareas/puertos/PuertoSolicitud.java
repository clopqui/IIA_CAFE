/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package iia.tareas.puertos;

import iia.conector.Conector;
import org.w3c.dom.Document;
import iia.utilidades.Mensaje;
import iia.utilidades.Slot;

/**
 *
 * @author chris
 */
public class PuertoSolicitud extends Puerto{

    public PuertoSolicitud(Conector conector) {
        super(conector);
    }
    
    /**
     * Establece el slot de entrada asociado al puerto de salida para la comunicación.
     * @param entrada El slot de entrada que se establecerá para la comunicación.
     */
    @Override
    public void setSlotEntrada(Slot entrada) {
        this.slotEntrada = entrada;
    }
    
    @Override
    public void setSlotSalida(Slot salida) {
        this.slotSalida = salida;
    }
    

    @Override
    protected void enviarInformacionMensaje(Mensaje m) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    protected void enviarInformacionDocumento(Document d) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void iniciar() {
        while(!slotEntrada.colaVacia()){
            Mensaje m = slotEntrada.recuperarMensaje();
            var hilo = new Thread(() -> {
                Document respuestaBD = conector.interaccionBD(m.getCuerpo());
                if(respuestaBD != null){
                    m.setCuerpo(respuestaBD);
                    slotSalida.pushMensaje(m);
                }
            });
            hilo.start();
        }
    }
}