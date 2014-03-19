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

import ru.game.frankenstein.util.Size;

import java.util.*;

/**
 * Params for monster generation
 */
public class MonsterGenerationParams
{
    /**
     * If true, 'dead' monster image will be generated in addition to base image. Dead image represents monster lying on its side,
     * with a pool of blood beneath it.
     */
    public boolean generateDead;

    /**
     * If true, monster will have text description generated. Text descriptions are created by combining descriptions of individual
     * monster parts, that were used in generation of this monster.
     */
    public boolean generateText;

    /**
     * If set, only those monster parts, that have at least one tag from this list, will be used in generation
     */
    public Set<String> tags;

    /**
     * Random used in generation. User can provide fixed-seed random to generate same monster image multiple times (so no need to store generated
     * images between program runs, only seed value needs to be stored).
     */
    public Random random;

    /**
     * Monster color map. Base colors, used for sprites in monster parts library and described in library specification, will be replaced by
     * colors from this map with matching id.
     */
    public Map<Integer, ? extends FrankensteinColor> colorMap;

    /**
     * If set, result monster image will be resized to this size
     */
    public Size targetSize = null;

    public static enum ShadowType
    {
        /**
         * No shadow will be added to generated image
         */
        SHADOW_NONE,
        /**
         * Shadow will be a sprite taken from list defined in part set
         */
        SHADOW_SPRITE,
        /**
         * Shadow will be generated using monster image by applying skew transform
         */
        SHADOW_SKEW
    }

    public ShadowType shadowType = ShadowType.SHADOW_NONE;

    /**
     * If targetSize is not-null, this value defines resizing approach: either constrain proportions and fit monster image into rectangle of
     * given size (it can still be smaller on one of the sides), or to stretch generated sprite exactly to targetSize
     */
    public boolean constrainProportions = true;

    public MonsterGenerationParams() {
        this(true, false, Collections.<String>emptySet());
    }

    public MonsterGenerationParams(boolean generateDead, boolean generateText, Set<String> tags) {
        this(generateDead, generateText, tags, new Random());
    }

    public MonsterGenerationParams(boolean generateDead, boolean generateText, String... tags) {
        this(generateDead, generateText, null, new Random());
        this.tags = new HashSet<String>();
        Collections.addAll(this.tags, tags);
    }


    public MonsterGenerationParams(boolean generateDead, boolean generateText, Set<String> tags, Random random) {
        this(generateDead, generateText, tags, random, null);
    }

    public MonsterGenerationParams(boolean generateDead, boolean generateText, Set<String> tags, Random random, Map<Integer, FrankensteinColor> colorMap) {
        this.generateDead = generateDead;
        this.generateText = generateText;
        this.tags = tags;
        this.random = random;
        this.colorMap = colorMap;
    }
}
