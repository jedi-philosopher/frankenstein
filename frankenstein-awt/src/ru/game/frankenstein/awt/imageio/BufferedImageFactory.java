
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

package ru.game.frankenstein.awt.imageio;

import ru.game.frankenstein.FrankensteinColor;
import ru.game.frankenstein.FrankensteinException;
import ru.game.frankenstein.FrankensteinImage;
import ru.game.frankenstein.ImageFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Loads buffered images
 */
public class BufferedImageFactory implements ImageFactory
{
    private File root;

    public BufferedImageFactory() {
    }

    public BufferedImageFactory(File root) {
        this.root = root;
    }

    public BufferedImageFactory(String s) {
        this(new File(s));
    }

    @Override
    public FrankensteinImage loadImage(String file) throws FrankensteinException {
        File f = new File(file);
        if (f.exists()) {
            return loadImage(f);
        }
        InputStream is;
        try {
            is = new FileInputStream(new File(root, file));
        } catch (FileNotFoundException e) {
            throw new FrankensteinException(e);
        }
        try {
            return loadImage(is);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                // ignore
            }
        }
    }

    @Override
    public FrankensteinImage loadImage(File file) throws FrankensteinException {
        try {
            BufferedImage bi = ImageIO.read(file);
            return new FrankensteinBufferedImage(bi);
        } catch (IOException e) {
            throw new FrankensteinException("Failed to load image", e);
        }
    }

    @Override
    public FrankensteinImage loadImage(InputStream is) throws FrankensteinException {
        try {
            BufferedImage bi = ImageIO.read(is);
            return new FrankensteinBufferedImage(bi);
        } catch (IOException e) {
            throw new FrankensteinException("Failed to load image", e);
        }
    }

    @Override
    public FrankensteinImage createImage(int width, int height) {
        return new FrankensteinBufferedImage(new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR));
    }

    @Override
    public FrankensteinColor decodeColor(String value) {
        return new AwtColor(Color.decode(value));
    }
}
