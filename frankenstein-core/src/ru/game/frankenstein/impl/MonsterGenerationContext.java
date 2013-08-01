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

package ru.game.frankenstein.impl;

import ru.game.frankenstein.*;
import ru.game.frankenstein.util.Rectangle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Internal structure used in monster generation
 */
public class MonsterGenerationContext
{
    /**
     * Axis-aligned rectangle that contains whole generated sprite inside it. Used for cropping.
     */
    private Rectangle cropRect = new Rectangle();

    private FrankensteinImage canvas;

    /**
     * Number of monster parts of type 'BODY' used in this monster. Works as a restriction to prevent infinite generation
     */
    private int bodyCount = 0;

    /**
     * Contains information about groups. All attachment points within same group receive same monster part
     */
    private Map<String, MonsterPart> groups = new HashMap<String, MonsterPart>();

    public static final int CANVAS_WIDTH = 400;

    public static final int CANVAS_HEIGHT = 400;

    private final MonsterGenerationParams params;

    /**
     * Parts that can be used in generating this monster. Can be equal to current part library set, or smaller, if
     * additional part filtering (by string tags or other means) is requested by user
     */
    private Map<MonsterPartType, Collection<MonsterPart>> suitableParts;

    public MonsterGenerationContext(MonsterPartsSet partsSet, ImageFactory imageFactory, MonsterGenerationParams params) throws FrankensteinException {
        this.canvas = imageFactory.createImage(CANVAS_WIDTH, CANVAS_HEIGHT);
        this.params = params;
        filterSuitableParts(partsSet);
    }


    public FrankensteinImage getCanvas() {
        return canvas;
    }

    public Rectangle getCropRect() {
        return cropRect;
    }

    public void addBody()
    {
        bodyCount++;
    }

    public int getBodyCount() {
        return bodyCount;
    }

    public Map<String, MonsterPart> getGroups() {
        return groups;
    }

    public FrankensteinImage getCroppedImage()
    {
        return canvas.getSubImage(cropRect);
    }

    public MonsterGenerationParams getParams() {
        return params;
    }

    public Map<MonsterPartType, Collection<MonsterPart>> getSuitableParts() {
        return suitableParts;
    }

    private boolean filter(MonsterPart mp)
    {
        if (mp.tags == null) {
            return true;
        }

        for (String tag : mp.tags) {
            if (params.tags.contains(tag)) {
                return true;
            }
        }

        return false;
    }

    private void filterSuitableParts(MonsterPartsSet set) throws FrankensteinException {
        if (params.tags == null || params.tags.isEmpty()) {
            // user didn't request filtering
            suitableParts = set.getParts();
            return;
        }

        suitableParts = new HashMap<MonsterPartType, Collection<MonsterPart>>();
        for (Map.Entry<MonsterPartType, Collection<MonsterPart>> entry : set.getParts().entrySet()) {
            Collection<MonsterPart> newCollection = new ArrayList<MonsterPart>(entry.getValue().size());
            for (MonsterPart monsterPart : entry.getValue()) {
                if (filter(monsterPart)) {
                    newCollection.add(monsterPart);
                }
            }
            suitableParts.put(entry.getKey(), newCollection);
        }

        if (suitableParts.get(MonsterPartType.MONSTER_BODY).isEmpty()) {
            throw new FrankensteinException("Can not generate monster, as no MONSTER_BODY parts are available because of provided restrictions (check your tags)");
        }
    }
}
