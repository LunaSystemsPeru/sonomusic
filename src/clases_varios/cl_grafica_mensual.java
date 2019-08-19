/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases_varios;

import clases.cl_venta;
import java.util.ArrayList;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import sonomusic.frm_principal;

/**
 *
 * @author luis
 */
public class cl_grafica_mensual {

    cl_venta c_venta = new cl_venta();
    int id_almacen = frm_principal.c_almacen.getId();

    public void llenar_series_diarias(JPanel panel) {

        XYSeries series = new XYSeries("S/");
        c_venta.setId_almacen(id_almacen);
        Integer[] valor_x = c_venta.ventas_diaras();
       // System.out.println("canidad_items" + valor_x.length);

        for (int i = 1; i < valor_x.length; i++) {
            int valor = 0;
            if (valor_x[i] != null) {
                valor = valor_x[i];
            }
            series.add(i, valor);
            //System.out.println(i + " valor " + valor);
        }

// Add the series to your data set
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);

// Generate the graph
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Ventas del Mes", // Title
                "Dias", // x-axis Label
                "Total S/", // y-axis Label
                dataset, // Dataset
                PlotOrientation.VERTICAL, // Plot Orientation
                true, // Show Legend
                true, // Use tooltips
                false // Configure chart to generate URLs?
        );

        ChartPanel chartPanel = new ChartPanel((JFreeChart) null);
        chartPanel.setChart(chart);
        chartPanel.setBounds(0, 0, 419, 309);
        panel.add(chartPanel);
    }
    
    public void llenar_series_mensuales(JPanel panel) {

        XYSeries series = new XYSeries("S/");
        c_venta.setId_almacen(id_almacen);
        Integer[] valor_x = c_venta.ventas_mensuales();
       // System.out.println("canidad_items" + valor_x.length);

        for (int i = 1; i < valor_x.length; i++) {
            int valor = 0;
            if (valor_x[i] != null) {
                valor = valor_x[i];
            }
            series.add(i, valor);
            //System.out.println(i + " valor " + valor);
        }

// Add the series to your data set
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);

// Generate the graph
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Ventas del Año", // Title
                "Meses", // x-axis Label
                "Total S/", // y-axis Label
                dataset, // Dataset
                PlotOrientation.VERTICAL, // Plot Orientation
                true, // Show Legend
                true, // Use tooltips
                false // Configure chart to generate URLs?
        );

        ChartPanel chartPanel = new ChartPanel((JFreeChart) null);
        chartPanel.setChart(chart);
        chartPanel.setBounds(0, 0, 419, 309);
        panel.add(chartPanel);
    }


}
