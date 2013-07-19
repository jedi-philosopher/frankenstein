package ru.game.frankenstein;

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
    public FrankensteinImage flip(boolean flipVertical, boolean flipHorizontal);

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
}
