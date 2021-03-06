/*
 * Copyright (C) 2017-2018 Dmig
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

import java.util.ArrayList;
import java.util.Objects;
import ru.dmig.pawns.Game;
import ru.dmig.pawns.UpdThread;
import ru.dmig.pawns.net.Network;
import ru.dmig.util.Angler;
import ru.epiclib.base.Base;

/**
 * Basic unit in game. Has own neuron network for thinking.
 *
 * @author Dmig
 */
public final class Pawn extends Agent implements Comparable<Pawn> {

    /**
     * Max speed for every pawn in every direction. (Maybe, I don't sure)
     */
    public static final double MAX_SPEED = 1;

    public static final double MAX_DEGREE_PER_TICK = 30;
    public static final float ZONE_PENALTY = 10;
    public static final double START_MASS = 20;
    //How much mass you spend for moving by distq?
    private static final float amountqForDist = 1;
    public static final float MIN_DISTANCE = 60;
    public static final boolean PRINT_FIT = false;
    public static final boolean PRINT_BAD_FIT = false;

    /*
        Inputs of neuron net:
    0:  relative angle to nearest food
    1:  distance to nearest food
    2:  relative angle to alternative food
    3:  distance to alternative food
    4:  relative angle to nearest killer
    5:  absolute angle of nearest killer
    6:  distance to nearest killer
    7:  x
    8:  y
    9:  memory cell value
        Outputs:
    0:  new speed
    1:  new absolute angle
    2:  memory cell new value
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
    private float penalty = 0;

    private float tempDistance = 0;

    private boolean alive = true;
    private ArrayList<String> fitsPr = new ArrayList<>();

    public Pawn(float x, float y, Network network) {
        super(1, 0, x, y, START_MASS);

        setRltAngleToFood(0);

        this.network = network;
    }

    public Pawn(float x, float y) {
        this(x, y, new Network(Game.LAYERS_OF_NET));
    }

    /**
     * Calculate out parameters from input parameters by network.
     *
     * @param pr Print?
     */
    public void calculate(boolean pr) {
        final double x = getX() / Game.LENGTH_OF_FIELD;
        final double y = getY() / Game.LENGTH_OF_FIELD;
        final double aangl = Angler.radToRoul(getAbsAngle());
        final double ranglF = Angler.radToRoul(getRltAngleToFood());
        final double ranglAF = Angler.radToRoul(getRltAngleToAltFood());
        double ranglE = Angler.radToRoul(getRltAngleToEnemy());
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

        if (!Game.KILLER_ENABLED) {
            ranglE = 0;
            distE = 0;
        }

        final double[] in = {ranglF, distF, ranglAF, distAF, ranglE, distE, x, y};
        setNews(in, pr);
    }

    private void setNews(final double[] in, boolean pr) throws IllegalArgumentException {
        final double[] out = network.calculate(in);
        double newAngle = out[0];
        //final double newSpeed = out[0];

        //if (!(Double.isNaN(newSpeed) || Double.isInfinite(newAngle))) {
        if (!Double.isInfinite(newAngle)) {
            try {
                setAbsAngle(Angler.roulToRad(newAngle));

                //setSpeed(newSpeed);
                setSpeed(1);

                if (pr) {
                    System.out.println("Rad new angle: raw(" + newAngle + "), new(" + Angler.roulToRad(newAngle) + ")");
                    //System.out.println("New speed: "+newSpeed);
                }
                //setMemory(out[2]);
            } catch (IllegalArgumentException ex) {
                System.err.println(ex);
                setSpeed(1);
                setAbsAngle(0);
            }
        } else {
            setSpeed(1);
            setAbsAngle(0);
        }
    }

    public void addDistance(float amount) {
        distance += amount;
        addTempDist(amount);
    }

    @Override
    public void updateCoords(double maxSpeed) {
        double mov = getSpeed() * maxSpeed;
        double angle = getAbsAngle();
        float xMov = (float) (Math.cos(angle) * mov);
        float yMov = (float) (Math.sin(angle) * mov);

        addX(xMov);
        addY(-yMov);

        float tMov = Math.abs(xMov) + Math.abs(yMov);
        addDistance(tMov);
    }

    public void updateCoords() {
        updateCoords(MAX_SPEED);
    }

    public float updateCoords(boolean dist) {
        if (dist) {
            float distA = distance;
            updateCoords();
            return (distance - distA);
        } else {
            updateCoords();
            return 0;
        }
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
        final double distanceFit = distance * 0.01;
        final double foodFit = foodGathered * 3.5;
        final double penalty = getPenalty();
        final double massFit = getMass();

        double total = distanceFit + foodFit + massFit - penalty;

        if (PRINT_FIT) {
            if (PRINT_BAD_FIT) {
                printFitness(distanceFit, foodFit, penalty, massFit, total);
            } else if (total > 900) {
                printFitness(distanceFit, foodFit, penalty, massFit, total);
            }
        }
        return total;
    }

    public void printFitness(double dist, double food, double penalty, double mass, double total) {
        String fit = "Dist: " + Base.maximumFractionDigits(2, dist) + ", "
                + "food: " + Base.maximumFractionDigits(2, food) + ", "
                + "penlty: " + Base.maximumFractionDigits(2, penalty) + ", "
                + "mass: " + Base.maximumFractionDigits(2, mass) + "; "
                + "Total: " + Base.maximumFractionDigits(2, total);
        boolean alrPrinted = false;
        for (int i = 0; i < fitsPr.size(); i++) {
            String get = fitsPr.get(i);
            if (get.equals(fit)) {
                alrPrinted = true;
                break;
            }
        }
        if (!alrPrinted) {
            System.out.println(fit);
            fitsPr.add(fit);
        }
    }

    public boolean isAlive() {
        return alive;
    }

    protected void kill() {
        alive = false;
        super.setMass(0);
        penalty += 10; //Some negative fitness for dying
    }

    /**
     * Hurt the maximal damage to pawn. Pawn will be killed
     */
    public void smite() {
        attack(getMass());
    }

    public void feed(double mass) {
        setMass(getMass() + mass);
        foodGathered++;
    }

    /**
     * Get category of pawn. If mass: 0 < x < 100 : 0 100 < x < 400 : 1 400 < x < 700 : 2 700 < x < 1000 : 3 @r
     *
     *
     * return code of category
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
        this.rltAngleToFood = Angler.doAngle(rltAngleToFood);
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
        this.rltAngleToEnemy = Angler.doAngle(rltAngleToEnemy);
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
        this.rltAngleToAltFood = Angler.doAngle(rltAngleToAltFood);
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
        this.absAngleOfEnemy = Angler.doAngle(absAngleOfEnemy);
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
        hash = 97 * hash + Float.floatToIntBits(this.penalty);
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
        if (Float.floatToIntBits(this.penalty) != Float.floatToIntBits(other.penalty)) {
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

    @Override
    public int compareTo(Pawn o) {
        return Double.compare(calcFitness(), o.calcFitness());
    }

    public void addTempDist(float amount) {
        float massConsumption = 0;
        while (tempDistance + amount >= Game.MASS_MOVE_TAX) {
            amount -= Game.MASS_MOVE_TAX;
            massConsumption += amountqForDist;
        }
        tempDistance += amount;
        if (massConsumption > 0) {
            spendMassForMove(massConsumption);
        }
    }

    public void spendMassForMove(float amount) {
        setMass(getMass() - amount);
        if (!alive) {
            UpdThread.incStarveKilled();
        }
    }

    /**
     * Set the value of mass
     *
     * @param mass new value of mass
     */
    @Override
    public final void setMass(double mass) {
        if (mass > 0) {
            super.setMass(mass);
        } else {
            kill();
        }
    }

    public float getPenalty() {
        return penalty;
    }

    public void addPenalty(float penalty) {
        this.penalty += penalty;
    }

}
