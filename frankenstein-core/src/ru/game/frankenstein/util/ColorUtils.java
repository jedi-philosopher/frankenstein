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

/**
 * Created with IntelliJ IDEA.
 * User: Egor.Smirnov
 * Date: 26.07.13
 * Time: 16:19
 */
package ru.game.frankenstein.util;


import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ColorUtils
{
    /**
     * Returns a lighter version of a color
     */
    public static Color lightenColor(Color color) {
        float[] hsb = new float[3];
        java.awt.Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
        hsb[1] *= 0.3;
        hsb[2] = (float) Math.min(1.0, hsb[2] * 1.25);
        int rgb = java.awt.Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
        return new Color((rgb & 0x00ff0000) >> 16, (rgb & 0x0000ff00) >> 8, (rgb & 0x000000ff));
    }

    public static Color darkenColor(Color color, float coeff) {
        return new Color((int)(color.getRed() * coeff), (int)(color.getGreen() * coeff), (int)(color.getBlue() * coeff));
    }

    /**
     * Creates a default set of a 4 tints of a given base color. Two are darker, one is equal and one is lighter.
     */
    public static Map<Integer, Color> createDefault4TintMap(Color baseColor)
    {
        Map<Integer, Color> result = new HashMap<Integer, Color>();
        final Color newShadowColor = darkenColor(baseColor, 0.8f);
        final Color newDarkShadowColor = darkenColor(baseColor, 0.5f);
        final Color newBrightColor = lightenColor(baseColor);

        result.put(1, newDarkShadowColor);
        result.put(2, newShadowColor);
        result.put(3, baseColor);
        result.put(4, newBrightColor);

        return result;
    }
}
