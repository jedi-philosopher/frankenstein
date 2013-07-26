package ru.game.frankenstein;

import java.awt.*;
import java.util.Collections;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Params for monster generation
 */
public class MonsterGenerationParams
{
    /**
     * If true, 'dead' monster image will be generated in addition to base image. Dead image represents monster lying on its side,
     * with a pool of blood beneath it.
     */
    public boolean generateDead;

    /**
     * If true, monster will have text description generated. Text descriptions are created by combining descriptions of individual
     * monster parts, that were used in generation of this monster.
     */
    public boolean generateText;

    /**
     * If set, only those monster parts, that have at least one tag from this list, will be used in generation
     */
    public Set<String> tags;

    /**
     * Random used in generation. User can provide fixed-seed random to generate same monster image multiple times (so no need to store generated
     * images between program runs, only seed value needs to be stored).
     */
    public Random random;

    /**
     * Monster color map. Base colors, used for sprites in monster parts library and described in library specification, will be replaced by
     * colors from this map with matching id.
     */
    public Map<Integer, Color> colorMap;

    public MonsterGenerationParams() {
        this(true, false, Collections.<String>emptySet());
    }

    public MonsterGenerationParams(boolean generateDead, boolean generateText, Set<String> tags) {
        this(generateDead, generateText, tags, new Random());
    }

    public MonsterGenerationParams(boolean generateDead, boolean generateText, Set<String> tags, Random random) {
        this(generateDead, generateText, tags, random, null);
    }

    public MonsterGenerationParams(boolean generateDead, boolean generateText, Set<String> tags, Random random, Map<Integer, Color> colorMap) {
        this.generateDead = generateDead;
        this.generateText = generateText;
        this.tags = tags;
        this.random = random;
        this.colorMap = colorMap;
    }
}
