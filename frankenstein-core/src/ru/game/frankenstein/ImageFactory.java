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

import java.io.File;
import java.io.InputStream;

/**
 * Interface for classes that are responsible for saving and loading images
 */
public interface ImageFactory
{
    public FrankensteinImage loadImage(String file) throws FrankensteinException;

    public FrankensteinImage loadImage(File file) throws FrankensteinException;

    public FrankensteinImage loadImage(InputStream is) throws FrankensteinException;

    public FrankensteinImage createImage(int width, int height);
}
