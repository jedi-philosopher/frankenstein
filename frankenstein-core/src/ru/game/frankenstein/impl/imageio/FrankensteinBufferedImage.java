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

package ru.game.frankenstein.impl.imageio;

import ru.game.frankenstein.FrankensteinImage;
import ru.game.frankenstein.util.Rectangle;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.util.Map;

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
            op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BICUBIC);
            return new FrankensteinBufferedImage(op.filter(myImage, null));
        }

        if (flipHorizontal && !flipVertical) {
            // Flip the image horizontally
            tx = AffineTransform.getScaleInstance(-1, 1);
            tx.translate(-myImage.getWidth(null), 0);
            op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BICUBIC);
            return new FrankensteinBufferedImage(op.filter(myImage, null));
        }

        // Flip the image vertically and horizontally;
        // equivalent to rotating the image 180 degrees
        tx = AffineTransform.getScaleInstance(-1, -1);
        tx.translate(-myImage.getWidth(null), -myImage.getHeight(null));
        op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BICUBIC);
        return new FrankensteinBufferedImage(op.filter(myImage, null));
    }

    @Override
    public void draw(FrankensteinImage other, int x, int y, int rotationCenterX, int rotationCenterY, int angle) {
        if (!(other instanceof FrankensteinBufferedImage)) {
            throw new IllegalArgumentException("other image should be of same class FrankensteinBufferedImage");
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

    @Override
    public FrankensteinImage replaceColors(Map<Color, Integer> sourceColors, Map<Integer, Color> newColors) {
        if (sourceColors == null || sourceColors.isEmpty() || newColors == null || newColors.isEmpty()) {
            return this;
        }
        Raster raster = myImage.getData();
        BufferedImage newImage = new BufferedImage(myImage.getWidth(), myImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
        WritableRaster newRaster = newImage.getWritableTile(0, 0);

        int[] tmpArray = new int[4];
        for (int i = 0; i < myImage.getWidth(); ++i) {
            for (int j = 0; j < myImage.getHeight(); ++j) {
                raster.getPixel(i, j, tmpArray);
                // raster is RGBA even if image is ARGB O_o
                Color color = new Color(tmpArray[0], tmpArray[1], tmpArray[2]);
                Integer id = sourceColors.get(color);
                if (id != null) {
                    Color newColor = newColors.get(id);
                    if (newColor != null) {
                        // leave same alpha, replace only color components
                        tmpArray[0] = newColor.getRed();
                        tmpArray[1] = newColor.getGreen();
                        tmpArray[2] = newColor.getBlue();
                        newRaster.setPixel(i, j, tmpArray);
                        continue;
                    } else {
                        System.err.println("No mapping for base color " + id);
                    }
                }
                // no mapping for this color, write it as it is
                newRaster.setPixel(i, j, tmpArray);
            }
        }

        return new FrankensteinBufferedImage(newImage);
    }

    public BufferedImage getImpl() {
        return myImage;
    }
}
