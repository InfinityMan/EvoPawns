/*
 * Copyright (C) 2017-2018 Dmig
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
import ru.dmig.pawns.Game;
import ru.epiclib.base.Base;

/**
 * Implements the neural network neuron
 *
 * @author Dmig
 */
public final class Neuron implements Serializable {

    //http://en.wikipedia.org/wiki/Box%E2%80%93Muller_transform
    private static final long serialVersionUID = 1734654068548880380L;

    public static double calc(final double[] inputs, final double[] weights) {
        if (inputs.length != weights.length - 1) {
            throw new IllegalArgumentException();
        }
        double potential = 0;
        for (int i = 1; i < weights.length; i++) {
            potential += inputs[i - 1] * weights[i]; //sum inputs
        }
        potential += weights[0];
        //return lineFunc(radius, potential, false);
        return potential;
    }

    public static double lineFunc(int radius, double value, boolean simmetric) {
        if (radius < 0) {
            throw new IllegalArgumentException();
        }
        if (radius == 0) {
            if (value < 0) {
                return 0;
            } else {
                return 1;
            }
        } else {
            if (simmetric) {
                if (value >= radius) {
                    return 1;
                } else if (value <= -radius) {
                    return 0;
                } else {
                    if (value < 0) {
                        return Math.abs(value / (radius * 2));
                    } else {
                        return (value / (radius * 2)) + 0.5;
                    }
                }
            } else {
                if (value >= radius) {
                    return 1;
                } else if (value <= 0) {
                    return 0;
                } else {
                    return (value / radius);
                }
            }
        }
    }

    public static double genWeight() {
        final byte MAX = 10;
        final byte MIN = -10;

        final short amountOfPeriods = MAX - MIN;
        final int periodId = Base.randomNumber(0, amountOfPeriods - 1);
        return (MIN + periodId + Math.random());
    }
    // weights[0] is bias
    public double[] weights;
    public double output;

    /**
     *
     * @param inputCount number of inputs not counting treshold
     * @param pause Need pause after restarting?
     */
    public Neuron(int inputCount, boolean pause) {
        inputCount++;
        weights = new double[inputCount];
        restart(pause);
    }

    public void calc(final Layer prevLayer) {
        final double[] inps = new double[prevLayer.neurons.length];
        for (int i = 0; i < inps.length; i++) {
            inps[i] = prevLayer.neurons[i].output;
        }
        calc(inps);
    }

    public void calc(final double[] inputs) {
        output = calc(inputs, weights);
    }

    /**
     * Restarts the neuron
     *
     * @param pause Need pause after restarting?
     */
    public void restart(boolean pause) {
        weights[0] = 0;
        for (int i = 0; i < weights.length; i++) {
            weights[i] = genWeight();
        }

//        weights[0] = 0;
//        for (int i = 0; i < weights.length; i++) {
//            if (Base.chance(50, 0)) {
//                weights[i] = -Math.random();
//            } else {
//                weights[i] = Math.random();
//            }
//        }
        if (pause) {
            try {
                Thread.sleep(0, 2);
            } catch (InterruptedException ex) {
                Game.exception();
            }
        }
    }

    public void tryToMutate() {
        weights[Base.randomNumber(0, weights.length - 1)] = genWeight();
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
