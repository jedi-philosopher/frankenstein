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

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Contains a set of monster parts and some additional settings that they have (such as base colors)
 */
public class MonsterPartsSet
{
    /**
     * Colors that are used in current part set and that can be replaced when colorizing a monster.
     * Mapping from actual color to ID.
     */
    private Map<Color, Integer> baseColors;

    /**
     * Parts used in this set
     */
    private Map<MonsterPartType, Collection<MonsterPart>> parts;

    /**
     * List of images containing drops of blood, that will be used when creating 'dead' monster sprite
     */
    private List<FrankensteinImage> bloodImages;

    /**
     * Image with shadow (a simple black figure with opacity) that will be drawn below monster
     */
    private List<FrankensteinImage> shadowImages;

    public MonsterPartsSet() {
        baseColors = new HashMap<Color, Integer>();
        parts = new HashMap<MonsterPartType, Collection<MonsterPart>>();
    }

    public MonsterPartsSet(Map<Color, Integer> baseColors, Map<MonsterPartType, Collection<MonsterPart>> parts, List<FrankensteinImage> bloodImages, List<FrankensteinImage> shadowImages) {
        this.baseColors = baseColors;
        this.parts = parts;
        this.bloodImages = bloodImages;
        this.shadowImages = shadowImages;
    }

    public Map<Color, Integer> getBaseColors() {
        return baseColors;
    }

    public Map<MonsterPartType, Collection<MonsterPart>> getParts() {
        return parts;
    }

    public List<FrankensteinImage> getBloodImages() {
        return bloodImages;
    }

    public List<FrankensteinImage> getShadowImages() {
        return shadowImages;
    }

    public void addParts(MonsterPart... newParts)
    {
        for (MonsterPart part : newParts) {
            Collection<MonsterPart> collection = parts.get(part.type);
            if (collection == null) {
                collection = new ArrayList<MonsterPart>();
            }
            collection.add(part);
            parts.put(part.type, collection);
        }
    }

    public void setBaseColors(Map<Color, Integer> baseColors) {
        this.baseColors = baseColors;
    }

    public void addBloodImage(FrankensteinImage bloodImage) {
        if (bloodImages == null) {
            bloodImages = new LinkedList<FrankensteinImage>();
        }
        bloodImages.add(bloodImage);
    }

    public void setShadowImages(List<FrankensteinImage> shadowImages) {
        this.shadowImages = shadowImages;
    }

    public void addShadowImage(FrankensteinImage shadowImage) {
        if (shadowImages == null) {
            shadowImages = new LinkedList<FrankensteinImage>();
        }
        shadowImages.add(shadowImage);
    }
}
