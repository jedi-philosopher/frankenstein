package ru.game.frankenstein;

/**
* Class for a piece of a monster. It can be a limb, body or decoration like mouth or eyes.
* Monster is created by combining these parts
*/
public class MonsterPart
{
    public final MonsterPartType type;

    /**
     * Points where other parts can be attached
     */
    public final AttachmentPoint[] attachmentPoints;

    /**
     * Some string id of an image, that will later be used to get this image using ImageFactory.load() method
     */
    public final String imageId;

    /**
     * List of user-defined tags for this part. User can provide list of tags when generating a monster, in this case only
     * parts containing at least one of those tags will be used.
     * This makes possible generation of monster look based on his behaviour. For example, only monsters with 'predator' tag will
     * have huge sharp deadly teeth.
     */
    public final String[] tags;

    /**
     * A list of text strings, that can be used in text description of a monster. When monster is created, resulting description will be
     * combined from random strings of every part.
     */
    public final String[] textDescriptions;

    /**
     * Cached image, retrieved using imageId
     */
    public transient FrankensteinImage image = null;

    public MonsterPart(MonsterPartType type, AttachmentPoint[] attachmentPoints, String imageId, String[] tags, String[] textDescriptions) {
        this.type = type;
        this.attachmentPoints = attachmentPoints;
        this.imageId = imageId;
        this.tags = tags;
        this.textDescriptions = textDescriptions;
    }
}
