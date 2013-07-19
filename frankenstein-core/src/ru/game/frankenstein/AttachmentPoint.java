package ru.game.frankenstein;

/**
 * Represents a point, where different monster parts can be connected.
 */
public class AttachmentPoint
{
    public final int x;

    public final int y;

    public final int angle;

    /**
     * Only monster parts of these types can be attached to this point
     */
    public final MonsterPartType[] availableTypes;

    /**
     * If set, all AttachmentPoints sharing same groupId will be given same limbs (e.g. if you want all legs of a monster to be the same)
     */
    public final String groupId;

    public final boolean flipX;

    public final boolean flipY;

    public AttachmentPoint(int x, int y, int angle, MonsterPartType[] availableTypes) {
        this(x, y, angle, availableTypes, null, false, false);
    }

    public AttachmentPoint(int x, int y, int angle, MonsterPartType[] availableTypes, String groupId, boolean flipX, boolean flipY) {
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.availableTypes = availableTypes;
        this.groupId = groupId;
        this.flipX = flipX;
        this.flipY = flipY;
    }
}
