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
package ru.dmig.util;

/**
 * Class for working with angles.
 * Rad: radians: 0 - !(Math.Pi * 2)
 * Deg: degrees: 0 - !360
 * Roul: roulette 0 - (~!)1 idk
 * @author Dmig
 */
public final class Angler {

    public static double radToRoul(double rad) {
        return rad / (Math.PI * 2);
    }

    public static double roulToRad(double roul) {
        return roul * (Math.PI * 2);
    }

    public static double degToRad(double deg) {
        return deg * 0.0174533;
    }

    public static double degToRoul(double deg) {
        return radToRoul(degToRad(deg));
    }

    public static double roulToDeg(double roul) {
        return radToDeg(roulToRad(roul));
    }

    public static double radToDeg(double rad) {
        return rad * 57.2958041;
    }

    /**
     * Converts any angle to normal. Radian in and out.
     * Some examples, but in degrees:
     * 65 -> 65
     * 365 -> 5
     * -65 -> 295
     * -365 -> 355
     * @param angle Angle to convert
     * @return Angle between 0 and ~359 (inclusive) in radians
     */
    public static double doAngle(double angle) {
        if (angle >= 0 && angle < 2 * Math.PI) {
            return angle;
        } else if (angle == Math.PI * 2) {
            return 0;
        } else if (angle < 0) {
            return doAngle(angle + Math.PI * 2);
        } else {
            return doAngle(angle - Math.PI * 2);
        }
    }
    
}
