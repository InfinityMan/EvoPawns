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

import ru.dmig.pawns.agents.Agent;
import ru.dmig.pawns.agents.Killer;
import ru.dmig.pawns.agents.Pawn;
import ru.epiclib.base.Base;

/**
 *
 * @author Dmig
 */
public class Generator {

    /**
     * Generates new pawn in spawn zone of the field: (1/4;3/4).
     * Also set random angle to pawn
     * @return New generated pawn
     */
    public static Pawn generatePawn() {
        Pawn pawn = new Pawn(Base.randomNumber(Game.LENGTH_OF_FIELD / 4, Game.LENGTH_OF_FIELD * 3 / 4), Base.randomNumber(Game.HEIGHT_OF_FIELD / 4, Game.HEIGHT_OF_FIELD * 3 / 4));
        pawn.setAbsAngle(randomAngle());
        return pawn;
    }

    /**
     * Generate the food out of danger zone.
     * @param amount Generated food. <code> 0 <= amount <= 1 000 000 </code>
     */
    public static void generateFood(int amount) {
        if(amount >= 0 && amount <= 1000000) {
        for (int i = 0; i < amount; i++) {
            Game.foods.add(generateFood());
        }
        } else throw new IllegalArgumentException();
    }

    private static Agent generateFood() {
        float x = Base.randomNumber(Game.DANGER_ZONE + 1, Game.LENGTH_OF_FIELD - Game.DANGER_ZONE - 1);
        float y = Base.randomNumber(Game.DANGER_ZONE + 1, Game.HEIGHT_OF_FIELD - Game.DANGER_ZONE - 1);
        float mass = Base.randomNumber(Game.MIN_MASS_OF_FOOD, Game.MAX_MASS_OF_FOOD);
        return new Agent(0, 0, x, y, mass);
    }

    /**
     * Regenerate every single piece of food if(chance).
     * @param chance Chance of respawning particular piece. <code> 0 <= chance <= 100 </code>
     */
    public static void regenerateFood(int chance) {
        if (chance >= 0 && chance <= 100) {
            for (int i = 0; i < Game.foods.size(); i++) {
                if (Base.chance(chance, 0)) {
                    Game.foods.set(i, generateFood());
                }
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static void newBullet(float x, float y, double angle, double mass, Pawn author) {
        Game.bullets.add(new Agent(Agent.MAX_BULLET_SPEED, angle, x, y, mass));
        Game.bullets.get(Game.bullets.size() - 1).authorOfBullet = author;
        Game.bullets.get(Game.bullets.size() - 1).t = Agent.Type.BULLET;
    }

    /**
     * Generates new random radian angle.
     * @return Radian angle from 0 to ~Math.Pi*2
     */
    public static double randomAngle() {
        int angle = Base.randomNumber(0, 359);
        double radAngle = angle * (Math.PI / 180);
        return radAngle;
    }

    public static Pawn[] generatePawns() {
        Pawn[] pawns = new Pawn[Game.AMOUNT_OF_PAWNS];
        for (int i = 0; i < pawns.length; i++) {
            pawns[i] = generatePawn();
        }
        return pawns;
    }
    
    public static Killer generateKiller() {
        float x = Base.randomNumber(Game.DANGER_ZONE + 1, Game.LENGTH_OF_FIELD - Game.DANGER_ZONE - 1);
        float y = Base.randomNumber(Game.DANGER_ZONE + 1, Game.HEIGHT_OF_FIELD - Game.DANGER_ZONE - 1);
        return new Killer(x, y);
    }
    
    public static void regenerateKillers(int chance) {
        if (chance >= 0 && chance <= 100) {
            for (int i = 0; i < Game.killers.size(); i++) {
                if (Base.chance(chance, 0)) {
                    Game.killers.set(i, generateKiller());
                }
            }
        } else {
            throw new IllegalArgumentException();
        }
    }
    
}
