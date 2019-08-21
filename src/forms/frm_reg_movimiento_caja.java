/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forms;

import clases.cl_movimiento_caja;
import clases.cl_varios;
import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;
import sonomusic.frm_principal;

/**
 *
 * @author luis
 */
public class frm_reg_movimiento_caja extends javax.swing.JInternalFrame {

    cl_movimiento_caja c_movimiento = new cl_movimiento_caja();
    cl_varios c_varios = new cl_varios();

    int id_almacen = frm_principal.c_almacen.getId();
    int id_usuario = frm_principal.c_usuario.getId_usuario();

    /**
     * Creates new form frm_reg_movimiento_caja
     */
    public frm_reg_movimiento_caja() {
        initComponents();
        String fecha = c_varios.getFechaActual();
        String query = "select mc.id_movimiento, mc.ingresa, mc.retira, mc.motivo, u.username "
                + "from cajas_movimientos as mc "
                + "inner join usuarios as u on u.id_usuarios = mc.id_usuarios "
                + "where mc.fecha = '" + fecha + "' and mc.id_almacen = '" + id_almacen + "'";
        c_movimiento.mostrar(t_movimientos, query);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jd_reg_movimiento = new javax.swing.JDialog();
        jToolBar2 = new javax.swing.JToolBar();
        btn_j_guardar = new javax.swing.JButton();
        btn_j_salir = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        txt_motivo = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txt_monto = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        cbx_tipo = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        t_movimientos = new javax.swing.JTable();
        jToolBar1 = new javax.swing.JToolBar();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        jd_reg_movimiento.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jd_reg_movimiento.setTitle("Guardar Movimiento");
        jd_reg_movimiento.setResizable(false);

        jToolBar2.setFloatable(false);
        jToolBar2.setOpaque(false);

        btn_j_guardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/accept.png"))); // NOI18N
        btn_j_guardar.setText("Guardar");
        btn_j_guardar.setEnabled(false);
        btn_j_guardar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn_j_guardar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btn_j_guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_j_guardarActionPerformed(evt);
            }
        });
        jToolBar2.add(btn_j_guardar);

        btn_j_salir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/cross.png"))); // NOI18N
        btn_j_salir.setText("Salir");
        btn_j_salir.setFocusable(false);
        btn_j_salir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn_j_salir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btn_j_salir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_j_salirActionPerformed(evt);
            }
        });
        jToolBar2.add(btn_j_salir);

        jLabel1.setText("Motivo:");

        txt_motivo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_motivoKeyPressed(evt);
            }
        });

        jLabel2.setText("Tipo Movimiento:");

        txt_monto.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_monto.setEnabled(false);
        txt_monto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_montoKeyTyped(evt);
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_montoKeyPressed(evt);
            }
        });

        jLabel3.setText("Monto:");

        cbx_tipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "INGRESA", "RETIRA" }));
        cbx_tipo.setEnabled(false);
        cbx_tipo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cbx_tipoKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jd_reg_movimientoLayout = new javax.swing.GroupLayout(jd_reg_movimiento.getContentPane());
        jd_reg_movimiento.getContentPane().setLayout(jd_reg_movimientoLayout);
        jd_reg_movimientoLayout.setHorizontalGroup(
            jd_reg_movimientoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jd_reg_movimientoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jd_reg_movimientoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_motivo)
                    .addGroup(jd_reg_movimientoLayout.createSequentialGroup()
                        .addGroup(jd_reg_movimientoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(txt_monto, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 256, Short.MAX_VALUE))
                    .addComponent(cbx_tipo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jd_reg_movimientoLayout.setVerticalGroup(
            jd_reg_movimientoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jd_reg_movimientoLayout.createSequentialGroup()
                .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_motivo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbx_tipo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_monto, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setTitle("Ver Movimientos Dinero");
        setToolTipText("");

        t_movimientos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(t_movimientos);

        jToolBar1.setFloatable(false);
        jToolBar1.setOpaque(false);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/add.png"))); // NOI18N
        jButton1.setText("Agregar");
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/banco.png"))); // NOI18N
        jButton3.setText("Deposito");
        jButton3.setFocusable(false);
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton3);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/cross.png"))); // NOI18N
        jButton2.setText("Salir");
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 513, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_j_salirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_j_salirActionPerformed
        jd_reg_movimiento.dispose();
    }//GEN-LAST:event_btn_j_salirActionPerformed

    private void txt_motivoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_motivoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String texto = txt_motivo.getText();
            if (texto.length() > 0) {
                cbx_tipo.setEnabled(true);
                cbx_tipo.requestFocus();
            } else {
                JOptionPane.showMessageDialog(null, "Falta Contenido");
            }
        }
    }//GEN-LAST:event_txt_motivoKeyPressed

    private void cbx_tipoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbx_tipoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txt_monto.setEnabled(true);
            txt_monto.requestFocus();
        }
    }//GEN-LAST:event_cbx_tipoKeyPressed

    private void txt_montoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_montoKeyTyped
        c_varios.solo_precio(evt);
    }//GEN-LAST:event_txt_montoKeyTyped

    private void txt_montoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_montoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String texto = txt_monto.getText();
            if (c_varios.esDecimal(texto)) {
                btn_j_guardar.setEnabled(true);
                btn_j_guardar.requestFocus();
            }
        }
    }//GEN-LAST:event_txt_montoKeyPressed

    private void btn_j_guardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_j_guardarActionPerformed
        c_movimiento.setFecha(c_varios.getFechaActual());
        c_movimiento.setId_almacen(id_almacen);
        c_movimiento.setId_usuario(id_usuario);
        c_movimiento.setMotivo(txt_motivo.getText());
        c_movimiento.obtener_codigo();
        int tipo = cbx_tipo.getSelectedIndex();
        double monto = Double.parseDouble(txt_monto.getText());
        if (tipo == 0) {
            c_movimiento.setIngresa(monto);
        } else {
            c_movimiento.setRetirar(monto);
        }

        boolean registrado = c_movimiento.insertar();
        if (registrado) {
            jd_reg_movimiento.dispose();
            String fecha = c_varios.getFechaActual();
            String query = "select mc.id_movimiento, mc.ingresa, mc.retira, mc.motivo, u.username "
                    + "from cajas_movimientos as mc "
                    + "inner join usuarios as u on u.id_usuarios = mc.id_usuarios "
                    + "where mc.fecha = '" + fecha + "' and mc.id_almacen = '" + id_almacen + "'";
            c_movimiento.mostrar(t_movimientos, query);
        }
    }//GEN-LAST:event_btn_j_guardarActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        jd_reg_movimiento.setModal(true);
        jd_reg_movimiento.setSize(400, 329);
        jd_reg_movimiento.setLocationRelativeTo(null);
        jd_reg_movimiento.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_j_guardar;
    private javax.swing.JButton btn_j_salir;
    private javax.swing.JComboBox<String> cbx_tipo;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JDialog jd_reg_movimiento;
    private javax.swing.JTable t_movimientos;
    private javax.swing.JTextField txt_monto;
    private javax.swing.JTextField txt_motivo;
    // End of variables declaration//GEN-END:variables
}