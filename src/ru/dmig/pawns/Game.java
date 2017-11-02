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
import java.util.Arrays;
import java.util.Calendar;
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
import ru.dmig.pawns.net.Neuron;
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
    public static int AMOUNT_OF_PAWNS = 240;

    public static int TURN_PAWN_AMOUNT = 8;

    /**
     * Interplanetary pawns array.
     */
    public static Pawn[] pawns;

    public static Pawn[][] allPawns;

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
    public static final int[] LAYERS_OF_NET = {6, 8, 4, 2};

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
    public static int TICK_DURATION = 2; // 20 is normal

    public static int CYCLE_AMOUNT = 10000;

    /**
     * Amount of rounds (generations) to play.
     */
    public static final int AMOUNT_OF_ROUNDS = 9000;


    public static int FOOD_AMOUNT = (int) Math.ceil(AMOUNT_OF_PAWNS * 5) + 8;

    public static final int MIN_MASS_OF_FOOD = 5;
    public static final int MAX_MASS_OF_FOOD = 5;

    public static final int DANGER_ZONE = 23;

    public static final double KILLER_DAMAGE = 45;

    public static final int KILLER_AMOUNT = 38;

    public static final int PAWN_SCAN_RANGE = 200;
    
    public static final boolean KILLER_ENABLED = true;
    public static final boolean SAVING_ENABLED = true;

    /**
     * Generation index, when <code>new.gen</code> loads.
     */
    public static final int GENERATION_FOR_UPDATE = 2;
    
    public static double[] fits;

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
            allPawns = Evolution.arrayToMatrix(Generator.generatePawns(AMOUNT_OF_PAWNS), TURN_PAWN_AMOUNT);
            pawns = allPawns[0];
            foods = new ArrayList<>();
            bullets = new ArrayList<>();
            if (KILLER_ENABLED) {
                killers = new ArrayList<>();
                for (int i = 0; i < KILLER_AMOUNT; i++) {
                    killers.add(Generator.generateKiller());
                }
            }
            Generator.generateFood(FOOD_AMOUNT);

            Frame.panel = new Panel();

            java.awt.EventQueue.invokeLater(() -> {
                Frame.frame = new Frame();
                Frame.frame.setVisible(true);
            });

            Activity.init();

            Thread.sleep(2000);

            upThread = new UpdThread();
            upThread.start();
        } catch (InterruptedException ex) {
            System.exit(-12121121);
        }
    }

    protected static void tutorial() {
        int set = JOptionPane.showConfirmDialog(null, "Нужна справка/настройки?", "Настройка", JOptionPane.YES_NO_OPTION);
        if (set == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(null, HELP);
            int maxSpeed = JOptionPane.showConfirmDialog(null, "Установить максимальную скорость?", "Скорость", JOptionPane.YES_NO_OPTION);
            if (maxSpeed == JOptionPane.YES_OPTION) {
                TICK_DURATION = 0;
            }

            String amountOfPawnsStr = JOptionPane.showInputDialog(null,
                    "Введите количество пешек для игры [По умолчанию: 16]: ");

            int amountOfPawns;

            if (!amountOfPawnsStr.isEmpty()) {
                try {
                    amountOfPawns = Integer.valueOf(amountOfPawnsStr);

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
            FOOD_AMOUNT = 320;
        }

    }


    /**
     * Prints on screen info about pawns successes
     *
     * @param pawns Pawn[] to print
     */
    public static void viewStats(Pawn[] pawns) {
        double[] fitnesses = new double[pawns.length];
        for (int i = 0; i < pawns.length; i++) {
            fitnesses[i] = pawns[i].calcFitness();
        }
        double avg = Arrayer.mediumValueOfArray(fitnesses);
        double fit = Arrayer.maxDoubleInArray(fitnesses);

        ChartPanel.cp.update(fit, avg);

        Activity.fittest = fit;
        Activity.avg = avg;
        Activity.killerKilled = (double) (UpdThread.getKillerKilled()) / AMOUNT_OF_PAWNS;
        Activity.borderKilled = (double) (UpdThread.getBorderKilled()) / AMOUNT_OF_PAWNS;
    }

    public static void exception() {
        System.err.println("EXXXXXXXXXXXXX!!1!");
        System.exit(-122112);
    }

    public static void saveGenoms(Pawn[] pawns, String fileName) {
        double[][] genoms = new double[pawns.length][];
        for (int i = 0; i < genoms.length; i++) {
            genoms[i] = Evolution.getGenomFromNet(pawns[i].network);
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
        if (Network.getGenomSize(LAYERS_OF_NET) != len) {
            throw new ParsingException("Invalid model of neural network");
        }
        Pawn[] pawns = new Pawn[genoms.length];
        for (int i = 0; i < pawns.length; i++) {
            pawns[i] = Generator.generatePawn();
            pawns[i].network = new Network(gens[i], LAYERS_OF_NET);
        }
        return pawns;
    }

    
}
