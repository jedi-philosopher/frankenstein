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
     * Creates a set of a lighter variants of a given color
     * @param baseColor Base, most dark, variant
     * @param count Number of lighter variants to generate
     * @return Map from color id to generated color. Base color will have id 1, the lighter the variant - the greater the id
     */
    public static Map<Integer, Color> createColorGradient(Color baseColor, int count)
    {
        Map<Integer, Color> result = new HashMap<Integer, Color>();
        result.put(1, baseColor);
        float[] hsb = new float[3];
        java.awt.Color.RGBtoHSB(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), hsb);

        float brightnessStep = 0;//(1 - hsb[2]) / count; // we do not want pure white, so it is max 0.8 in brightness
        float saturationStep = 0.3f;
        for (int i = 1; i <= count; ++i) {
            hsb[1] *= saturationStep;
            hsb[2] = (float) Math.min(1.0, hsb[2] + i * brightnessStep);
            int rgb = java.awt.Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
            result.put(i, new Color((rgb & 0x00ff0000) >> 16, (rgb & 0x0000ff00) >> 8, (rgb & 0x000000ff)));
        }
        return result;
    }
}
