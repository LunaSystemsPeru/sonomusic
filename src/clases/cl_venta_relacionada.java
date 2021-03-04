/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import java.sql.Statement;

/**
 *
 * @author luisoyanguren
 */
public class cl_venta_relacionada {
    cl_conectar c_conectar = new cl_conectar();
    cl_varios varios = new cl_varios();
    
    private int idventa; //id de la nota de creito o debito
    private int iddocrelacionado; // id de la boleta o factura relacionada
    private int idalmacen;
    private int idmotivo;

    public cl_venta_relacionada() {
    }

    public int getIdventa() {
        return idventa;
    }

    public void setIdventa(int idventa) {
        this.idventa = idventa;
    }

    public int getIddocrelacionado() {
        return iddocrelacionado;
    }

    public void setIddocrelacionado(int iddocrelacionado) {
        this.iddocrelacionado = iddocrelacionado;
    }

    public int getIdalmacen() {
        return idalmacen;
    }

    public void setIdalmacen(int idalmacen) {
        this.idalmacen = idalmacen;
    }

    public int getIdmotivo() {
        return idmotivo;
    }

    public void setIdmotivo(int idmotivo) {
        this.idmotivo = idmotivo;
    }
    
    public boolean insertar() {
        boolean registrado = false;
        Statement st = c_conectar.conexion();
        String query = "insert into venta_relacionado "
                + "values ('" + idventa + "', '" + idalmacen + "', '" + iddocrelacionado + "', '" + idmotivo + "')";
        System.out.println(query);
        int resultado = c_conectar.actualiza(st, query);
        if (resultado > -1) {
            registrado = true;
        }
        return registrado;
    }
    
}
