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
public class UpdThread extends Thread {

    private int newTickDuration = Game.TICK_DURATION;
    private double newDurationOfRound = Game.DURATION_OF_ROUND;

    public UpdThread() {
        this.start();
    }

    @Override
    public void run() {
        for (int i = 0; i < Game.AMOUNT_OF_ROUNDS; i++) {
            simulateRound();
        }
    }

    private void simulateRound() {
        Game.generation++;
    
        for (int i = 0; i < (Game.AMOUNT_OF_PAWNS/Game.TURN_PAWN_AMOUNT); i++) {
            Game.pawns = Game.allPawns[i];
            
            //Changing global speed in start of new round
            Game.TICK_DURATION = newTickDuration;
            Game.DURATION_OF_ROUND = newDurationOfRound;
            

            final double startTime = Calendar.getInstance().getTimeInMillis();
            final double finishTime = startTime + Game.DURATION_OF_ROUND;
            double currentTime = startTime;
            
            double halfOfRoundTime = startTime + Game.DURATION_OF_ROUND / 2;
            while (finishTime > currentTime) {
                
                if (currentTime <= halfOfRoundTime && (currentTime + Game.TICK_DURATION >= halfOfRoundTime)) {
                    Generator.regenerateFood(20);
                }
                
                processPawns();
                processKillers();
                
                collisionSensor();
                
                try {
                    Frame.frame.update();
                    Thread.sleep(Game.TICK_DURATION);
                    currentTime += Game.TICK_DURATION;
                } catch (InterruptedException ex) {
                    System.exit(-122112);
                }
            }
        }
        Game.viewStats(Game.pawns);
        Game.allPawns = Game.arrayToMatrix(Game.evolution(Game.getPawnArray(Game.allPawns)), Game.TURN_PAWN_AMOUNT);
    }

    private void processPawns() {
//        for (Pawn pawn : Game.pawns) {
//            if (pawn.isAlive()) {
//                setRelativeToFood(pawn);
//                setRelativeToEnemy(pawn);
//                pawn.calculate();
//                pawn.updateCoords();
//                dangerZoneProcess(pawn);
//            }
//        }
        for (int i = 0; i < Game.pawns.length; i++) {
            if (Game.pawns[i].isAlive()) {
                setRelativeToFood(Game.pawns[i]);
                //setRelativeToEnemy(Game.pawns[i]);
                Game.pawns[i].calculate(false);
                Game.pawns[i].updateCoords();
                dangerZoneProcess(Game.pawns[i]);
            }
        }
    }

    private void processKillers() {
        for (int i = 0; i < Game.killers.size(); i++) {
            if (Game.killers.get(i).isInDangerZone()) {
                Game.killers.get(i).placeInCenter();
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

        for (int i = 0; i < Game.pawns.length; i++) {
            if (Game.pawns[i].isAlive()) {
                for (Agent bullet : Game.bullets) {
                    if (Game.pawns[i].getX() + Panel.PAWN_DIAMETER / 2 >= bullet.getX()
                            && Game.pawns[i].getX() - Panel.PAWN_DIAMETER / 2 <= bullet.getX()
                            && Game.pawns[i].getY() + Panel.PAWN_DIAMETER / 2 >= bullet.getY()
                            && Game.pawns[i].getY() - Panel.PAWN_DIAMETER / 2 <= bullet.getY()) {
                        if (bullet.authorOfBullet != Game.pawns[i]) {
                            Game.pawns[i].attack(bullet.getMass());
                        }
                    }
                }

                synchronized (Game.foods) {

                    int deleted = 0;
                    for (Iterator<Agent> itr = Game.foods.iterator(); itr.hasNext();) {
                        Agent food = itr.next();
                        if (Game.pawns[i].getX() + Panel.PAWN_DIAMETER / 2 >= food.getX()
                                && Game.pawns[i].getX() - Panel.PAWN_DIAMETER / 2 <= food.getX()
                                && Game.pawns[i].getY() + Panel.PAWN_DIAMETER / 2 >= food.getY()
                                && Game.pawns[i].getY() - Panel.PAWN_DIAMETER / 2 <= food.getY()) {
                            Game.pawns[i].feed(food.getMass());
                            itr.remove();
                            deleted++;
                        }
                    }
                    Generator.generateFood(deleted);

                }

                for (Iterator<Killer> itr = Game.killers.iterator(); itr.hasNext();) {
                    Killer killer = itr.next();
                    if (Game.pawns[i].getX() + Panel.PAWN_DIAMETER / 2 >= killer.getX()
                            && Game.pawns[i].getX() - Panel.PAWN_DIAMETER / 2 <= killer.getX()
                            && Game.pawns[i].getY() + Panel.PAWN_DIAMETER / 2 >= killer.getY()
                            && Game.pawns[i].getY() - Panel.PAWN_DIAMETER / 2 <= killer.getY()) {
                        Game.pawns[i].attack(Game.KILLER_DAMAGE);
                        if (Base.chance(60, 0)) {
                            Game.pawns[i].smite();
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
        Agent killer = Game.killers.get(getNearestAgent(p.getX(), p.getY(), Game.killers));

        p.setRltAngleToEnemy(getAngle(p.getX(), p.getY(), killer.getX(), killer.getY()));
        p.setAbsAngleOfEnemy(killer.getAbsAngle());
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
    public void changeSpeed(boolean half) {
        if (half) {
            newTickDuration = newTickDuration * 2;
            newDurationOfRound = newDurationOfRound * 2;
        } else {
            if (newTickDuration > 4 && newDurationOfRound > 3) {
                newTickDuration = newTickDuration / 2;
                newDurationOfRound = newDurationOfRound / 2;
            }
        }
    }

}
