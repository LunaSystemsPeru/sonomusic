/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sonomusic;

import javax.swing.JFrame;
import org.jvnet.substance.SubstanceLookAndFeel;
import pdfs.pdfComprobanteVenta;
import pdfs.pdfGuiaTraslado;

/**
 *
 * @author CALIDAD
 */
public class Sonomusic {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        JFrame.setDefaultLookAndFeelDecorated(true);
        SubstanceLookAndFeel.setSkin("org.jvnet.substance.skin.BusinessBlackSteelSkin");
        frm_principal menu = new frm_principal();
        menu.setVisible(true);
/*
        pdfComprobanteVenta df = new pdfComprobanteVenta();
        df.generarPDF();
*/
    }
    
}
