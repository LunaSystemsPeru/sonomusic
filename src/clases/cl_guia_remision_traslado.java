/*
 * Copyright (c) 2019, luis
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package clases;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author luis
 */
public class cl_guia_remision_traslado {

    cl_conectar c_conectar = new cl_conectar();

    private int id_traslado;
    private int id_almacen;
    private String serie;
    private int numero;
    private String id_almacen_llegada;
    private String ruc_transporte;
    private String razon_transporte;
    private String dni_chofer;
    private String placa;
    private String hash;

    public cl_guia_remision_traslado() {
    }

    public int getId_traslado() {
        return id_traslado;
    }

    public void setId_traslado(int id_traslado) {
        this.id_traslado = id_traslado;
    }

    public int getId_almacen() {
        return id_almacen;
    }

    public void setId_almacen(int id_almacen) {
        this.id_almacen = id_almacen;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getId_almacen_llegada() {
        return id_almacen_llegada;
    }

    public void setId_almacen_llegada(String id_almacen_llegada) {
        this.id_almacen_llegada = id_almacen_llegada;
    }

    public String getRuc_transporte() {
        return ruc_transporte;
    }

    public void setRuc_transporte(String ruc_transporte) {
        this.ruc_transporte = ruc_transporte.trim();
    }

    public String getRazon_transporte() {
        return razon_transporte;
    }

    public void setRazon_transporte(String razon_transporte) {
        this.razon_transporte = razon_transporte.trim().toUpperCase();
    }

    public String getDni_chofer() {
        return dni_chofer;
    }

    public void setDni_chofer(String dni_chofer) {
        this.dni_chofer = dni_chofer.trim();
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public boolean registrar() {
        boolean registrado = false;
        Statement st = c_conectar.conexion();
        String query = "insert into guias_traslado "
                + "Values ('" + id_traslado + "', '" + id_almacen + "', '" + id_almacen_llegada + "','" + ruc_transporte + "', "
                + "'" + razon_transporte + "','" + dni_chofer + "','" + placa + "', '" + serie + "', '" + numero + "', '', '0')";
        int resultado = c_conectar.actualiza(st, query);
        if (resultado > -1) {
            registrado = true;
        }
        c_conectar.cerrar(st);
        return registrado;
    }

    public void mostrar(JTable tabla, String query) {
        try {
            DefaultTableModel modelo = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int fila, int columna) {
                    return false;
                }
            };
            modelo.addColumn("Id");
            modelo.addColumn("Fecha");
            modelo.addColumn("Nro Guia");
            modelo.addColumn("Tienda Origen");
            modelo.addColumn("Tienda Destino");
            modelo.addColumn("Transportista");
            modelo.addColumn("Hash");

            Statement st = c_conectar.conexion();
            ResultSet rs = c_conectar.consulta(st, query);

            while (rs.next()) {
                Object[] fila = new Object[7];
                fila[0] = rs.getString("id_venta");
                fila[1] = rs.getString("fecha");
                fila[2] = rs.getString("serie") + "-" + rs.getString("numero");
                fila[3] = rs.getString("nombre");
                fila[4] = rs.getString("serventa") + "-" + rs.getString("numventa");
                fila[5] = rs.getString("username");
                fila[6] = rs.getString("hash");
                modelo.addRow(fila);
            }

            c_conectar.cerrar(rs);
            c_conectar.cerrar(st);

            tabla.setModel(modelo);
            tabla.getColumnModel().getColumn(0).setPreferredWidth(10);
            tabla.getColumnModel().getColumn(1).setPreferredWidth(40);
            tabla.getColumnModel().getColumn(2).setPreferredWidth(100);
            tabla.getColumnModel().getColumn(3).setPreferredWidth(300);
            tabla.getColumnModel().getColumn(4).setPreferredWidth(100);
            tabla.getColumnModel().getColumn(5).setPreferredWidth(80);
            tabla.getColumnModel().getColumn(6).setPreferredWidth(100);
        } catch (SQLException ex) {
            System.out.print(ex);
        }
    }

}
