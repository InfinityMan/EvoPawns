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

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import ru.dmig.pawns.agents.Agent;
import ru.dmig.pawns.agents.Killer;
import ru.dmig.pawns.agents.Pawn;
import ru.dmig.pawns.gui.Activity;
import ru.dmig.pawns.gui.ChartPanel;
import ru.dmig.pawns.gui.Frame;
import ru.dmig.pawns.gui.Panel;
import ru.dmig.pawns.net.Network;
import ru.epiclib.base.Arrayer;
import ru.epiclib.base.Base;
import ru.epiclib.base.FileWorker;
import ru.epiclib.evo.EvoAlg;

/**
 * Class for lauching game; main variables; static functions
 *
 * @author Dmig
 */
public class Game {

    /**
     * Amount of pawns for game.
     */
    public static int AMOUNT_OF_PAWNS = 32;

    /**
     * Interplanetary pawns array.
     */
    public static Pawn[] pawns;

    /**
     * Interplanetary food list.
     */
    public static volatile ArrayList<Agent> foods;

    /**
     * Interplanetary bullets list.
     */
    public static ArrayList<Agent> bullets;
    
    public static ArrayList<Killer> killers;

    /**
     * Current generation in game.
     */
    public static int generation = 0;

    public static UpdThread upThread;

    /**
     * Setting of minds anatomy of pawns.
     */
    public static final int[] LAYERS_OF_NET = {9, 8, 5, 3, 3};

    /**
     * Length of field to simulate.
     */
    public static final int LENGTH_OF_FIELD = 1200;

    /**
     * Height of field to simulate.
     */
    public static final int HEIGHT_OF_FIELD = 1000;

    /**
     * Duration of one tick of game.
     */
    public static int TICK_DURATION = 20; // 2 is min

    /**
     * Duration of one generation playing in milliseconds.
     */
    public static double DURATION_OF_ROUND = 16 * 1000; //1500 is min

    /**
     * Amount of rounds (generations) to play.
     */
    public static final int AMOUNT_OF_ROUNDS = 10000;

    /**
     * Chance of mutation of every paricular weight. From 0 to 100. "By definition at 100 mutation rate, every variable is chosen randomly each generation and no information is retained."
     */
    public static final int MUTATION_RATE = 5;

    public static int FOOD_AMOUNT = (int) Math.ceil(AMOUNT_OF_PAWNS * 5) + 8;

    public static final int MIN_MASS_OF_FOOD = 5;
    public static final int MAX_MASS_OF_FOOD = 5;

    public static final int DANGER_ZONE = 25;

    public static final double KILLER_DAMAGE = 45;
    
    public static final int KILLER_AMOUNT = 30;

    /**
     * Generation index, when <code>new.gen</code> loads.
     */
    public static final int GENERATION_FOR_UPDATE = 5;
    
    public static final String HELP = "Программа представляет собой симулятор развития нейронных сетей с помощью эволюционного алгоритма.\n"
            + "Оранжевый кружок на поле - это пешка, она принимает решения с помощью нейронной сети, получая на вход:\n"
            + "Свою скорость, свой угл движения, угол к ближайшей еде, угол к ближайшей угрозе, дистанцию до угрозы, свои координаты.\n"
            + "На выходе, пешка изменяет свою скорость и угол движения.\n"
            + "Кружочки тёмно-синего цвета - еда.";

    public static void main(String[] args) throws InterruptedException {
        tutorial();
        newRun();

    }

    public static void newRun() {
        try {
            ChartPanel.lauch();
            pawns = Generator.generatePawns();
            foods = new ArrayList<>();
            bullets = new ArrayList<>();
            killers = new ArrayList<>();
            Generator.generateFood(FOOD_AMOUNT);
            for (int i = 0; i < KILLER_AMOUNT; i++) {
                killers.add(Generator.generateKiller());
            }

            Frame.panel = new Panel();

            java.awt.EventQueue.invokeLater(() -> {
                Frame.frame = new Frame();
                Frame.frame.setVisible(true);
            });

            Activity.init();

            Thread.sleep(2000);

            upThread = new UpdThread();
        } catch (InterruptedException ex) {
            System.exit(-12121121);
        }
    }

    protected static void tutorial() {
        int set = JOptionPane.showConfirmDialog(null, "Нужна справка/настройки?", "Настройка", JOptionPane.YES_NO_OPTION);
        if (set == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(null, HELP);
            int maxSpeed = JOptionPane.showConfirmDialog(null, "Установить максимальную скорость?", "Скорость", JOptionPane.YES_NO_OPTION);
            if(maxSpeed == JOptionPane.YES_OPTION) {
                TICK_DURATION = 2;
                DURATION_OF_ROUND = 1.5 * 1000;
            }
            
            String amountOfPawnsStr = JOptionPane.showInputDialog(null,
                    "Введите количество пешек для игры [По умолчанию: 16]: ");
            
            int amountOfPawns;
            
            if (!amountOfPawnsStr.isEmpty()) {
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

            AMOUNT_OF_PAWNS = amountOfPawns;
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
        if (generation == GENERATION_FOR_UPDATE) {
            try {
                return loadGenoms("gens//new.gen");
            } catch (FileNotFoundException ex) {
            } catch (ParsingException ex) {
                ex.printStackTrace();
            }
        }
        int i;

        double[][] newGens = new double[AMOUNT_OF_PAWNS][];
        Pawn[] newPawns = new Pawn[AMOUNT_OF_PAWNS];

        if (pawns.length
                > 3) {
            double[] fitnesses = new double[AMOUNT_OF_PAWNS];
            for (i = 0; i < pawns.length; i++) {
                fitnesses[i] = pawns[i].calcFitness();
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
                newPawns[i] = Generator.generatePawn();
                newPawns[i].network.setWeights(genomIntoWeights(newGens[i], LAYERS_OF_NET));
            }

            for (i = 0; i < newPawns.length; i++) {
                if (Base.chance(4, 0)) {
                    newPawns[i] = Generator.generatePawn();
                }
            }
        } else {
            for (i = 0; i < newGens.length; i++) {
                newGens[i] = weightsIntoGenom(pawns[i].network.getWeights());
            }
            for (i = 0; i < newGens.length; i++) {
                for (int j = 0; j < newGens[i].length; j++) {
                    if (Base.chance(MUTATION_RATE, 0)) {
                        newGens[i][j] = Math.random(); //TODO mutation
                    }
                }
            }
            newPawns = Generator.generatePawns();
            for (i = 0; i < AMOUNT_OF_PAWNS; i++) {
                newPawns[i].network.setWeights(genomIntoWeights(newGens[i], LAYERS_OF_NET));
            }
        }
        if (generation % 5 == 0) {
            Generator.regenerateFood(100);
            Generator.regenerateKillers(91);
            if (generation % 10 == 0) {
                saveGenoms(newPawns, "gens//gen" + generation + ".gen");
            }
        } else {
            Generator.regenerateFood(50);
            Generator.regenerateKillers(20);
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
            if (Base.chance(70, 0)) {
                parentsIDs[i] = Base.chances(thousChances, 3);
            } else {
                parentsIDs[i] = Base.randomNumber(0, lFitnesses.length - 1);
            }

            lFitnesses[parentsIDs[i]] = 0; //TODO normal deleting
        }
        return parentsIDs;
    }

    /**
     * Prints on screen info about last generation
     */
    public static void viewStats() {
        double[] fitnesses = new double[AMOUNT_OF_PAWNS];
        for (int i = 0; i < AMOUNT_OF_PAWNS; i++) {
            fitnesses[i] = pawns[i].calcFitness();
        }
        double avg = Arrayer.mediumValueOfArray(fitnesses);
        double fit = Arrayer.maxDoubleInArray(fitnesses);

        ChartPanel.cp.update(fit, avg);

        double[] pens = new double[AMOUNT_OF_PAWNS];
        for (int i = 0; i < pens.length; i++) {
            pens[i] = pawns[i].dangerZonePenalty;
        }
    }

    public static void exception() {
        System.err.println("EXXXXXXXXXXXXX!!1!");
    }

    public static void saveGenoms(Pawn[] pawns, String fileName) {
        double[][] genoms = new double[pawns.length][];
        for (int i = 0; i < genoms.length; i++) {
            genoms[i] = weightsIntoGenom(pawns[i].network.getWeights());
        }

        String genomsStr = "";
        for (int i = 0; i < genoms.length; i++) {
            if (i != 0) {
                genomsStr += "\n";
            }
            for (int j = 0; j < genoms[i].length; j++) {
                if (j != 0) {
                    genomsStr += ";" + genoms[i][j];
                } else {
                    genomsStr += genoms[i][j];
                }
            }
        }

        FileWorker.write(fileName, genomsStr);
    }

    public static Pawn[] loadGenoms(String fileName) throws FileNotFoundException, ParsingException {
        String[] genoms = FileWorker.read(fileName).split("\n");
        double[][] gens = new double[genoms.length][];
        int len = 0;
        for (int i = 0; i < genoms.length; i++) {
            String[] gensStr = genoms[i].split(";");
            if (i == 0) {
                len = gensStr.length;
            }
            if (len == gensStr.length) {
                gens[i] = new double[len];
                for (int j = 0; j < len; j++) {
                    try {
                        gens[i][j] = Double.valueOf(gensStr[j]);
                    } catch (NumberFormatException ex) {
                        throw new ParsingException("NumberFormatException: " + ex);
                    }
                }
            } else {
                throw new ParsingException("Lengths of genoms are different");
            }

        }
        if (Network.getSize(LAYERS_OF_NET) != len) {
            throw new ParsingException("Invalid model of neural network");
        }
        Pawn[] pawns = new Pawn[genoms.length];
        for (int i = 0; i < pawns.length; i++) {
            pawns[i] = Generator.generatePawn();
            pawns[i].network = new Network(LAYERS_OF_NET);
            pawns[i].network.setWeights(genomIntoWeights(gens[i], LAYERS_OF_NET));
        }
        return pawns;
    }

}
