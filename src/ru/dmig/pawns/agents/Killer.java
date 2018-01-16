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
package ru.dmig.pawns.agents;

/**
 * Unit for killing pawns
 *
 * @author Dmig
 */
public class Killer extends Agent {

    public static final double MAX_SPEED = 0.25;

    private double rltAngleToPawn;

    public Killer(float x, float y) {
        super(x, y);
    }

    public Killer() {
        super();
    }

    public Killer(double speed, double absAngle, double mass) {
        super(speed, absAngle, mass);
    }

    public void calculate() {
        setAbsAngle(getRltAngleToPawn());

        double mov = getSpeed() * MAX_SPEED;
        double angle = getAbsAngle();
        float xMov = (float) (Math.cos(angle) * mov);
        float yMov = (float) (Math.sin(angle) * mov);

        addX(xMov);
        addY(yMov);
    }

    /**
     * Get the value of rltAngleToPawn
     *
     * @return the value of rltAngleToPawn
     */
    public double getRltAngleToPawn() {
        return rltAngleToPawn;
    }

    /**
     * Set the value of rltAngleToPawn
     *
     * @param rltAngleToPawn new value of rltAngleToPawn
     */
    public void setRltAngleToPawn(double rltAngleToPawn) {
        this.rltAngleToPawn = rltAngleToPawn;
    }

}
