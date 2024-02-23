/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package json;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author gerenciatecnica
 */
public class cl_envio_server {

    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.125 Safari/537.36";

    public static String[] enviar_documento(int id_venta, int tipo_documento, int id_almacen) {
        String SERVER_PATH = "http://174.138.2.254/audionet/composer/generateXML/";

        String url = null;
        String[] datos = new String[6];
        // open a connection to the site
        if (tipo_documento == 1) {
            url = SERVER_PATH + "boleta.php?id_venta=" + id_venta + "&id_almacen=" + id_almacen;
        }
        if (tipo_documento == 2) {
            url = SERVER_PATH + "factura.php?id_venta=" + id_venta + "&id_almacen=" + id_almacen;
        }
        if (tipo_documento == 3) {
            url = SERVER_PATH + "nota_credito.php?id_venta=" + id_venta + "&id_almacen=" + id_almacen;
        }

        if (tipo_documento == 5) {
            url = SERVER_PATH + "guia-remision.php?id_venta=" + id_venta + "&id_almacen=" + id_almacen;
        }

        String text_post = "id_venta=" + id_venta + "&id_almacen=" + id_almacen;
        StringBuffer response;

        System.out.println(url);
        try {
            //Creamos un nuevo objeto URL con la url donde pedir el JSON
            URL obj = new URL(url);
            //Creamos un objeto de conexión
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            //Añadimos la cabecera
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setRequestProperty("Content-Length", Integer.toString(text_post.length()));
            // Enviamos la petición por POST
            con.setDoOutput(true);

            try ( DataOutputStream dos = new DataOutputStream(con.getOutputStream())) {
                dos.writeBytes(text_post);
            }

            //Capturamos la respuesta del servidor
            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);

            //if (responseCode != 200) {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            //Mostramos la respuesta del servidor por consola
            System.out.println("Respuesta del servidor: " + response);
            //cerramos la conexión
            in.close();
            // }

            System.out.println("INFORMACIÓN OBTENIDA DEL SERVIDOR:");

            JSONParser Jparser = new JSONParser();
            JSONObject jsonObject = (JSONObject) Jparser.parse(response.toString());
            String estatus = jsonObject.get("aceptado").toString();

            datos[0] = jsonObject.get("nombreDocumento").toString();
            datos[1] = "";
            datos[2] = "";
            datos[3] = "";
            datos[4] = jsonObject.get("observaciones").toString();

            if (estatus.equals("true")) {
                datos[5] = "aceptado";
            } else {
                datos[5] = "error";
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return datos;
    }

    public static String[] showJSONRUC_JMP(String json) throws ParseException {
        String[] datos = new String[4];
        System.out.println("INFORMACIÓN OBTENIDA DE LA BASE DE DATOS:");

        JSONParser Jparser = new JSONParser();
        JSONObject jsonObject = (JSONObject) Jparser.parse(json);
        boolean estatus = (Boolean) jsonObject.get("success");

        //System.out.println("el estado es: " + estatus);
        //JSONArray result = (JSONArray) jsonObject.get("result");
        //array cuando es repetitivo
        //estructurs cuando es uno simple
        //aprendi de aqui
        //https://examples.javacodegeeks.com/core-java/json/java-json-parser-example/
        JSONObject result = (JSONObject) jsonObject.get("result");
        //System.out.println("razon social: " + result.get("RazonSocial"));
        datos[0] = result.get("RazonSocial").toString();
        datos[1] = result.get("Direccion").toString();
        datos[2] = result.get("Condicion").toString();
        datos[3] = result.get("Estado").toString();
        return datos;
    }

}
