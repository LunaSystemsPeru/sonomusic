/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file,
choose Tools | Templates
 * and open the template in the editor.
 */
package pdfs;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfCell;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dereck
 */
public class pdfGuiaTraslado {

    private static final String FILE_NAME = "itext.pdf";

    private static final Font WhiteBold9Font = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD, Color.WHITE);
    private static final Font WhiteBold8Font = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD, Color.WHITE);
    private static final Font paragraphFont = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL);

    private static final Font BOLD9Font = new Font(Font.HELVETICA, 9, Font.BOLD);
    private static final Font NORMAL9Font = new Font(Font.HELVETICA, 9, Font.NORMAL);
    private static final Font blueFont = new Font(Font.HELVETICA, 9, Font.NORMAL, Color.RED);
    private static final Font smallBold = new Font(Font.HELVETICA, 8, Font.BOLD);

    public pdfGuiaTraslado() {
    }

    public void generarPDF() {
        try {
            Document document = new Document(PageSize.A4, 20, 20, 20, 20);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(new File(FILE_NAME)));

            //open
            document.open();
            // We add metadata to PDF
            // Añadimos los metadatos del PDF
            document.addTitle("GUIA DE REMISION T001 - 0000400");
            document.addSubject("IMPORTACIONES RODSON MUSIC EIRL");
            document.addKeywords("Java, PDF, iText");
            document.addAuthor("LUNA SYSTEMS PERU - SOFTWARE DE INVENTARIO Y FACTURACION ELECTRONICA");
            document.addCreator("LUNA SYSTEMS PERU");
            // First page

            // We create the table (Creamos la tabla).
            PdfPTable tablatitulo = new PdfPTable(3);
            tablatitulo.setWidthPercentage(100);

            // Now we fill the PDF table
            //generar tabla de encazabeado de datos de guia
            float[] columnWidths = new float[]{30f, 45f, 25f};
            tablatitulo.setWidths(columnWidths);
            tablatitulo.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
            // Ahora llenamos la tabla del PDF
            PdfPCell columnHeader = null;

            // Fill table rows (rellenamos las filas de la tabla).
            Image imagen = Image.getInstance("reports/rodson.png");
            imagen.scaleToFit(100f, 100f);
            columnHeader = new PdfPCell(imagen);
            columnHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            columnHeader.setBorder(PdfPCell.NO_BORDER);
            columnHeader.setRowspan(3);
            tablatitulo.addCell(columnHeader);

            columnHeader = new PdfPCell(new Phrase("IMPORTACIONES RODSON MUSIC EIRL", BOLD9Font));
            columnHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            columnHeader.setBorder(PdfPCell.NO_BORDER);
            tablatitulo.addCell(columnHeader);

            columnHeader = new PdfPCell(new Phrase("RUC: 10469932091", WhiteBold9Font));
            columnHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            columnHeader.setVerticalAlignment(Element.ALIGN_MIDDLE);
            columnHeader.setBackgroundColor(Color.darkGray);
            columnHeader.setBorderColor(Color.darkGray);
            tablatitulo.addCell(columnHeader);

            // Fill table rows (rellenamos las filas de la tabla).
            columnHeader = new PdfPCell(new Phrase("AAHH VILLA DEL SALVADOR MZ D5 LT 26 - NUEVO CHIMBOTE - SANTA - ANCASH", NORMAL9Font));
            columnHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            columnHeader.setBorder(PdfPCell.NO_BORDER);
            tablatitulo.addCell(columnHeader);

            columnHeader = new PdfPCell(new Phrase("GUIA DE REMISION ELECTRONICA", WhiteBold9Font));
            columnHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            columnHeader.setVerticalAlignment(Element.ALIGN_MIDDLE);
            columnHeader.setBackgroundColor(Color.darkGray);
            //columnHeader.setBorder(PdfPCell.NO_BORDER);
            columnHeader.setBorderColor(Color.darkGray);
            tablatitulo.addCell(columnHeader);

            // Fill table rows (rellenamos las filas de la tabla).
            columnHeader = new PdfPCell(new Phrase("TIENDA: NUEVO CHIMBOTE", NORMAL9Font));
            columnHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            columnHeader.setBorder(PdfPCell.NO_BORDER);
            tablatitulo.addCell(columnHeader);

            columnHeader = new PdfPCell(new Phrase("T001-0000400", WhiteBold9Font));
            columnHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            columnHeader.setVerticalAlignment(Element.ALIGN_MIDDLE);
            columnHeader.setBackgroundColor(Color.darkGray);
            //columnHeader.setBorder(PdfPCell.NO_BORDER);
            columnHeader.setBorderColor(Color.darkGray);
            tablatitulo.addCell(columnHeader);

            //paragraphMore.add(tablatitulo);
            //Paragraph paragraph = new Paragraph("", subcategoryFont);
            document.add(tablatitulo);
            Paragraph saltoDeLinea = new Paragraph(" ");
            document.add(saltoDeLinea);

            PdfPTable tablaencabezado = new PdfPTable(3);
            tablaencabezado.setWidthPercentage(100);
            tablaencabezado.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
            //generar tabla de encazabeado de datos de guia
            columnWidths = new float[]{49f, 2f, 49f};
            tablaencabezado.setWidths(columnWidths);
            // Ahora llenamos la tabla del PDF
            // Fill table rows (rellenamos las filas de la tabla).
            columnHeader = new PdfPCell(new Phrase("Datos Envio", WhiteBold8Font));
            columnHeader.setHorizontalAlignment(Element.ALIGN_LEFT);
            columnHeader.setBackgroundColor(Color.darkGray);
            columnHeader.setBorder(PdfPCell.NO_BORDER);
            tablaencabezado.addCell(columnHeader);

            columnHeader = new PdfPCell(new Phrase(""));
            columnHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            columnHeader.setBorder(PdfPCell.NO_BORDER);
            tablaencabezado.addCell(columnHeader);

            columnHeader = new PdfPCell(new Phrase("Datos Destinatario", WhiteBold8Font));
            columnHeader.setHorizontalAlignment(Element.ALIGN_LEFT);
            columnHeader.setBackgroundColor(Color.darkGray);
            columnHeader.setBorder(PdfPCell.NO_BORDER);
            tablaencabezado.addCell(columnHeader);
            tablaencabezado.setHeaderRows(1);

            tablaencabezado.addCell(new Paragraph("Fecha de Envio: Setiembre 02, 2021", paragraphFont));
            tablaencabezado.addCell("");
            tablaencabezado.addCell(new Paragraph("RUC: 10469932091", paragraphFont));

            tablaencabezado.addCell(new Paragraph("Motivo: Traslado entre Almacenes", paragraphFont));
            tablaencabezado.addCell("");
            tablaencabezado.addCell(new Paragraph("OYANGUREN GIRON LUIS ENRIQUE", paragraphFont));

            //paragraphMore.add(tablaencabezado);
            // We add the paragraph with the table (Añadimos el elemento con la tabla).
            document.add(tablaencabezado);

            document.add(saltoDeLinea);

            PdfPTable tabladireccion = new PdfPTable(3);
            tabladireccion.setWidthPercentage(100);
            tabladireccion.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
            //generar tabla de encazabeado de datos de guia
            columnWidths = new float[]{49f, 2f, 49f};
            tabladireccion.setWidths(columnWidths);
            // Ahora llenamos la tabla del PDF
            // Fill table rows (rellenamos las filas de la tabla).
            columnHeader = new PdfPCell(new Phrase("Direccion de Partida", WhiteBold8Font));
            columnHeader.setHorizontalAlignment(Element.ALIGN_LEFT);
            columnHeader.setBackgroundColor(Color.darkGray);
            columnHeader.setBorder(PdfPCell.NO_BORDER);
            tabladireccion.addCell(columnHeader);

            columnHeader = new PdfPCell(new Phrase(""));
            columnHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            columnHeader.setBorder(PdfPCell.NO_BORDER);
            tabladireccion.addCell(columnHeader);

            columnHeader = new PdfPCell(new Phrase("Datos de Destino", WhiteBold8Font));
            columnHeader.setHorizontalAlignment(Element.ALIGN_LEFT);
            columnHeader.setBackgroundColor(Color.darkGray);
            columnHeader.setBorder(PdfPCell.NO_BORDER);
            tabladireccion.addCell(columnHeader);
            tabladireccion.setHeaderRows(1);

            tabladireccion.addCell(new Paragraph("020801 | AAHH VILLA DEL SALVADOR MZ D5 LT 26", paragraphFont));
            tabladireccion.addCell("");
            tabladireccion.addCell(new Paragraph("150101 | Z.I CALLAO PUERTO NRO 1312", paragraphFont));

            //paragraphMore.add(tablaencabezado);
            // We add the paragraph with the table (Añadimos el elemento con la tabla).
            document.add(tabladireccion);
            document.add(saltoDeLinea);

            PdfPTable tablaproductos = new PdfPTable(5);
            tablaproductos.setWidthPercentage(100);
            tablaproductos.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
            //generar tabla de encazabeado de datos de guia
            columnWidths = new float[]{4f, 76f, 6f, 6f, 8f};
            tablaproductos.setWidths(columnWidths);
            // Ahora llenamos la tabla del PDF
            // Fill table rows (rellenamos las filas de la tabla).
            columnHeader = new PdfPCell(new Phrase("Item", WhiteBold8Font));
            columnHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            columnHeader.setBackgroundColor(Color.darkGray);
            columnHeader.setBorder(PdfPCell.NO_BORDER);
            tablaproductos.addCell(columnHeader);

            columnHeader = new PdfPCell(new Phrase("Producto", WhiteBold8Font));
            columnHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            columnHeader.setBackgroundColor(Color.darkGray);
            columnHeader.setBorder(PdfPCell.NO_BORDER);
            tablaproductos.addCell(columnHeader);

            columnHeader = new PdfPCell(new Phrase("Cant.", WhiteBold8Font));
            columnHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            columnHeader.setBackgroundColor(Color.darkGray);
            columnHeader.setBorder(PdfPCell.NO_BORDER);
            tablaproductos.addCell(columnHeader);

            columnHeader = new PdfPCell(new Phrase("U.M.", WhiteBold8Font));
            columnHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            columnHeader.setBackgroundColor(Color.darkGray);
            columnHeader.setBorder(PdfPCell.NO_BORDER);
            tablaproductos.addCell(columnHeader);

            columnHeader = new PdfPCell(new Phrase("Peso", WhiteBold8Font));
            columnHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            columnHeader.setBackgroundColor(Color.darkGray);
            columnHeader.setBorder(PdfPCell.NO_BORDER);
            tablaproductos.addCell(columnHeader);

            tablaproductos.setHeaderRows(1);

            tablaproductos.addCell(new Paragraph("1", paragraphFont));
            tablaproductos.addCell(new Paragraph("PARLANTE DE 18\" 1600WT PICO / 800WT RMS MODELO ITALIANO RODSON", paragraphFont));
            tablaproductos.addCell(new Paragraph("4", paragraphFont));
            tablaproductos.addCell(new Paragraph("UND", paragraphFont));
            tablaproductos.addCell(new Paragraph("1 kg", paragraphFont));

            document.add(tablaproductos);
            document.add(saltoDeLinea);

            PdfPTable tablafooter = new PdfPTable(3);
            tablafooter.setWidthPercentage(100);
            tablafooter.setTotalWidth(100);
            tablafooter.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
            //generar tabla de encazabeado de datos de guia
            columnWidths = new float[]{48f, 4f, 48f};
            tablafooter.setWidths(columnWidths);
            // Ahora llenamos la tabla del PDF
            // Fill table rows (rellenamos las filas de la tabla).
            columnHeader = new PdfPCell(new Phrase("Transportista", WhiteBold8Font));
            columnHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            columnHeader.setBackgroundColor(Color.darkGray);
            columnHeader.setBorder(PdfPCell.NO_BORDER);
            tablafooter.addCell(columnHeader);

            columnHeader = new PdfPCell(new Phrase("", WhiteBold8Font));
            columnHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            columnHeader.setBackgroundColor(Color.darkGray);
            columnHeader.setBorder(PdfPCell.NO_BORDER);
            tablafooter.addCell(columnHeader);

            columnHeader = new PdfPCell(new Phrase("Datos del Vehiculo.", WhiteBold8Font));
            columnHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            columnHeader.setBackgroundColor(Color.darkGray);
            columnHeader.setBorder(PdfPCell.NO_BORDER);
            tablafooter.addCell(columnHeader);

            tablafooter.setHeaderRows(1);

            tablafooter.addCell(new Paragraph("RUC.: 20532059250", paragraphFont));
            tablafooter.addCell(new Paragraph("", paragraphFont));
            tablafooter.addCell(new Paragraph("PLACA: D2D744", paragraphFont));

            tablafooter.addCell(new Paragraph("TRANSPORTES EL RAPIDO SAC", paragraphFont));
            tablafooter.addCell(new Paragraph("", paragraphFont));
            tablafooter.addCell(new Paragraph("DNI CHOFER: 32547698", paragraphFont));

            tablafooter.addCell(new Paragraph("", paragraphFont));
            tablafooter.addCell(new Paragraph("", paragraphFont));
            tablafooter.addCell(new Paragraph("", paragraphFont));

            tablafooter.addCell(new Paragraph("HASH: dhfihfig39fbef723fg", paragraphFont));
            tablafooter.addCell(new Paragraph("", paragraphFont));
            tablafooter.addCell(new Paragraph("Observaciones:", paragraphFont));

            tablafooter.addCell(new Paragraph("Consulta tu comprobante en: esonomusic.lunasystemsperu.com", paragraphFont));
            tablafooter.addCell(new Paragraph("", paragraphFont));
            tablafooter.addCell(new Paragraph("Representacion impresa de la GUIA DE REMISION ELECTRONICA", paragraphFont));

            FooterTable event = new FooterTable(tablafooter);
            writer.setPageEvent(event);
            //document.add(tablafooter);

            //close
            document.close();
            System.out.println("Done");

        } catch (DocumentException ex) {
            Logger.getLogger(pdfGuiaTraslado.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException fileNotFoundException) {
            System.out.println("No such file was found to generate the PDF "
                    + "(No se encontró el fichero para generar el pdf)" + fileNotFoundException);
        } catch (IOException ex) {
            Logger.getLogger(pdfGuiaTraslado.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public class FooterTable extends PdfPageEventHelper {

        protected PdfPTable footer;

        public FooterTable(PdfPTable footer) {
            this.footer = footer;
        }

        public void onEndPage(PdfWriter writer, Document document) {
            footer.writeSelectedRows(0, -1, 20, 100, writer.getDirectContent());
        }
    }
}
