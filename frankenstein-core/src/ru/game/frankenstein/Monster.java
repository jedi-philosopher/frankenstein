package ru.game.frankenstein;

/**
 * Result of a generation
 */
public class Monster
{
    /**
     * Monster image itself
     */
    public final FrankensteinImage monsterImage;

    /**
     * Additional image of a dead monster. monsterImage dropped on its side and with drops of blood beneath it.
     * Can be null.
     */
    public final FrankensteinImage deadImage;

    /**
     * Detailed text description for a monster.
     * Can be null.
     */
    public final String textDescription;

    /**
     * List of tags used in monster generation.
     * Can be null.
     */
    public final String[] tags;

    public Monster(FrankensteinImage monsterImage) {
        this(monsterImage, null, null, null);
    }

    public Monster(FrankensteinImage monsterImage, FrankensteinImage deadImage, String textDescription, String[] tags) {
        this.monsterImage = monsterImage;
        this.deadImage = deadImage;
        this.textDescription = textDescription;
        this.tags = tags;
    }
}
