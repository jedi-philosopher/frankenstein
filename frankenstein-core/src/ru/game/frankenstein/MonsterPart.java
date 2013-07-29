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
    private transient FrankensteinImage image = null;

    public MonsterPart(MonsterPartType type, AttachmentPoint[] attachmentPoints, String imageId, String[] tags, String[] textDescriptions) {
        this.type = type;
        this.attachmentPoints = attachmentPoints;
        this.imageId = imageId;
        this.tags = tags;
        this.textDescriptions = textDescriptions;
    }

    public FrankensteinImage getImage(ImageFactory imageFactory) throws FrankensteinException {
        if (image == null) {
            image = imageFactory.loadImage(imageId);
        }
        return image;
    }
}
