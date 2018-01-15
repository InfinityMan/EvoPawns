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
import ru.dmig.pawns.Evolution;
import ru.epiclib.base.Base;

/**
 * Implements the neural network
 *
 * @author Dmig
 */
public final class Network implements Serializable {

    private static final long serialVersionUID = -6200117127679042364L;

    protected double[] output;

    public Layer[] layers;

    static double sigma(double x) {
        //1.0/(1.0+e^(-1.4e*x))
        return 1.0 / (1.0 + Math.exp(-(1.4 * Math.E * x)));
    }

    public int getInputDimension() {
        if (layers == null || layers.length == 0 || layers[0] == null || layers[0].neurons == null || layers[0].neurons[0] == null || layers[0].neurons[0].weights == null) {
            return 0;
        }
        return layers[0].neurons[0].weights.length - 1;
    }

    public int getOutputDimension() {
        if (layers == null || layers.length == 0 || layers[layers.length - 1] == null || layers[layers.length - 1].neurons == null) {
            return 0;
        }
        return layers[layers.length - 1].neurons.length;
    }

    public Network(int[] layersDimensions) {
        this(layersDimensions, true);
    }

    public Network(int[] layersDimensions, boolean pause) {
        if (layersDimensions.length < 3 || layersDimensions.length > 12) {
            throw new IllegalArgumentException();
        }
        layers = new Layer[layersDimensions.length - 1];
        for (int i = 0; i < layersDimensions.length - 1; i++) {
            layers[i] = new Layer(layersDimensions[i + 1], layersDimensions[i], pause);
        }
    }

    public Network(double[] genom, int[] layers) {
        this(layers, false);
        if (genom.length != getSize(layers)) {
            throw new IllegalArgumentException();
        }
        double[] gens = new double[getSize(layers)];
        for (int i = 0; i < gens.length; i++) {
            gens[i] = genom[i];
        }
        setWeights(Evolution.genomIntoWeights(gens, layers));
    }

    public double[] calculate(final double[] input) {
        if (input.length != getInputDimension()) {
            throw new IllegalArgumentException("Wrong input dimension"); //Control for in
        }
        int i, j;

        for (i = 0; i < layers.length; i++) { //i - layerID
            Layer l = layers[i];
            for (j = 0; j < l.neurons.length; j++) { //j - neuronID
                if (i == 0) {
                    l.neurons[j].calc(input);
                } else {
                    l.neurons[j].calc(layers[i - 1]);
                }
            }
        }

        double[] out = new double[getOutputDimension()];
        Layer lastLayer = layers[layers.length - 1];
        if (out.length != lastLayer.neurons.length) {
            throw new IllegalStateException(); //Control for out
        }
        for (i = 0; i < lastLayer.neurons.length; i++) {
            out[i] = lastLayer.neurons[i].output;
        }
        return out;
    }

    public double[][][] getWeights() {
        double[][][] weights = new double[layers.length][][];
        for (int i = 0; i < weights.length; i++) {
            weights[i] = layers[i].getWeights();
        }
        return weights;
    }

    public void setWeights(double[][][] w) {
        if (w.length == layers.length) {
            for (int i = 0; i < w.length; i++) {
                layers[i].setWeights(w[i]);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void printWeights() {
        for (Layer mLayer : layers) {
            for (Neuron neuron : mLayer.neurons) {
                for (int k = 0; k < neuron.weights.length; k++) {
                    System.out.print(Base.maximumFractionDigits(2, neuron.weights[k]) + " ");
                }
                System.out.println();
            }
            System.out.println("=======");
        }
    }

    public int getSize() {
        int[] layersD = new int[layers.length + 1];
        for (int i = 0; i < layersD.length; i++) {
            if (i != 0) {
                layersD[i] = layers[i - 1].neurons.length;
            } else {
                layersD[0] = layers[0].neurons[0].weights.length - 1;
            }
        }
        return getSize(layersD);
    }

    public static int getSize(int[] layersD) {
        int size = 0;
        for (int i = 0; i < layersD.length - 1; i++) {
            size += (layersD[i] * layersD[i + 1]);
        }
        for (int i = 1; i < layersD.length; i++) {
            size += layersD[i];
        }
        return size;
    }

    public int getNeuronAmount() {
        int ret = 0;
        for (Layer layer : layers) {
            ret += layer.neurons.length;
        }
        return ret;
    }

    public Neuron[] getNeurons() {
        Neuron[] neurons = new Neuron[getNeuronAmount()];
        int index = 0;
        for (Layer layer : layers) {
            for (Neuron neuron : layer.neurons) {
                neurons[index] = neuron;
                index++;
            }
        }
        return neurons;
    }

    public void mutate() {
        int i = 0, id = Base.randomNumber(0, getNeuronAmount() - 1);
        for (Layer layer : layers) {
            for (Neuron neuron : layer.neurons) {
                if (i == id) {
                    neuron.tryToMutate();
                    return;
                } else {
                    i++;
                }
            }
        }
    }

}
