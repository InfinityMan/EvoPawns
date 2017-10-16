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
import ru.dmig.pawns.agents.Pawn;
import ru.epiclib.base.Base;

/**
 *
 * @author Dmig
 */
public class Generator {

    public static Pawn generatePawn() {
        Pawn pawn = new Pawn(Base.randomNumber(Game.LENGTH_OF_FIELD / 4, Game.LENGTH_OF_FIELD * 3 / 4), Base.randomNumber(Game.HEIGHT_OF_FIELD / 4, Game.HEIGHT_OF_FIELD * 3 / 4));
        pawn.setAbsAngle(randomAngle());
        return pawn;
    }

    public static void generateFood(int amount) {
        for (int i = 0; i < amount; i++) {
            Game.foods.add(generateFood());
        }
    }

    private static Agent generateFood() {
        float x = Base.randomNumber(Game.DANGER_ZONE + 1, Game.LENGTH_OF_FIELD - Game.DANGER_ZONE - 1);
        float y = Base.randomNumber(Game.DANGER_ZONE + 1, Game.HEIGHT_OF_FIELD - Game.DANGER_ZONE - 1);
        float mass = Base.randomNumber(Game.MIN_MASS_OF_FOOD, Game.MAX_MASS_OF_FOOD);
        return new Agent(0, 0, x, y, mass);
    }

    public static void regenerateFood(int chance) {
        for (int i = 0; i < Game.foods.size(); i++) {
            if (Base.chance(chance, 0)) {
                Game.foods.set(i, generateFood());
            }
        }
    }

    public static void newBullet(float x, float y, double angle, double mass, Pawn author) {
        Game.bullets.add(new Agent(Agent.MAX_BULLET_SPEED, angle, x, y, mass));
        Game.bullets.get(Game.bullets.size() - 1).authorOfBullet = author;
        Game.bullets.get(Game.bullets.size() - 1).t = Agent.Type.BULLET;
    }

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
    
}
