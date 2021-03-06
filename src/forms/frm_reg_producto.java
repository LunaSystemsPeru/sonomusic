/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forms;

import clases.cl_producto;
import clases.cl_productos_clasificacion;
import clases.cl_productos_empresa;
import clases.cl_proveedor;
import clases.cl_varios;
import clases_autocomplete.cla_empresa;
import clases_autocomplete.cla_producto_clasificacion;
import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import models.m_empresas;
import models.m_producto_clasificacion;

/**
 *
 * @author luis
 */
public class frm_reg_producto extends javax.swing.JDialog {

    public static cl_producto c_producto = new cl_producto();

    cl_varios c_varios = new cl_varios();

    //autollenado clasificacon
    m_producto_clasificacion m_clasificacion = new m_producto_clasificacion();
    m_empresas m_empresa = new m_empresas();

    cl_productos_clasificacion c_clasificacion;
    cl_productos_empresa c_producto_empresa;

    static DefaultTableModel detalle;
    static int fila_seleccionada;

    public static boolean registrar;

    /**
     * Creates new form frm_reg_producto
     */
    public frm_reg_producto(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        m_clasificacion.cbx_clasificaciones(cbx_clasificacion);
        m_empresa.cbx_empresas(cbx_empresa);

        if (registrar == false) {
            this.setTitle("Modificar Producto");
            cl_proveedor c_proveedor = new cl_proveedor();
            c_producto.validar_id();
            c_proveedor.setId_proveedor(c_producto.getId_proveedor());
            c_proveedor.cargar_datos();
            txt_descripcion.setText(c_producto.getDescripcion());
            txt_modelo.setText(c_producto.getModelo());
            txt_marca.setText(c_producto.getMarca());
            txt_comision.setText(c_varios.formato_numero(c_producto.getComision()));
            txt_costo.setText(c_varios.formato_numero(c_producto.getCosto()));
            txt_proveedor.setText(c_proveedor.getRuc() + " | " + c_proveedor.getRazon_social());

            c_clasificacion = new cl_productos_clasificacion();
            c_clasificacion.setId_clasificacion(c_producto.getId_clasificacion());
            c_clasificacion.obtener_datos();
            System.out.println(c_clasificacion.getDescripcion());
            cbx_clasificacion.setEnabled(true);
            cbx_clasificacion.getModel().setSelectedItem(new cla_producto_clasificacion(c_clasificacion.getId_clasificacion(), c_clasificacion.getDescripcion()));

            //llenar precios
            c_producto_empresa = new cl_productos_empresa();
            c_producto_empresa.setId_producto(c_producto.getId());
            c_producto_empresa.mostrar(t_precio);

            txt_modelo.setEnabled(true);
            txt_marca.setEnabled(true);
            txt_costo.setEnabled(true);
            txt_comision.setEnabled(true);
            btn_guardar.setEnabled(true);
        } else {
            formato_precio();
        }
    }

    private void formato_precio() {
        detalle = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        detalle.addColumn("Id");
        detalle.addColumn("Empresa");
        detalle.addColumn("Precio");
        t_precio.setModel(detalle);
        t_precio.getColumnModel().getColumn(0).setPreferredWidth(50);
        t_precio.getColumnModel().getColumn(1).setPreferredWidth(400);
        t_precio.getColumnModel().getColumn(2).setPreferredWidth(80);
        c_varios.centrar_celda(t_precio, 0);
        c_varios.derecha_celda(t_precio, 2);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jd_modificar_precio = new javax.swing.JDialog();
        jLabel5 = new javax.swing.JLabel();
        txt_nom_empresa = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txt_precio_nuevo = new javax.swing.JTextField();
        btn_guardar_precio = new javax.swing.JButton();
        jToolBar1 = new javax.swing.JToolBar();
        btn_guardar = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txt_descripcion = new javax.swing.JTextField();
        txt_modelo = new javax.swing.JTextField();
        txt_marca = new javax.swing.JTextField();
        txt_costo = new javax.swing.JTextField();
        cbx_clasificacion = new javax.swing.JComboBox<>();
        txt_proveedor = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txt_comision = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        cbx_empresa = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        txt_precio = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        t_precio = new javax.swing.JTable();
        btn_modificar = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();

        jd_modificar_precio.setTitle("Modificar Precio de Empresa seleccionada");

        jLabel5.setText("Empresa:");

        txt_nom_empresa.setEditable(false);
        txt_nom_empresa.setBackground(new java.awt.Color(255, 255, 255));

        jLabel6.setText("Precio.");

        txt_precio_nuevo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_precio_nuevoKeyPressed(evt);
            }
        });

        btn_guardar_precio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/accept.png"))); // NOI18N
        btn_guardar_precio.setText("Guardar");
        btn_guardar_precio.setEnabled(false);
        btn_guardar_precio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_guardar_precioActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jd_modificar_precioLayout = new javax.swing.GroupLayout(jd_modificar_precio.getContentPane());
        jd_modificar_precio.getContentPane().setLayout(jd_modificar_precioLayout);
        jd_modificar_precioLayout.setHorizontalGroup(
            jd_modificar_precioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jd_modificar_precioLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jd_modificar_precioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jd_modificar_precioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jd_modificar_precioLayout.createSequentialGroup()
                        .addComponent(txt_precio_nuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 170, Short.MAX_VALUE)
                        .addComponent(btn_guardar_precio))
                    .addComponent(txt_nom_empresa))
                .addContainerGap())
        );
        jd_modificar_precioLayout.setVerticalGroup(
            jd_modificar_precioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jd_modificar_precioLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jd_modificar_precioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_nom_empresa, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jd_modificar_precioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_precio_nuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_guardar_precio, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Registrar Producto");
        setResizable(false);

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

        jLabel1.setText("Descripcion");

        jLabel2.setText("Modelo:");

        jLabel3.setText("Marca:");

        jLabel4.setText("Costo:");

        jLabel8.setText("Clasificacion:");

        jLabel9.setText("Proveedor:");

        txt_descripcion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_descripcionKeyPressed(evt);
            }
        });

        txt_modelo.setEnabled(false);
        txt_modelo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_modeloKeyPressed(evt);
            }
        });

        txt_marca.setEnabled(false);
        txt_marca.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_marcaKeyPressed(evt);
            }
        });

        txt_costo.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_costo.setEnabled(false);
        txt_costo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_costoKeyTyped(evt);
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_costoKeyPressed(evt);
            }
        });

        cbx_clasificacion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbx_clasificacion.setEnabled(false);
        cbx_clasificacion.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbx_clasificacionItemStateChanged(evt);
            }
        });
        cbx_clasificacion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cbx_clasificacionKeyPressed(evt);
            }
        });

        txt_proveedor.setEnabled(false);

        jLabel10.setText("Comision:");

        txt_comision.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_comision.setEnabled(false);
        txt_comision.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_comisionKeyTyped(evt);
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_comisionKeyPressed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Precios por Empresa"));

        jLabel11.setText("Empresa:");

        cbx_empresa.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbx_empresa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cbx_empresaKeyPressed(evt);
            }
        });

        jLabel12.setText("Precio Venta:");

        txt_precio.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_precio.setText("0.00");
        txt_precio.setEnabled(false);
        txt_precio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_precioKeyPressed(evt);
            }
        });

        t_precio.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Id.", "Empresa", "Precio Vta."
            }
        ));
        t_precio.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                t_precioMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(t_precio);
        if (t_precio.getColumnModel().getColumnCount() > 0) {
            t_precio.getColumnModel().getColumn(0).setPreferredWidth(30);
            t_precio.getColumnModel().getColumn(1).setPreferredWidth(300);
            t_precio.getColumnModel().getColumn(2).setPreferredWidth(80);
        }

        btn_modificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/application_edit.png"))); // NOI18N
        btn_modificar.setText("Modificar Precio");
        btn_modificar.setEnabled(false);
        btn_modificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_modificarActionPerformed(evt);
            }
        });

        jLabel7.setText("Enter: para agregar precio");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbx_empresa, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txt_precio, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btn_modificar)))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbx_empresa, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_precio, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_modificar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

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
                            .addComponent(jLabel8)
                            .addComponent(jLabel9)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_descripcion)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_modelo, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_costo, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(56, 56, 56)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel10))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_marca)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(txt_comision, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))))
                            .addComponent(txt_proveedor)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(cbx_clasificacion, javax.swing.GroupLayout.PREFERRED_SIZE, 406, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 183, Short.MAX_VALUE))))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_descripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_modelo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_marca, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txt_comision, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txt_costo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbx_clasificacion, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_proveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void btn_guardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_guardarActionPerformed
        c_producto.setDescripcion(txt_descripcion.getText());
        c_producto.setModelo(txt_modelo.getText());
        c_producto.setMarca(txt_marca.getText());
        c_producto.setPrecio(Double.parseDouble(txt_precio.getText()));
        c_producto.setCosto(Double.parseDouble(txt_costo.getText()));
        c_producto.setComision(Double.parseDouble(txt_comision.getText()));
        cla_producto_clasificacion cla_clasificacion = (cla_producto_clasificacion) cbx_clasificacion.getSelectedItem();
        c_producto.setId_clasificacion(cla_clasificacion.getId_clasificacion());

        int total_filas = t_precio.getRowCount();
        boolean realizado = false;

        if (total_filas == 2) {
            if (registrar) {
                c_producto.obtener_codigo();
                realizado = c_producto.registrar();

                c_producto_empresa = new cl_productos_empresa();
                //llenar precios de empresa

                for (int i = 0; i < total_filas; i++) {
                    int id_empresa = Integer.parseInt(t_precio.getValueAt(i, 0).toString());
                    Double precio = Double.parseDouble(t_precio.getValueAt(i, 2).toString());
                    c_producto_empresa.setId_producto(c_producto.getId());
                    c_producto_empresa.setId_empresa(id_empresa);
                    c_producto_empresa.setPrecio(precio);
                    c_producto_empresa.insertar();
                }

                JOptionPane.showMessageDialog(null, "<html>el codigo del producto es: <h1>" + c_producto.getId() + "</h1></html>");
            } else {
                realizado = c_producto.modificar();
            }
        } else {
            JOptionPane.showMessageDialog(null, "FALTAN ESTABLACER LOS PRECIOS");
        }

        if (realizado) {
            this.dispose();
        }
    }//GEN-LAST:event_btn_guardarActionPerformed

    private void txt_descripcionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_descripcionKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String texto = txt_descripcion.getText();
            if (texto.length() > 0) {
                txt_modelo.setEnabled(true);
                txt_modelo.requestFocus();
            }
        }
    }//GEN-LAST:event_txt_descripcionKeyPressed

    private void txt_modeloKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_modeloKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String texto = txt_modelo.getText();
            if (texto.length() > 0) {
                txt_marca.setEnabled(true);
                txt_marca.requestFocus();
            }
        }
    }//GEN-LAST:event_txt_modeloKeyPressed

    private void txt_marcaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_marcaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String texto = txt_marca.getText();
            if (texto.length() > 0) {
                txt_costo.setEnabled(true);
                txt_costo.requestFocus();
            }
        }
    }//GEN-LAST:event_txt_marcaKeyPressed

    private void txt_comisionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_comisionKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String texto = txt_comision.getText();
            if (c_varios.esDecimal(texto)) {
                cbx_clasificacion.setEnabled(true);
                cbx_clasificacion.requestFocus();
            }
        }
    }//GEN-LAST:event_txt_comisionKeyPressed

    private void txt_costoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_costoKeyTyped
        c_varios.solo_precio(evt);
        c_varios.limitar_caracteres(evt, txt_costo, 15);
    }//GEN-LAST:event_txt_costoKeyTyped

    private void txt_costoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_costoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String texto = txt_costo.getText();
            if (c_varios.esDecimal(texto)) {
                txt_comision.setEnabled(true);
                txt_comision.requestFocus();
            }
        }
    }//GEN-LAST:event_txt_costoKeyPressed

    private void txt_comisionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_comisionKeyTyped
        c_varios.solo_precio(evt);
        c_varios.limitar_caracteres(evt, txt_comision, 5);
    }//GEN-LAST:event_txt_comisionKeyTyped

    private void cbx_clasificacionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbx_clasificacionKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cbx_empresa.setEnabled(true);
            cbx_empresa.requestFocus();
            // btn_guardar.setEnabled(true);
            // btn_guardar.requestFocus();
        }
    }//GEN-LAST:event_cbx_clasificacionKeyPressed

    private void cbx_empresaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbx_empresaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txt_precio.setEnabled(true);
            txt_precio.selectAll();
            txt_precio.requestFocus();
        }
    }//GEN-LAST:event_cbx_empresaKeyPressed

    private void agregar_precio() {
        cla_empresa cla_empresa = (cla_empresa) cbx_empresa.getSelectedItem();
        Object fila_precio[] = new Object[3];
        fila_precio[0] = cla_empresa.getId_empresa();
        fila_precio[1] = cla_empresa.getRazon_social();
        fila_precio[2] = txt_precio.getText();

        detalle.addRow(fila_precio);
        txt_precio.setText("0.00");
        txt_precio.setEnabled(false);
        btn_guardar.setEnabled(true);
        cbx_empresa.requestFocus();
    }

    private void txt_precioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_precioKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String texto = txt_precio.getText();
            if (c_varios.esDecimal(texto)) {
                if (Double.parseDouble(texto) > 0) {
                    if (registrar) {
                        agregar_precio();
                    } else {
                        cla_empresa cla_empresa = (cla_empresa) cbx_empresa.getSelectedItem();
                        c_producto_empresa = new cl_productos_empresa();
                        c_producto_empresa.setId_producto(c_producto.getId());
                        c_producto_empresa.setId_empresa(cla_empresa.getId_empresa());
                        c_producto_empresa.setPrecio(Double.parseDouble(texto));
                        c_producto_empresa.insertar();
                        c_producto_empresa.mostrar(t_precio);
                        txt_precio.setText("0.00");
                        txt_precio.setEnabled(false);
                        btn_guardar.setEnabled(true);
                        cbx_empresa.requestFocus();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "INGRESE UN PRECIO CORRECTO");
                    txt_precio.selectAll();
                    txt_precio.requestFocus();
                }
            }
        }
    }//GEN-LAST:event_txt_precioKeyPressed

    private void cbx_clasificacionItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbx_clasificacionItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_cbx_clasificacionItemStateChanged

    private void t_precioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_t_precioMouseClicked
        if (t_precio.getRowCount() > 0) {
            fila_seleccionada = t_precio.getSelectedRow();
            btn_modificar.setEnabled(true);
        }
    }//GEN-LAST:event_t_precioMouseClicked

    private void btn_modificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_modificarActionPerformed
        //abrir modal para modificar precio
        //mostrar empresa
        int id_empresa = Integer.parseInt(t_precio.getValueAt(fila_seleccionada, 0).toString());
        String nombre_empresa = t_precio.getValueAt(fila_seleccionada, 1).toString();
        String precio = t_precio.getValueAt(fila_seleccionada, 2).toString();

        txt_nom_empresa.setText(nombre_empresa);
        txt_precio_nuevo.setText(precio);
        txt_precio.requestFocus();

        jd_modificar_precio.setModal(true);
        jd_modificar_precio.setSize(458, 142);
        jd_modificar_precio.setLocationRelativeTo(null);
        jd_modificar_precio.setVisible(true);
    }//GEN-LAST:event_btn_modificarActionPerformed

    private void txt_precio_nuevoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_precio_nuevoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String texto = txt_precio_nuevo.getText();
            if (c_varios.esDecimal(texto)) {
                if (Double.parseDouble(texto) > 0) {
                    btn_guardar_precio.setEnabled(true);
                    btn_guardar_precio.requestFocus();
                } else {
                    JOptionPane.showMessageDialog(null, "NO HA INGRESADO UN PRECIO CORRECTO");
                    txt_precio_nuevo.selectAll();
                    txt_precio_nuevo.requestFocus();
                }
            }
        }
    }//GEN-LAST:event_txt_precio_nuevoKeyPressed

    private void btn_guardar_precioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_guardar_precioActionPerformed
        int id_empresa = Integer.parseInt(t_precio.getValueAt(fila_seleccionada, 0).toString());
        String texto = txt_precio_nuevo.getText();
        if (registrar) {
            t_precio.setValueAt(texto, fila_seleccionada, 2);
        } else {
            //actualizar precio anterior 
            c_producto_empresa = new cl_productos_empresa();
            c_producto_empresa.setId_producto(c_producto.getId());
            c_producto_empresa.setId_empresa(id_empresa);
            c_producto_empresa.setPrecio(Double.parseDouble(texto));
            c_producto_empresa.actualizar_precio();
            c_producto_empresa.mostrar(t_precio);
        }
        jd_modificar_precio.setVisible(false);
        jd_modificar_precio.dispose();
    }//GEN-LAST:event_btn_guardar_precioActionPerformed

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
            java.util.logging.Logger.getLogger(frm_reg_producto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frm_reg_producto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frm_reg_producto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frm_reg_producto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                frm_reg_producto dialog = new frm_reg_producto(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btn_guardar_precio;
    private javax.swing.JButton btn_modificar;
    private javax.swing.JComboBox<String> cbx_clasificacion;
    private javax.swing.JComboBox<String> cbx_empresa;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JDialog jd_modificar_precio;
    private javax.swing.JTable t_precio;
    private javax.swing.JTextField txt_comision;
    private javax.swing.JTextField txt_costo;
    private javax.swing.JTextField txt_descripcion;
    private javax.swing.JTextField txt_marca;
    private javax.swing.JTextField txt_modelo;
    private javax.swing.JTextField txt_nom_empresa;
    private javax.swing.JTextField txt_precio;
    private javax.swing.JTextField txt_precio_nuevo;
    private javax.swing.JTextField txt_proveedor;
    // End of variables declaration//GEN-END:variables
}
