/*
 * Copyright (C) 2017 Dmig
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ru.dmig.pawns;

import java.awt.Dimension;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;

/**
 *
 * @author Dmig
 */
public class ChartPanel extends JFrame {
    
    public static ArrayList<Double> fittests = new ArrayList<>();
    public static ArrayList<Double> averages = new ArrayList<>();
    
    public XYChart chart;
    public static ChartPanel cp;

    public ChartPanel() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        
        chart = new XYChart(800, 600);
        ArrayList<Integer> gen = new ArrayList<>();
        for (int i = 0; i < fittests.size(); i++) {
            gen.add(i);
        }
        //chart.addSeries("Fittest", gen, fittests);
        //chart.addSeries("Averages", gen, averages);
        
        XChartPanel<XYChart> c = new XChartPanel<>(chart);
        c.setMinimumSize(new Dimension(1000, 800));
        add(c);
        
        cp = this;

    }
    
    public void update() {
        chart.removeSeries("Fittest");
        chart.removeSeries("Averages");
        ArrayList<Integer> gen = new ArrayList<>();
        for (int i = 0; i < fittests.size(); i++) {
            gen.add(i);
        }
        chart.addSeries("Fittest", gen, fittests);
        chart.addSeries("Averages", gen, averages);
        
        pack();
        repaint();
    }
    
    public void update(double fit, double avg) {
        fittests.add(fit);
        averages.add(avg);
        update();
    }
    
    public static double[] arrayListToArray(ArrayList<Double> doubles) {
        double[] ret = new double[doubles.size()];
        for (int i = 0; i < doubles.size(); i++) {
            ret[i] = doubles.get(i);
        }
        return ret;
    }
    
    public static void lauch() {
        java.awt.EventQueue.invokeLater(() -> {
            new ChartPanel().setVisible(true);
        });
    }
    
}
