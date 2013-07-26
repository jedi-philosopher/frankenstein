package ru.game.frankenstein.impl;

import ru.game.frankenstein.FrankensteinImage;
import ru.game.frankenstein.ImageFactory;
import ru.game.frankenstein.MonsterGenerationParams;
import ru.game.frankenstein.MonsterPart;
import ru.game.frankenstein.util.Rectangle;

import java.util.HashMap;
import java.util.Map;

/**
 * Internal structure used in monster generation
 */
public class MonsterGenerationContext
{
    /**
     * Axis-aligned rectangle that contains whole generated sprite inside it. Used for cropping.
     */
    private Rectangle cropRect = new Rectangle();

    private FrankensteinImage canvas;

    /**
     * Number of monster parts of type 'BODY' used in this monster. Works as a restriction to prevent infinite generation
     */
    private int bodyCount = 0;

    /**
     * Contains information about groups. All attachment points within same group receive same monster part
     */
    private Map<String, MonsterPart> groups = new HashMap<String, MonsterPart>();

    public static final int CANVAS_WIDTH = 400;

    public static final int CANVAS_HEIGHT = 400;

    private final MonsterGenerationParams params;

    public MonsterGenerationContext(ImageFactory imageFactory, MonsterGenerationParams params)
    {
        this.canvas = imageFactory.createImage(CANVAS_WIDTH, CANVAS_HEIGHT);
        this.params = params;
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

    public Map<String, MonsterPart> getGroups() {
        return groups;
    }

    public FrankensteinImage getCroppedImage()
    {
        return canvas.getSubImage(cropRect);
    }

    public MonsterGenerationParams getParams() {
        return params;
    }
}
