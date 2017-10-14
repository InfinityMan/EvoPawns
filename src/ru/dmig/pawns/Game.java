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

import ru.dmig.pawns.gui.ChartPanel;
import ru.dmig.pawns.gui.Frame;
import ru.dmig.pawns.gui.Panel;
import ru.dmig.pawns.agents.Pawn;
import java.util.ArrayList;
import javax.swing.JOptionPane;
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
    public static int AMOUNT_OF_PAWNS = 32;
    
    public static boolean DISTANCE_FITNESS = true;

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
    public static final int[] LAYERS_OF_NET = {5, 4, 4, 2};

    /**
     * Length of field to simulate
     */
    public static final int LENGTH_OF_FIELD = 1200;

    /**
     * Height of field to simulate
     */
    public static final int HEIGHT_OF_FIELD = 1000;

    /**
     * Duration of one tick of game
     */
    public static int TICK_DURATION = 8;

    /**
     * Duration of one generation playing in milliseconds
     */
    public static double DURATION_OF_ROUND = 6 * 1000;

    /**
     * Amount of rounds (generations) to play
     */
    public static final int AMOUNT_OF_ROUNDS = 10000;

    /**
     * Chance of mutation of every paricular weight. From 0 to 100. "By definition at 100 mutation rate, every variable is chosen randomly each generation and no information is retained."
     */
    public static final int MUTATION_RATE = 5;
    
    public static int FOOD_AMOUNT = (int) Math.ceil(AMOUNT_OF_PAWNS * 5) + 8;
    
    public static final int MIN_MASS_OF_FOOD = 5;
    public static final int MAX_MASS_OF_FOOD = 5;

    public static void main(String[] args) throws InterruptedException {
        tutorial();
        newRun();

    }
    
    public static void newRun() {
        try {
            ChartPanel.lauch();
            pawns = generatePawns();
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

    protected static void tutorial() {
        JOptionPane.showMessageDialog(null, "Здравствуйте."
                + " В общем, на графике, если он есть, синия линия - значение крутости самой сильной пешки, оранжевая - средняя крутость.");
        JOptionPane.showMessageDialog(null, "В замечательной версии -32.853, было добавлено: пешки, крутость, еда, графика -2 уровня, настройка количества пешек, и длительности раунда.");
        String amountOfPawnsStr = JOptionPane.showInputDialog(null, "Введите количество пешек для игры [По умолчанию: 16]: ");
        String timeOfRoundStr = JOptionPane.showInputDialog(null,"Введите длительность раунда (жизни одного поколения) в секундах [По умолчанию: 8]: ");
        
        int amountOfPawns;
        int timeOfRound;
        
        if (!amountOfPawnsStr.equals("")) {
            try {
                amountOfPawns = Integer.valueOf(amountOfPawnsStr);
                FOOD_AMOUNT = (int) Math.ceil(AMOUNT_OF_PAWNS * 5) + 8;
                if (amountOfPawns < 4 || amountOfPawns > 40 || amountOfPawns % 4 != 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Тут ошибка с введённым количеством пешек. Теперь оно установлено по умолчанию. Ограничения: должно быть больше 3, но меньше 41, при этом обязательно делиться на 4.");
                amountOfPawns = AMOUNT_OF_PAWNS;
            }
        } else {
            amountOfPawns = AMOUNT_OF_PAWNS;
        }
        
        if (!timeOfRoundStr.equals("")) {
            try {
                timeOfRound = Integer.valueOf(timeOfRoundStr);
                if (timeOfRound < 1 || timeOfRound > 60) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Тут ошибка с введённым количеством секунд на раунд. Теперь оно установлено по умолчанию. Ограничения: должно быть больше 0, но меньше 61.");
                timeOfRound = (int) DURATION_OF_ROUND;
            }
        } else {
            timeOfRound = (int) DURATION_OF_ROUND;
        }
        
        AMOUNT_OF_PAWNS = amountOfPawns;
        DURATION_OF_ROUND = timeOfRound;
    }
    
    public static void newBullet(float x, float y, double angle, double mass, Pawn author) {
        bullets.add(new Agent(Agent.MAX_BULLET_SPEED, angle, x, y, mass));
        bullets.get(bullets.size()-1).authorOfBullet = author;
        bullets.get(bullets.size()-1).t = Agent.Type.BULLET;
    }
    
    public static Pawn[] generatePawns() {
        Pawn[] pawns = new Pawn[AMOUNT_OF_PAWNS];
        for (int i = 0; i < pawns.length; i++) {
            pawns[i] = generatePawn();
        }
        return pawns;
    }
    
    public static Pawn generatePawn() {
        Pawn pawn = new Pawn(Base.randomNumber(LENGTH_OF_FIELD/4, LENGTH_OF_FIELD*3/4), Base.randomNumber(HEIGHT_OF_FIELD/4, HEIGHT_OF_FIELD*3/4));
        pawn.setAbsAngle(randomAngle());
        return pawn;
    }
    
    public static void generateFood(int amount) {
        for (int i = 0; i < amount; i++) {
            foods.add(generateFood());
        }
    }
    
    private static Agent generateFood() {
        float x = Base.randomNumber(20, LENGTH_OF_FIELD-20);
        float y = Base.randomNumber(20, HEIGHT_OF_FIELD-20);
        float mass = Base.randomNumber(MIN_MASS_OF_FOOD, MAX_MASS_OF_FOOD);
        
        return new Agent(0, 0, x, y, mass);
    }
    
    public static void regenerateFood(int chance) {
        for (int i = 0; i < foods.size(); i++) {
            if(Base.chance(chance, 0)) {
                foods.set(i, generateFood());
            }
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
        pawns[0].network.printWeights();
        
        double[][] newGens = new double[AMOUNT_OF_PAWNS][];
        Pawn[] newPawns = new Pawn[AMOUNT_OF_PAWNS];
        
        if (pawns.length > 3) {
            double[] fitnesses = new double[AMOUNT_OF_PAWNS];
            for (i = 0; i < pawns.length; i++) {
                fitnesses[i] = pawns[i].calcFitness(DISTANCE_FITNESS);
            }

            //Crossover
            int[] parents = getParents(fitnesses); //test for %4

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

            for (i = 0; i < AMOUNT_OF_PAWNS; i++) {
                newPawns[i] = generatePawn();
                newPawns[i].network.setWeights(genomIntoWeights(newGens[i], LAYERS_OF_NET));
            }
            
            for (int j = 0; j < newPawns.length; j++) {
                if(Base.chance(4, 0)) {
                    newPawns[j] = generatePawn();
                }
            }
        } else {
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
            newPawns = generatePawns();
            for (i = 0; i < AMOUNT_OF_PAWNS; i++) {
                newPawns[i].network.setWeights(genomIntoWeights(newGens[i], LAYERS_OF_NET));
            }
        }
        regenerateFood(50);
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
        //System.out.println("Generation: " + generation);
        double[] fitnesses = new double[AMOUNT_OF_PAWNS];
        for (int i = 0; i < AMOUNT_OF_PAWNS; i++) {
            fitnesses[i] = pawns[i].calcFitness(DISTANCE_FITNESS);
            System.out.println(fitnesses[i]);
        }
        double avg = Arrayer.mediumValueOfArray(fitnesses);
        double fit = Arrayer.maxDoubleInArray(fitnesses);
        //System.out.println("Fittest: " + fit);
        //System.out.println("Average: " + avg);
        
        ChartPanel.cp.update(fit, avg);
    }
    
    public static void exception() {
        System.err.println("EXXXXXXXXXXXXX!!1!");
    }
    
    public static double randomAngle() {
        int angle = Base.randomNumber(0, 359);
        double radAngle = angle * (Math.PI / 180);
        return radAngle;
    }

}
