/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author luisoyanguren
 */
public class cl_motivo_documento {
    cl_conectar c_conectar = new cl_conectar();
    cl_varios varios = new cl_varios();
    
    private int idtido;
    private int idmotivo;
    private String descripcion;

    public cl_motivo_documento() {
    }

    public int getIdtido() {
        return idtido;
    }

    public void setIdtido(int idtido) {
        this.idtido = idtido;
    }

    public int getIdmotivo() {
        return idmotivo;
    }

    public void setIdmotivo(int idmotivo) {
        this.idmotivo = idmotivo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public boolean obtenerDatos() {
        boolean existe = false;
        try {
            Statement st = c_conectar.conexion();
            String query = "select * "
                    + "from motivo_documento "
                    + "where idmotivo = '" + idmotivo + "'";
            System.out.println(query);
            ResultSet rs = c_conectar.consulta(st, query);
            if (rs.next()) {
                existe = true;
                descripcion = rs.getString("nombre");
                idtido = rs.getInt("idtido");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return existe;
    }
    
    public void obtenerCodigo() {
        try {
            Statement st = c_conectar.conexion();
            String query = "select ifnull(max(idmotivo) + 1, 1) as codigo "
                    + "from motivo_documento ";
            ResultSet rs = c_conectar.consulta(st, query);
            if (rs.next()) {
                idmotivo = rs.getInt("codigo");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public boolean insertar() {
        boolean registrado = false;
        Statement st = c_conectar.conexion();
        String query = "insert into motivo_documento "
                + "values ('" + idmotivo + "', '" + descripcion + "', '" + idtido + "')";
        System.out.println(query);
        int resultado = c_conectar.actualiza(st, query);
        if (resultado > -1) {
            registrado = true;
        }
        return registrado;
    }
}
