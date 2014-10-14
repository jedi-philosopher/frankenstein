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

package ru.game.frankenstain.test;

import ru.game.frankenstein.FrankensteinColor;
import ru.game.frankenstein.FrankensteinException;
import ru.game.frankenstein.FrankensteinImage;
import ru.game.frankenstein.ImageFactory;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class TestImageFactory implements ImageFactory
{
    private final Map<String, FrankensteinImage> map = new HashMap<String, FrankensteinImage>();

    private final ImageFactory impl;

    public TestImageFactory(ImageFactory impl) {
        this.impl = impl;
    }

    public void addImage(String id, FrankensteinImage img)
    {
        map.put(id, img);
    }

    @Override
    public FrankensteinImage loadImage(String file) throws FrankensteinException {
        return map.get(file);
    }

    @Override
    public FrankensteinImage loadImage(File file) throws FrankensteinException {
        throw new UnsupportedOperationException();
    }

    @Override
    public FrankensteinImage loadImage(InputStream is) throws FrankensteinException {
        throw new UnsupportedOperationException();
    }

    @Override
    public FrankensteinImage createImage(int width, int height) {
        return impl.createImage(width, height);
    }

    @Override
    public FrankensteinColor decodeColor(String value) {
        return impl.decodeColor(value);
    }
}
