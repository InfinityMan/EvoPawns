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

import java.util.Objects;
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
    3: relative angle to nearest pawn
    4: distance to nearest pawn
    4: x
    5: y
        Outputs:
    0: new speed
    1: new absolute angle
     */
    private double rltAngleToFood;
    private double rltAngleToEnemy;

    private float distToEnemy;

    public Network network;

    public double newSpeed;
    public double newAbsAngle;

    public float distance = 0;
    public float foodGathered = 0;
    public float dangerZonePenalty = 0;

    private boolean alive = true;

    public Pawn(float x, float y, Network network) {
        super(0.2, 0, x, y, 100);

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
        double x = getX() / Game.LENGTH_OF_FIELD;
        double y = getY() / Game.LENGTH_OF_FIELD;
        double aangl = getAbsAngle() / (Math.PI * 2);
        double ranglF = getRltAngleToFood() / (Math.PI * 2);
        double ranglE = getRltAngleToEnemy() / (Math.PI * 2);
        double distE = 1;
        if (getDistToEnemy() <= 200) {
            distE = getDistToEnemy() / 200;
        }

        double[] in = {getSpeed(), aangl, ranglF, ranglE, distE, x, y};
        double[] out = {newSpeed, newAbsAngle};

        network.calculate(in, out, true);

        try {
            setNewSpeed(out[0]);
        } catch (IllegalArgumentException e) {
            setNewSpeed(0);
        }
        try {
            setNewAbsAngle(out[1] * Math.PI * 2);
        } catch (IllegalArgumentException e) {
            setNewAbsAngle(0);
        }

//        if(shootPrice > 0) {
//            Game.newBullet(getX(), getY(), getAbsAngle(), shootPrice, this);
//            totalDamageUsed += shootPrice;
//        }
    }

    public void attack(double damage) {
        if (damage > getMass()) {
            kill();
        } else {
            setMass(getMass() - damage);
        }
    }

    /**
     * Calculate fitness of this pawn
     *
     * @return Fitness of this pawn
     */
    public double calcFitness() {
        double distanceFit = distance * 0.01;
        double foodFit = foodGathered * 18;
        double dngPenalty = dangerZonePenalty * 0.12;
        double massFit = getMass() * 0.9;

        return distanceFit + foodFit + massFit - dngPenalty;
    }

    public boolean isPawnInDangerZone() {
        if (getX() <= Game.DANGER_ZONE || getX() >= Game.LENGTH_OF_FIELD - Game.DANGER_ZONE
                || getY() <= Game.DANGER_ZONE || getY() >= Game.LENGTH_OF_FIELD - Game.DANGER_ZONE) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isAlive() {
        return alive;
    }

    protected void kill() {
        alive = false;
    }

    public void feed(double mass) {
        setMass(getMass() + mass);
        foodGathered += mass;
    }

    /**
     * Get category of pawn. If mass: 0 < x < 100 : 0 100 < x < 400 : 1 400 < x < 700 : 2 700 < x < 1000 : 3
     * @r
     *
     * eturn code of category
     */
    public int getCategory() {
        if (getMass() >= 0 && getMass() <= 100) {
            return 0;
        } else if (getMass() > 100 && getMass() <= 400) {
            return 1;
        } else if (getMass() > 400 && getMass() <= 700) {
            return 2;
        } else if (getMass() > 700 && getMass() <= 1000) {
            return 3;
        } else {
            throw new IllegalStateException();
        }
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
     * Get the value of distToEnemy
     *
     * @return the value of distToEnemy
     */
    public float getDistToEnemy() {
        return distToEnemy;
    }

    /**
     * Set the value of distToEnemy
     *
     * @param distToEnemy new value of distToEnemy
     */
    public void setDistToEnemy(float distToEnemy) {
        this.distToEnemy = distToEnemy;
    }

    public void setNewSpeed(double newSpeed) {
        if (newSpeed >= 0 && newSpeed <= 1) {
            this.newSpeed = newSpeed;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void setNewAbsAngle(double newAbsAngle) {
        if (newAbsAngle >= 0 && newAbsAngle < 2 * Math.PI) {
            this.newAbsAngle = newAbsAngle;
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (int) (Double.doubleToLongBits(this.rltAngleToFood) ^ (Double.doubleToLongBits(this.rltAngleToFood) >>> 32));
        hash = 97 * hash + (int) (Double.doubleToLongBits(this.rltAngleToEnemy) ^ (Double.doubleToLongBits(this.rltAngleToEnemy) >>> 32));
        hash = 97 * hash + Float.floatToIntBits(this.distToEnemy);
        hash = 97 * hash + Objects.hashCode(this.network);
        hash = 97 * hash + (int) (Double.doubleToLongBits(this.newSpeed) ^ (Double.doubleToLongBits(this.newSpeed) >>> 32));
        hash = 97 * hash + (int) (Double.doubleToLongBits(this.newAbsAngle) ^ (Double.doubleToLongBits(this.newAbsAngle) >>> 32));
        hash = 97 * hash + Float.floatToIntBits(this.distance);
        hash = 97 * hash + Float.floatToIntBits(this.foodGathered);
        hash = 97 * hash + Float.floatToIntBits(this.dangerZonePenalty);
        hash = 97 * hash + (this.alive ? 1 : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Pawn other = (Pawn) obj;
        if (Double.doubleToLongBits(this.rltAngleToFood) != Double.doubleToLongBits(other.rltAngleToFood)) {
            return false;
        }
        if (Double.doubleToLongBits(this.rltAngleToEnemy) != Double.doubleToLongBits(other.rltAngleToEnemy)) {
            return false;
        }
        if (Float.floatToIntBits(this.distToEnemy) != Float.floatToIntBits(other.distToEnemy)) {
            return false;
        }
        if (Double.doubleToLongBits(this.newSpeed) != Double.doubleToLongBits(other.newSpeed)) {
            return false;
        }
        if (Double.doubleToLongBits(this.newAbsAngle) != Double.doubleToLongBits(other.newAbsAngle)) {
            return false;
        }
        if (Float.floatToIntBits(this.distance) != Float.floatToIntBits(other.distance)) {
            return false;
        }
        if (Float.floatToIntBits(this.foodGathered) != Float.floatToIntBits(other.foodGathered)) {
            return false;
        }
        if (Float.floatToIntBits(this.dangerZonePenalty) != Float.floatToIntBits(other.dangerZonePenalty)) {
            return false;
        }
        if (this.alive != other.alive) {
            return false;
        }
        if (!Objects.equals(this.network, other.network)) {
            return false;
        }
        return true;
    }

}
