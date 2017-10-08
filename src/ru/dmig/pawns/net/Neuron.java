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
 * Implements the neural network neuron
 *
 * @author Dmig
 * @author jnemec (on github)
 * @author honzour (on github)
 */
public class Neuron implements Serializable {

    //http://en.wikipedia.org/wiki/Box%E2%80%93Muller_transform
    private static final long serialVersionUID = 1734654068548880380L;
    // weights[0] is -treshold
    public double[] weights;
    public double output;

    public Neuron(List<Double> weights) {
        int inputCount = weights.size();
        this.weights = new double[inputCount];

        for (int i = 0; i < inputCount; i++) {
            this.weights[i] = weights.get(i);
        }
    }

    /**
     *
     * @param inputCount number of inputs not counting treshold
     */
    public Neuron(int inputCount) {
        inputCount++;
        weights = new double[inputCount];
        restart();
    }

    public void restart() {
        int inputCount = weights.length;

        weights[0] = 0;
        for (int i = 1; i < inputCount; i++) {
            weights[0] += Math.random();
        }
        for (int i = 1; i < inputCount; i++) {
            weights[i] = Math.random();
        }
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append('[');
        for (int i = 0; i < weights.length; i++) {
            sb.append(weights[i]);
            if (i < weights.length - 1) {
                sb.append(", ");
            }
        }
        sb.append(']');
        return sb.toString();
    }

}
