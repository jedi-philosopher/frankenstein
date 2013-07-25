package ru.game.frankenstein;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Contains a set of monster parts and some additional settings that they have (such as base colors)
 */
public class MonsterPartsSet
{
    /**
     * Colors that are used in current part set and that can be replaced when colorizing a monster.
     * Start from 1, which is the darkest color.
     */
    private Map<Integer, Color> baseColors;

    /**
     * Parts used in this set
     */
    private Map<MonsterPartType, Collection<MonsterPart>> parts;

    /**
     * List of images containing drops of blood, that will be used when creating 'dead' monster sprite
     */
    private FrankensteinImage[] bloodImages;

    /**
     * Image with shadow (a simple black figure with opacity) that will be drawn below monster
     */
    private FrankensteinImage shadowImage;

    public MonsterPartsSet() {
        baseColors = new HashMap<Integer, Color>();
        parts = new HashMap<MonsterPartType, Collection<MonsterPart>>();
    }

    public MonsterPartsSet(Map<Integer, Color> baseColors, Map<MonsterPartType, Collection<MonsterPart>> parts, FrankensteinImage[] bloodImages, FrankensteinImage shadowImage) {
        this.baseColors = baseColors;
        this.parts = parts;
        this.bloodImages = bloodImages;
        this.shadowImage = shadowImage;
    }

    public Map<Integer, Color> getBaseColors() {
        return baseColors;
    }

    public Map<MonsterPartType, Collection<MonsterPart>> getParts() {
        return parts;
    }

    public FrankensteinImage[] getBloodImages() {
        return bloodImages;
    }

    public FrankensteinImage getShadowImage() {
        return shadowImage;
    }

    public void addParts(MonsterPart... newParts)
    {
        for (MonsterPart part : newParts) {
            Collection<MonsterPart> collection = parts.get(part.type);
            if (collection == null) {
                collection = new ArrayList<MonsterPart>();
            }
            collection.add(part);
            parts.put(part.type, collection);
        }
    }
}
