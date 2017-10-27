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
    public static final double MAX_SPEED = 6;

    /*
        Inputs of neuron net:
    0:  speed
    1:  absolute angle of movement
    2:  relative angle to nearest food
    3:  distance to nearest food
    4:  relative angle to alternative food
    5:  distance to alternative food
    6:  relative angle to nearest killer
    7:  absolute angle of nearest killer
    8:  distance to nearest killer
    9:  x
    10: y
    11: memory previous value
        Outputs:
    0: new speed
    1: new absolute angle
    2: memory new value
     */
    private double rltAngleToFood;
    private double rltAngleToAltFood;
    private double rltAngleToEnemy;
    private double absAngleOfEnemy;

    private float distToEnemy;
    private float distToFood;
    private float distToAltFood;

    private double memory;

    public Network network;

    public float distance = 0;
    public float foodGathered = 0;
    public float dangerZonePenalty = 0;

    private boolean alive = true;

    public Pawn(float x, float y, Network network) {
        super(0.2, 0, x, y, 100);

        setRltAngleToFood(0);

        this.network = network;
    }

    public Pawn(float x, float y) {
        this(x, y, new Network(Game.LAYERS_OF_NET));
    }

    /**
     * Calculate out parameters from input parameters by network.
     */
    public void calculate(boolean pr) {
        final double x = getX() / Game.LENGTH_OF_FIELD;
        final double y = getY() / Game.LENGTH_OF_FIELD;
        final double aangl = getAbsAngle() / (Math.PI * 2);
        final double ranglF = getRltAngleToFood() / (Math.PI * 2);
        final double ranglAF = getRltAngleToAltFood() / (Math.PI * 2);
        final double ranglE = getRltAngleToEnemy() / (Math.PI * 2);
//        final double aanglE = getAbsAngleOfEnemy() / (Math.PI * 2);
        double distE = 1;
        double distF = 1;
        double distAF = 1;
        if (getDistToEnemy() <= 200) {
            distE = getDistToEnemy() / 200;
        }
        if (getDistToFood() <= 200) {
            distF = getDistToFood() / 200;
        }
        if (getDistToAltFood() <= 200) {
            distAF = getDistToAltFood() / 200;
        }

        if (pr) {
            System.out.println("x " + x);
            System.out.println("y " + y);
            System.out.println("angle " + getAbsAngle());
            System.out.println("angle food " + getRltAngleToFood());
            System.out.println("dist food " + distF);
            System.out.println("angle afood " + getRltAngleToAltFood());
            System.out.println("dist afood " + distAF);
            System.out.println("angle enemy " + getRltAngleToEnemy());
            System.out.println("dist enemy " + distE);
            System.out.println("mem" + getMemory());
        }

        double[] in = {getSpeed(), aangl, ranglF, distF, ranglAF, distAF, ranglE, distE, x, y};

        double[] out = network.calculate(in);

        try {
            setSpeed(out[0]);
            setAbsAngle(out[1] * Math.PI * 2);
            //setMemory(out[2]);
        } catch (IllegalArgumentException ex) {
            System.out.println("Ill: " + out[0] + " " + out[1] * Math.PI * 2);
        }
    }

    @Override
    public void updateCoords(double maxSpeed) {
        double mov = getSpeed() * maxSpeed;
        double angle = getAbsAngle();
        float xMov = (float) (Math.cos(angle) * mov);
        float yMov = (float) (Math.sin(angle) * mov);

        addX(xMov);
        addY(-yMov);

        distance += Math.abs(xMov);
        distance += Math.abs(yMov);
    }

    public void updateCoords() {
        updateCoords(MAX_SPEED);
    }

    public void attack(double damage) {
        if (damage >= getMass()) {
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
        double distanceFit = distance * 0.0102;
        double foodFit = foodGathered * 17.5;
        double dngPenalty = dangerZonePenalty * 0.14;
        double massFit = getMass() * 0.447;

        return distanceFit + foodFit + massFit - dngPenalty;
    }

    public boolean isAlive() {
        return alive;
    }

    protected void kill() {
        alive = false;
        setMass(0);
        dangerZonePenalty += 10; //Some negative fitness for dying
    }

    /**
     * Hurt the maximal damage to pawn. Pawn will be killed
     */
    public void smite() {
        attack(getMass());
    }

    public void feed(double mass) {
        setMass(getMass() + mass);
        foodGathered += mass;
    }

    /**
     * Get category of pawn. If mass: 0 < x < 100 : 0 100 < x < 400 : 1 400 < x < 700 : 2 700 < x < 1000 : 3 @r
     *
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

    /**
     * Get the value of memory
     *
     * @return the value of memory
     */
    public double getMemory() {
        return memory;
    }

    /**
     * Set the value of memory
     *
     * @param memory new value of memory
     */
    public void setMemory(double memory) throws IllegalArgumentException {
        if (memory >= 0 && memory <= 1) {
            this.memory = memory;
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Get the value of distToFood
     *
     * @return the value of distToFood
     */
    public float getDistToFood() {
        return distToFood;
    }

    /**
     * Set the value of distToFood
     *
     * @param distToFood new value of distToFood
     */
    public void setDistToFood(float distToFood) {
        this.distToFood = distToFood;
    }

    /**
     * Get the value of rltAngleToAltFood
     *
     * @return the value of rltAngleToAltFood
     */
    public double getRltAngleToAltFood() {
        return rltAngleToAltFood;
    }

    /**
     * Set the value of rltAngleToAltFood
     *
     * @param rltAngleToAltFood new value of rltAngleToAltFood
     */
    public void setRltAngleToAltFood(double rltAngleToAltFood) {
        this.rltAngleToAltFood = rltAngleToAltFood;
    }

    /**
     * Get the value of distToAltFood
     *
     * @return the value of distToAltFood
     */
    public float getDistToAltFood() {
        return distToAltFood;
    }

    /**
     * Set the value of distToAltFood
     *
     * @param distToAltFood new value of distToAltFood
     */
    public void setDistToAltFood(float distToAltFood) {
        this.distToAltFood = distToAltFood;
    }

    /**
     * Get the value of absAngleOfEnemy
     *
     * @return the value of absAngleOfEnemy
     */
    public double getAbsAngleOfEnemy() {
        return absAngleOfEnemy;
    }

    /**
     * Set the value of absAngleOfEnemy
     *
     * @param absAngleOfEnemy new value of absAngleOfEnemy
     */
    public void setAbsAngleOfEnemy(double absAngleOfEnemy) {
        this.absAngleOfEnemy = absAngleOfEnemy;
    }

    public double getPawnConcentration() {
        int amount = getPawnAmount(Game.PAWN_SCAN_RANGE);
        return (amount > 10 ? 1 : amount / 10);
    }

    public int getPawnAmount(int RADIUS) {
        int ret = 0;
        for (Pawn pawn : Game.pawns) {
            if (!equals(pawn)) {
                if (Math.abs(getX() - pawn.getX()) < RADIUS && Math.abs(getY() - pawn.getY()) < RADIUS) {
                    ret++;
                }
            }
        }
        return ret;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (int) (Double.doubleToLongBits(this.rltAngleToFood) ^ (Double.doubleToLongBits(this.rltAngleToFood) >>> 32));
        hash = 97 * hash + (int) (Double.doubleToLongBits(this.rltAngleToEnemy) ^ (Double.doubleToLongBits(this.rltAngleToEnemy) >>> 32));
        hash = 97 * hash + Float.floatToIntBits(this.distToEnemy);
        hash = 97 * hash + Objects.hashCode(this.network);
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
