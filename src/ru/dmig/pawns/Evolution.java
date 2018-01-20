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
package ru.dmig.pawns;

import java.io.FileNotFoundException;
import static java.lang.System.currentTimeMillis;
import java.util.ArrayList;
import java.util.Arrays;
import ru.dmig.pawns.agents.Pawn;
import ru.dmig.pawns.net.Network;
import ru.epiclib.base.Arrayer;
import ru.epiclib.base.Base;
import ru.epiclib.evo.EvoAlg;

/**
 *
 * @author Dmig
 */
public final class Evolution {

    /**
     * Chance of mutation of every paricular weight. From 0 to 100. "By definition at 100 mutation rate, every variable is chosen randomly each generation and no information is retained."
     */
    public static int MUTATION_RATE = 17;
    public static int ELITE_AMOUNT = 4;

    public static double fitTime = 0;
    public static double parentsTime = 0;
    public static double crossoverTime = 0;
    public static double pawnsTime = 0;
    public static double mutTime = 0;

    public static Pawn[] mutate(Pawn[] pawns) {
        //Start point to 'ELITE_AMOUNT' because elite don't mutate
        for (int i = ELITE_AMOUNT; i < pawns.length; i++) {
            if (Base.chance(MUTATION_RATE, 0)) {
                pawns[i].network.mutate();
            }
        }
        return pawns;
    }

    public static double[] getGenomFromNet(Network net) {
        return weightsIntoGenom(net.getWeights());
    }

    /**
     * Calculate indexes of fittest pawns in given array
     *
     * @param fitnesses Sorted and reversed array of fitnesses of pawns
     * @return Indexes of fittest pawns
     */
    public static int[] getParents(double[] fitnesses) {
        if (fitnesses.length % 4 != 0 && !Game.MINI) {
            throw new IllegalArgumentException(); //TODO normal crossover
        }

        final int len = fitnesses.length;
        int[] parentsIDs = new int[len];

        //Moving elite to parents first
        for (int i = 0; i < ELITE_AMOUNT; i++) {
            parentsIDs[i] = i;
        }

        //Moving other parents to array
        for (int i = ELITE_AMOUNT; i < len; i++) {
            final int idx = Base.randomNumber(0, fitnesses.length - 1);
            int idx2 = idx;
            while (idx == idx2) {
                idx2 = Base.randomNumber(0, fitnesses.length - 1);
            }
            if (fitnesses[idx] > fitnesses[idx2]) {
                parentsIDs[i] = idx;
            } else {
                parentsIDs[i] = idx2;
            }
        }

        return parentsIDs;
    }

    /**
     * Converts genom array into weights matrix of layers
     *
     * @param g genom array for converting
     * @param layers to setting output weights matrix
     * @return weight matrix for layers
     */
    public static double[][][] genomIntoWeights(double[] g, int[] layers) {
        double[][][] ret = new double[layers.length - 1][][];
        int indexOfG = 0;
        int neuronAmount;
        int prevNeuronAmount;
        for (int i = 0; i < layers.length - 1; i++) {
            prevNeuronAmount = layers[i];
            neuronAmount = layers[i + 1];
            ret[i] = new double[neuronAmount][];
            for (int j = 0; j < neuronAmount; j++) {
                ret[i][j] = new double[prevNeuronAmount + 1];
                for (int k = 0; k < prevNeuronAmount + 1; k++) {
                    ret[i][j][k] = g[indexOfG];
                    indexOfG++;
                }
            }
        }
        return ret;
    }

    /**
     * Converts weights matrix into genom array
     *
     * @param w weights matrix for converting
     * @return genom array
     */
    public static double[] weightsIntoGenom(double[][][] w) {
        int i;
        int j;
        int k;
        ArrayList<Double> retu = new ArrayList<>();
        for (i = 0; i < w.length; i++) {
            for (j = 0; j < w[i].length; j++) {
                for (k = 0; k < w[i][j].length; k++) {
                    retu.add(w[i][j][k]);
                }
            }
        }
        double[] ret = new double[retu.size()];
        for (i = 0; i < retu.size(); i++) {
            ret[i] = retu.get(i);
        }
        return ret;
    }

    /**
     * Evolution algorithms for pawns array with ready fitnesses
     *
     * @param pawns Pawns array ready for crossover and mutation
     * @return Pawns array after crossover and mutation on start pawns
     */
    public static Pawn[] evolution(Pawn[] pawns) {
        double timeIn = currentTimeMillis();
        int i;
        double[][] newGens = new double[pawns.length][];
        Pawn[] newPawns = new Pawn[pawns.length];
        double[] fitnesses = new double[pawns.length];

        for (i = 0; i < pawns.length; i++) {
            fitnesses[i] = pawns[i].calcFitness();
        }

        Arrays.sort(fitnesses);
        Arrays.sort(pawns);
        Arrayer.reverseArray(fitnesses);
        Arrayer.reverseArray(pawns);

        fitTime = currentTimeMillis() - timeIn;
        timeIn = currentTimeMillis();

        //Get Parents
        final int[] parents = getParents(fitnesses); //test for %4

        parentsTime = currentTimeMillis() - timeIn;
        timeIn = currentTimeMillis();

        crossover(parents, pawns, newGens);

        crossoverTime = currentTimeMillis() - timeIn;
        timeIn = currentTimeMillis();

        //Creating new pawns from gens
        for (i = 0; i < pawns.length; i++) {
            if (Base.chance(97, 0)) {
                newPawns[i] = Generator.generatePawn(new Network(newGens[i], Game.LAYERS_OF_NET));
            } else {
                newPawns[i] = Generator.generatePawn();
            }
        }
        pawnsTime = currentTimeMillis() - timeIn;
        timeIn = currentTimeMillis();

        //Mutation
        mutate(newPawns);
        mutTime = currentTimeMillis() - timeIn;

        for (int j = 0; j < pawns.length; j++) {
            pawns[j] = null;
        }
        pawns = null;
        return newPawns;
    }

    private static void crossover(final int[] parents, Pawn[] pawns, double[][] newGens) {
        //Elite
        for (int i = 0; i < ELITE_AMOUNT; i++) {
            newGens[i] = getGenomFromNet(pawns[parents[i]].network);
        }

        //Crossover
        int idx = ELITE_AMOUNT;
        for (int i = 0; i < parents.length - ELITE_AMOUNT; i++) {
            final double[] genomA = getGenomFromNet(pawns[parents[i]].network);
            final double[] genomB = getGenomFromNet(pawns[parents[i + 1]].network);
            newGens[idx] = EvoAlg.crossover(genomA, genomB);
            idx++;
        }
    }

}
