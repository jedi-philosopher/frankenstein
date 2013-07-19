package ru.game.frankenstein.impl.imageio;

import ru.game.frankenstein.FrankensteinImage;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * Implementation of a FrankensteinImage that uses java standard BufferedImage class
 */
public class FrankensteinBufferedImage implements FrankensteinImage
{
    private BufferedImage myImage;

    public FrankensteinBufferedImage(BufferedImage myImage) {
        this.myImage = myImage;
    }

    @Override
    public int getWidth() {
        return myImage.getWidth();
    }

    @Override
    public int getHeight() {
        return myImage.getHeight();
    }

    @Override
    public FrankensteinImage flip(boolean flipVertical, boolean flipHorizontal) {
        return null;
    }

    @Override
    public void draw(FrankensteinImage other, int x, int y, int rotationCenterX, int rotationCenterY, int angle) {
        if (!(other instanceof FrankensteinBufferedImage)) {
            throw new IllegalArgumentException("other image should be of same class FrankensteinBufferedImage");
        }

        if (x < 0 || y < 0) {
            throw new IllegalArgumentException("Both x and y should be > 0");
        }

        AffineTransform at = new AffineTransform();
        at.rotate(Math.toRadians(angle), rotationCenterX, rotationCenterY);
        at.translate(x, y);
        ((Graphics2D)myImage.getGraphics()).drawImage(((FrankensteinBufferedImage)other).myImage, at, null);
    }
}
