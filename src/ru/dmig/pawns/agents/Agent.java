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
import ru.dmig.pawns.Generator;
import ru.epiclib.base.Base;

/**
 * Class for object on game field.
 * Agent has: speed, absolute angle, coordinates
 * @author Dmig
 */
public class Agent {
    
    public static final float MAX_BULLET_SPEED = 60;
    
    public enum Type {PAWN, BULLET, FOOD};
    
    public Type t = Type.PAWN;
    
    private double speed; // [0;1]
    private double absAngle; // [0;2pi)
    
    private float x;
    private float y;
    
    private double mass; // (0;1000]
    public Pawn authorOfBullet;
    
    public Agent(double speed, double absAngle, float x, float y, double mass) {
        this.speed = 0;
        this.absAngle = 0;
        this.x = 0;
        this.y = 0;
        this.mass = 0;

        try {
            setSpeed(speed);
        } catch (IllegalArgumentException e) {
            setSpeed(0);
        }
        try {
            setAbsAngle(absAngle);
        } catch (IllegalArgumentException e) {
            setAbsAngle(0);
        }
        addX(x);
        addY(y);
        setMass(mass);
    }
    
    public Agent(float x, float y) {
        this.speed = 0;
        this.absAngle = 0;
        this.x = 0;
        this.y = 0;
        
        addX(x);
        addY(y);
    }
    
    public Agent() {
        this(0,0);
        placeInRandomPosition();
    }
    
    public Agent(double speed, double absAngle, double mass) {
        this(speed, absAngle, 0, 0, mass);
        placeInRandomPosition();
    }
    
    public Agent(double mass) {
        this();
        this.mass = 0;
        setMass(mass);
        placeInRandomPosition();
    }

    /**
     * Updating coordinates of agent by current speed and angle
     * @param maxSpeed Max speed of agent
     */
    public void updateCoords(double maxSpeed) {
        double mov = getSpeed() * maxSpeed;
        double angle = getAbsAngle();
        float xMov = (float) (Math.cos(angle) * mov);
        float yMov = (float) (Math.sin(angle) * mov);

        addX(xMov);
        addY(-yMov);
    }
    
    public boolean isInDangerZone() {
        return getX() <= Game.DANGER_ZONE || getX() >= Game.LENGTH_OF_FIELD - Game.DANGER_ZONE
                || getY() <= Game.DANGER_ZONE || getY() >= Game.HEIGHT_OF_FIELD - Game.DANGER_ZONE;
    }
    
    public final void placeInRandomPosition() {
        x = Base.randomNumber(Game.DANGER_ZONE + 1, Game.LENGTH_OF_FIELD - Game.DANGER_ZONE - 1);
        y = Base.randomNumber(Game.DANGER_ZONE + 1, Game.HEIGHT_OF_FIELD - Game.DANGER_ZONE - 1);
    }
    
    public final void placeInStartPlace() {
        x = Game.DANGER_ZONE +1;
        y = Game.DANGER_ZONE +1;
    }
    
    public final void placeInCenter() {
        x = Game.LENGTH_OF_FIELD / 2;
        y = Game.HEIGHT_OF_FIELD / 2;
    }

    /**
     * Get the value of speed
     *
     * @return the value of speed
     */
    public final double getSpeed() {
        return speed;
    }

    /**
     * Set the value of speed
     *
     * @param speed new value of speed
     */
    public final void setSpeed(double speed) throws IllegalArgumentException {
        if(speed >= 0 && speed <= 1) {
            this.speed = speed;
        } else throw new IllegalArgumentException();
    }

    /**
     * Get the value of absAngle
     *
     * @return the value of absAngle
     */
    public final double getAbsAngle() {
        return absAngle;
    }

    /**
     * Set the value of absAngle
     *
     * @param absAngle new value of absAngle
     */
    public final void setAbsAngle(double absAngle) throws IllegalArgumentException {
        if(absAngle >= 0 && absAngle < 2*Math.PI) {
            this.absAngle = absAngle;
        } else throw new IllegalArgumentException();
    }
    
    /**
     * Get the value of x
     *
     * @return the value of x
     */
    public final float getX() {
        return x;
    }

    /**
     * Adds the local x to pawn.x
     *
     * @param x addend of x
     */
    public final void addX(float x) {
        if (t == Type.PAWN) {
            if (this.x + x >= Game.LENGTH_OF_FIELD) {
            } else if (this.x + x < 0) {
            } else {
                this.x += x;
            }
        } else {
            if (this.x + x >= Game.LENGTH_OF_FIELD) {
                Game.bullets.remove(this);
            } else if (this.x + x < 0) {
                Game.bullets.remove(this);
            } else {
                this.x += x;
            }
        }
    }

    /**
     * Get the value of y
     *
     * @return the value of y
     */
    public final float getY() {
        return y;
    }

    /**
     * Adds the local y to pawn.y
     *
     * @param y addend of y
     */
    public final void addY(float y) {
        if (t == Type.PAWN) {
            if (this.y + y >= Game.HEIGHT_OF_FIELD) {
                //setSpeed(0);
                //setAbsAngle(3*Math.PI/2);
            } else if (this.y + y < 0) {
                //setSpeed(0);
                //setAbsAngle(Math.PI/2);
            } else {
                this.y += y;
            }
        } else {
            if (this.y + y >= Game.HEIGHT_OF_FIELD) {
                Game.bullets.remove(this);
            } else if (this.y + y < 0) {
                Game.bullets.remove(this);
            } else {
                this.y += y;
            }
        }
    }
    
    /**
     * Get the value of mass
     *
     * @return the value of mass
     */
    public final double getMass() {
        return mass;
    }

    /**
     * Set the value of mass
     *
     * @param mass new value of mass
     */
    public final void setMass(double mass) {
        this.mass = mass;
    }

    
}
