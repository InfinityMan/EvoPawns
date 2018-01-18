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
import java.util.Iterator;
import ru.dmig.pawns.agents.*;
import ru.dmig.pawns.gui.Frame;
import ru.dmig.pawns.gui.Panel;
import ru.epiclib.base.Base;

/**
 * Updates the pawns, simulation, and e.t.c.
 *
 * @author Dmig
 */
public final class UpdThread extends Thread {

    public static final boolean TIME_PRINT = false;
    public static final int CYCLE_AMOUNT_BEFORE_TICK_TEST = 20;

    private static final int MAX_REMAIN = 1000;

    protected static int killerKilled = 0;
    protected static int borderKilled = 0;
    protected static int starveKilled = 0;

    private boolean stopped = false;

    private static final double KILLER_SMITE_CHANCE = 0.07;

    protected static double getAngle(float x1, float y1, float x2, float y2) {
        y1 = Game.HEIGHT_OF_FIELD - y1;
        y2 = Game.HEIGHT_OF_FIELD - y2;

        double x = x2 - x1;
        double y = y2 - y1;
        double xDiff = Math.abs(x);
        double yDiff = Math.abs(y);
        int sqr;
        double degreeToAdd;
        if (x < 0 && y > 0) {
            sqr = 2;
            degreeToAdd = Math.PI / 2;
        } else if (x < 0 && y < 0) {
            sqr = 3;
            degreeToAdd = Math.PI;
        } else if (x > 0 && y < 0) {
            sqr = 4;
            degreeToAdd = Math.PI * 3 / 2;
        } else {
            sqr = 1;
            degreeToAdd = 0;
        }
        if (sqr == 4 || sqr == 2) {
            if (yDiff == 0) {
                return 0;
            }
            return (degreeToAdd + Math.atan(xDiff / yDiff));
        } else {
            if (xDiff == 0) {
                return 0;
            }
            return (degreeToAdd + Math.atan(yDiff / xDiff));
        }
    }

    protected static double getDistance(float x1, float y1, float x2, float y2) {
        y1 = Game.HEIGHT_OF_FIELD - y1;
        y2 = Game.HEIGHT_OF_FIELD - y2;

        double xDiff = x2 - x1;
        double yDiff = y2 - y1;
        double x = Math.abs(xDiff);
        double y = Math.abs(yDiff);
        return Math.sqrt(x * x + y * y);
    }

    public static <T extends Agent> int getNearestAgent(float x, float y, ArrayList<T> agents) {
        double lastDiff = Game.LENGTH_OF_FIELD;
        int lastID = 0;
        for (int i = 0; i < agents.size(); i++) {
            Agent get = agents.get(i);
            double xDiff = Math.abs(get.getX() - x);
            double yDiff = Math.abs(get.getY() - y);
            double distance = Math.sqrt(xDiff * xDiff + yDiff * yDiff);
            if (distance < lastDiff) {
                lastID = i;
                lastDiff = distance;
            }
        }
        return lastID;
    }

    public static <T extends Agent> int getNearestAgent(float x, float y, ArrayList<T> agents, T except) {
        double lastDiff = Game.LENGTH_OF_FIELD;
        int lastID = 0;
        for (int i = 0; i < agents.size(); i++) {
            Agent get = agents.get(i);
            if (!get.equals(except)) {
                double xDiff = Math.abs(get.getX() - x);
                double yDiff = Math.abs(get.getY() - y);
                double distance = Math.sqrt(xDiff * xDiff + yDiff * yDiff);
                if (distance < lastDiff) {
                    lastID = i;
                    lastDiff = distance;
                }
            }
        }
        return lastID;
    }

    public static boolean testCollision(float x1, float y1, float x2, float y2, int rad) {
        return (x1 + rad >= x2) && (x1 - rad <= x2) && (y1 + rad >= y2) && (y1 - rad <= y2);
    }

    public static int getKillerKilled() {
        return killerKilled;
    }

    public static int getBorderKilled() {
        return borderKilled;
    }

    public static void incStarveKilled() {
        starveKilled++;
    }

    public static int getStarveKilled() {
        return starveKilled;
    }

    public static Pawn[] getPawnArray(Pawn[][] matrix) {
        return matrixToArray(matrix);
    }

    public static Pawn[] matrixToArray(Pawn[][] matrix) {
        if (matrix == null) {
            throw new IllegalArgumentException("Matrix is null");
        }
        if (matrix.length == 0) {
            throw new IllegalArgumentException("Matrix have size 0");
        }
        if (matrix[0].length == 0) {
            throw new IllegalArgumentException("Matrix element 0 have size 0");
        }
        Pawn[] ret = new Pawn[matrix.length * matrix[0].length];
        int len = matrix[0].length;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < len; j++) {
                ret[i * len + j] = matrix[i][j];
            }
        }
        return ret;
    }

    public static Pawn[][] arrayToMatrix(Pawn[] array, int period) {
        Pawn[][] ret = new Pawn[array.length / period][period];
        for (int i = 0; i < array.length; i++) {
            //if (i % period == 0 && i != 0) {
            ret[i / period][i % period] = array[i];
            //}
        }
        return ret;
    }
    private int newTickDuration = Game.TICK_DURATION;
    private int remainingCycles = MAX_REMAIN;

    public UpdThread() {
    }

    @Override
    public void run() {
        if (!stopped) {
            for (int i = 0; i < Game.AMOUNT_OF_ROUNDS; i++) {
                simulateGeneration();
                if (stopped) {
                    break;
                }
            }
        }
    }
    
    public void normalStop() {
        stopped = true;
    }

    @SuppressWarnings("SleepWhileInLoop")
    private void simulateGeneration() {
        Game.generation++;

        double timeIn = System.currentTimeMillis();

        if (TIME_PRINT) {
            System.out.print("Cycles: ");
        }

        for (int i = 0; i < (Game.AMOUNT_OF_PAWNS / Game.TURN_PAWN_AMOUNT); i++) {
            Game.pawns = Game.allPawns[i];

            simulateRound();
            if (stopped) {
                break;
            }
        }
        if (!stopped) {
            if (TIME_PRINT) {
                System.out.print("\nSIM: " + (System.currentTimeMillis() - timeIn) + "\nEVO: ");
            }

            Game.viewStats(Game.pawns);
            killerKilled = 0;
            borderKilled = 0;
            starveKilled = 0;

            timeIn = System.currentTimeMillis();

            Game.allPawns = arrayToMatrix(Evolution.evolution(getPawnArray(Game.allPawns)), Game.TURN_PAWN_AMOUNT);

            if (TIME_PRINT) {
                System.out.println(System.currentTimeMillis() - timeIn);
            }

            System.gc();

            Generator.regenerateFood(100);
            if (Game.generation % 5 == 0) {
                if (Game.KILLER_ENABLED) {
                    Generator.regenerateKillers(91);
                }
                if (Game.SAVING_ENABLED && Game.generation % 50 == 0) {
                    Game.saveBaseGen();
                } else {
                    if (Game.KILLER_ENABLED) {
                        Generator.regenerateKillers(23);
                    }
                }
            }
        }

    }

    private void simulateRound() {
        int cyc = 0;
        for (cyc = 0; cyc < Game.CYCLE_AMOUNT; cyc++) {
            if (simulateTick(cyc)) {
                break;
            }
        }
        if (TIME_PRINT) {
            System.out.print(cyc + " ");
        }
        Generator.regenerateFood(75);
    }

    private boolean simulateTick(int cyc) {
        if (stopped) {
            return true;
        }
        int tick = Game.TICK_DURATION;
        boolean isTickZero = (tick == 0);
        //Changing global speed in Cycling
        if (cyc % CYCLE_AMOUNT_BEFORE_TICK_TEST == 0) {
            if (newTickDuration != tick) {
                tick = newTickDuration;
                Game.TICK_DURATION = newTickDuration;
            }

            isTickZero = (tick == 0);
        }
        if (cyc == Game.CYCLE_AMOUNT / 2) {
            Generator.regenerateFood(24);
        }
        if (!processPawns()) {
            return true;
        }
        if (Game.KILLER_ENABLED) {
            processKillers();
        }
        collisionSensor();
        if (remainingCycles == 0) {
            return true;
        }
        if (!isTickZero) {
            try {
                Thread.sleep(tick);
            } catch (InterruptedException ex) {
                Game.exception();
            }
        }
        Frame.frame.update();
        return false;
    }

    private boolean processPawns() {
        float distSum = 0;
        for (Pawn pawn : Game.pawns) {
            if (pawn.isAlive()) {
                setRelativeToFood(pawn);
                if (Game.KILLER_ENABLED) {
                    setRelativeToEnemy(pawn);
                }
                pawn.calculate(Game.DEBUG);
                distSum += pawn.updateCoords(true);
                dangerZoneProcess(pawn);
            }
        }
        return distSum >= 1;
    }

    private void processKillers() {
        for (int i = 0; i < Game.killers.size(); i++) {
            if (Game.killers.get(i).isInDangerZone()) {
                Game.killers.get(i).placeInRandomPosition();
            } else if (Game.KILLER_MOVING) {
                Game.killers.get(i).updateCoords(Killer.MAX_SPEED);
            }
        }
    }

    private void dangerZoneProcess(Pawn p) {
        if (p.isInDangerZone()) {
            p.addPenalty(Pawn.ZONE_PENALTY);
            p.attack(5);
            if (p.isAlive()) {
                if (Base.chance(7, 0)) {
                    p.smite();
                    borderKilled++;
                }
            }
        }
    }

    private void collisionSensor() {

        for (Pawn pawn : Game.pawns) {
            if (pawn.isAlive()) {
                for (Agent bullet : Game.bullets) {
                    if (testCollision(pawn.getX(), pawn.getY(), bullet.getX(), bullet.getY(), Panel.PAWN_DIAMETER / 2)) {
                        if (bullet.authorOfBullet != pawn) {
                            pawn.attack(bullet.getMass());
                        }
                    }
                }
                synchronized (Game.foods) {
                    int deleted = 0;
                    for (Iterator<Agent> itr = Game.foods.iterator(); itr.hasNext();) {
                        Agent food = itr.next();
                        if (pawn.getX() + Panel.PAWN_DIAMETER / 2 >= food.getX() && pawn.getX() - Panel.PAWN_DIAMETER / 2 <= food.getX() && pawn.getY() + Panel.PAWN_DIAMETER / 2 >= food.getY() && pawn.getY() - Panel.PAWN_DIAMETER / 2 <= food.getY()) {
                            pawn.feed(food.getMass());
                            itr.remove();
                            deleted++;
                        }
                    }
                    if (deleted == 0) {
                        remainingCycles--;
                    } else {
                        Generator.generateFood(deleted);
                        remainingCycles = MAX_REMAIN;
                    }
                }
                if (Game.KILLER_ENABLED) {
                    for (Iterator<Killer> it = Game.killers.iterator(); it.hasNext();) {
                        Killer killer = it.next();
                        if (testCollision(pawn.getX(), pawn.getY(), killer.getX(), killer.getY(), Panel.PAWN_DIAMETER / 2)) {
                            pawn.attack(Game.KILLER_DAMAGE);
                            if (Base.chance((int) KILLER_SMITE_CHANCE * 100, 0)) {
                                pawn.smite();
                            }
                            if (!pawn.isAlive()) {
                                killerKilled++;
                            }
                        }
                    }
                }
            }
        }
    }

    private void setRelativeToFood(Pawn p) {
        Agent food = Game.foods.get(getNearestAgent(p.getX(), p.getY(), Game.foods));
        Agent altFood = Game.foods.get(getNearestAgent(p.getX(), p.getY(), Game.foods, food));

        p.setRltAngleToFood(getAngle(p.getX(), p.getY(), food.getX(), food.getY()));
        p.setDistToFood((float) getDistance(p.getX(), p.getY(), food.getX(), food.getY()));

        p.setRltAngleToAltFood(getAngle(p.getX(), p.getY(), altFood.getX(), altFood.getY()));
        p.setDistToAltFood((float) getDistance(p.getX(), p.getY(), altFood.getX(), altFood.getY()));
    }

    private void setRelativeToEnemy(Pawn p) {
        final Agent killer = Game.killers.get(getNearestAgent(p.getX(), p.getY(), Game.killers));

        p.setRltAngleToEnemy(getAngle(p.getX(), p.getY(), killer.getX(), killer.getY()));
        //p.setAbsAngleOfEnemy(killer.getAbsAngle());
        p.setDistToEnemy((float) getDistance(p.getX(), p.getY(), killer.getX(), killer.getY()));
    }

    /**
     *
     * @param half If half new speed will be half of current; if false speed doubles
     */
    public boolean changeSpeed(boolean half) {
        if (half) {
            if (Game.TICK_DURATION > 0) {
                newTickDuration = newTickDuration * 2;
            } else {
                newTickDuration = 12;
            }
            return true;
        } else {
            if (newTickDuration > 12) {
                newTickDuration = newTickDuration / 2;
                return true;
            } else if (newTickDuration != 0) {
                newTickDuration = 0;
                return true;
            }
        }
        return false;
    }

}
