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
import java.util.Calendar;

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

}
