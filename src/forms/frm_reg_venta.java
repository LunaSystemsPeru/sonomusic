/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forms;

import clases.cl_cliente;
import clases.cl_cobros_ventas;
import clases.cl_conectar;
import clases.cl_documentos_almacen;
import clases.cl_guia_remision;
import clases.cl_producto;
import clases.cl_productos_almacen;
import clases.cl_productos_empresa;
import clases.cl_productos_ventas;
import clases.cl_ubigeo;
import clases.cl_varios;
import clases.cl_venta;
import clases.cl_venta_cupon;
import clases_autocomplete.cla_cliente;
import clases_autocomplete.cla_mis_documentos;
import clases_autocomplete.cla_producto;
import clases_autocomplete.cla_ubigeo;
import clases_hilos.cl_enviar_venta;
import com.mxrck.autocompleter.AutoCompleterCallback;
import com.mxrck.autocompleter.TextAutoCompleter;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import json.cl_json_entidad;
import models.m_mis_documentos;
import models.m_ubigeo;
import org.json.simple.parser.ParseException;
import sonomusic.frm_principal;
import vistas.frm_ver_ubicacion_producto;

/**
 *
 * @author luis
 */
public class frm_reg_venta extends javax.swing.JInternalFrame {

    cl_conectar c_conectar = new cl_conectar();
    cl_varios c_varios = new cl_varios();

    cl_cliente c_cliente = new cl_cliente();
    cl_producto c_producto = new cl_producto();
    cl_productos_almacen c_producto_almacen = new cl_productos_almacen();
    cl_documentos_almacen c_doc_almacen = new cl_documentos_almacen();
    cl_documentos_almacen c_doc_guia;
    cl_productos_empresa c_producto_empresa = new cl_productos_empresa();

    m_mis_documentos m_t_documentos = new m_mis_documentos();
    m_ubigeo m_ubigeo = new m_ubigeo();

    cl_ubigeo c_ubigeo;
    cl_guia_remision c_guia;

    cl_venta c_venta = new cl_venta();
    cl_productos_ventas c_detalle = new cl_productos_ventas();
    cl_cobros_ventas c_cobro = new cl_cobros_ventas();

    cl_venta_cupon c_cupon = new cl_venta_cupon();

    static DefaultTableModel detalle;

    TextAutoCompleter tac_productos = null;
    TextAutoCompleter tac_clientes = null;

    double final_efectivo = 0;
    double final_tarjeta = 0;
    double final_total = 0;
    double final_vale = 0;
    int final_estado = 1;

    int id_empresa = frm_principal.c_empresa.getId();
    int id_almacen = frm_principal.c_almacen.getId();
    int id_usuario = frm_principal.c_usuario.getId_usuario();

    int fila_seleccionada = -1;

    /**
     * Creates new form frm_reg_venta
     */
    public frm_reg_venta() {
        initComponents();
        txt_fecha.setText(c_varios.fecha_usuario(c_varios.getFechaActual()));
        c_producto_almacen.setAlmacen(id_almacen);
        c_producto_empresa.setId_empresa(id_empresa);
        cargar_productos();
        modelo_venta();

        m_t_documentos.cbx_documentos_venta(cbx_tipo_doc);
    }

    private void modelo_venta() {
        //formato de tabla detalle de venta
        detalle = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        detalle.addColumn("Id");
        detalle.addColumn("Descripcion");
        detalle.addColumn("Cant.");
        detalle.addColumn("Precio");
        detalle.addColumn("Parcial");
        t_detalle.setModel(detalle);
        t_detalle.getColumnModel().getColumn(0).setPreferredWidth(20);
        t_detalle.getColumnModel().getColumn(1).setPreferredWidth(400);
        t_detalle.getColumnModel().getColumn(2).setPreferredWidth(50);
        t_detalle.getColumnModel().getColumn(3).setPreferredWidth(50);
        t_detalle.getColumnModel().getColumn(4).setPreferredWidth(70);
        c_varios.centrar_celda(t_detalle, 0);
        c_varios.derecha_celda(t_detalle, 2);
        c_varios.derecha_celda(t_detalle, 3);
        c_varios.derecha_celda(t_detalle, 4);
    }

    private double calcular_total() {
        double total = 0;
        int contar_filas = t_detalle.getRowCount();
        for (int i = 0; i < contar_filas; i++) {
            total = total + Double.parseDouble(t_detalle.getValueAt(i, 4).toString());
        }
        lbl_total_venta.setText("S/ " + c_varios.formato_totales(total));
        lbl_pago_venta.setText("S/ " + c_varios.formato_totales(total));
        return total;
    }

    private void calcular_vuelto() {
        double efectivo = Double.parseDouble(txt_j_efectivo.getText());
        double tarjeta = Double.parseDouble(txt_j_tarjeta.getText());
        double cupon = Double.parseDouble(txt_j_cupon.getText());
        double total = calcular_total();
        double suma_pago = efectivo + tarjeta + cupon;

        final_efectivo = efectivo;
        final_tarjeta = tarjeta;
        final_vale = cupon;
        final_estado = 1;

        double vuelto = 0;
        double faltante = 0;

        if (suma_pago > total) {
            if (efectivo > total) {
                final_efectivo = total;
            }
            if (tarjeta > total) {
                final_tarjeta = total;
            }
            if (cupon > total) {
                final_vale = total;
            }

            vuelto = suma_pago - total;
            lbl_alerta.setText("Alerta: --");
        }
        if (total > suma_pago) {
            final_estado = 2;
            faltante = total - suma_pago;
            lbl_alerta.setText("Alerta: El monto es menor al total. Vta. al credito");
        }

        txt_j_vuelto.setText(c_varios.formato_numero(vuelto));
        txt_j_faltante.setText(c_varios.formato_numero(faltante));
        txt_j_pagado.setText(c_varios.formato_numero(suma_pago));

    }

    private void cargar_productos() {
        try {
            if (tac_productos != null) {
                tac_productos.removeAllItems();
            }
            tac_productos = new TextAutoCompleter(txt_buscar_producto, new AutoCompleterCallback() {
                @Override
                public void callback(Object selectedItem) {
                    Object itemSelected = selectedItem;
                    c_producto.setId(0);
                    c_producto_almacen.setProducto(0);
                    if (itemSelected instanceof cla_producto) {
                        int pcodigo = ((cla_producto) itemSelected).getId_producto();
                        String pnombre = ((cla_producto) itemSelected).getDescripcion();
                        System.out.println("producto seleccionado " + pnombre);
                        c_producto.setId(pcodigo);
                        c_producto_almacen.setProducto(pcodigo);
                        c_producto_empresa.setId_producto(pcodigo);
                    } else {
                        System.out.println("El item es de un tipo desconocido");
                    }
                }
            });

            tac_productos.setMode(0);
            tac_productos.setCaseSensitive(false);
            Statement st = c_conectar.conexion();
            String sql = "select p.descripcion, pa.cactual, pe.precio, p.id_producto, p.marca, p.modelo "
                    + "from productos as p "
                    + "inner join productos_almacen as pa on pa.id_producto = p.id_producto "
                    + "inner join almacen as al on al.id_almacen = pa.id_almacen "
                    + "inner join productos_empresa as pe on pe.id_empresa = al.id_empresa and pe.id_producto = pa.id_producto "
                    + "where pa.id_almacen = '" + id_almacen + "' and pa.cactual > 0";
            ResultSet rs = c_conectar.consulta(st, sql);
            while (rs.next()) {
                int id_producto = rs.getInt("id_producto");
                String descripcion = rs.getString("descripcion") + " | " + rs.getString("marca") + " | " + rs.getString("modelo")
                        + "    |    Cant: " + rs.getInt("cactual") + "    |    Precio: S/ " + c_varios.formato_numero(rs.getDouble("precio"));
                tac_productos.addItem(new cla_producto(id_producto, descripcion));
            }
            c_conectar.cerrar(rs);
            c_conectar.cerrar(st);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error " + e.getLocalizedMessage());
            System.out.println(e.getLocalizedMessage());
        }
    }

    private boolean valida_tabla(int producto) {
        //estado de ingreso
        boolean ingresar = false;
        int cuenta_iguales = 0;

        //verificar fila no se repite
        int contar_filas = t_detalle.getRowCount();
        if (contar_filas == 0) {
            ingresar = true;
        } else {
            for (int j = 0; j < contar_filas; j++) {
                int id_producto_fila = Integer.parseInt(t_detalle.getValueAt(j, 0).toString());
                if (producto == id_producto_fila) {
                    cuenta_iguales++;
                    JOptionPane.showMessageDialog(null, "El Producto a Ingresar ya existe en la lista");
                }
            }

            if (cuenta_iguales == 0) {
                ingresar = true;
            }
        }

        return ingresar;
    }

    private void cargar_clientes(int tipo) {
        try {
            if (tac_clientes != null) {
                tac_clientes.removeAllItems();
            }
            //TIPO:
            //1 para separacion
            // 2 para facturas
            // 3 para boleta
            JTextField text_box = txt_nom_cliente;
            String sql = "";
            if (tipo == 1) {
                txt_nom_cliente.setEnabled(true);
                txt_doc_cliente.setEnabled(false);
                txt_nom_cliente.requestFocus();
                text_box = txt_nom_cliente;
                sql = "select id_cliente, documento, nombre "
                        + "from clientes "
                        + "where LENGTH(documento) != 11 ";
            }

            if (tipo == 2) {
                txt_nom_cliente.setEnabled(false);
                txt_doc_cliente.setEnabled(true);
                txt_doc_cliente.requestFocus();
                text_box = txt_doc_cliente;
                sql = "select id_cliente, documento, nombre "
                        + "from clientes "
                        + "where LENGTH(documento) = 11 ";
            }

            if (tipo == 3) {
                txt_nom_cliente.setEnabled(true);
                txt_doc_cliente.setEnabled(false);
                text_box = txt_nom_cliente;
                sql = "select id_cliente, documento, nombre "
                        + "from clientes "
                        + "where LENGTH(documento) != 11 ";
                txt_nom_cliente.requestFocus();
            }

            tac_clientes = new TextAutoCompleter(text_box, new AutoCompleterCallback() {
                @Override
                public void callback(Object selectedItem) {
                    Object itemSelected = selectedItem;
                    c_cliente.setCodigo(0);
                    if (itemSelected instanceof cla_cliente) {
                        int ccodigo = ((cla_cliente) itemSelected).getId_cliente();
                        String cnombre = ((cla_cliente) itemSelected).getNombre();
                        System.out.println("cliente seleccionado " + cnombre);
                        c_cliente.setCodigo(ccodigo);
                    } else {
                        System.out.println("El item es de un tipo desconocido");
                    }
                }
            });

            tac_clientes.setMode(0);
            tac_clientes.setCaseSensitive(false);
            Statement st = c_conectar.conexion();
            ResultSet rs = c_conectar.consulta(st, sql);
            System.out.println(sql);
            while (rs.next()) {
                int id_cliente = rs.getInt("id_cliente");
                String descripcion = "";
                if (tipo == 3) {
                    descripcion = rs.getString("nombre") + " | " + rs.getString("documento");
                }
                if (tipo == 2) {
                    descripcion = rs.getString("documento");
                }
                tac_clientes.addItem(new cla_cliente(id_cliente, descripcion));
            }
            c_conectar.cerrar(rs);
            c_conectar.cerrar(st);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error " + e.getLocalizedMessage());
            System.out.println(e.getLocalizedMessage());
        }

        if (tipo == 3) {
            txt_nom_cliente.requestFocus();
        }
        if (tipo == 2) {
            txt_doc_cliente.requestFocus();
        }
        if (tipo == 1) {
            txt_nom_cliente.requestFocus();
        }
    }

    private void limpiar_buscar() {
        btn_ver_tiendas.setEnabled(false);
        txt_buscar_producto.setText("");
        txt_cantidad.setText("");
        txt_cactual.setText("");
        txt_precio.setText("");
        txt_cantidad.setEnabled(false);
        txt_cactual.setEnabled(false);
        txt_precio.setEnabled(false);
        txt_buscar_producto.requestFocus();
    }

    private void limpiar_cliente() {
        btn_crear_cliente.setEnabled(true);
        txt_doc_cliente.setText("");
        txt_nom_cliente.setText("");
        txt_dir_cliente.setText("");
        txt_nom_cliente.setEnabled(true);
        txt_nom_cliente.requestFocus();
    }

    private void reinicia_cliente() {
        btn_actualizar.setEnabled(false);
        btn_crear_cliente.setEnabled(false);
        txt_doc_cliente.setText("");
        txt_nom_cliente.setText("");
        txt_dir_cliente.setText("");
        txt_nom_cliente.setEnabled(false);
    }

    private void llenar_venta() {
        c_venta.setId_almacen(id_almacen);
        c_venta.setId_usuario(id_usuario);
        c_venta.setId_venta(c_venta.obtener_codigo());
        c_venta.setId_cliente(c_cliente.getCodigo());
        //c_venta.setFecha(c_varios.getFechaActual());
        c_venta.setFecha(c_varios.fecha_myql(txt_fecha.getText()));
        c_venta.setId_tido(c_doc_almacen.getId_tido());
        c_venta.setSerie(c_doc_almacen.getSerie());
        c_venta.setNumero(c_doc_almacen.getNumero());
        c_venta.setTotal(final_total);
        c_venta.setId_tipo_venta(cbx_tipo_venta.getSelectedIndex() + 1);
        c_venta.setPagado(final_efectivo + final_tarjeta);
        c_venta.setEstado(1);
        c_venta.setEnviado_sunat(0);
    }

    private void llenar_cobros() {
        double total = final_total;
        double efectivo = final_efectivo;
        double tarjeta = final_tarjeta;
        double vale = final_vale;

        c_cobro.setId_venta(c_venta.getId_venta());
        c_cobro.setId_almacen(c_venta.getId_almacen());
        c_cobro.setFecha(c_venta.getFecha());

        if (efectivo > 0) {
            c_cobro.setId_cobro(c_cobro.obtener_codigo());
            if (efectivo > total) {
                c_cobro.setMonto(total);
            } else {
                c_cobro.setMonto(efectivo);
            }
            c_cobro.setTipo_pago(1);
            c_cobro.registrar();
        }

        if (tarjeta > 0) {
            c_cobro.setId_cobro(c_cobro.obtener_codigo());
            c_cobro.setMonto(tarjeta);
            c_cobro.setTipo_pago(2);
            c_cobro.registrar();
        }

        if (vale > 0) {
            c_cobro.setId_cobro(c_cobro.obtener_codigo());
            c_cobro.setMonto(vale);
            c_cobro.setTipo_pago(3);
            c_cobro.registrar();

            c_cupon.setUsado(vale);
            c_cupon.actualizar_cupon();
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

        jd_fin_venta = new javax.swing.JDialog();
        jPanel5 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        lbl_pago_venta = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        txt_j_efectivo = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txt_j_tarjeta = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txt_j_cupon = new javax.swing.JTextField();
        btn_bus_cupon = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        txt_j_faltante = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        txt_j_vuelto = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        txt_j_pagado = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        btn_pago = new javax.swing.JButton();
        lbl_alerta = new javax.swing.JLabel();
        jd_modificar_item = new javax.swing.JDialog();
        jPanel7 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        txt_jd_idproducto = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        txt_jd_descripcion = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        txt_jd_cantidad = new javax.swing.JTextField();
        txt_jd_precio = new javax.swing.JTextField();
        btn_jd_actualizar = new javax.swing.JButton();
        btn_jd_eliminar = new javax.swing.JButton();
        jd_guia_remision = new javax.swing.JDialog();
        jPanel8 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        txt_llegada_guia = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        cbx_departamento = new javax.swing.JComboBox<>();
        cbx_provincia = new javax.swing.JComboBox<>();
        cbx_distrito = new javax.swing.JComboBox<>();
        lbl_ubigeo = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        cbx_tipo_transporte = new javax.swing.JComboBox<>();
        jLabel27 = new javax.swing.JLabel();
        txt_ruc_transportista = new javax.swing.JTextField();
        txt_razon_transportista = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        txt_dni_chofer = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        txt_placa_vehiculo = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txt_buscar_producto = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txt_cantidad = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txt_precio = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txt_cactual = new javax.swing.JTextField();
        btn_ver_tiendas = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txt_fecha = new javax.swing.JFormattedTextField();
        cbx_tipo_venta = new javax.swing.JComboBox<>();
        cbx_tipo_doc = new javax.swing.JComboBox<>();
        txt_serie = new javax.swing.JTextField();
        txt_numero = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txt_doc_cliente = new javax.swing.JTextField();
        txt_nom_cliente = new javax.swing.JTextField();
        txt_dir_cliente = new javax.swing.JTextField();
        btn_crear_cliente = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        lbl_total_venta = new javax.swing.JLabel();
        btn_grabar = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        btn_actualizar = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        cbx_guia = new javax.swing.JComboBox<>();
        txt_direccion_guia = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        btn_editar_guiar = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        t_detalle = new javax.swing.JTable();
        jLabel23 = new javax.swing.JLabel();
        lbl_ayuda = new javax.swing.JLabel();

        jd_fin_venta.setTitle("Grabar Venta");

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Total Venta:");

        lbl_pago_venta.setFont(new java.awt.Font("Dialog", 1, 36)); // NOI18N
        lbl_pago_venta.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_pago_venta.setText("S/ 2,500.00");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lbl_pago_venta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_pago_venta, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel12.setText("Efectivo:");

        txt_j_efectivo.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_j_efectivo.setText("0.00");
        txt_j_efectivo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_j_efectivoKeyPressed(evt);
            }
        });

        jLabel13.setText("Tarjeta:");

        txt_j_tarjeta.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_j_tarjeta.setText("0.00");
        txt_j_tarjeta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_j_tarjetaKeyPressed(evt);
            }
        });

        jLabel14.setText("Cupon:");

        txt_j_cupon.setEditable(false);
        txt_j_cupon.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_j_cupon.setText("0.00");

        btn_bus_cupon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/magnifier.png"))); // NOI18N
        btn_bus_cupon.setToolTipText("Buscar Cupones Activos");
        btn_bus_cupon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_bus_cuponActionPerformed(evt);
            }
        });

        jLabel15.setText("Faltante:");

        txt_j_faltante.setEditable(false);
        txt_j_faltante.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_j_faltante.setText("0.00");
        txt_j_faltante.setFocusable(false);

        jLabel18.setText("Vuelto");

        txt_j_vuelto.setEditable(false);
        txt_j_vuelto.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_j_vuelto.setText("0.00");
        txt_j_vuelto.setFocusable(false);

        jLabel16.setText("Suma Pagos:");

        txt_j_pagado.setEditable(false);
        txt_j_pagado.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_j_pagado.setText("0.00");
        txt_j_pagado.setFocusable(false);

        jLabel19.setText("jLabel19");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txt_j_tarjeta, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                            .addComponent(txt_j_efectivo, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                            .addComponent(txt_j_cupon))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_bus_cupon, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txt_j_vuelto)
                            .addComponent(txt_j_faltante)
                            .addComponent(txt_j_pagado, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 496, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_j_efectivo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_j_pagado, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_j_tarjeta, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_j_faltante, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(btn_bus_cupon, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_j_vuelto, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_j_cupon, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel19)
                .addContainerGap())
        );

        btn_pago.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/add.png"))); // NOI18N
        btn_pago.setText("Agregar Pago");
        btn_pago.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_pagoActionPerformed(evt);
            }
        });

        lbl_alerta.setForeground(java.awt.Color.red);
        lbl_alerta.setText("Alerta: ---");

        javax.swing.GroupLayout jd_fin_ventaLayout = new javax.swing.GroupLayout(jd_fin_venta.getContentPane());
        jd_fin_venta.getContentPane().setLayout(jd_fin_ventaLayout);
        jd_fin_ventaLayout.setHorizontalGroup(
            jd_fin_ventaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jd_fin_ventaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jd_fin_ventaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jd_fin_ventaLayout.createSequentialGroup()
                        .addComponent(lbl_alerta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_pago)))
                .addContainerGap())
        );
        jd_fin_ventaLayout.setVerticalGroup(
            jd_fin_ventaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jd_fin_ventaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jd_fin_ventaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_pago, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_alerta))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jd_modificar_item.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jd_modificar_item.setTitle("Actualizar Item");

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Modificar ITem"));

        jLabel17.setText("Codigo:");

        txt_jd_idproducto.setEnabled(false);

        jLabel20.setText("Descripcion:");

        txt_jd_descripcion.setEnabled(false);

        jLabel21.setText("Cantidad:");

        jLabel22.setText("Precio:");

        txt_jd_cantidad.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_jd_cantidad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_jd_cantidadKeyTyped(evt);
            }
        });

        txt_jd_precio.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_jd_precio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_jd_precioKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_jd_idproducto, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(txt_jd_cantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 164, Short.MAX_VALUE)
                                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_jd_precio, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(170, 170, 170))
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(txt_jd_descripcion)
                                .addContainerGap())))))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_jd_idproducto, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_jd_descripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_jd_cantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_jd_precio, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        btn_jd_actualizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/application_edit.png"))); // NOI18N
        btn_jd_actualizar.setText("Actualizar");
        btn_jd_actualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_jd_actualizarActionPerformed(evt);
            }
        });

        btn_jd_eliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/cross.png"))); // NOI18N
        btn_jd_eliminar.setText("Elimintar Item");
        btn_jd_eliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_jd_eliminarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jd_modificar_itemLayout = new javax.swing.GroupLayout(jd_modificar_item.getContentPane());
        jd_modificar_item.getContentPane().setLayout(jd_modificar_itemLayout);
        jd_modificar_itemLayout.setHorizontalGroup(
            jd_modificar_itemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jd_modificar_itemLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jd_modificar_itemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jd_modificar_itemLayout.createSequentialGroup()
                        .addComponent(btn_jd_eliminar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_jd_actualizar)))
                .addContainerGap())
        );
        jd_modificar_itemLayout.setVerticalGroup(
            jd_modificar_itemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jd_modificar_itemLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jd_modificar_itemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_jd_actualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_jd_eliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jd_guia_remision.setTitle("Datos para la Guia de Remision");

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del Destinatario"));

        jLabel25.setText("Direccion Llegada:");

        txt_llegada_guia.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_llegada_guiaKeyPressed(evt);
            }
        });

        jLabel26.setText("Ubigeo:");

        cbx_departamento.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "---" }));
        cbx_departamento.setEnabled(false);
        cbx_departamento.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cbx_departamentoKeyPressed(evt);
            }
        });

        cbx_provincia.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SELEC. DEPARTAMENTO" }));
        cbx_provincia.setEnabled(false);
        cbx_provincia.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cbx_provinciaKeyPressed(evt);
            }
        });

        cbx_distrito.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SELEC. PROVINCIA" }));
        cbx_distrito.setEnabled(false);
        cbx_distrito.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cbx_distritoKeyPressed(evt);
            }
        });

        lbl_ubigeo.setText("000000");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel25)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel26)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_ubigeo)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_llegada_guia)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(cbx_departamento, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbx_provincia, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbx_distrito, 0, 156, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_llegada_guia, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(cbx_departamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbx_provincia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbx_distrito, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_ubigeo))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del Transportista"));

        jLabel28.setText("Tipo de Transporte:");

        cbx_tipo_transporte.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "T. PRIVADO", "T. PUBLICO" }));
        cbx_tipo_transporte.setEnabled(false);
        cbx_tipo_transporte.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cbx_tipo_transporteKeyPressed(evt);
            }
        });

        jLabel27.setText("Transportista:");

        txt_ruc_transportista.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_ruc_transportista.setEnabled(false);

        txt_razon_transportista.setEnabled(false);

        jLabel29.setText("DNI Chofer:");

        txt_dni_chofer.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_dni_chofer.setEnabled(false);
        txt_dni_chofer.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_dni_choferKeyTyped(evt);
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_dni_choferKeyPressed(evt);
            }
        });

        jLabel30.setText("Placa Vehiculo:");

        txt_placa_vehiculo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_placa_vehiculo.setEnabled(false);
        txt_placa_vehiculo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_placa_vehiculoKeyTyped(evt);
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_placa_vehiculoKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel28)
                    .addComponent(jLabel27)
                    .addComponent(jLabel29))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbx_tipo_transporte, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txt_dni_chofer)
                            .addComponent(txt_ruc_transportista, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE))
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_razon_transportista))
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addComponent(jLabel30)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txt_placa_vehiculo, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(104, 104, 104)))))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbx_tipo_transporte, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_ruc_transportista, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_razon_transportista, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_dni_chofer, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_placa_vehiculo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jd_guia_remisionLayout = new javax.swing.GroupLayout(jd_guia_remision.getContentPane());
        jd_guia_remision.getContentPane().setLayout(jd_guia_remisionLayout);
        jd_guia_remisionLayout.setHorizontalGroup(
            jd_guia_remisionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jd_guia_remisionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jd_guia_remisionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jd_guia_remisionLayout.setVerticalGroup(
            jd_guia_remisionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jd_guia_remisionLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        setClosable(true);
        setTitle("Reg. Documento Venta");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Agregar Productos"));

        jLabel1.setText("Buscar:");

        txt_buscar_producto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txt_buscar_productoFocusGained(evt);
            }
        });
        txt_buscar_producto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_buscar_productoKeyPressed(evt);
            }
        });

        jLabel2.setText("Cantidad:");

        txt_cantidad.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_cantidad.setEnabled(false);
        txt_cantidad.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txt_cantidadFocusGained(evt);
            }
        });
        txt_cantidad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_cantidadKeyTyped(evt);
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_cantidadKeyPressed(evt);
            }
        });

        jLabel3.setText("Precio:");

        txt_precio.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_precio.setEnabled(false);
        txt_precio.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txt_precioFocusGained(evt);
            }
        });
        txt_precio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_precioKeyTyped(evt);
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_precioKeyPressed(evt);
            }
        });

        jLabel10.setText("Cant. Actual:");

        txt_cactual.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_cactual.setEnabled(false);

        btn_ver_tiendas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/magnifier.png"))); // NOI18N
        btn_ver_tiendas.setText("Ver en Otras tiendas");
        btn_ver_tiendas.setEnabled(false);
        btn_ver_tiendas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ver_tiendasActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_buscar_producto))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_cantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_cactual, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_precio, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_ver_tiendas)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_buscar_producto, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_cantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_precio, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_cactual, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_ver_tiendas, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos Generales Venta"));

        jLabel4.setText("Fecha:");

        jLabel5.setText("Tipo Venta:");

        jLabel6.setText("Tipo Documento:");

        try {
            txt_fecha.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txt_fecha.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_fecha.setText("10/10/2018");

        cbx_tipo_venta.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "VENTA", "SEPARACION" }));
        cbx_tipo_venta.setEnabled(false);
        cbx_tipo_venta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cbx_tipo_ventaKeyPressed(evt);
            }
        });

        cbx_tipo_doc.setEnabled(false);
        cbx_tipo_doc.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cbx_tipo_docFocusGained(evt);
            }
        });
        cbx_tipo_doc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cbx_tipo_docKeyPressed(evt);
            }
        });

        txt_serie.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_serie.setEnabled(false);

        txt_numero.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_numero.setEnabled(false);

        jLabel7.setText("Cliente:");

        txt_doc_cliente.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_doc_cliente.setText("0");
        txt_doc_cliente.setEnabled(false);
        txt_doc_cliente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txt_doc_clienteFocusGained(evt);
            }
        });
        txt_doc_cliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_doc_clienteKeyPressed(evt);
            }
        });

        txt_nom_cliente.setEnabled(false);
        txt_nom_cliente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txt_nom_clienteFocusGained(evt);
            }
        });
        txt_nom_cliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_nom_clienteKeyPressed(evt);
            }
        });

        txt_dir_cliente.setEnabled(false);

        btn_crear_cliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/add.png"))); // NOI18N
        btn_crear_cliente.setToolTipText("Agregar Cliente");
        btn_crear_cliente.setEnabled(false);
        btn_crear_cliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_crear_clienteActionPerformed(evt);
            }
        });

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Total a Pagar:");

        lbl_total_venta.setFont(new java.awt.Font("Dialog", 1, 36)); // NOI18N
        lbl_total_venta.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_total_venta.setText("S/ 0.00");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbl_total_venta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_total_venta, javax.swing.GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE)
                .addContainerGap())
        );

        btn_grabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/accept.png"))); // NOI18N
        btn_grabar.setText("Grabar");
        btn_grabar.setEnabled(false);
        btn_grabar.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                btn_grabarFocusGained(evt);
            }
        });
        btn_grabar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_grabarActionPerformed(evt);
            }
        });

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/cross.png"))); // NOI18N
        jButton3.setText("Cancelar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        btn_actualizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/reaload_16.png"))); // NOI18N
        btn_actualizar.setToolTipText("Actualizar Lista de Clientes");
        btn_actualizar.setEnabled(false);
        btn_actualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_actualizarActionPerformed(evt);
            }
        });

        jLabel11.setText("Desea Guia de Remision:");

        cbx_guia.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Si, Emitir", "No, alli nomas" }));
        cbx_guia.setSelectedIndex(1);
        cbx_guia.setEnabled(false);
        cbx_guia.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cbx_guiaFocusGained(evt);
            }
        });
        cbx_guia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbx_guiaActionPerformed(evt);
            }
        });
        cbx_guia.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cbx_guiaKeyPressed(evt);
            }
        });

        txt_direccion_guia.setEnabled(false);
        txt_direccion_guia.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txt_direccion_guiaFocusGained(evt);
            }
        });
        txt_direccion_guia.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_direccion_guiaKeyPressed(evt);
            }
        });

        jLabel24.setText("Direccion de Entrega:");

        btn_editar_guiar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/application_edit.png"))); // NOI18N
        btn_editar_guiar.setText(" Editar Informacion");
        btn_editar_guiar.setEnabled(false);
        btn_editar_guiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_editar_guiarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btn_grabar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_direccion_guia, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton3))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbx_guia, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txt_dir_cliente, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_nom_cliente, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(33, 33, 33))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel7)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btn_crear_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btn_actualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                        .addComponent(txt_serie)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txt_numero, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(cbx_tipo_doc, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cbx_tipo_venta, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txt_fecha)
                                    .addComponent(txt_doc_cliente))))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel24)
                        .addGap(36, 36, 36)
                        .addComponent(btn_editar_guiar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_fecha, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbx_tipo_venta, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbx_tipo_doc, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_serie, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_numero, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txt_doc_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btn_crear_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_nom_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_dir_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbx_guia, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(btn_actualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_editar_guiar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_direccion_guia, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_grabar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Ver Productos en Venta"));

        t_detalle.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1520", "GUITARRA ACUSTICA VOZZEX", "1", "152.00", "152.00"}
            },
            new String [] {
                "ID.", "Descripcion", "Cant.", "Precio", "Parcial"
            }
        ));
        t_detalle.setShowVerticalLines(false);
        t_detalle.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                t_detalleMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(t_detalle);
        if (t_detalle.getColumnModel().getColumnCount() > 0) {
            t_detalle.getColumnModel().getColumn(0).setPreferredWidth(50);
            t_detalle.getColumnModel().getColumn(1).setPreferredWidth(550);
            t_detalle.getColumnModel().getColumn(2).setPreferredWidth(50);
            t_detalle.getColumnModel().getColumn(3).setPreferredWidth(80);
            t_detalle.getColumnModel().getColumn(4).setPreferredWidth(80);
        }

        jLabel23.setText("Ayuda:");

        lbl_ayuda.setText("jLabel24");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel23)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_ayuda, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 407, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_ayuda, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void txt_buscar_productoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_buscar_productoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (txt_buscar_producto.getText().length() > 25) {
                if (c_producto_almacen.validar_id()) {
                    //validar que no existe en la tabla
                    if (valida_tabla(c_producto.getId())) {
                        c_producto.validar_id();
                        c_producto_empresa.obtener_datos();
                        btn_ver_tiendas.setEnabled(true);
                        txt_precio.setText(c_varios.formato_numero(c_producto_empresa.getPrecio()));
                        txt_cactual.setText(c_producto_almacen.getCantidad() + "");
                        txt_cantidad.setText("1");
                        txt_cantidad.setEnabled(true);
                        txt_cantidad.selectAll();
                        txt_cantidad.requestFocus();
                    } else {
                        c_producto.setId(0);
                        c_producto_empresa.setId_empresa(0);
                        c_producto_almacen.setProducto(0);
                        limpiar_buscar();
                        JOptionPane.showMessageDialog(null, "ESTE PRODUCTO YA ESTA SELECCIONADO");
                    }
                } else {
                    c_producto.setId(0);
                    c_producto_empresa.setId_empresa(0);
                    c_producto_almacen.setProducto(0);
                    limpiar_buscar();
                    JOptionPane.showMessageDialog(null, "ERROR AL SELECCIONAR PRODUCTO");
                }
            }

            if (txt_buscar_producto.getText().length() == 0) {
                //si nro de filas es mayor a 0 entonces ir a datos generales
                int contar_filas = t_detalle.getRowCount();
                if (contar_filas > 0) {
                    cbx_tipo_venta.setEnabled(true);
                    cbx_tipo_venta.requestFocus();
                }
            }
        }
    }//GEN-LAST:event_txt_buscar_productoKeyPressed

    private void txt_cantidadKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_cantidadKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String tcantidad = txt_cantidad.getText();
            if (c_varios.esEntero(tcantidad)) {
                txt_precio.setEnabled(true);
                txt_precio.selectAll();
                txt_precio.requestFocus();
            }
        }
    }//GEN-LAST:event_txt_cantidadKeyPressed

    private void txt_precioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_precioKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            boolean error = false;
            String tprecio = txt_precio.getText();
            double precio = 0;
            int cantidad = 0;
            if (!c_varios.esDecimal(tprecio)) {
                error = true;
            } else {
                precio = Double.parseDouble(tprecio);
            }

            //validar cantidad
            String tcantidad = txt_cantidad.getText();
            if (!c_varios.esEntero(tcantidad)) {
                error = true;
            } else {
                cantidad = Integer.parseInt(tcantidad);
                if (cantidad <= 0) {
                    JOptionPane.showMessageDialog(null, "NO PUEDE SER CERO (0)");
                    error = true;
                }
            }

            int cactual = Integer.parseInt(txt_cactual.getText());
            if (cactual <= 0) {
                JOptionPane.showMessageDialog(null, "ERROR NO HAY STOCK PARA ESTE PRODUCTO");
                error = true;
            }

            double parcial = precio * cantidad;

            //formar objeto y agregar para tabla
            if (!error) {
                Object fila[] = new Object[5];
                fila[0] = c_producto.getId();
                fila[1] = c_producto.getDescripcion() + " | " + c_producto.getMarca() + " | " + c_producto.getModelo();
                fila[2] = cantidad;
                fila[3] = c_varios.formato_numero(precio);
                fila[4] = c_varios.formato_numero(parcial);

                detalle.addRow(fila);
                calcular_total();
                limpiar_buscar();
            } else {
                JOptionPane.showMessageDialog(null, "ERROR CON EL PRECIO O LA CANTIDAD");
            }
        }

        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            limpiar_buscar();
        }
    }//GEN-LAST:event_txt_precioKeyPressed

    private void cbx_tipo_ventaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbx_tipo_ventaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            c_venta.setId_tipo_venta(cbx_tipo_venta.getSelectedIndex() + 1);

            //si es venta seleccionar tipo documento
            if (c_venta.getId_tipo_venta() == 1) {
                reinicia_cliente();
                cbx_tipo_doc.setEnabled(true);
                cbx_tipo_doc.requestFocus();
            }

            //si es separacion saltar a cliente y cargar datos de nota de venta
            if (c_venta.getId_tipo_venta() == 2) {
                //bloquer tipo documento
                cbx_tipo_doc.setEnabled(false);

                //cargar datos de nota de venta
                c_doc_almacen.setId_tido(7);
                c_doc_almacen.setId_almacen(id_almacen);
                c_doc_almacen.comprobar_documento();

                txt_serie.setText(c_doc_almacen.getSerie());
                txt_numero.setText(c_doc_almacen.getNumero() + "");

                //ir a nombre cliente
                limpiar_cliente();
                cargar_clientes(3);
            }
        }
    }//GEN-LAST:event_cbx_tipo_ventaKeyPressed

    private void cbx_tipo_docKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbx_tipo_docKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cla_mis_documentos cla_tido = (cla_mis_documentos) cbx_tipo_doc.getSelectedItem();
            int id_tido = cla_tido.getId_tido();

            //cargar datos de nota de venta
            c_doc_almacen.setId_tido(id_tido);
            c_doc_almacen.setId_almacen(id_almacen);
            c_doc_almacen.comprobar_documento();

            txt_serie.setText(c_doc_almacen.getSerie());
            txt_numero.setText(c_doc_almacen.getNumero() + "");

            //si es nota de venta salta a grabar
            if (id_tido == 6) {
                c_cliente.setCodigo(0);
                c_cliente.comprobar_cliente();
                txt_doc_cliente.setEnabled(false);
                txt_nom_cliente.setEnabled(false);
                txt_doc_cliente.setText(c_cliente.getDocumento());
                txt_nom_cliente.setText(c_cliente.getNombre());
                txt_dir_cliente.setText(c_cliente.getDireccion());
                btn_grabar.setEnabled(true);
                btn_grabar.requestFocus();
            }
            //si es boleta o factura salta a cliente
            if (id_tido == 1 || id_tido == 2) {
                if (id_tido == 1) {
                    //ir a nombre cliente
                    limpiar_cliente();
                    cargar_clientes(3);
                }

                if (id_tido == 2) {
                    //ir a ruc cliente
                    limpiar_cliente();
                    cargar_clientes(2);

                }
            }

            if (id_tido == 7) {
                JOptionPane.showMessageDialog(null, "NO SE PUEDE SELECCIONAR ESTE DOCUMENTO");
            }
        }
    }//GEN-LAST:event_cbx_tipo_docKeyPressed

    private void txt_nom_clienteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_nom_clienteKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (txt_nom_cliente.getText().length() > 15) {
                //validar cliente
                if (c_cliente.comprobar_cliente()) {
                    txt_doc_cliente.setText(c_cliente.getDocumento());
                    txt_dir_cliente.setText(c_cliente.getDireccion());
                    btn_grabar.setEnabled(true);
                    btn_grabar.requestFocus();
                } else {
                    limpiar_cliente();
                    JOptionPane.showMessageDialog(null, "CLIENTE NO SELECCIONADO \nSELECCIONE CON ENTER");
                }
            }
        }
    }//GEN-LAST:event_txt_nom_clienteKeyPressed

    private void txt_doc_clienteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_doc_clienteKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (txt_doc_cliente.getText().length() == 11) {
                //validar cliente
                c_cliente.setCodigo(0);
                c_cliente.setDocumento(txt_doc_cliente.getText());
                if (c_cliente.comprobar_cliente_doc()) {
                    c_cliente.comprobar_cliente();
                    txt_nom_cliente.setText(c_cliente.getNombre());
                    txt_dir_cliente.setText(c_cliente.getDireccion());
                    cbx_guia.setEnabled(true);
                    cbx_guia.requestFocus();
                    /*
                    btn_grabar.setEnabled(true);
                    btn_grabar.requestFocus();
                     */
                } else {
                    JOptionPane.showMessageDialog(this, "CLIENTE NO EXISTE");
                }
            }
        }
    }//GEN-LAST:event_txt_doc_clienteKeyPressed

    private void btn_crear_clienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_crear_clienteActionPerformed
        btn_actualizar.setEnabled(true);
        Frame f = JOptionPane.getRootFrame();
        frm_reg_cliente dialog = new frm_reg_cliente(f, true);
        frm_reg_cliente.accion = "registrar";
        frm_reg_cliente.origen = "reg_venta";
        frm_reg_cliente.c_cliente.setCodigo(0);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }//GEN-LAST:event_btn_crear_clienteActionPerformed

    private void btn_grabarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_grabarActionPerformed
        //btn_pago.setEnabled(false);

        jd_fin_venta.setModal(true);
        jd_fin_venta.setSize(580, 302);
        jd_fin_venta.setLocationRelativeTo(null);

        final_total = calcular_total();
        txt_j_efectivo.selectAll();
        txt_j_efectivo.requestFocus();

        jd_fin_venta.setVisible(true);
    }//GEN-LAST:event_btn_grabarActionPerformed

    private void txt_j_efectivoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_j_efectivoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String texto = txt_j_efectivo.getText();
            if (texto.length() > 0 && c_varios.esDecimal(texto)) {
                calcular_vuelto();
                double monto = Double.parseDouble(texto);
                if (monto >= final_total) {
                    btn_pago.setEnabled(true);
                    btn_pago.requestFocus();
                } else {
                    txt_j_tarjeta.selectAll();
                    txt_j_tarjeta.requestFocus();
                }
            } else {
                JOptionPane.showMessageDialog(null, "ERROR CON EL MONTO");
            }
        }
    }//GEN-LAST:event_txt_j_efectivoKeyPressed

    private void txt_j_tarjetaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_j_tarjetaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String texto = txt_j_tarjeta.getText();
            if (texto.length() > 0 && c_varios.esDecimal(texto)) {
                calcular_vuelto();
                // btn_bus_cupon.requestFocus();
                btn_pago.requestFocus();
            } else {
                JOptionPane.showMessageDialog(null, "ERROR CON EL MONTO");
            }
        }
    }//GEN-LAST:event_txt_j_tarjetaKeyPressed

    private void btn_pagoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_pagoActionPerformed
        btn_pago.setEnabled(false);

        if (final_total > 0) {
            btn_pago.setEnabled(false);
            llenar_venta();
            if (c_venta.registrar()) {
                int contar_tabla = t_detalle.getRowCount();
                for (int i = 0; i < contar_tabla; i++) {
                    c_detalle.setId_almacen(id_almacen);
                    c_detalle.setId_venta(c_venta.getId_venta());
                    c_detalle.setId_producto(Integer.parseInt(t_detalle.getValueAt(i, 0).toString()));
                    c_detalle.setCantidad(Integer.parseInt(t_detalle.getValueAt(i, 2).toString()));
                    c_producto.setId(c_detalle.getId_producto());
                    c_producto.validar_id();
                    c_detalle.setCosto(c_producto.getCosto());
                    c_detalle.setPrecio(Double.parseDouble(t_detalle.getValueAt(i, 3).toString()));

                    c_detalle.registrar();
                }

                llenar_cobros();

                //generar txt 
                if (c_venta.getId_tipo_venta() == 1) {
                    if (c_venta.getId_tido() == 1 || c_venta.getId_tido() == 2) {

                        int emite_guia = cbx_guia.getSelectedIndex();

                        if (emite_guia == 0) {
                            c_guia = new cl_guia_remision();
                            c_guia.setId_almacen(c_venta.getId_almacen());
                            c_guia.setId_venta(c_venta.getId_venta());
                            c_guia.setId_empresa(id_empresa);
                            c_guia.setLlegada(txt_llegada_guia.getText().toUpperCase());
                            c_guia.setUbigeo(lbl_ubigeo.getText());
                            c_guia.setRuc_transporte(txt_ruc_transportista.getText());
                            c_guia.setRazon_transporte(txt_razon_transportista.getText());
                            c_guia.setDni_chofer(txt_dni_chofer.getText());
                            c_guia.setPlaca(txt_placa_vehiculo.getText());

                            c_doc_guia = new cl_documentos_almacen();
                            c_doc_guia.setId_tido(5);
                            c_doc_guia.setId_almacen(c_venta.getId_almacen());
                            c_doc_guia.comprobar_documento();

                            c_guia.setSerie(c_doc_guia.getSerie());
                            c_guia.setNumero(c_doc_guia.getNumero());

                            c_guia.registrar();

                        }

                        try {
                            //Ponemos a "Dormir" el programa durante los ms que queremos
                            Thread.sleep(2 * 1000);
                            System.out.println("durmiendo app x 2 segundos");
                        } catch (InterruptedException e) {
                            System.out.println(e);
                        }

                        cl_enviar_venta c_enviar = new cl_enviar_venta();
                        c_enviar.setGuia(emite_guia);
                        c_enviar.setId_venta(c_venta.getId_venta());
                        c_enviar.setId_almacen(id_almacen);
                        c_enviar.start();

                    }
                }

                if (c_venta.getId_tipo_venta() == 2) {

                    /*si es separacion no imprime, se desabilita porque 
                    * decidieron imprimir al entrega los productos
                    * se genera una boleta o factura.
                     */
 /*
                    Print_Separacion_Ticket ticket = new Print_Separacion_Ticket();
                    ticket.setId_almacen(c_venta.getId_almacen());
                    ticket.setId_venta(c_venta.getId_venta());

                    ticket.generar_ticket();

                    jd_fin_venta.dispose();
                    this.dispose();

                    frm_reg_venta reg_venta = new frm_reg_venta();
                    c_varios.llamar_ventana(reg_venta);*/
                }

                jd_fin_venta.dispose();
                this.dispose();

                frm_reg_venta reg_venta = new frm_reg_venta();
                c_varios.llamar_ventana(reg_venta);

            }
        } else {
            JOptionPane.showMessageDialog(null, "NO ES UNA VENTA VALIDA, MONTO TOTAL MENOR O IGUAL A CERO (0)");
        }
    }//GEN-LAST:event_btn_pagoActionPerformed

    private void dormir_programa() {
        try {
            //Ponemos a "Dormir" el programa durante los ms que queremos
            Thread.sleep(15 * 1000);
        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }
    private void btn_ver_tiendasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ver_tiendasActionPerformed
        Frame f = JOptionPane.getRootFrame();
        frm_ver_ubicacion_producto.id_producto = c_producto.getId();
        frm_ver_ubicacion_producto dialog = new frm_ver_ubicacion_producto(f, true);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }//GEN-LAST:event_btn_ver_tiendasActionPerformed

    private void t_detalleMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_t_detalleMouseClicked
        int nro_filas = t_detalle.getRowCount();
        if (nro_filas > 0) {
            fila_seleccionada = t_detalle.getSelectedRow();
            String id_producto = t_detalle.getValueAt(fila_seleccionada, 0).toString();
            String descripcion = t_detalle.getValueAt(fila_seleccionada, 1).toString();
            int cantidad = Integer.parseInt(t_detalle.getValueAt(fila_seleccionada, 2).toString());
            double precio = Double.parseDouble(t_detalle.getValueAt(fila_seleccionada, 3).toString());

            jd_modificar_item.setModal(true);
            jd_modificar_item.setSize(881, 260);
            jd_modificar_item.setLocationRelativeTo(null);

            //cargar datos
            txt_jd_idproducto.setText(id_producto);
            txt_jd_descripcion.setText(descripcion);
            txt_jd_cantidad.setText(cantidad + "");
            txt_jd_precio.setText(c_varios.formato_numero(precio));

            jd_modificar_item.setVisible(true);

            txt_jd_cantidad.requestFocus();
        }
    }//GEN-LAST:event_t_detalleMouseClicked

    private void btn_jd_actualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_jd_actualizarActionPerformed
        String tcantidad = txt_jd_cantidad.getText();
        String tprecio = txt_jd_precio.getText();

        int suma_error = 0;

        if (!c_varios.esEntero(tcantidad) && !tcantidad.isEmpty()) {
            suma_error++;
            JOptionPane.showMessageDialog(null, "ERROR AL INGRESAR CANTIDAD");
        }

        if (!c_varios.esDecimal(tprecio) && !tprecio.isEmpty()) {
            suma_error++;
            JOptionPane.showMessageDialog(null, "ERROR AL MODIFICAR PRECIO");
        }

        if (suma_error == 0) {
            int cantidad = Integer.parseInt(tcantidad);
            double precio = Double.parseDouble(tprecio);
            double parcial = cantidad * precio;
            t_detalle.setValueAt(cantidad, fila_seleccionada, 2);
            t_detalle.setValueAt(c_varios.formato_numero(precio), fila_seleccionada, 3);
            t_detalle.setValueAt(c_varios.formato_numero(parcial), fila_seleccionada, 4);

            calcular_total();
            jd_modificar_item.dispose();
            txt_buscar_producto.requestFocus();

        }
    }//GEN-LAST:event_btn_jd_actualizarActionPerformed

    private void txt_jd_cantidadKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_jd_cantidadKeyTyped
        c_varios.solo_numeros(evt);
    }//GEN-LAST:event_txt_jd_cantidadKeyTyped

    private void txt_jd_precioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_jd_precioKeyTyped
        c_varios.solo_precio(evt);
    }//GEN-LAST:event_txt_jd_precioKeyTyped

    private void btn_actualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_actualizarActionPerformed

        int tipo_venta = cbx_tipo_venta.getSelectedIndex() + 1;
        cla_mis_documentos cla_tido = (cla_mis_documentos) cbx_tipo_doc.getSelectedItem();
        int id_tido = cla_tido.getId_tido();
        if (tipo_venta == 1) {
            if (id_tido == 1) {
                //ir a nombre cliente
                limpiar_cliente();
                cargar_clientes(3);
            }

            if (id_tido == 2) {
                //ir a ruc cliente
                limpiar_cliente();
                cargar_clientes(2);

            }
        }
        if (tipo_venta == 2) {
            //ir a nombre cliente
            limpiar_cliente();
            cargar_clientes(3);
        }
    }//GEN-LAST:event_btn_actualizarActionPerformed

    private void txt_buscar_productoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_buscar_productoFocusGained
        lbl_ayuda.setText("ESCRIBIR PARA MOSTRAR PRODUCTOS        ENTER: PARA LLENAR DATOS DEL CLIENTE");
    }//GEN-LAST:event_txt_buscar_productoFocusGained

    private void txt_cantidadKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_cantidadKeyTyped
        c_varios.solo_numeros(evt);
    }//GEN-LAST:event_txt_cantidadKeyTyped

    private void txt_cantidadFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_cantidadFocusGained
        lbl_ayuda.setText("ESCRIBIR CANTIDAD        ENTER: PARA MODIFICAR PRECIO");
    }//GEN-LAST:event_txt_cantidadFocusGained

    private void txt_precioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_precioKeyTyped
        c_varios.solo_precio(evt);
    }//GEN-LAST:event_txt_precioKeyTyped

    private void txt_precioFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_precioFocusGained
        lbl_ayuda.setText("MODIFICAR CANTIDAD        ENTER: PARA GUARDAR EN DETALLE");
    }//GEN-LAST:event_txt_precioFocusGained

    private void btn_jd_eliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_jd_eliminarActionPerformed
        detalle.removeRow(fila_seleccionada);
        calcular_total();
        jd_modificar_item.dispose();
        txt_buscar_producto.requestFocus();
    }//GEN-LAST:event_btn_jd_eliminarActionPerformed

    private void btn_bus_cuponActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_bus_cuponActionPerformed
        c_cupon.setId_almacen(id_almacen);
        boolean hay_cupon = c_cupon.validar_cliente(c_cliente.getCodigo());
        if (hay_cupon) {
            c_cupon.validar_cupon();
            double restante_cupon = c_cupon.getMonto() - c_cupon.getUsado();
            txt_j_cupon.setText(c_varios.formato_numero(restante_cupon));

            calcular_vuelto();
            double monto = restante_cupon;
            if (monto >= final_total) {
                JOptionPane.showMessageDialog(null, "SE ENCONTRO UN PAGO GUARDADO");
                btn_pago.setEnabled(true);
                btn_pago.requestFocus();
            } else {
                txt_j_efectivo.selectAll();
                txt_j_efectivo.requestFocus();
            }
        } else {
            JOptionPane.showMessageDialog(null, "NO SE ENCONTRO CUPON ASOCIADO AL CLIENTE");
            txt_j_efectivo.selectAll();
            txt_j_efectivo.requestFocus();
        }
    }//GEN-LAST:event_btn_bus_cuponActionPerformed

    private void txt_direccion_guiaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_direccion_guiaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            //si esta seleccionado guia
            if (cbx_guia.getSelectedIndex() == 0) {
                //si texto es mayor a vacio
                String texto = txt_direccion_guia.getText();
                if (texto.length() > 10) {
                    btn_grabar.setEnabled(true);
                    btn_grabar.requestFocus();
                } else {
                    JOptionPane.showMessageDialog(null, "Falta Ingresar Direccion de Entrega");
                    txt_direccion_guia.selectAll();
                    txt_direccion_guia.requestFocus();
                }
            } else {
                JOptionPane.showMessageDialog(null, "ERROR!!!, Usted ha seleccionado no emitir guia");
                cbx_guia.requestFocus();
            }
        }
    }//GEN-LAST:event_txt_direccion_guiaKeyPressed

    private void cbx_guiaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbx_guiaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            int guia_seleccionada = cbx_guia.getSelectedIndex();
            if (guia_seleccionada == 0) {
                btn_grabar.setEnabled(false);

                jd_guia_remision.setModal(true);
                jd_guia_remision.setSize(667, 307);
                jd_guia_remision.setLocationRelativeTo(null);
                txt_direccion_guia.setText(txt_dir_cliente.getText());
                txt_llegada_guia.setText(txt_dir_cliente.getText());
                m_ubigeo.cbx_departamentos(cbx_departamento);
                c_ubigeo = new cl_ubigeo();
                jd_guia_remision.setVisible(true);

            } else {
                btn_editar_guiar.setEnabled(false);
                txt_direccion_guia.setText("");
                btn_grabar.setEnabled(true);
                btn_grabar.requestFocus();
            }
        }
    }//GEN-LAST:event_cbx_guiaKeyPressed

    private void txt_direccion_guiaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_direccion_guiaFocusGained
        lbl_ayuda.setText("ESCRIBIR DIRECCION DE ENTREGA DE LA VENTA - CLIENTE ESPECIFICARA, ENTER PARA FINALIZAR");
    }//GEN-LAST:event_txt_direccion_guiaFocusGained

    private void cbx_guiaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cbx_guiaFocusGained
        lbl_ayuda.setText("ENTER PARA SELECCIONAR SI DESEA GUIA DE REMISION");
    }//GEN-LAST:event_cbx_guiaFocusGained

    private void btn_grabarFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_btn_grabarFocusGained
        lbl_ayuda.setText("ENTER PARA COBRAR VENTA");
    }//GEN-LAST:event_btn_grabarFocusGained

    private void cbx_tipo_docFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cbx_tipo_docFocusGained
        lbl_ayuda.setText("ENTER PARA SELECCIONAR CLIENTE");
    }//GEN-LAST:event_cbx_tipo_docFocusGained

    private void txt_nom_clienteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_nom_clienteFocusGained
        lbl_ayuda.setText("BUSCAR CLIENTE POR DNI O APELLIDOS, SI NO EXISTE CLIC EN '+' ");
    }//GEN-LAST:event_txt_nom_clienteFocusGained

    private void txt_doc_clienteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_doc_clienteFocusGained
        lbl_ayuda.setText("ESCRIBIR NUMERO DE RUC PARA BUSCAR CLIENTE, SI NO EXISTE CLIC EN '+'");
    }//GEN-LAST:event_txt_doc_clienteFocusGained

    private void txt_llegada_guiaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_llegada_guiaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (txt_llegada_guia.getText().length() > 10) {
                cbx_departamento.setEnabled(true);
                cbx_departamento.requestFocus();
            }
        }
    }//GEN-LAST:event_txt_llegada_guiaKeyPressed

    private void cbx_departamentoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbx_departamentoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cla_ubigeo ca_ubigeo = (cla_ubigeo) cbx_departamento.getSelectedItem();
            c_ubigeo.setId_ubigeo(ca_ubigeo.getId());
            c_ubigeo.cargar_datos();
            lbl_ubigeo.setText(c_ubigeo.getCod_departamento() + c_ubigeo.getCod_provincia() + c_ubigeo.getCod_distrito());
            m_ubigeo.cbx_provincias(cbx_provincia, c_ubigeo.getCod_departamento());
            cbx_provincia.setEnabled(true);
            cbx_provincia.requestFocus();
        }
    }//GEN-LAST:event_cbx_departamentoKeyPressed

    private void cbx_distritoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbx_distritoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cla_ubigeo ca_ubigeo = (cla_ubigeo) cbx_distrito.getSelectedItem();
            c_ubigeo.setId_ubigeo(ca_ubigeo.getId());
            c_ubigeo.cargar_datos();
            lbl_ubigeo.setText(c_ubigeo.getCod_departamento() + c_ubigeo.getCod_provincia() + c_ubigeo.getCod_distrito());
            cbx_tipo_transporte.setEnabled(true);
            cbx_tipo_transporte.requestFocus();
        }
    }//GEN-LAST:event_cbx_distritoKeyPressed

    private void cbx_provinciaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbx_provinciaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cla_ubigeo ca_ubigeo = (cla_ubigeo) cbx_provincia.getSelectedItem();
            c_ubigeo.setId_ubigeo(ca_ubigeo.getId());
            c_ubigeo.cargar_datos();
            lbl_ubigeo.setText(c_ubigeo.getCod_departamento() + c_ubigeo.getCod_provincia() + c_ubigeo.getCod_distrito());
            m_ubigeo.cbx_distritos(cbx_distrito, c_ubigeo.getCod_departamento(), c_ubigeo.getCod_provincia());
            cbx_distrito.setEnabled(true);
            cbx_distrito.requestFocus();
        }
    }//GEN-LAST:event_cbx_provinciaKeyPressed

    private void cbx_tipo_transporteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbx_tipo_transporteKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            int tipo_transporte = cbx_tipo_transporte.getSelectedIndex();
            if (tipo_transporte == 0) {
                txt_ruc_transportista.setText(txt_doc_cliente.getText());
                txt_razon_transportista.setText(txt_nom_cliente.getText());
                txt_dni_chofer.setEnabled(true);
                txt_dni_chofer.requestFocus();
            }

            if (tipo_transporte == 1) {
                txt_ruc_transportista.setText("");
                txt_razon_transportista.setText("");
                txt_ruc_transportista.setEnabled(true);
                txt_ruc_transportista.requestFocus();
            }
        }
    }//GEN-LAST:event_cbx_tipo_transporteKeyPressed

    private void txt_dni_choferKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_dni_choferKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String dni = txt_dni_chofer.getText();
            if (dni.length() == 8) {
                try {
                    String json = cl_json_entidad.getJSONDNI_LUNASYSTEMS(dni);
                    //Lo mostramos
                    String datos = cl_json_entidad.showJSONDNIL(json);
                    JOptionPane.showMessageDialog(null, "DATOS ENCONTRADOS: " + datos);
                } catch (ParseException e) {
                    JOptionPane.showMessageDialog(null, "ERROR EN BUSCAR DNI " + e.getLocalizedMessage());
                }
                txt_placa_vehiculo.setEnabled(true);
                txt_placa_vehiculo.requestFocus();
            } else {
                JOptionPane.showMessageDialog(null, "DEBE COLOCAR UN NUMERO DE DNI CORRECTO");
                txt_dni_chofer.requestFocus();
            }
        }
    }//GEN-LAST:event_txt_dni_choferKeyPressed

    private void cbx_guiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbx_guiaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbx_guiaActionPerformed

    private void txt_placa_vehiculoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_placa_vehiculoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (txt_placa_vehiculo.getText().length() > 6) {
                txt_direccion_guia.setText(txt_llegada_guia.getText());
                btn_editar_guiar.setEnabled(true);
                btn_grabar.setEnabled(true);
                btn_grabar.requestFocus();
                jd_guia_remision.setVisible(false);
            }
        }
    }//GEN-LAST:event_txt_placa_vehiculoKeyPressed

    private void btn_editar_guiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_editar_guiarActionPerformed
        jd_guia_remision.setModal(true);
        jd_guia_remision.setSize(667, 307);
        jd_guia_remision.setLocationRelativeTo(null);
        c_ubigeo = new cl_ubigeo();
        jd_guia_remision.setVisible(true);
    }//GEN-LAST:event_btn_editar_guiarActionPerformed

    private void txt_dni_choferKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_dni_choferKeyTyped
        c_varios.limitar_caracteres(evt, txt_dni_chofer, 8);
        c_varios.solo_numeros(evt);
    }//GEN-LAST:event_txt_dni_choferKeyTyped

    private void txt_placa_vehiculoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_placa_vehiculoKeyTyped
        c_varios.limitar_caracteres(evt, txt_placa_vehiculo, 8);
    }//GEN-LAST:event_txt_placa_vehiculoKeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JButton btn_actualizar;
    private javax.swing.JButton btn_bus_cupon;
    private javax.swing.JButton btn_crear_cliente;
    private javax.swing.JButton btn_editar_guiar;
    private javax.swing.JButton btn_grabar;
    private javax.swing.JButton btn_jd_actualizar;
    private javax.swing.JButton btn_jd_eliminar;
    private javax.swing.JButton btn_pago;
    private javax.swing.JButton btn_ver_tiendas;
    private javax.swing.JComboBox<String> cbx_departamento;
    private javax.swing.JComboBox<String> cbx_distrito;
    private javax.swing.JComboBox<String> cbx_guia;
    private javax.swing.JComboBox<String> cbx_provincia;
    private javax.swing.JComboBox<String> cbx_tipo_doc;
    private javax.swing.JComboBox<String> cbx_tipo_transporte;
    private javax.swing.JComboBox<String> cbx_tipo_venta;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JDialog jd_fin_venta;
    private javax.swing.JDialog jd_guia_remision;
    private javax.swing.JDialog jd_modificar_item;
    private javax.swing.JLabel lbl_alerta;
    private javax.swing.JLabel lbl_ayuda;
    private javax.swing.JLabel lbl_pago_venta;
    private javax.swing.JLabel lbl_total_venta;
    private javax.swing.JLabel lbl_ubigeo;
    private javax.swing.JTable t_detalle;
    private javax.swing.JTextField txt_buscar_producto;
    private javax.swing.JTextField txt_cactual;
    private javax.swing.JTextField txt_cantidad;
    private javax.swing.JTextField txt_dir_cliente;
    private javax.swing.JTextField txt_direccion_guia;
    private javax.swing.JTextField txt_dni_chofer;
    private javax.swing.JTextField txt_doc_cliente;
    private javax.swing.JFormattedTextField txt_fecha;
    private javax.swing.JTextField txt_j_cupon;
    private javax.swing.JTextField txt_j_efectivo;
    private javax.swing.JTextField txt_j_faltante;
    private javax.swing.JTextField txt_j_pagado;
    private javax.swing.JTextField txt_j_tarjeta;
    private javax.swing.JTextField txt_j_vuelto;
    private javax.swing.JTextField txt_jd_cantidad;
    private javax.swing.JTextField txt_jd_descripcion;
    private javax.swing.JTextField txt_jd_idproducto;
    private javax.swing.JTextField txt_jd_precio;
    private javax.swing.JTextField txt_llegada_guia;
    private javax.swing.JTextField txt_nom_cliente;
    private javax.swing.JTextField txt_numero;
    private javax.swing.JTextField txt_placa_vehiculo;
    private javax.swing.JTextField txt_precio;
    private javax.swing.JTextField txt_razon_transportista;
    private javax.swing.JTextField txt_ruc_transportista;
    private javax.swing.JTextField txt_serie;
    // End of variables declaration//GEN-END:variables
}
