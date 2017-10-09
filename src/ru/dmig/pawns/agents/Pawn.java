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

import ru.dmig.pawns.Game;
import ru.dmig.pawns.net.Network;

/**
 * Basic unit in game. Has own neuron network for thinking.
 *
 * @author Dmig
 */
public final class Pawn extends Agent {

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

    private double mass; // (0;1000]
    private double rltAngleToEnemy; // [-180;180)

    public Network network;

    public double newSpeed;
    public double newAbsAngle;
    public double shootPrice;

    public float distance = 0;

    public Pawn(float x, float y, Network network) {
        super(0.2,0,x,y);
        
        setMass(100);
        setRltAngleToEnemy(0); //TODO

        this.network = network;

        newSpeed = getSpeed();
        newAbsAngle = getAbsAngle();
        shootPrice = 0;
    }

    public Pawn(float x, float y) {
        this(x, y, new Network(Game.LAYERS_OF_NET));
    }

    /**
     * Calculate out parameters from input parameters by network.
     */
    public void calculate() {
        double[] in = {getSpeed(), getAbsAngle(), mass, rltAngleToEnemy};
        double[] out = {newSpeed, newAbsAngle, shootPrice};

        network.calculate(in, out, true);

        try {
            setNewSpeed(out[0]);
        } catch (IllegalArgumentException e) {
            setNewSpeed(getSpeed());
        }
        try {
            setNewAbsAngle(out[1]);
        } catch (IllegalArgumentException e) {
            setNewAbsAngle(getAbsAngle());
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
     * Add mass gathered from food
     * @param food Full food amount
     */
    public void feed(double food) {
        double energyGathered = food;
        switch (getCategory()) {
            case 0:
                //Nothing to change
                break;
            case 1:
                energyGathered = energyGathered * 3 / 4;
                break;
            case 2:
                energyGathered = energyGathered * 2 / 5;
                break;
            case 3:
                energyGathered = energyGathered / 10;
                break;
            default:
                throw new AssertionError();
        }
        setMass(getMass() + energyGathered);
    }
    
    /**
     * Remove mass from pawn by attack damage
     * @param damage Full food amount
     */
    public void attack(double damage) {
        double damageGet = damage;
        switch (getCategory()) {
            case 0:
                damageGet = damageGet / 2;
                break;
            case 1:
                //Nothing to change
                break;
            case 2:
                damageGet = damageGet * 2;
                break;
            case 3:
                damageGet = damageGet * 4;
                break;
            default:
                throw new AssertionError();
        }
        setMass(getMass() - damageGet);
    }
    
    /**
     * Get category of pawn.
     * If mass:
     * 0 < x < 100 : 0
     * 100 < x < 400 : 1
     * 400 < x < 700 : 2
     * 700 < x < 1000 : 3
     * @return code of category
     */
    public int getCategory() {
        if(getMass() >= 0 && getMass() <= 100) {
            return 0;
        } else if(getMass() > 100 && getMass() <= 400) {
            return 1;
        } else if(getMass() > 400 && getMass() <= 700) {
            return 2;
        } else if(getMass() > 700 && getMass() <= 1000) {
            return 3;
        } else throw new IllegalStateException();
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
