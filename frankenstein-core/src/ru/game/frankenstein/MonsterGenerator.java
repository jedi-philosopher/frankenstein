/*
 * Copyright  2013 Egor Smirnov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

    private final ImageFactory myImageFactory;

    private final MonsterPartsSet partsSet;

    private static final int RETRY_COUNT = 5;

    public MonsterGenerator(ImageFactory myImageFactory, MonsterPartsSet partsSet) {
        this.myImageFactory = myImageFactory;
        this.partsSet = partsSet;
    }

    public Monster generateMonster(MonsterGenerationParams params) throws FrankensteinException
    {
        MonsterGenerationContext context = new MonsterGenerationContext(partsSet, myImageFactory, params);

        // first select main body
        MonsterPart part = CollectionUtils.selectRandomElement(params.random, context.getSuitableParts().get(MonsterPartType.MONSTER_BODY));
        final int centerX = MonsterGenerationContext.CANVAS_WIDTH / 2;
        final int centerY = MonsterGenerationContext.CANVAS_HEIGHT / 2;
        context.getCanvas().draw(part.getImage(myImageFactory), centerX, centerY, 0, 0, 0);

        context.getCropRect().setCoordinates(centerX, centerY, part.getImage(myImageFactory).getWidth(), part.getImage(myImageFactory).getHeight());
        // now select limbs and other parts
        processPart(context, new Point(centerX, centerY), 0, null, part);


        FrankensteinImage resultMonsterImage = context.getCroppedImage();

        resultMonsterImage = resultMonsterImage.replaceColors(partsSet.getBaseColors(), params.colorMap);

        if (params.targetSize != null) {
            resultMonsterImage = resultMonsterImage.resize(params.targetSize, params.constrainProportions);
        }

        FrankensteinImage deadImage = null;
        if (params.generateDead) {
            deadImage = createCorpseImage(params, resultMonsterImage);
        }

        resultMonsterImage = addShadow(params.shadowType, resultMonsterImage).cropImage();

        return new Monster(resultMonsterImage, deadImage, null, null);
    }

    private FrankensteinImage addShadow(MonsterGenerationParams.ShadowType type, FrankensteinImage original)
    {
        if (type == null || type == MonsterGenerationParams.ShadowType.SHADOW_NONE) {
            return original;
        }

        if (type == MonsterGenerationParams.ShadowType.SHADOW_SPRITE) {
            FrankensteinImage shadowImage = CollectionUtils.selectRandomElement(partsSet.getShadowImages());
            if (shadowImage == null) {
                return original;
            }

            FrankensteinImage imgWithShadow = myImageFactory.createImage(original.getWidth(), original.getHeight() + shadowImage.getHeight() / 2);
            imgWithShadow.draw(shadowImage, (imgWithShadow.getWidth() - shadowImage.getWidth()) / 2, imgWithShadow.getHeight() - shadowImage.getHeight(), 0, 0, 0);
            imgWithShadow.draw(original, 0, 0, 0, 0, 0);
            return imgWithShadow;
        }

        if (type == MonsterGenerationParams.ShadowType.SHADOW_SKEW) {
            FrankensteinImage shadowImage = original.getShadow();
            FrankensteinImage canvas = myImageFactory.createImage(Math.max(original.getWidth(), shadowImage.getWidth()), Math.max(original.getHeight(), shadowImage.getHeight()));
            canvas.draw(shadowImage, 0, canvas.getHeight() - shadowImage.getHeight(), 0, 0, 0);
            canvas.draw(original, 0, 0, 0, 0, 0);
            return canvas;
        }

        throw new IllegalArgumentException("Unsupported shadow type " + type);
    }

    /**
     * Creates image of a dead animal using original image.
     * If animal width is greater than height - flips it horizontally. Otherwise rotates it 90 degrees.
     * Adds drops of blood
     */
    private FrankensteinImage createCorpseImage(MonsterGenerationParams params, FrankensteinImage source) {
        if (partsSet.getBloodImages() == null || partsSet.getBloodImages().isEmpty()) {
            System.err.println("Can not create dead monster image as no blood images specified in monster part library");
            return null;
        }
        FrankensteinImage result;
        final FrankensteinImage bloodImage = CollectionUtils.selectRandomElement(partsSet.getBloodImages());
        if (source.getWidth() > source.getHeight()) {
            final FrankensteinImage flippedSource = source.flip(true, true);
            final FrankensteinImage withShadow = addShadow(params.shadowType, flippedSource);
            result = myImageFactory.createImage(withShadow.getWidth(), withShadow.getHeight() + bloodImage.getHeight() / 3);
            // draw blood drops at center
            result.draw(bloodImage, result.getWidth() / 2 - 32, result.getHeight() - bloodImage.getHeight(), 0, 0, 0);
            result.draw(withShadow, 0, 0, 0, 0, 0);
        } else {
            // draw blood drops at center

            final FrankensteinImage rotated = source.rotate(90);
            final FrankensteinImage withShadow = addShadow(params.shadowType, rotated);
            result = myImageFactory.createImage(withShadow.getWidth(), withShadow.getHeight() + bloodImage.getHeight() / 3);
            result.draw(bloodImage, result.getWidth() / 2 - 32, result.getHeight() - bloodImage.getHeight(), 0, 0, 0);
            result.draw(withShadow, 0, 0, 0, 0, 0);
        }
        return result.cropImage();
    }

    /**
     * Takes given monster part and adds it to a canvas
     */
    private void addPartToCanvas(MonsterGenerationContext context, Point anchor, int sourceAngle, AttachmentPoint sourcePoint, AttachmentPoint partPoint, MonsterPart part) throws FrankensteinException {
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

        int vectorX = sourcePoint.x;
        int vectorY = sourcePoint.y;

        int vectorXRotated = (int) Math.round(vectorX * Math.cos(Math.toRadians(sourceAngle)) - vectorY * Math.sin(Math.toRadians(sourceAngle)))  - centerX;
        int vectorYRotated = (int) Math.round(vectorX * Math.sin(Math.toRadians(sourceAngle)) + vectorY * Math.cos(Math.toRadians(sourceAngle))) - centerY;

        int x = anchor.x + vectorXRotated;
        int y = anchor.y + vectorYRotated;

        context.getCanvas().draw(image, x, y, centerX, centerY, sourcePoint.angle + sourceAngle);

        Rectangle AABB = GeometryUtils.getRotatedRectangleAABB(x + centerX, y + centerY, x, y, x + image.getWidth(), y + image.getHeight(), (float) Math.toRadians(sourcePoint.angle + sourceAngle));

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

        return CollectionUtils.selectRandomElement(context.getParams().random, context.getSuitableParts().get(type));
    }

    private void processPart(MonsterGenerationContext context, Point root, int angle, AttachmentPoint apForRoot,  MonsterPart part) throws FrankensteinException {
        for (AttachmentPoint ap : part.attachmentPoints) {
            if (ap == apForRoot) {
                // this is attachment point that leads back to our parent limb, do not process it
                continue;
            }
            MonsterPart newPart;
            AttachmentPoint partPoint = null;
            int retries = 0;
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
            } while (retries ++ < RETRY_COUNT);

            if (partPoint == null) {
                //TODO: look better, instead of random
                System.err.println("Failed to find suitable part for attachment point");
                continue;
            }

            if (newPart.type == MonsterPartType.MONSTER_BODY) {
                context.addBody();
            }
            addPartToCanvas(context, root, angle, ap, partPoint, newPart);

            Point vectorToAttachPoint = new Point(ap.x, ap.y);
            Point oldVectorToRootInLimb = new Point(-partPoint.x, -partPoint.y);
            Point newVectorToRoot = new Point((int)Math.round(oldVectorToRootInLimb.x * Math.cos(Math.toRadians(angle + ap.angle)) - oldVectorToRootInLimb.y * Math.sin(Math.toRadians(angle + ap.angle)))
                    , (int) Math.round(oldVectorToRootInLimb.x * Math.sin(Math.toRadians(angle + ap.angle)) + oldVectorToRootInLimb.y * Math.cos(Math.toRadians(angle + ap.angle)))
            );
            Point newRoot = new Point(root.x + vectorToAttachPoint.x + newVectorToRoot.x, root.y + vectorToAttachPoint.y + newVectorToRoot.y);

            processPart(context, newRoot, angle + ap.angle, partPoint, newPart);
        }
    }
}
