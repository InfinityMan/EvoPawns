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

import ru.dmig.pawns.agents.Pawn;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import ru.dmig.pawns.agents.Agent;

/**
 * JPanel for painting all activity of pawns here
 *
 * @author Dmig
 */
public class Panel extends JPanel {

    public static final int PAWN_DIAMETER = 20;
    public static final int BULLET_DIAMETER = 6;
    public static final int FOOD_DIAMETER = 5;

    @Override
    public void paint(Graphics g) {
        Graphics2D gr2d = (Graphics2D) g;
        gr2d.setBackground(Color.white);
        gr2d.setColor(Color.black);

        for (Pawn pawn : Game.pawns) {
            int x = Math.round(pawn.getX());
            int y = Math.round(pawn.getY());
            gr2d.drawOval(x-PAWN_DIAMETER/2, y-PAWN_DIAMETER/2, PAWN_DIAMETER, PAWN_DIAMETER);
            gr2d.drawLine(x, y, (int) Math.round(x+Math.cos(pawn.getAbsAngle())*PAWN_DIAMETER),
                    (int) Math.round(y+Math.sin(pawn.getAbsAngle())*PAWN_DIAMETER));
        }
        
        gr2d.setColor(Color.red);
        
        Game.bullets.forEach((bullet) -> {
            int x = Math.round(bullet.getX());
            int y = Math.round(bullet.getY());
            gr2d.drawOval(x-BULLET_DIAMETER/2, y-BULLET_DIAMETER/2, BULLET_DIAMETER, BULLET_DIAMETER);
            gr2d.drawLine(x, y, (int) Math.round(x+Math.cos(bullet.getAbsAngle())*BULLET_DIAMETER),
                    (int) Math.round(y+Math.sin(bullet.getAbsAngle())*BULLET_DIAMETER));
        });
        
        gr2d.setColor(Color.green);
        
        Game.foods.forEach((food) -> {
            int x = Math.round(food.getX());
            int y = Math.round(food.getY());
            gr2d.drawOval(x-FOOD_DIAMETER/2, y-FOOD_DIAMETER/2, FOOD_DIAMETER, FOOD_DIAMETER);
            gr2d.drawLine(x, y, (int) Math.round(x+Math.cos(food.getAbsAngle())*FOOD_DIAMETER),
                    (int) Math.round(y+Math.sin(food.getAbsAngle())*FOOD_DIAMETER));
        });

    }

}
