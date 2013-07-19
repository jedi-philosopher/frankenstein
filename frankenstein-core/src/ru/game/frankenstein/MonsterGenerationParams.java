package ru.game.frankenstein;

import java.util.Collections;
import java.util.Set;

/**
 * Params for monster generation
 */
public class MonsterGenerationParams
{
    public final boolean generateDead;

    public final boolean generateText;

    public final Set<String> tags;

    public MonsterGenerationParams() {
        this(true, false, Collections.<String>emptySet());
    }

    public MonsterGenerationParams(boolean generateDead, boolean generateText, Set<String> tags) {
        this.generateDead = generateDead;
        this.generateText = generateText;
        this.tags = tags;
    }
}
