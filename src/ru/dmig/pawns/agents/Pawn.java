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
    public static final double MAX_SPEED = 4;

    /*
        Inputs of neuron net:
    0: speed
    1: absolute angle of movement
    2: relative angle to nearest food
    3: x
    4: y
        Outputs:
    0: new speed
    1: new absolute angle
     */

    private double rltAngleToFood;
    
    public Network network;

    public double newSpeed;
    public double newAbsAngle;

    public float distance = 0;
    public float foodGathered = 0;
    public float dangerZonePenalty = 0;

    public Pawn(float x, float y, Network network) {
        super(0.2,0,x,y,100);
        
        setRltAngleToFood(0);

        this.network = network;

        newSpeed = getSpeed();
        newAbsAngle = getAbsAngle();
    }

    public Pawn(float x, float y) {
        this(x, y, new Network(Game.LAYERS_OF_NET));
    }

    /**
     * Calculate out parameters from input parameters by network.
     */
    public void calculate() {
        double x = getX()/Game.LENGTH_OF_FIELD;
        double y = getY()/Game.LENGTH_OF_FIELD;
        double aangl = getAbsAngle()/(Math.PI*2);
        double rangl = getRltAngleToFood()/(Math.PI*2);
        double[] in = {getSpeed(), aangl, rangl, x, y};
        double[] out = {newSpeed, newAbsAngle};

        network.calculate(in, out, true);
        
        try {
            setNewSpeed(out[0]);
        } catch (IllegalArgumentException e) {
            setNewSpeed(0);
        }
        try {
            setNewAbsAngle(out[1]*Math.PI*2);
        } catch (IllegalArgumentException e) {
            setNewAbsAngle(0);
        }
        
//        if(shootPrice > 0) {
//            Game.newBullet(getX(), getY(), getAbsAngle(), shootPrice, this);
//            totalDamageUsed += shootPrice;
//        }
    }
    
    public void bulletHit(double bulletMass) {
        if(bulletMass > getMass()) {
            System.out.println("rip");
        } else {
            setMass(getMass() - bulletMass);
        }
    }

    /**
     * Calculate fitness of this pawn
     *
     * @return Fitness of this pawn
     */
    public double calcFitness(boolean distanceFitness) {
        double distanceFit = distance * 0.01;
        double foodFit = foodGathered * 18;
        double dngPenalty = dangerZonePenalty * 0.12;
        if (distanceFitness) {
            return distanceFit + foodFit - dngPenalty;
        } else {
            return foodFit;
        }
    }

    public boolean isPawnInDangerZone() {
        if(getX() <= Game.DANGER_ZONE || getX() >= Game.LENGTH_OF_FIELD-Game.DANGER_ZONE ||
                getY() <= Game.DANGER_ZONE || getY() >= Game.LENGTH_OF_FIELD-Game.DANGER_ZONE) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Add mass gathered from food
     * @param food Full food amount
     */
    public void feed(double food) {
        foodGathered += food;
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
     * Get the value of rltAngleToFood
     *
     * @return the value of rltAngleToFood
     */
    public double getRltAngleToFood() {
        return rltAngleToFood;
    }

    /**
     * Set the value of rltAngleToFood
     *
     * @param rltAngleToFood new value of rltAngleToFood
     */
    public void setRltAngleToFood(double rltAngleToFood) {
        this.rltAngleToFood = rltAngleToFood;
    }

    public void setNewSpeed(double newSpeed) {
        if (newSpeed >= 0 && newSpeed <= 1) {
            this.newSpeed = newSpeed;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void setNewAbsAngle(double newAbsAngle) {
        if (newAbsAngle >= 0 && newAbsAngle < 2*Math.PI) {
            this.newAbsAngle = newAbsAngle;
        } else {
            throw new IllegalArgumentException();
        }
    }

}
