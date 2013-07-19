package ru.game.frankenstein;


import ru.game.frankenstein.impl.MonsterGenerationContext;
import ru.game.frankenstein.util.CollectionUtils;
import ru.game.frankenstein.util.GeometryUtils;
import ru.game.frankenstein.util.Point;

import java.awt.*;
import java.util.Map;

/**
 * Main class, performs actual monster generation
 */
public class MonsterGenerator
{

    /**
     * Maximum amount of parts of type MONSTER_BODY in a single monster
     */
    public static final int BODY_LIMIT = 3;

    private ImageFactory myImageFactory;

    private MonsterPartsSet partsSet;

    public MonsterGenerator(ImageFactory myImageFactory, MonsterPartsSet partsSet) {
        this.myImageFactory = myImageFactory;
        this.partsSet = partsSet;
    }

    public Monster generateMonster(MonsterGenerationParams params)
    {
        return null;
    }

    /**
     * Takes given monster part and adds it to a canvas

     */
    private void addPartToCanvas(MonsterGenerationContext context, Point anchor, AttachmentPoint sourcePoint, AttachmentPoint partPoint, MonsterPart part)
    {
        int centerX = partPoint.x;
        int centerY = partPoint.y;

        FrankensteinImage image = part.image;
        if (sourcePoint.flipX || sourcePoint.flipY) {
            if (sourcePoint.flipX) {
                centerX = part.image.getWidth() - centerX;
            }
            if (sourcePoint.flipY) {
                centerY = part.image.getHeight() - centerY;
            }
            image = part.image.flip(sourcePoint.flipX, sourcePoint.flipY);
        }

        int x = anchor.x + sourcePoint.x - centerX;
        int y = anchor.y + sourcePoint.y - centerY;

        context.getCanvas().draw(part.image, x, y, centerX, centerY, sourcePoint.angle);

        Rectangle AABB = GeometryUtils.getRotatedRectangleAABB(x + centerX, y + centerY, x, y, x + image.getWidth(), y + image.getHeight(), (float) Math.toRadians(sourcePoint.angle));

        int cropX = (int) Math.min(context.getCropRect().getX(), AABB.getX());
        int cropY = (int) Math.min(context.getCropRect().getY(), AABB.getY());
        context.getCropRect().setBounds(cropX, cropY, (int)Math.max(context.getCropRect().getX() + context.getCropRect().getWidth(), AABB.getX() + AABB.getWidth()) - cropX, (int)Math.max(context.getCropRect().getY() + context.getCropRect().getHeight(), AABB.getY() + AABB.getHeight()) - cropY);

    }

    private MonsterPart selectRandomPartForPoint(MonsterGenerationContext context, AttachmentPoint ap) {
        MonsterPartType type = CollectionUtils.selectRandomElement(ap.availableTypes);
        if (type == MonsterPartType.MONSTER_BODY && context.getBodyCount()>= BODY_LIMIT) {
            for (MonsterPartType pt : ap.availableTypes) {
                if (pt != MonsterPartType.MONSTER_BODY) {
                    type = pt;
                    break;
                }
            }
        }

        return CollectionUtils.selectRandomElement(partsSet.getParts().get(type));
    }

    private int processPart(MonsterGenerationContext context, Point root, MonsterPart part, int bodyCount, Map<String, MonsterPart> groups) {
        for (AttachmentPoint ap : part.attachmentPoints) {
            MonsterPart newPart = null;
            if (ap.groupId == null) {
                newPart = selectRandomPartForPoint(context, ap);
            } else {
                newPart = groups.get(ap.groupId);
                if (newPart == null) {
                    newPart = selectRandomPartForPoint(context, ap);
                    groups.put(ap.groupId, newPart);
                }
            }

            addPartToCanvas(context, root, ap, null, newPart);

            bodyCount += processPart(context, new Point(ap.x, ap.y), newPart, bodyCount, groups);
        }
        return bodyCount;
    }
}
