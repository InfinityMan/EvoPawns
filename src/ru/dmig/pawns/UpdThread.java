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

import java.util.Calendar;
import java.util.Iterator;
import ru.dmig.pawns.agents.Agent;
import ru.dmig.pawns.agents.Pawn;
import ru.dmig.pawns.gui.Frame;
import ru.dmig.pawns.gui.Panel;

/**
 * Updates the pawns, simulation, and e.t.c.
 *
 * @author Dmig
 */
public class UpdThread extends Thread {

    private double startTime;
    private double currentTime;
    private double finishTime;

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
        startTime = Calendar.getInstance().getTimeInMillis();
        finishTime = startTime + Game.DURATION_OF_ROUND;
        currentTime = startTime;

        double halfOfRoundTime = startTime + Game.DURATION_OF_ROUND / 2;
        while (finishTime > currentTime) {

            if (currentTime <= halfOfRoundTime && (currentTime + Game.TICK_DURATION >= halfOfRoundTime)) {
                Generator.regenerateFood(20);
            }

            processPawns();
            processBullets();

            collisionSensor();

            try {
                Frame.frame.update();
                Thread.sleep(Game.TICK_DURATION);
                currentTime += Game.TICK_DURATION;
            } catch (InterruptedException ex) {
                System.exit(-122112);
            }
        }

        Game.viewStats();
        Game.pawns = Game.evolution(Game.pawns);
    }

    private void processPawns() {
        Pawn[] pawns = Game.pawns;

        for (int i = 0; i < Game.AMOUNT_OF_PAWNS; i++) {

            if (pawns[i].isAlive()) {
                setRelatives(pawns[i]);
                setRelativeToEnemy(pawns[i]);

                pawns[i].calculate();

                pawns[i].setSpeed(pawns[i].newSpeed);
                pawns[i].setAbsAngle(pawns[i].newAbsAngle);

                //Projections TODO
                double mov = pawns[i].getSpeed() * Pawn.MAX_SPEED;
                double angle = pawns[i].getAbsAngle();
                float xMov = (float) (Math.cos(angle) * mov);
                float yMov = (float) (Math.sin(angle) * mov);

                pawns[i].addX(xMov);
                pawns[i].addY(-yMov);

                if (pawns[i].isPawnInDangerZone()) {
                    pawns[i].dangerZonePenalty++;
                }

                pawns[i].distance += Math.abs(xMov);
                pawns[i].distance += Math.abs(yMov);
            }

        }
    }

    private void processBullets() {
        Game.bullets.forEach((bullet) -> {
            bullet.updateCoords();
        });
    }

    private void collisionSensor() {

        for (int i = 0; i < Game.AMOUNT_OF_PAWNS; i++) {
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

                for (int j = 0; j < Game.AMOUNT_OF_PAWNS; j++) {
                    Pawn pawn = Game.pawns[j];
                    if (Game.pawns[i].getX() + Panel.PAWN_DIAMETER / 2 >= pawn.getX()
                            && Game.pawns[i].getX() - Panel.PAWN_DIAMETER / 2 <= pawn.getX()
                            && Game.pawns[i].getY() + Panel.PAWN_DIAMETER / 2 >= pawn.getY()
                            && Game.pawns[i].getY() - Panel.PAWN_DIAMETER / 2 <= pawn.getY()) {
                        Game.pawns[i].attack(Game.PAWN_DAMAGE);
                    }
                }
                
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
        }
    }

    private void setRelatives(Pawn p) {
        Agent food = Game.foods.get(getNearestFood(p.getX(), p.getY()));
        p.setRltAngleToFood(getAngle(p.getX(), p.getY(), food.getX(), food.getY()));
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
        Pawn enemy = Game.pawns[getNearestPawn(p)];

        p.setRltAngleToEnemy(getAngle(p.getX(), p.getY(), enemy.getX(), enemy.getY()));
        p.setDistToEnemy((float) getDistance(p.getX(), p.getY(), enemy.getX(), enemy.getY()));
    }

    private int getNearestFood(float x, float y) {
        double lastDiff = Game.LENGTH_OF_FIELD;
        int lastID = 0;
        for (int i = 0; i < Game.foods.size(); i++) {
            Agent get = Game.foods.get(i);
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

    protected int getNearestPawn(Pawn pawn) {
        double lastDiff = Game.LENGTH_OF_FIELD;
        int lastID = 0;
        for (int i = 0; i < Game.pawns.length; i++) {
            if (!pawn.equals(Game.pawns[i])) {
                Agent get = Game.pawns[i];
                double xDiff = Math.abs(get.getX() - pawn.getX());
                double yDiff = Math.abs(get.getY() - pawn.getY());
                double distance = Math.sqrt(xDiff * xDiff + yDiff * yDiff);
                if (distance < lastDiff) {
                    lastID = i;
                    lastDiff = distance;
                }
            }
        }
        return lastID;
    }

}
