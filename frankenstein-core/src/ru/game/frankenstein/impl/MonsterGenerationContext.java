package ru.game.frankenstein.impl;

import ru.game.frankenstein.FrankensteinImage;
import ru.game.frankenstein.ImageFactory;

import java.awt.*;

/**
 * Internal structure used in monster generation
 */
public class MonsterGenerationContext
{
    /**
     * Axis-aligned rectangle that contains whole generated sprite inside it. Used for cropping.
     */
    private Rectangle cropRect;

    private FrankensteinImage canvas;

    /**
     * Number of monster parts of type 'BODY' used in this monster. Works as a restriction to prevent infinite generation
     */
    private int bodyCount = 0;

    private static final int CANVAS_WIDTH = 400;

    private static final int CANVAS_HEIGHT = 400;

    public MonsterGenerationContext(ImageFactory imageFactory)
    {
        canvas = imageFactory.createImage(CANVAS_WIDTH, CANVAS_HEIGHT);
    }


    public FrankensteinImage getCanvas() {
        return canvas;
    }

    public Rectangle getCropRect() {
        return cropRect;
    }

    public void addBody()
    {
        bodyCount++;
    }

    public int getBodyCount() {
        return bodyCount;
    }
}
