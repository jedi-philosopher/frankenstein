package ru.game.frankenstein.impl.imageio;

import ru.game.frankenstein.FrankensteinImage;
import ru.game.frankenstein.util.Rectangle;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
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
    public FrankensteinImage flip(boolean flipHorizontal, boolean flipVertical) {
        AffineTransform tx;
        AffineTransformOp op;
        if (flipVertical && ! flipHorizontal) {
            tx = AffineTransform.getScaleInstance(1, -1);
            tx.translate(0, -myImage.getHeight(null));
            op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
            return new FrankensteinBufferedImage(op.filter(myImage, null));
        }

        if (flipHorizontal && !flipVertical) {
            // Flip the image horizontally
            tx = AffineTransform.getScaleInstance(-1, 1);
            tx.translate(-myImage.getWidth(null), 0);
            op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
            return new FrankensteinBufferedImage(op.filter(myImage, null));
        }

        // Flip the image vertically and horizontally;
        // equivalent to rotating the image 180 degrees
        tx = AffineTransform.getScaleInstance(-1, -1);
        tx.translate(-myImage.getWidth(null), -myImage.getHeight(null));
        op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return new FrankensteinBufferedImage(op.filter(myImage, null));
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
        at.translate(x, y);
        if (angle != 0) {
            at.rotate(Math.toRadians(angle), rotationCenterX, rotationCenterY);
        }
        ((Graphics2D)myImage.getGraphics()).drawImage(((FrankensteinBufferedImage)other).myImage, at, null);
    }

    @Override
    public FrankensteinImage getSubImage(Rectangle rectangle) {
        return new FrankensteinBufferedImage(myImage.getSubimage(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight()));
    }

    public BufferedImage getImpl() {
        return myImage;
    }
}
