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
