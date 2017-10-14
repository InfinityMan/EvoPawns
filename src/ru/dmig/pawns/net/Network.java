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
import ru.epiclib.base.Base;

/**
 * Implements the neural network
 *
 * @author Dmig
 * @author jnemec (on github)
 * @author honzour (on github)
 */
public class Network implements Serializable {

    private static final long serialVersionUID = -6200117127679042346L;

    public double[][] mInputScale;
    protected double[][] mOutputScale;

    protected double[] mOutput;

    public Layer[] mLayers;

    private long mIteration;

    static double sigma(double x) {
        return 1.0 / (1.0 + Math.exp(-x));
    }

    public int getInputDimension() {
        if (mLayers == null || mLayers.length == 0 || mLayers[0] == null || mLayers[0].neurons == null || mLayers[0].neurons[0] == null || mLayers[0].neurons[0].weights == null) {
            return 0;
        }
        return mLayers[0].neurons[0].weights.length - 1;
    }

    public int getOutputDimension() {
        if (mLayers == null || mLayers.length == 0 || mLayers[mLayers.length - 1] == null || mLayers[mLayers.length - 1].neurons == null) {
            return 0;
        }
        return mLayers[mLayers.length - 1].neurons.length;
    }

    /**
     * Parsing constructor
     *
     * @param layersData
     */
    public Network(List<List<List<Double>>> layersData) {
        mLayers = new Layer[layersData.size()];
        for (int i = 0; i < mLayers.length; i++) {
            mLayers[i] = new Layer(layersData.get(i));
        }
    }

    public Network(int[] layersDimensions) {
        mLayers = new Layer[layersDimensions.length - 1];
        for (int i = 0; i < layersDimensions.length - 1; i++) {
            mLayers[i] = new Layer(layersDimensions[i + 1], layersDimensions[i], true);
        }
    }

    /**
     * Input and output are like in the training set, all scaling is done here
     *
     * @param input before scaling. Array data will not be changed in this method.
     * @param output after scaling
     * @param scaleOutput
     */
    public void calculate(double[] input, double output[], boolean scaleOutput) {
        int i, j, k;

        for (i = 0; i < mLayers.length; i++) {
            Layer l = mLayers[i];
            for (j = 0; j < l.neurons.length; j++) {
                Neuron n = l.neurons[j];
                double potential = 0;
                for (k = 1; k < n.weights.length; k++) {
                    potential += (i == 0 ? (mInputScale == null ? input[k - 1] : input[k - 1] * mInputScale[k - 1][0] + mInputScale[k - 1][1])
                            : mLayers[i - 1].neurons[k - 1].output) * n.weights[k];
                }
                potential += n.weights[0];
                n.output = sigma(potential);
            }
        }
        Layer last = mLayers[mLayers.length - 1];
        for (i = 0; i < last.neurons.length; i++) {
            output[i] = last.neurons[i].output;
        }
        if (mOutputScale != null && scaleOutput) {
            for (i = 0; i < mOutputScale.length; i++) {
                output[i] = output[i] * mOutputScale[i][0] + mOutputScale[i][1];
            }
        }
    }

    public long getItration() {
        return mIteration;
    }

    public double[][][] getWeights() {
        double[][][] weights = new double[mLayers.length][][];
        for (int i = 0; i < weights.length; i++) {
            weights[i] = mLayers[i].getWeights();
        }
        return weights;
    }

    public void setWeights(double[][][] w) {
        if (w.length == mLayers.length) {
            for (int i = 0; i < w.length; i++) {
                mLayers[i].setWeights(w[i]);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void printWeights() {
        for (Layer mLayer : mLayers) {
            for (Neuron neuron : mLayer.neurons) {
                for (int k = 0; k < neuron.weights.length; k++) {
                    System.out.print(Base.maximumFractionDigits(2, neuron.weights[k]) + " ");
                }
                System.out.println();
            }
            System.out.println("=======");
        }
    }

}
