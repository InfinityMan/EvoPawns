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

import ru.dmig.pawns.agents.Pawn;
import java.util.ArrayList;
import ru.epiclib.base.Arrayer;
import ru.epiclib.base.Base;
import ru.epiclib.evo.EvoAlg;

/**
 * Class for lauching game; main variables; static functions
 *
 * @author Dmig
 */
public class Game {

    /**
     * Amount of pawns for game
     */
    public static final int AMOUNT_OF_PAWNS = 8;

    /**
     * Interplanetary pawns array
     */
    public static Pawn[] pawns;

    /**
     * Current generation in game
     */
    public static int generation = 0;

    /**
     * Setting of minds anatomy of pawns
     */
    public static final int[] LAYERS_OF_NET = {4, 4, 3, 3};

    /**
     * Length of field to simulate
     */
    public static final int LENGTH_OF_FIELD = 600;

    /**
     * Height of field to simulate
     */
    public static final int HEIGHT_OF_FIELD = 600;

    /**
     * Duration of one tick of game
     */
    public static final int TICK_DURATION = 50;

    /**
     * Duration of one generation playing in milliseconds
     */
    public static final double DURATION_OF_ROUND = 4 * 1000;

    /**
     * Amount of rounds (generations) to play
     */
    public static final int AMOUNT_OF_ROUNDS = 50;

    /**
     * Chance of mutation of every paricular weight. From 0 to 100. "By definition at 100 mutation rate, every variable is chosen randomly each generation and no information is retained."
     */
    public static final int MUTATION_RATE = 5;

    public static void main(String[] args) throws InterruptedException {

        pawns = new Pawn[AMOUNT_OF_PAWNS];
        for (int i = 0; i < pawns.length; i++) {
            pawns[i] = new Pawn(Base.randomNumber(0, LENGTH_OF_FIELD), Base.randomNumber(0, HEIGHT_OF_FIELD));
        }

        Frame.panel = new Panel();

        java.awt.EventQueue.invokeLater(() -> {
            Frame.frame = new Frame();
            Frame.frame.setVisible(true);
        });

        Thread.sleep(2000);

        new UpdThread();

    }

    /**
     * Converts weights matrix into genom array
     *
     * @param w weights matrix for converting
     * @return genom array
     */
    public static double[] weightsIntoGenom(double[][][] w) {
        int i, j, k;
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
     * Converts genom array into weights matrix of layers
     *
     * @param g genom array for converting
     * @param layers to setting output weights matrix
     * @return weight matrix for layers
     */
    public static double[][][] genomIntoWeights(double[] g, int[] layers) {
        double[][][] ret = new double[layers.length - 1][][];
        int indexOfG = 0, neuronAmount, prevNeuronAmount;
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
     * Evolution algorithms for pawns array with ready fitnesses
     *
     * @param pawns Pawns array ready for crossover and mutation
     * @return Pawns array after crossover and mutation on start pawns
     */
    public static Pawn[] evolution(Pawn[] pawns) {
        int i;
        double[] fitnesses = new double[AMOUNT_OF_PAWNS];
        for (i = 0; i < pawns.length; i++) {
            fitnesses[i] = pawns[i].calcFitness();
        }

        //Crossover
        int[] parents = getParents(fitnesses); //test for %4
        double[][] newGens = new double[AMOUNT_OF_PAWNS][];
        for (i = 0; i < parents.length; i += 2) {
            double[] genomA = weightsIntoGenom(pawns[parents[i]].network.getWeights());
            double[] genomB = weightsIntoGenom(pawns[parents[i + 1]].network.getWeights());
            double[] genomAB = new double[genomA.length];
            double[] genomBA = new double[genomB.length];
            EvoAlg.crossover(genomA, genomB, genomAB, genomBA);
            newGens[i] = genomA;
            newGens[i + 1] = genomB;
            newGens[AMOUNT_OF_PAWNS - i - 1] = genomAB;
            newGens[AMOUNT_OF_PAWNS - i - 2] = genomBA;
        }
        //Mutation
        for (i = 0; i < newGens.length; i++) {
            for (int j = 0; j < newGens[i].length; j++) {
                if (Base.chance(MUTATION_RATE, 0)) {
                    newGens[i][j] = Math.random(); //TODO mutation
                }
            }
        }
        //Creating new pawns from gens
        Pawn[] newPawns = new Pawn[AMOUNT_OF_PAWNS];
        for (i = 0; i < AMOUNT_OF_PAWNS; i++) {
            newPawns[i] = new Pawn(pawns[i].getX(), pawns[i].getY());
            newPawns[i].network.setWeights(genomIntoWeights(newGens[i], LAYERS_OF_NET));
        }
        return newPawns;
    }

    /**
     * Calculate indexes of fittest pawns in given array
     *
     * @param fitnesses Array of fitnesses of pawns
     * @return Indexes of fittest pawns
     */
    public static int[] getParents(double[] fitnesses) {
        if (fitnesses.length % 4 != 0) {
            throw new IllegalArgumentException(); //TODO normal crossover
        }
        int[] parentsIDs = new int[fitnesses.length / 2];
        double[] lFitnesses = fitnesses;

        for (int i = 0; i < fitnesses.length / 2; i++) {
            parentsIDs[i] = Arrayer.maxDoubleInArrayID(lFitnesses);
            lFitnesses[parentsIDs[i]] = -1000000; //TODO normal deleting
        }
        return parentsIDs;
    }

    /**
     * Prints on screen info about last generation
     */
    public static void viewStats() {
        System.out.println("Generation: " + generation);
        double[] fitnesses = new double[AMOUNT_OF_PAWNS];
        for (int i = 0; i < AMOUNT_OF_PAWNS; i++) {
            fitnesses[i] = pawns[i].calcFitness();
            System.out.println(fitnesses[i]);
        }
        double avg = Arrayer.mediumValueOfArray(fitnesses);
        System.out.println("Average: " + avg);
    }

}
