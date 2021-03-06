/*
 * Copyright  2013 Egor Smirnov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.game.frankenstein.util;



public class GeometryUtils
{
    /**
     * Return axis-aligned bounding box of a rectangle with given coordinates, rotated around a given center point
     */
    public static Rectangle getRotatedRectangleAABB(int centerX, int centerY, int x1, int y1, int x2, int y2, float angleRadians) {
        double x1prim = (x1 - centerX) * Math.cos(angleRadians) - (y1 - centerY) * Math.sin(angleRadians);
        double y1prim = (x1 - centerX) * Math.sin(angleRadians) + (y1 - centerY) * Math.cos(angleRadians);

        double x12prim = (x1 - centerX) * Math.cos(angleRadians) - (y2 - centerY) * Math.sin(angleRadians);
        double y12prim = (x1 - centerX) * Math.sin(angleRadians) + (y2 - centerY) * Math.cos(angleRadians);

        double x2prim = (x2 - centerX) * Math.cos(angleRadians) - (y2 - centerY) * Math.sin(angleRadians);
        double y2prim = (x2 - centerX) * Math.sin(angleRadians) + (y2 - centerY) * Math.cos(angleRadians);

        double x21prim = (x2 - centerX) * Math.cos(angleRadians) - (y1 - centerY) * Math.sin(angleRadians);
        double y21prim = (x2 - centerX) * Math.sin(angleRadians) + (y1 - centerY) * Math.cos(angleRadians);

        double rx1 = centerX + Math.min(Math.min(x1prim, x2prim), Math.min(x12prim, x21prim));
        double ry1 = centerY + Math.min(Math.min(y1prim, y2prim), Math.min(y12prim, y21prim));

        double rx2 = centerX + Math.max(Math.max(x1prim, x2prim), Math.max(x12prim, x21prim));
        double ry2 = centerY + Math.max(Math.max(y1prim, y2prim), Math.max(y12prim, y21prim));

        return new Rectangle((int) rx1, (int) ry1, (int) (rx2 - rx1), (int) (ry2 - ry1));
    }
}
