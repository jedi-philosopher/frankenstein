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

import ru.game.frankenstein.util.Rectangle;
import ru.game.frankenstein.util.Size;

import java.awt.*;
import java.util.Map;

/**
 * Base interface, wrapping all required image processing functionality
 */
public interface FrankensteinImage
{
    /**
     * Get image width in pixels
     */
    public int getWidth();

    /**
     * Get image height in pixels
     */
    public int getHeight();

    /**
     * Returns a copy of this image that is flipped horizontal or vertical or both
     */
    public FrankensteinImage flip(boolean flipHorizontal, boolean flipVertical);

    /**
     * Draw other image on this one in given coordinates and rotation.
     * @param other Image to draw on this image canvas
     * @param x Offset of other image
     * @param y Offset of other image
     * @param rotationCenterX Center of rotation X of other image
     * @param rotationCenterY Center of rotation Y of other image
     * @param angle Angle of rotation of other image, around given center
     */
    public void draw(FrankensteinImage other, int x, int y, int rotationCenterX, int rotationCenterY, int angle);

    /**
     * Get a cropped copy of this image
     */
    public FrankensteinImage getSubImage(Rectangle rectangle);

    public FrankensteinImage replaceColors(Map<Color, Integer> sourceColors, Map<Integer, Color> newColors);

    public FrankensteinImage resize(Size targetSize, boolean constrainProportions);

    /**
     * Create shadow image.
     * It is image mask (that has black pixels for each non-transparent pixel of original image) that is additionally skewed.
     */
    public FrankensteinImage getShadow();

    /**
     * Create a bounding box for an image and crop using it
     */
    public FrankensteinImage cropImage();
}
