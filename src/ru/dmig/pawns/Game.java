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
import ru.dmig.pawns.agents.Agent;
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
    public static final int AMOUNT_OF_PAWNS = 1;

    /**
     * Interplanetary pawns array
     */
    public static Pawn[] pawns;
    
    /**
     * Interplanetary food list
     */
    public static ArrayList<Agent> foods;
    
    /**
     * Interplanetary bullets list
     */
    public static ArrayList<Agent> bullets;

    /**
     * Current generation in game
     */
    public static int generation = 0;
    
    public static UpdThread upThread;

    /**
     * Setting of minds anatomy of pawns
     */
    public static final int[] LAYERS_OF_NET = {6, 5, 4, 3};

    /**
     * Length of field to simulate
     */
    public static final int LENGTH_OF_FIELD = 1000;

    /**
     * Height of field to simulate
     */
    public static final int HEIGHT_OF_FIELD = 800;

    /**
     * Duration of one tick of game
     */
    public static final int TICK_DURATION = 50;

    /**
     * Duration of one generation playing in milliseconds
     */
    public static final double DURATION_OF_ROUND = 6 * 1000;

    /**
     * Amount of rounds (generations) to play
     */
    public static final int AMOUNT_OF_ROUNDS = 1000;

    /**
     * Chance of mutation of every paricular weight. From 0 to 100. "By definition at 100 mutation rate, every variable is chosen randomly each generation and no information is retained."
     */
    public static final int MUTATION_RATE = 5;
    
    public static final int FOOD_AMOUNT = AMOUNT_OF_PAWNS + 3;
    
    public static final int MIN_MASS_OF_FOOD = 5;
    public static final int MAX_MASS_OF_FOOD = 7;

    public static void main(String[] args) throws InterruptedException {

        newRun();

    }
    
    public static void newRun() {
        try {
            ChartPanel.lauch();
            generatePawns();
            foods = new ArrayList<>();
            bullets = new ArrayList<>();
            generateFood(FOOD_AMOUNT);
            
            Frame.panel = new Panel();
            
            java.awt.EventQueue.invokeLater(() -> {
                Frame.frame = new Frame();
                Frame.frame.setVisible(true);
            });
            
            Thread.sleep(2000);
            
            upThread = new UpdThread();
        } catch (InterruptedException ex) {
            System.exit(-12121121);
        }
    }
    
    public static void newBullet(float x, float y, double angle, double mass, Pawn author) {
        bullets.add(new Agent(Agent.MAX_BULLET_SPEED, angle, x, y, mass));
        bullets.get(bullets.size()-1).authorOfBullet = author;
        bullets.get(bullets.size()-1).t = Agent.Type.BULLET;
    }
    
    public static void generatePawns() {
        pawns = new Pawn[AMOUNT_OF_PAWNS];
        for (int i = 0; i < pawns.length; i++) {
            pawns[i] = new Pawn(Base.randomNumber(0, LENGTH_OF_FIELD), Base.randomNumber(0, HEIGHT_OF_FIELD));
            pawns[i].setAbsAngle(Base.randomNumber(0, 359));
        }
    }
    
    public static void generateFood(int amount) {
        for (int i = 0; i < amount; i++) {
            foods.add(new Agent(0,0,Base.randomNumber(0, LENGTH_OF_FIELD), 
                Base.randomNumber(0, HEIGHT_OF_FIELD),Base.randomNumber(MIN_MASS_OF_FOOD, MAX_MASS_OF_FOOD)));
        }
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
        if (pawns.length > 3) {
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
                newPawns[i] = new Pawn(Base.randomNumber(0, LENGTH_OF_FIELD), Base.randomNumber(0, HEIGHT_OF_FIELD));
                newPawns[i].network.setWeights(genomIntoWeights(newGens[i], LAYERS_OF_NET));
            }
            return newPawns;
        } else {
            double[][] newGens = new double[AMOUNT_OF_PAWNS][];
            for (int j = 0; j < newGens.length; j++) {
                newGens[j] = weightsIntoGenom(pawns[j].network.getWeights());
            }
            for (i = 0; i < newGens.length; i++) {
                for (int j = 0; j < newGens[i].length; j++) {
                    if (Base.chance(MUTATION_RATE, 0)) {
                        newGens[i][j] = Math.random(); //TODO mutation
                    }
                }
            }
            Pawn[] newPawns = new Pawn[AMOUNT_OF_PAWNS];
            for (i = 0; i < AMOUNT_OF_PAWNS; i++) {
                newPawns[i] = new Pawn(Base.randomNumber(0, LENGTH_OF_FIELD), Base.randomNumber(0, HEIGHT_OF_FIELD));
                newPawns[i].network.setWeights(genomIntoWeights(newGens[i], LAYERS_OF_NET));
            }
            return newPawns;
        }
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
        double[] chances = new double[fitnesses.length];
        int[] thousChances = new int[fitnesses.length];

        for (int i = 0; i < parentsIDs.length; i++) {
            double totalSumm = 0;
            
            for (int j = 0; j < lFitnesses.length; j++) {
                totalSumm += lFitnesses[j];
            }
            for (int j = 0; j < lFitnesses.length; j++) {
                chances[j] = (lFitnesses[j] / totalSumm) * 100;
                thousChances[j] = (int) Math.round(chances[j] * 1000);
            }
            if(Base.chance(70, 0)) {
                parentsIDs[i] = Base.chances(thousChances, 3);
            } else {
                parentsIDs[i] = Base.randomNumber(0, lFitnesses.length-1);
            }
            
            lFitnesses[parentsIDs[i]] = 0; //TODO normal deleting
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
        double fit = Arrayer.maxDoubleInArray(fitnesses);
        System.out.println("Fittest: " + fit);
        System.out.println("Average: " + avg);
        System.out.println("Food: "+pawns[0].foodGathered);
        
        ChartPanel.cp.update(fit, avg);
    }
    
    public static void exception() {
        System.err.println("EXXXXXXXXXXXXX!!1!");
    }

}
