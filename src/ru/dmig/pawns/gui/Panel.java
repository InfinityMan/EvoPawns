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
package ru.dmig.pawns.gui;

import ru.dmig.pawns.agents.Pawn;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import javax.swing.JPanel;
import ru.dmig.pawns.Game;
import ru.dmig.pawns.agents.Agent;

/**
 * JPanel for painting all activity of pawns here
 *
 * @author Dmig
 */
public class Panel extends JPanel {

    public static final int PAWN_DIAMETER = 6;
    public static final int BULLET_DIAMETER = 6;
    public static final int FOOD_DIAMETER = 3;

    public static final int KILLER_DIAMETER = 4;

    public static final Color MY_ORANGE = new Color(219, 118, 67);
    public static final Color MY_WHITE = new Color(255, 255, 255);
    public static final Color MY_BLUE = new Color(25, 42, 90);

    private static final boolean DRAW_HUNDRED = false;
    private static final boolean LEADER_DRAWING = true;

    @Override
    public void paint(Graphics g) {
        Graphics2D gr = (Graphics2D) g;
        gr.setBackground(MY_WHITE);
        
        if (DRAW_HUNDRED) {
            gr.setColor(Color.GRAY);
            gr.drawLine(100, 100, 200, 100);
        }
        
        gr.setColor(Color.PINK);
        drawDangerZone(gr);
        
        drawAllFood(gr);
        drawAllPawns(gr);

        if (Game.KILLER_ENABLED) {
            drawAllKillers(gr);
        }

        gr.setColor(Color.red);

        Game.bullets.forEach((bullet) -> {
            int x = Math.round(bullet.getX());
            int y = Math.round(bullet.getY());
            gr.drawOval(x - BULLET_DIAMETER / 2, y - BULLET_DIAMETER / 2, BULLET_DIAMETER, BULLET_DIAMETER);
            gr.drawLine(x, y, (int) Math.round(x + Math.cos(bullet.getAbsAngle()) * BULLET_DIAMETER),
                    (int) Math.round(y + Math.sin(bullet.getAbsAngle()) * BULLET_DIAMETER));
        });


    }

    private void drawAllKillers(Graphics2D gr) {
        gr.setColor(Color.RED);

        for (int i = 0; i < Game.killers.size(); i++) {
            int x = Math.round(Game.killers.get(i).getX());
            int y = Math.round(Game.killers.get(i).getY());
            gr.drawOval(x - KILLER_DIAMETER / 2, y - KILLER_DIAMETER / 2, KILLER_DIAMETER, KILLER_DIAMETER);
        }
    }

    private void drawAllFood(Graphics2D g) {
        g.setColor(MY_BLUE);
        synchronized (Game.foods) {
            for (Iterator<Agent> itr = Game.foods.iterator(); itr.hasNext();) {
                try {
                    drawFood(g, itr.next());
                } catch (ConcurrentModificationException ex) {
                    System.err.println("ConcurrentEx: " + ex);
                }
            }
        }

    }

    private void drawFood(Graphics2D g, Agent food) {
        final int x = Math.round(food.getX());
        final int y = Math.round(food.getY());
        g.drawOval(x - FOOD_DIAMETER / 2, y - FOOD_DIAMETER / 2, FOOD_DIAMETER, FOOD_DIAMETER);
    }

    private void drawDangerZone(Graphics2D g) {
        int len = Game.LENGTH_OF_FIELD;
        int heg = Game.HEIGHT_OF_FIELD;
        int dng = Game.DANGER_ZONE;
        g.fillRect(0, 0, len, dng);
        g.fillRect(len - dng, dng, dng, heg - dng);
        g.fillRect(0, heg - dng, len - dng, dng);
        g.fillRect(0, dng, dng, heg - dng - dng);
    }

    private void drawAllPawns(Graphics2D g) {
        g.setColor(MY_ORANGE);

        if (LEADER_DRAWING) {
            boolean leaderDrawed = false;
            for (Pawn pawn : Game.pawns) {
                if (pawn.isAlive()) {
                    if (!leaderDrawed) {
                        g.setColor(Color.blue);
                        drawPawn(g, pawn);
                        g.setColor(MY_ORANGE);
                        leaderDrawed = true;
                    } else {
                        drawPawn(g, pawn);
                    }
                }
            }
        } else {
            for (Pawn pawn : Game.pawns) {
                if (pawn.isAlive()) {
                    drawPawn(g, pawn);
                }
            }
        }
    }

    private void drawPawn(Graphics2D g, Pawn pawn) {
        int x = Math.round(pawn.getX());
        int y = Math.round(pawn.getY());
        g.drawOval(x - PAWN_DIAMETER / 2, y - PAWN_DIAMETER / 2, PAWN_DIAMETER, PAWN_DIAMETER);
        g.drawLine(x, y, (int) Math.round(x + Math.cos(pawn.getAbsAngle()) * PAWN_DIAMETER),
                (int) Math.round(y - Math.sin(pawn.getAbsAngle()) * PAWN_DIAMETER));
    }

}
