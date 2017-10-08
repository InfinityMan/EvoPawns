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
package ru.dmig.pawns.net;

import java.io.Serializable;
import java.util.List;

/**
 * Implements the neural network layer of neurons
 *
 * @author Dmig
 * @author jnemec (on github)
 * @author honzour (on github)
 */
public class Layer implements Serializable {

    private static final long serialVersionUID = -4328364654490455402L;
    public Neuron[] neurons;

    public Layer(List<List<Double>> layersData) {
        final int count = layersData.size();

        neurons = new Neuron[count];
        for (int i = 0; i < count; i++) {
            neurons[i] = new Neuron(layersData.get(i));
        }
    }

    public Layer(int count, int previousCount) {
        neurons = new Neuron[count];
        for (int i = 0; i < count; i++) {
            neurons[i] = new Neuron(previousCount);
        }
    }

    public double[][] getWeights() {
        double[][] weights = new double[neurons.length][];
        for (int i = 0; i < neurons.length; i++) {
            weights[i] = neurons[i].weights;
        }
        return weights;
    }

    public void setWeights(double[][] w) {
        if (neurons.length == w.length) {
            for (int i = 0; i < w.length; i++) {
                if (neurons[i].weights.length == w[i].length) {
                    neurons[i].weights = w[i];
                } else {
                    throw new IllegalArgumentException();
                }
            }
        } else {
            throw new IllegalArgumentException();
        }
    }
}
