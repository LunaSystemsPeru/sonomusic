/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import java.io.IOException;
import java.net.Socket;
import java.sql.*;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrador
 */
public class cl_conectar {

    private static Connection conexion = null;
    
    private static String bd; // Nombre de BD.
    private static String user; // Usuario de BD.
    private static String password; // Password de BD.
    private static String url;

    private final String __PRODUCCION = "PRODUCCION";
    private final String __PRUEBA = "PRUEBA";

    // Driver para MySQL en este caso.
    private final String driver = "com.mysql.jdbc.Driver";

    /**
     * Método neecesario para conectarse al Driver y poder usar MySQL.
     *
     * @return
     */
    public boolean conectar() {
        boolean conectado;
        try {
            getServidor(__PRODUCCION);

            String server = "jdbc:mysql://" + url + ":3306/" + bd;

            Class.forName(driver);
            conexion = DriverManager.getConnection(server, user, password);
            conectado = true;
            System.out.println("Conectando al Servidor: " + server);

        } catch (ClassNotFoundException | SQLException e) {
            //JOptionPane.showMessageDialog(null, "Error: Imposible realizar la conexion a BD." + server + "," + user + "," + password);
            JOptionPane.showMessageDialog(null, "Error al conectar \n" + e.getLocalizedMessage());
            System.out.print(e);
            e.printStackTrace();
            //System.exit(0);
            conectado = false;
        }
        return conectado;
    }

    /**
     * Método para establecer la conexión con la base de datos.
     *
     * @return
     */
    public Connection conx() {
        return conexion;
    }

    private void getServidor(String status) {
        if (status.equals(__PRUEBA)) {
            bd = "goempres_audionet"; // Nombre de BD.
            user = "goempres_user_audionet"; // Usuario de BD.
            password = "Xzo%ALQ@3)sg"; // Password de BD.
            url = "localhost";
        }

        if (status.equals(__PRODUCCION)) {
            bd = "brunoasc_new_sonomusic"; // Nombre de BD.
            user = "brunoasc_luis_bd"; // Usuario de BD.
            password = "C]6&TN4Bt@&I"; // Password de BD.
            url = "artemisa.servidoresph.com";
        }
    }

    public boolean verificar_conexion() {
        boolean conectado = false;
        String dirWeb = "www.google.com";
        int puerto = 80;
        try {
            Socket s = new Socket(dirWeb, puerto);
            if (s.isConnected()) {
                System.out.println("Conexión establecida con la dirección: " + dirWeb + " a travéz del puerto: " + puerto);
                conectado = true;
            }
        } catch (IOException e) {
            conectado = false;
            System.err.println("No se pudo establecer conexión con: " + dirWeb + " a travez del puerto: " + puerto);
        }
        return conectado;
    }

    // Métodos
    public static Connection getConnection() {

        if (conexion == null) {
            new cl_conectar();
        }

        return conexion;
    } // Fin getConnection

    public Statement conexion() {
        Statement st = null;

        try {
            st = this.getConnection().createStatement();
        } catch (SQLException e) {
            System.out.println("Error: Conexión incorrecta.");
            e.printStackTrace();
        }
        return st;
    }

    /**
     * Método para realizar consultas del tipo: SELECT * FROM tabla WHERE..."
     *
     * @param st
     * @param cadena La consulta en concreto
     * @return
     */
    public ResultSet consulta(Statement st, String cadena) {
        ResultSet rs = null;
        try {
            rs = st.executeQuery(cadena);
        } catch (SQLException e) {
            System.out.println("Error con: " + cadena);
            System.out.println("SQLException: " + e.getMessage());
            e.printStackTrace();
        }
        return rs;
    }

    /**
     * Método para realizar consultas de actualización, creación o eliminación.
     *
     * @param st
     * @param cadena La consulta en concreto
     * @return
     */
    public int actualiza(Statement st, String cadena) {
        int rs = -1;
        try {
            rs = st.executeUpdate(cadena);
        } catch (SQLException e) {
            System.out.println("Error con: " + cadena);
            System.out.println("SQLException: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "SQLException: " + e.getMessage());
            e.printStackTrace();
        }
        return rs;
    }

    /**
     * Método para cerrar la consula
     *
     * @param rs
     */
    public void cerrar(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (Exception e) {
                System.out.print("Error: No es posible cerrar la consulta.");
            }
        }
    }

    /**
     * Método para cerrar la conexión.
     *
     * @param st
     */
    public void cerrar(java.sql.Statement st) {
        if (st != null) {
            try {
                st.close();
            } catch (Exception e) {
                System.out.print("Error: No es posible cerrar la conexión.");
            }
        }
    }
}
