package ru.game.frankenstein;

import ru.game.frankenstein.util.Rectangle;

import java.awt.*;
import java.util.Map;

/**
 * Base interface, wrapping all required image processing functionality
 */
public interface FrankensteinImage
{
    /**
     * Get image width in pixels
     */
    public int getWidth();

    /**
     * Get image height in pixels
     */
    public int getHeight();

    /**
     * Returns a copy of this image that is flipped horizontal or vertical or both
     */
    public FrankensteinImage flip(boolean flipHorizontal, boolean flipVertical);

    /**
     * Draw other image on this one in given coordinates and rotation.
     * @param other Image to draw on this image canvas
     * @param x Offset of other image
     * @param y Offset of other image
     * @param rotationCenterX Center of rotation X of other image
     * @param rotationCenterY Center of rotation Y of other image
     * @param angle Angle of rotation of other image, around given center
     */
    public void draw(FrankensteinImage other, int x, int y, int rotationCenterX, int rotationCenterY, int angle);

    /**
     * Get a cropped copy of this image
     */
    public FrankensteinImage getSubImage(Rectangle rectangle);

    public FrankensteinImage replaceColors(Map<Color, Integer> sourceColors, Map<Integer, Color> newColors);
}
