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

    public final boolean flipHorizontal;

    public final boolean flipVertical;

    public AttachmentPoint(int x, int y, int angle, MonsterPartType[] availableTypes) {
        this(x, y, angle, availableTypes, null, false, false);
    }

    public AttachmentPoint(int x, int y, int angle, MonsterPartType[] availableTypes, String groupId, boolean flipHorizontal, boolean flipVertical) {
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.availableTypes = availableTypes;
        this.groupId = groupId;
        this.flipHorizontal = flipHorizontal;
        this.flipVertical = flipVertical;
    }
}
