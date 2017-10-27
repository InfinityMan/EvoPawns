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

import java.util.ArrayList;
import java.util.Calendar;
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

    private int newTickDuration = Game.TICK_DURATION;

    public UpdThread() {
    }

    @Override
    public void run() {
        for (int i = 0; i < Game.AMOUNT_OF_ROUNDS; i++) {
            simulateRound();
        }
    }

    @SuppressWarnings("SleepWhileInLoop")
    private void simulateRound() {
        Game.generation++;
        int tick = Game.TICK_DURATION;

        double timeIn = System.currentTimeMillis();
        for (int i = 0; i < (Game.AMOUNT_OF_PAWNS / Game.TURN_PAWN_AMOUNT); i++) {
            Game.pawns = Game.allPawns[i];

            //Changing global speed in start of new round
            if (newTickDuration != tick) {
                tick = newTickDuration;
                Game.TICK_DURATION = newTickDuration;
            }

            final boolean isTickZero = (tick == 0);
            for (int cyc = 0; cyc < Game.CYCLE_AMOUNT; cyc++) {
                if (cyc == Game.CYCLE_AMOUNT / 2) {
                    Generator.regenerateFood(20);
                }

                processPawns();
                processKillers();

                collisionSensor();

                if (!isTickZero) {
                    try {
                        Thread.sleep(tick);
                    } catch (InterruptedException ex) {
                        System.exit(-122112);
                    }
                }
                Frame.frame.update();
            }
        }
        System.err.print((System.currentTimeMillis() - timeIn) + ": ");
        Game.viewStats(Game.pawns);
        timeIn = System.currentTimeMillis();
        Game.allPawns = Game.arrayToMatrix(Game.evolution(Game.getPawnArray(Game.allPawns)), Game.TURN_PAWN_AMOUNT);
        System.err.println(System.currentTimeMillis() - timeIn);
    }

    private void processPawns() {
        for (Pawn pawn : Game.pawns) {
            if (pawn.isAlive()) {
                setRelativeToFood(pawn);
                setRelativeToEnemy(pawn);
                pawn.calculate(false);
                pawn.updateCoords();
                dangerZoneProcess(pawn);
            }
        }
    }

    private void processKillers() {
        for (int i = 0; i < Game.killers.size(); i++) {
            if (Game.killers.get(i).isInDangerZone()) {
                Game.killers.get(i).placeInRandomPosition();
            } else {
                //Game.killers.get(i).updateCoords(Killer.MAX_SPEED);
            }
        }
    }

    private void dangerZoneProcess(Pawn p) {
        if (p.isInDangerZone()) {
            p.dangerZonePenalty++;
            p.attack(5);
            if (p.isAlive()) {
                if (Base.chance(7, 0)) {
                    p.smite();
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
                    Generator.generateFood(deleted);
                }
                for (Iterator<Killer> it = Game.killers.iterator(); it.hasNext();) {
                    Killer killer = it.next();
                    if (testCollision(pawn.getX(), pawn.getY(), killer.getX(), killer.getY(), Panel.PAWN_DIAMETER / 2)) {
                        pawn.attack(Game.KILLER_DAMAGE);
                        if (Base.chance(60, 0)) {
                            pawn.smite();
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
            return (degreeToAdd + Math.atan(xDiff / yDiff));
        } else {
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

    private void setRelativeToEnemy(Pawn p) {
        final Agent killer = Game.killers.get(getNearestAgent(p.getX(), p.getY(), Game.killers));

        p.setRltAngleToEnemy(getAngle(p.getX(), p.getY(), killer.getX(), killer.getY()));
        //p.setAbsAngleOfEnemy(killer.getAbsAngle());
        p.setDistToEnemy((float) getDistance(p.getX(), p.getY(), killer.getX(), killer.getY()));
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

    /**
     *
     * @param half If half new speed will be half of current; if false speed doubles
     */
    public boolean changeSpeed(boolean half) {
        if (half) {
            if(Game.TICK_DURATION > 0) {
                newTickDuration = newTickDuration * 2;
            } else {
                newTickDuration = 24;
            }
            return true;
        } else {
            if (newTickDuration > 12) {
                newTickDuration = newTickDuration / 2;
                return true;
            } else if(newTickDuration == 12) {
                newTickDuration = 0;
                return true;
            }
        }
        return false;
    }

    public static boolean testCollision(float x1, float y1, float x2, float y2, int rad) {
        return (x1 + rad >= x2) && (x1 - rad <= x2) && (y1 + rad >= y2) && (y1 - rad <= y2);
    }

}
