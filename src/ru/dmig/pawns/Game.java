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

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
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
import ru.epiclib.base.FileWorker;

/**
 * Class for lauching game; main variables; static functions
 *
 * @author Dmig
 */
public class Game {

    /**
     * Amount of pawns for game.
     */
    public static int AMOUNT_OF_PAWNS = 80;

    public static int TURN_PAWN_AMOUNT = 10;

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
     * Setting of nn anatomy of pawns.
     */
    public static final int[] LAYERS_OF_NET = {8, 6, 2, 1};

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
    public static int TICK_DURATION = 24; // 24 is normal

    public static int CYCLE_AMOUNT = 21000;

    /**
     * Amount of rounds (generations) to play.
     */
    public static final int AMOUNT_OF_ROUNDS = 10000;

    public static int FOOD_AMOUNT = 747;

    //!!! Only 1 after point sign is work right !!!
    public static final float MIN_MASS_OF_FOOD = 4.9f;
    public static final float MAX_MASS_OF_FOOD = 5.1f;

    public static final int DANGER_ZONE = 10;

    public static final double KILLER_DAMAGE = 52;

    public static int KILLER_AMOUNT = 150;

    public static final int PAWN_SCAN_RANGE = 200;

    //When you need to eat your mass?
    public static double MASS_MOVE_TAX = 10 + 4;

    public static final boolean KILLER_ENABLED = true;
    public static final boolean KILLER_MOVING = true;
    public static final boolean SAVING_ENABLED = true;
    public static final boolean LOADING_ENABLED = true;
    public static final boolean DEBUG = false;
    public static final boolean MINI = false;

    /**
     * Generation index, when <code>new.gen</code> loads.
     */
    public static final int GENERATION_FOR_UPDATE = 1;

    public static double[] fits;

    public static final String HELP = "��������� ������������ ����� ��������� �������� ��������� ����� � ������� ������������� ���������.\n"
            + "��������� ������ �� ���� - ��� �����, ��� ��������� ������� � ������� ��������� ����, ������� �� ����:\n"
            + "���� � ��������� ���, ��������� �� ���, ���� � ��������� ������, ��������� �� ������, ���� \"����������\" ����������.\n"
            + "�� ������, ����� �������� ���� ���� ��������.\n"
            + "�������� ����-������ ����� - ���. �������� - ������.\n"
            + "������ ����� ���� �������� �������� ��������.";

    public static final String VERSION = "Dev 0.4 (shared)";

    public static void main(String[] args) throws InterruptedException {
        testDevice();
        tutorial();
        newRun(true);
    }

    public static void newRun(boolean launchGUI) {
        try {
            if (MINI) {
                AMOUNT_OF_PAWNS = 2;
                TURN_PAWN_AMOUNT = 2;
            }

            allPawns = Evolution.arrayToMatrix(Generator.generatePawns(AMOUNT_OF_PAWNS), TURN_PAWN_AMOUNT);
            pawns = allPawns[0];
            foods = new ArrayList<>();
            bullets = new ArrayList<>();
            if (KILLER_ENABLED) {
                killers = new ArrayList<>();
                for (int i = 0; i < KILLER_AMOUNT; i++) {
                    killers.add(Generator.generateKiller());
                }
            } else {
                KILLER_AMOUNT = 0;
            }
            Generator.generateFood(FOOD_AMOUNT);

            if (launchGUI) {
                ChartPanel.lauch();
                Frame.panel = new Panel();

                java.awt.EventQueue.invokeLater(() -> {
                    Frame.frame = new Frame();
                    Frame.frame.setVisible(true);
                });

                Activity.init();
                Thread.sleep(1200);
            } else {
                ChartPanel.clear();
            }

            generation = 0;
            UpdThread.killerKilled = 0;
            UpdThread.borderKilled = 0;
            UpdThread.starveKilled = 0;

            Thread.sleep(800);

            upThread = new UpdThread();
            upThread.start();
        } catch (InterruptedException ex) {
            System.exit(-12121121);
        }
    }

    protected static void tutorial() {
        if (JOptionPane.showConfirmDialog(null, "����� �������?", "�����������",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(null, HELP);
        }
    }

    private static void testDevice() {
        //Test for dir gens
        testDirectory("gens");
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
        Activity.killerKilled = (double) (UpdThread.getKillerKilled()) / (double) AMOUNT_OF_PAWNS;
        Activity.borderKilled = (double) (UpdThread.getBorderKilled()) / (double) AMOUNT_OF_PAWNS;
        Activity.starveKilled = (double) (UpdThread.getStarveKilled()) / (double) AMOUNT_OF_PAWNS;
    }

    public static void exception() {
        System.err.println("EXXXXXXXXXXXXX!!1!");
        System.exit(-122112);
    }

    public static void saveGenoms(Pawn[] pawns, String fileName) {
        testDirectory(fileName);
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
        testDirectory(fileName);
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
            pawns[i].network = new Network(gens[i], LAYERS_OF_NET);
        }
        System.out.println("Successfull load");
        return pawns;
    }

    public static double askUserValue(String message) throws NumberFormatException {
        return Double.valueOf(JOptionPane.showInputDialog(null, message, "Enter a value", JOptionPane.QUESTION_MESSAGE));
    }

    public static double askUserValue() throws NumberFormatException {
        return askUserValue("");
    }

    public static void restartGame() {
        saveGen("restGen" + generation + ".gen");
        newRun(false);
    }

    public static void setFoodAmount(int fa) {
        if (fa >= 0) {
            FOOD_AMOUNT = fa;
            foods = new ArrayList<>();
            Generator.generateFood(FOOD_AMOUNT);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static void setKillerAmount(int ka) {
        KILLER_AMOUNT = ka;
        killers = new ArrayList<>();
        for (int i = 0; i < KILLER_AMOUNT; i++) {
            killers.add(Generator.generateKiller());
        }
    }

    public static void saveGen(String fileName) {
        Game.saveGenoms(Evolution.matrixToArray(Game.allPawns), "gens" + File.separator + fileName);
    }

    public static void saveBaseGen() {
        saveGen("gen" + Game.generation + ".gen");
    }

    /**
     * Method tests dirName folder exist and create folder with this name if folder doesn't exist
     * @param dirName dirName to find or/and create
     */
    public static void testDirectory(String dirName) {
        File file = new File(dirName);
        if (!file.exists()) {
            file.mkdir();
        }
    }

}
