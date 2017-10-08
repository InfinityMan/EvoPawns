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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

/**
 * JPanel for painting all activity of pawns here
 *
 * @author Dmig
 */
public class Panel extends JPanel {

    public static final int RADIUS = 30;

    @Override
    public void paint(Graphics g) {
        Graphics2D gr2d = (Graphics2D) g;
        gr2d.setBackground(Color.white);

        //long diag = Math.round(Math.sqrt(2 * RADIUS * RADIUS));
        for (Pawn pawn : Game.pawns) {
            gr2d.drawOval(Math.round(pawn.getX()) - RADIUS, Math.round(pawn.getY()) + RADIUS, RADIUS, RADIUS);
        }

    }

}
