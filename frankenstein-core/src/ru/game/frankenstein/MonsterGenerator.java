package ru.game.frankenstein;


import ru.game.frankenstein.impl.MonsterGenerationContext;
import ru.game.frankenstein.util.CollectionUtils;
import ru.game.frankenstein.util.GeometryUtils;
import ru.game.frankenstein.util.Point;
import ru.game.frankenstein.util.Rectangle;

import java.util.ArrayList;
import java.util.List;

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

    public Monster generateMonster(MonsterGenerationParams params) throws FrankensteinException
    {
        MonsterGenerationContext context = new MonsterGenerationContext(myImageFactory, params);

        // first select main body
        MonsterPart part = CollectionUtils.selectRandomElement(params.random, partsSet.getParts().get(MonsterPartType.MONSTER_BODY));
        final int centerX = MonsterGenerationContext.CANVAS_WIDTH / 2;
        final int centerY = MonsterGenerationContext.CANVAS_HEIGHT / 2;
        context.getCanvas().draw(part.getImage(myImageFactory), centerX, centerY, 0, 0, 0);

        context.getCropRect().setCoordinates(centerX, centerY, part.getImage(myImageFactory).getWidth(), part.getImage(myImageFactory).getHeight());
        // now select limbs and other parts
        processPart(context, new Point(centerX, centerY), part);


        FrankensteinImage resultMonsterImage = context.getCroppedImage();

        resultMonsterImage = resultMonsterImage.replaceColors(partsSet.getBaseColors(), params.colorMap);


       /* Image img = colorise(canvas.getSubImage((int) cropRect.getX(), (int) cropRect.getY(), (int) cropRect.getWidth(), (int) cropRect.getHeight()), CollectionUtils.selectRandomElement(allowedColors));
        Image imgWithShadow;
        try {
            imgWithShadow = new Image(img.getWidth(), img.getHeight() + 16);
            imgWithShadow.getGraphics().drawImage(shadowImages, (imgWithShadow.getWidth() - shadowImages.getWidth()) / 2, imgWithShadow.getHeight() - shadowImages.getHeight());
            imgWithShadow.getGraphics().drawImage(img, 0, 0);
        } catch (SlickException e) {
            e.printStackTrace();
            imgWithShadow = img;
        }
        Image corpseImg = createCorpseImage(img);
        desc.setImages(imgWithShadow, corpseImg);*/

        return new Monster(resultMonsterImage);
    }

    /**
     * Takes given monster part and adds it to a canvas

     */
    private void addPartToCanvas(MonsterGenerationContext context, Point anchor, AttachmentPoint sourcePoint, AttachmentPoint partPoint, MonsterPart part) throws FrankensteinException {
        int centerX = partPoint.x;
        int centerY = partPoint.y;

        FrankensteinImage image = part.getImage(myImageFactory);
        if (sourcePoint.flipHorizontal || sourcePoint.flipVertical) {
            if (sourcePoint.flipHorizontal) {
                centerX = part.getImage(myImageFactory).getWidth() - centerX;
            }
            if (sourcePoint.flipVertical) {
                centerY = part.getImage(myImageFactory).getHeight() - centerY;
            }
            image = part.getImage(myImageFactory).flip(sourcePoint.flipHorizontal, sourcePoint.flipVertical);
        }

        int x = anchor.x + sourcePoint.x - centerX;
        int y = anchor.y + sourcePoint.y - centerY;

        context.getCanvas().draw(image, x, y, centerX, centerY, sourcePoint.angle);

        Rectangle AABB = GeometryUtils.getRotatedRectangleAABB(x + centerX, y + centerY, x, y, x + image.getWidth(), y + image.getHeight(), (float) Math.toRadians(sourcePoint.angle));

        int cropX = Math.min(context.getCropRect().getX(), AABB.getX());
        int cropY = Math.min(context.getCropRect().getY(), AABB.getY());
        context.getCropRect().setCoordinates(cropX, cropY, Math.max(context.getCropRect().getX() + context.getCropRect().getWidth(), AABB.getX() + AABB.getWidth()) - cropX, Math.max(context.getCropRect().getY() + context.getCropRect().getHeight(), AABB.getY() + AABB.getHeight()) - cropY);

    }

    private MonsterPart selectRandomPartForPoint(MonsterGenerationContext context, AttachmentPoint ap) {
        MonsterPartType type = CollectionUtils.selectRandomElement(context.getParams().random, ap.availableTypes);
        if (type == MonsterPartType.MONSTER_BODY && context.getBodyCount()>= BODY_LIMIT) {
            for (MonsterPartType pt : ap.availableTypes) {
                if (pt != MonsterPartType.MONSTER_BODY) {
                    type = pt;
                    break;
                }
            }
        }

        return CollectionUtils.selectRandomElement(context.getParams().random, partsSet.getParts().get(type));
    }

    private void processPart(MonsterGenerationContext context, Point root, MonsterPart part) throws FrankensteinException {
        for (AttachmentPoint ap : part.attachmentPoints) {
            MonsterPart newPart;
            AttachmentPoint partPoint;
            do {
                if (ap.groupId == null) {
                    newPart = selectRandomPartForPoint(context, ap);
                } else {
                    newPart = context.getGroups().get(ap.groupId);
                    if (newPart == null) {
                        newPart = selectRandomPartForPoint(context, ap);
                        context.getGroups().put(ap.groupId, newPart);
                    }
                }

                // check that this part can be attached to this source
                List<AttachmentPoint> newPartPoints = new ArrayList<AttachmentPoint>();
                for (AttachmentPoint newPartPoint: newPart.attachmentPoints) {
                    for (MonsterPartType mpt : newPartPoint.availableTypes) {
                        if (mpt == part.type) {
                            newPartPoints.add(newPartPoint);
                        }
                    }
                }
                if (newPartPoints.isEmpty()) {
                    // this part has no points for attaching to current root
                    continue;
                }
                partPoint = CollectionUtils.selectRandomElement(context.getParams().random, newPartPoints);
                break;
            } while (true);

            if (newPart.type == MonsterPartType.MONSTER_BODY) {
                context.addBody();
            }
            addPartToCanvas(context, root, ap, partPoint, newPart);
        }
    }
}
