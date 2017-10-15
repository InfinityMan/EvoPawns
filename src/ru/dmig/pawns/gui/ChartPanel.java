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
package ru.dmig.pawns.gui;

import java.awt.Dimension;
import java.util.ArrayList;
import javax.swing.JFrame;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.style.Styler;
import ru.epiclib.base.Arrayer;

/**
 *
 * @author Dmig
 */
public class ChartPanel extends JFrame {

    public static ArrayList<Double> fittests = new ArrayList<>();
    public static ArrayList<Double> averages = new ArrayList<>();

    public static ArrayList<Double> aFittests = new ArrayList<>();
    public static ArrayList<Double> aAverages = new ArrayList<>();

    public XYChart chart;
    public static ChartPanel cp;

    private static final int MIN_LENGTH = 1200;
    private static final int MIN_HEIGHT = 700;

    public ChartPanel() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        chart = new XYChart(MIN_LENGTH, MIN_HEIGHT, Styler.ChartTheme.Matlab);
        chart.setXAxisTitle("Generation (Поколение)");
        chart.setYAxisTitle("Fitness (Крутость)");
        ArrayList<Integer> gen = new ArrayList<>();
        for (int i = 0; i < fittests.size(); i++) {
            gen.add(i);
        }
        //chart.addSeries("Fittest", gen, fittests);
        //chart.addSeries("Averages", gen, averages);

        XChartPanel<XYChart> c = new XChartPanel<>(chart);
        c.setMinimumSize(new Dimension(MIN_LENGTH, MIN_HEIGHT));
        add(c);

        setTitle("Chart of fitness");
        cp = this;

    }

    public void update() {
        chart.removeSeries("Fittest");
        chart.removeSeries("Averages");
        ArrayList<Integer> gen = new ArrayList<>();
        for (int i = 1; i < fittests.size() + 1; i++) {
            gen.add(i);
        }
        chart.removeSeries("FittestA");
        chart.removeSeries("AveragesA");
        if (fittests.size() >= 21) {

            double[] lastFits = new double[20];
            double[] lastAvgs = new double[20];
            for (int i = 0; i < 20; i++) {
                lastFits[i] = fittests.get(fittests.size() - 1 - i);
                lastAvgs[i] = averages.get(averages.size() - 1 - i);
            }
            aFittests.add(Arrayer.mediumValueOfArray(lastFits));
            aAverages.add(Arrayer.mediumValueOfArray(lastAvgs));
            ArrayList<Integer> genA = new ArrayList<>();

        } else {
            aFittests.add(0d);
            aAverages.add(0d);
        }

        chart.addSeries("Fittest", gen, fittests);
        chart.addSeries("Averages", gen, averages);
        chart.addSeries("FittestA", gen, aFittests);
        chart.addSeries("AveragesA", gen, aAverages);

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
