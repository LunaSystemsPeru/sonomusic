/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import clases.cl_conectar;
import clases_autocomplete.cla_mis_documentos;
import clases_autocomplete.cla_motivo_nota;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import sonomusic.frm_principal;

/**
 *
 * @author luis
 */
public class m_motivo_nota {
    
    cl_conectar c_conectar = new cl_conectar();
   
    private int idtido;

    public m_motivo_nota() {
    }

    public void setIdtido(int idtido) {
        this.idtido = idtido;
    }
    
    
    public void cbx_motivo (JComboBox cbx) {
        try {
            cbx.removeAllItems();
            
            Statement st = c_conectar.conexion();
            
            String query = "select * from motivo_documento "
                    + "where idtido = '"+this.idtido+"' "
                    + "order by nombre asc";
            ResultSet rs = c_conectar.consulta(st, query);
            
            while (rs.next()) {
                cbx.addItem(new cla_motivo_nota(rs.getInt("idmotivo"), rs.getString("nombre")));
            }
            
            c_conectar.cerrar(st);
            c_conectar.cerrar(rs);
        } catch (SQLException ex) {
            System.out.println(ex);
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    
    
}
