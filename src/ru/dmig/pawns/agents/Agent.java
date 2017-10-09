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

/**
 * Class for object on game field.
 * Agent has: speed, absolute angle, coordinates
 * @author Dmig
 */
public class Agent {
    
    private double speed; // [0;1]
    private double absAngle; // [0;360)
    
    private float x;
    private float y;
    
    public Agent(double speed, double absAngle, float x, float y) {
        this.speed = 0;
        this.absAngle = 0;
        this.x = 0;
        this.y = 0;

        setSpeed(speed);
        setAbsAngle(absAngle);
        addX(x);
        addY(y);
    }
    
    public Agent(float x, float y) {
        this.speed = 0;
        this.absAngle = 0;
        this.x = 0;
        this.y = 0;
        
        addX(x);
        addY(y);
    }

    /**
     * Updating coordinates of agent by current speed and angle
     */
    public void updateCoords() {
        double mov = getSpeed() * Pawn.MAX_SPEED;
        double angle = getAbsAngle();
        float xMov = (float) (Math.cos(angle) * mov);
        float yMov = (float) (Math.sin(angle) * mov);

        addX(xMov);
        addY(yMov);
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
    public final void setSpeed(double speed) {
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
    public final void setAbsAngle(double absAngle) {
        if(absAngle >= 0 && absAngle < 360) {
            this.absAngle = absAngle;
        } throw new IllegalArgumentException();
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
    public final float getY() {
        return y;
    }

    /**
     * Adds the local y to pawn.y
     *
     * @param y addend of y
     */
    public final void addY(float y) {
        if (this.y + y >= Game.HEIGHT_OF_FIELD) {
            this.y = (this.y + y) - Game.HEIGHT_OF_FIELD;
        } else if (this.y + y < 0) {
            this.y = (this.y + y) + Game.HEIGHT_OF_FIELD;
        } else {
            this.y += y;
        }
    }

    
}
