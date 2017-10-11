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
import ru.dmig.pawns.agents.Pawn;
import java.util.Calendar;
import ru.dmig.pawns.agents.Agent;
import ru.epiclib.base.Arrayer;

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
        while (finishTime > currentTime) {

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
            
            setRelatives(pawns[i]);
            
            pawns[i].calculate();

            pawns[i].setSpeed(pawns[i].newSpeed);
            pawns[i].setAbsAngle(pawns[i].newAbsAngle);

            //Projections TODO
            double mov = pawns[i].getSpeed() * Pawn.MAX_SPEED;
            double angle = pawns[i].getAbsAngle();
            float xMov = (float) (Math.cos(angle) * mov);
            float yMov = (float) (Math.sin(angle) * mov);

            pawns[i].addX(xMov);
            pawns[i].addY(yMov);

            pawns[i].distance += Math.abs(xMov);
            pawns[i].distance += Math.abs(yMov);

        }
    }
    
    private void processBullets() {
        Game.bullets.forEach((bullet) -> {
            bullet.updateCoords();
        });
    }
    
    private void collisionSensor() {
        
        for (int i = 0; i < Game.AMOUNT_OF_PAWNS; i++) {
            for (Agent bullet : Game.bullets) {
                if(Game.pawns[i].getX() + Panel.PAWN_DIAMETER/2 >= bullet.getX() &&
                        Game.pawns[i].getX() - Panel.PAWN_DIAMETER/2 <= bullet.getX() &&
                        Game.pawns[i].getY() + Panel.PAWN_DIAMETER/2 >= bullet.getY() && 
                        Game.pawns[i].getY() - Panel.PAWN_DIAMETER/2 <= bullet.getY()) {
                    if(bullet.authorOfBullet != Game.pawns[i]) {
                        Game.pawns[i].bulletHit(bullet.getMass());
                        //System.out.println("Boom");
                    }
                }
            }
            
            ArrayList<Integer> idToDelete = new ArrayList<>();
            for (int j = 0; j < Game.foods.size(); j++) {
                Agent food = Game.foods.get(j);
                if(Game.pawns[i].getX() + Panel.PAWN_DIAMETER/2 >= food.getX() &&
                        Game.pawns[i].getX() - Panel.PAWN_DIAMETER/2 <= food.getX() &&
                        Game.pawns[i].getY() + Panel.PAWN_DIAMETER/2 >= food.getY() && 
                        Game.pawns[i].getY() - Panel.PAWN_DIAMETER/2 <= food.getY()) {
                    Game.pawns[i].feed(food.getMass());
                    idToDelete.add(j);
                }
            }
            for (int j = 0; j < idToDelete.size(); j++) {
                int id = Arrayer.maxIntInList(idToDelete);
                Game.foods.remove(id);
                Game.generateFood(1);
            }
            
        }
    }
    
    private void setRelatives(Pawn p) {
        Agent nearestFood = Game.foods.get(getNearestFood(p.getX(), p.getY()));
        double x = nearestFood.getX() - p.getX();
        double y = p.getY() - nearestFood.getY();
        double xDiff = Math.abs(x);
        double yDiff = Math.abs(y);
        double distance = Math.sqrt(xDiff*xDiff + yDiff*yDiff);
        p.setDistanceToFood((float) distance);
        int degreeToAdd = 0; //if x > 0 && y > 0
        if(x < 0 && y > 0) {
            degreeToAdd = 90;
        } else if(x < 0 && y < 0) {
            degreeToAdd = 180;
        } else {
            degreeToAdd = 270;
        }
        p.setRltAngleToFood(degreeToAdd + (Math.asin((double) Math.abs(y)/distance))*57.296d);
    }
    
    private int getNearestFood(float x, float y) {
        double lastDiff = Game.LENGTH_OF_FIELD;
        int lastID = 0;
        for (int i = 0; i < Game.foods.size(); i++) {
            Agent get = Game.foods.get(i);
            double xDiff = Math.abs(get.getX() - x);
            double yDiff = Math.abs(get.getY() - y);
            double distance = Math.sqrt(xDiff*xDiff + yDiff*yDiff);
            if(distance < lastDiff) {
                lastID = i;
                lastDiff = distance;
            } 
        }
        return lastID;
    }

}
