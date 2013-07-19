
package ru.game.frankenstein.impl.imageio;

import ru.game.frankenstein.FrankensteinException;
import ru.game.frankenstein.FrankensteinImage;
import ru.game.frankenstein.ImageFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Loads buffered images
 */
public class BufferedImageFactory implements ImageFactory
{
    @Override
    public FrankensteinImage loadImage(String file) throws FrankensteinException {
        File f = new File(file);
        if (f.exists()) {
            return loadImage(f);
        }
        InputStream is = BufferedImage.class.getClassLoader().getResourceAsStream(file);
        try {
            if (is != null) {
                return loadImage(is);
            }
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }

        throw new FrankensteinException("Failed to find image " + file);
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
}
