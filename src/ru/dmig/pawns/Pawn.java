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

import ru.dmig.pawns.net.Network;

/**
 * Basic unit in game. Has own neuron network for thinking.
 *
 * @author Dmig
 */
public final class Pawn {

    /**
     * Max speed for every pawn in every direction. (Maybe, I don't sure)
     */
    public static final double MAX_SPEED = 16;

    /*
    Inputs of neuron net:
        0: speed [0;1]
        1: absolute angle of movement [0;360)
        2: mass
        3: relative angle to nearest enemy [-180;180)
    Outputs:
        0: new speed
        1: new absolute angle
        2: shoot price [0;10]
     */
    private double speed; // [0;1]
    private double absAngle; // [0;360)
    private double mass; // (0;1000]
    private double rltAngleToEnemy; // [-180;180)

    private float x;
    private float y;

    protected Network network;

    public double newSpeed;
    public double newAbsAngle;
    public double shootPrice;

    public float distance = 0;

    public Pawn(float x, float y, Network network) {
        setSpeed(1);
        setAbsAngle(0);
        setMass(100);
        setRltAngleToEnemy(0); //TODO

        this.network = network;

        this.x = 0;
        this.y = 0;

        addX(x);
        addY(y);

        newSpeed = speed;
        newAbsAngle = absAngle;
        shootPrice = 0;
    }

    public Pawn(float x, float y) {
        this(x, y, new Network(Game.LAYERS_OF_NET));
    }

    /**
     * Calculate out parameters from input parameters by network.
     */
    public void calculate() {
        double[] in = {speed, absAngle, mass, rltAngleToEnemy};
        double[] out = {newSpeed, newAbsAngle, shootPrice};

        network.calculate(in, out, true);

        try {
            setNewSpeed(out[0]);
        } catch (IllegalArgumentException e) {
            setNewSpeed(speed);
        }
        try {
            setNewAbsAngle(out[1]);
        } catch (IllegalArgumentException e) {
            setNewAbsAngle(absAngle);
        }
        try {
            setShootPrice(out[2]);
        } catch (IllegalArgumentException e) {
            setShootPrice(0);
        }
    }

    /**
     * Calculate fitness of this pawn
     *
     * @return Fitness of this pawn
     */
    public double calcFitness() {
        return distance;
    }

    /**
     * Get the value of speed
     *
     * @return the value of speed
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * Set the value of speed
     *
     * @param speed new value of speed
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    /**
     * Get the value of absAngle
     *
     * @return the value of absAngle
     */
    public double getAbsAngle() {
        return absAngle;
    }

    /**
     * Set the value of absAngle
     *
     * @param absAngle new value of absAngle
     */
    public void setAbsAngle(double absAngle) {
        this.absAngle = absAngle;
    }

    /**
     * Get the value of mass
     *
     * @return the value of mass
     */
    public double getMass() {
        return mass;
    }

    /**
     * Set the value of mass
     *
     * @param mass new value of mass
     */
    public void setMass(double mass) {
        this.mass = mass;
    }

    /**
     * Get the value of rltAngleToEnemy
     *
     * @return the value of rltAngleToEnemy
     */
    public double getRltAngleToEnemy() {
        return rltAngleToEnemy;
    }

    /**
     * Set the value of rltAngleToEnemy
     *
     * @param rltAngleToEnemy new value of rltAngleToEnemy
     */
    public void setRltAngleToEnemy(double rltAngleToEnemy) {
        this.rltAngleToEnemy = rltAngleToEnemy;
    }

    /**
     * Get the value of x
     *
     * @return the value of x
     */
    public float getX() {
        return x;
    }

    /**
     * Adds the local x to pawn.x
     *
     * @param x addend of x
     */
    public void addX(float x) {
        if (this.x + x >= Game.LENGTH_OF_FIELD) {
            this.x = (this.x + x) - Game.LENGTH_OF_FIELD;
        } else if (this.x + x < 0) {
            this.x = (this.x + x) + Game.LENGTH_OF_FIELD;
        } else {
            this.x += x;
        }
    }

    /**
     * Get the value of y
     *
     * @return the value of y
     */
    public float getY() {
        return y;
    }

    /**
     * Adds the local y to pawn.y
     *
     * @param y addend of y
     */
    public void addY(float y) {
        if (this.y + y >= Game.HEIGHT_OF_FIELD) {
            this.y = (this.y + y) - Game.HEIGHT_OF_FIELD;
        } else if (this.y + y < 0) {
            this.y = (this.y + y) + Game.HEIGHT_OF_FIELD;
        } else {
            this.y += y;
        }
    }

    public void setNewSpeed(double newSpeed) {
        if (newSpeed >= 0 && newSpeed <= 1) {
            this.newSpeed = newSpeed;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void setNewAbsAngle(double newAbsAngle) {
        if (newAbsAngle >= 0 && newAbsAngle < 360) {
            this.newAbsAngle = newAbsAngle;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void setShootPrice(double shootPrice) {
        if (shootPrice >= 0 && shootPrice <= 10) {
            this.shootPrice = shootPrice;
        } else {
            throw new IllegalArgumentException();
        }
    }

}
