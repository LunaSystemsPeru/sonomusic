/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forms;

import clases.cl_proveedor;
import clases.cl_varios;
import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;
import json.cl_json_entidad;
import nicon.notify.core.Notification;

/**
 *
 * @author luis
 */
public class frm_reg_proveedor extends javax.swing.JDialog {

    cl_varios c_varios = new cl_varios();
    public static cl_proveedor c_proveedor = new cl_proveedor();

    public static String origen = "";
    public static boolean registrar = true;

    /**
     * Creates new form frm_reg_proveedor
     */
    public frm_reg_proveedor(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        System.out.println("que hare " + registrar );

        if (!registrar) {
            c_proveedor.cargar_datos();
            txt_condicion.setText(c_proveedor.getCondicion());
            txt_nom.setText(c_proveedor.getRazon_social());
            txt_dir.setText(c_proveedor.getDireccion());
            txt_estado.setText(c_proveedor.getEstado());
            txt_ndoc.setText(c_proveedor.getRuc());
            jButton1.setEnabled(true);
            txt_ndoc.setEnabled(true);
            txt_nom.setEnabled(true);
            txt_dir.setEnabled(true);
            txt_estado.setEnabled(true);
            txt_condicion.setEnabled(true);
            btn_guardar.setEnabled(true);
        } else {
            jButton1.setEnabled(false);
        }
    }

    private void obtenerDatos() {
        String documento = txt_ndoc.getText();
        if (documento.length() > 0) {
            switch (documento.length()) {
                case 8:
                    System.out.println("buscar dni");
                    Notification.show("Proveedor", "Buscando datos en RENIEC");
                    try {
                        String json = cl_json_entidad.getJSONDNI_LUNASYSTEMS(documento);
                        //Lo mostramos
                        String datos = cl_json_entidad.showJSONDNI(json);
                        txt_nom.setText(datos);
                        txt_condicion.setText("HABIDO");
                        txt_estado.setText("ACTIVO");
                        if (txt_nom.getText().length() > 0) {
                            txt_dir.setText("");
                            txt_dir.setEnabled(true);
                            txt_dir.requestFocus();
                        }

                    } catch (org.json.simple.parser.ParseException ex) {
                        JOptionPane.showMessageDialog(null, "ERROR EN BUSCAR RUC " + ex.getLocalizedMessage());
                    }
                    break;
                case 11:
                    System.out.println("buscar ruc");
                    Notification.show("Proveedor", "Buscando datos en SUNAT");
                    try {
                        String json = cl_json_entidad.getJSONRUC_LUNASYSTEMS(documento);
                        //Lo mostramos
                        String[] datos = cl_json_entidad.showJSONRUC_JMP(json);
                        txt_nom.setText(datos[0]);
                        txt_dir.setText(datos[1]);
                        txt_condicion.setText(datos[2]);
                        txt_estado.setText(datos[3]);
                        if (txt_nom.getText().length() > 0) {
                            btn_guardar.setEnabled(true);
                            btn_guardar.requestFocus();
                        }

                    } catch (org.json.simple.parser.ParseException ex) {
                        JOptionPane.showMessageDialog(null, "ERROR EN BUSCAR RUC " + ex.getLocalizedMessage());
                    }
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "NRO DEL DOCUMENTO DEL PROVEEDOR ES INCORRECTO");
                    txt_ndoc.requestFocus();
                    break;
            }

        } else {
            JOptionPane.showMessageDialog(null, "INGRESE DOCUMENTO DEL PROVEEDOR");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txt_ndoc = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txt_nom = new javax.swing.JTextField();
        txt_dir = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jToolBar1 = new javax.swing.JToolBar();
        btn_guardar = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btn_salir = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txt_condicion = new javax.swing.JTextField();
        txt_estado = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Registrar Proveedores");

        jLabel2.setText("Nro de Doc:");

        jLabel3.setText("Nombre o Razon Social:");

        txt_ndoc.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_ndoc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_ndocKeyTyped(evt);
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_ndocKeyPressed(evt);
            }
        });

        jLabel4.setText("Direccion:");

        txt_nom.setEnabled(false);
        txt_nom.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_nomKeyTyped(evt);
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_nomKeyPressed(evt);
            }
        });

        txt_dir.setEnabled(false);
        txt_dir.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_dirKeyTyped(evt);
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_dirKeyPressed(evt);
            }
        });

        jLabel6.setText("Datos del Proveedor:");

        jToolBar1.setFloatable(false);

        btn_guardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/accept.png"))); // NOI18N
        btn_guardar.setText("Guardar");
        btn_guardar.setEnabled(false);
        btn_guardar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn_guardar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btn_guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_guardarActionPerformed(evt);
            }
        });
        jToolBar1.add(btn_guardar);
        jToolBar1.add(jSeparator1);

        btn_salir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/cross.png"))); // NOI18N
        btn_salir.setText("Salir");
        btn_salir.setFocusable(false);
        btn_salir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn_salir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btn_salir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_salirActionPerformed(evt);
            }
        });
        jToolBar1.add(btn_salir);

        jLabel7.setText("Condicion:");

        jLabel8.setText("Estado:");

        txt_condicion.setEnabled(false);

        txt_estado.setEnabled(false);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/error.png"))); // NOI18N
        jButton1.setText("Volver a Obtener Datos!");
        jButton1.setEnabled(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_dir)
                            .addComponent(txt_nom)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(txt_estado, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                                        .addComponent(txt_condicion, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(txt_ndoc, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButton1)))
                                .addGap(0, 52, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_ndoc, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_nom, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_dir, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txt_condicion, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txt_estado, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txt_ndocKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_ndocKeyTyped
        c_varios.limitar_caracteres(evt, txt_ndoc, 11);
        c_varios.solo_numeros(evt);
    }//GEN-LAST:event_txt_ndocKeyTyped

    private void txt_ndocKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_ndocKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            obtenerDatos();
        }
    }//GEN-LAST:event_txt_ndocKeyPressed

    private void txt_nomKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_nomKeyTyped
        c_varios.limitar_caracteres(evt, txt_nom, 245);
    }//GEN-LAST:event_txt_nomKeyTyped

    private void txt_nomKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_nomKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (txt_nom.getText().length() > 0) {
                txt_dir.setEnabled(true);
                txt_dir.requestFocus();
            }
        }
    }//GEN-LAST:event_txt_nomKeyPressed

    private void txt_dirKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_dirKeyTyped
        c_varios.limitar_caracteres(evt, txt_dir, 245);
    }//GEN-LAST:event_txt_dirKeyTyped

    private void txt_dirKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_dirKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (txt_dir.getText().length() > 0) {
                txt_condicion.setEnabled(true);
                txt_condicion.requestFocus();
            }
        }
    }//GEN-LAST:event_txt_dirKeyPressed

    private void btn_salirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_salirActionPerformed
        if (origen.equals("reg_ingreso")) {
            this.dispose();
        }
    }//GEN-LAST:event_btn_salirActionPerformed

    private void btn_guardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_guardarActionPerformed
        c_proveedor.setCondicion(txt_condicion.getText());
        c_proveedor.setEstado(txt_estado.getText());
        c_proveedor.setDireccion(txt_dir.getText());
        c_proveedor.setRazon_social(txt_nom.getText());
        c_proveedor.setRuc(txt_ndoc.getText());

        boolean registrado = false;
        if (registrar) {
            c_proveedor.obtener_codigo();
            registrado = c_proveedor.registrar();
        } else {
            registrado = c_proveedor.modificar();
        }

        if (registrado) {
            this.dispose();
            origen = "";
            registrar = true;
        } else {
            JOptionPane.showMessageDialog(null, "ERRROR!!! EN PROVEEDOR");
        }
    }//GEN-LAST:event_btn_guardarActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        obtenerDatos();
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(frm_reg_proveedor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frm_reg_proveedor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frm_reg_proveedor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frm_reg_proveedor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                frm_reg_proveedor dialog = new frm_reg_proveedor(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_guardar;
    private javax.swing.JButton btn_salir;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JTextField txt_condicion;
    public static javax.swing.JTextField txt_dir;
    private javax.swing.JTextField txt_estado;
    public static javax.swing.JTextField txt_ndoc;
    public static javax.swing.JTextField txt_nom;
    // End of variables declaration//GEN-END:variables
}
