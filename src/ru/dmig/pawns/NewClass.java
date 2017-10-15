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

import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import ru.dmig.pawns.agents.Pawn;
import ru.dmig.pawns.net.Network;

/**
 *
 * @author Dmig
 */
public class NewClass {
    public static void main(String[] args) throws InterruptedException {
        Pawn[] pawn = {new Pawn(0, 0)};
        Game.saveGenoms(pawn, "test.g");
        Thread.sleep(2);
        try {
            Game.loadGenoms("test.g")[0].network.printWeights();
        } catch (FileNotFoundException ex) {
            System.err.println("FNF");
        } catch (ParsingException ex) {
            System.err.println(ex);
        }
    }
}
