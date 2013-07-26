package ru.game.frankenstein;

import java.util.Collections;
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
    public final boolean generateDead;

    /**
     * If true, monster will have text description generated. Text descriptions are created by combining descriptions of individual
     * monster parts, that were used in generation of this monster.
     */
    public final boolean generateText;

    /**
     * If set, only those monster parts, that have at least one tag from this list, will be used in generation
     */
    public final Set<String> tags;

    /**
     * Random used in generation. User can provide fixed-seed random to generate same monster image multiple times (so no need to store generated
     * images between program runs, only seed value needs to be stored).
     */
    public final Random random;

    public MonsterGenerationParams() {
        this(true, false, Collections.<String>emptySet());
    }

    public MonsterGenerationParams(boolean generateDead, boolean generateText, Set<String> tags) {
        this.generateDead = generateDead;
        this.generateText = generateText;
        this.tags = tags;
        this.random = new Random();
    }

    public MonsterGenerationParams(boolean generateDead, boolean generateText, Set<String> tags, Random random) {
        this.generateDead = generateDead;
        this.generateText = generateText;
        this.tags = tags;
        this.random = random;
    }
}
