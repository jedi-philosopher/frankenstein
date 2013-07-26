package ru.game.frankenstain.test;

import junit.framework.Assert;
import org.junit.Test;
import ru.game.frankenstein.impl.imageio.FrankensteinBufferedImage;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created with IntelliJ IDEA.
 * User: Egor.Smirnov
 * Date: 26.07.13
 * Time: 14:51
 */
public class TestBufferedImage
{
    final int TEST_IMAGE_SIZE = 16;
    public BufferedImage createBlankImage()
    {
        BufferedImage bi = new BufferedImage(TEST_IMAGE_SIZE, TEST_IMAGE_SIZE, BufferedImage.TYPE_3BYTE_BGR);
        Graphics g = bi.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, TEST_IMAGE_SIZE, TEST_IMAGE_SIZE);
        return bi;
    }

    @Test
    public void testFlipHorizontal()
    {
        BufferedImage bi = createBlankImage();
        bi.setRGB(0, 0, Color.RED.getRGB());

        FrankensteinBufferedImage frankensteinBufferedImage = new FrankensteinBufferedImage(bi);
        FrankensteinBufferedImage flipped = (FrankensteinBufferedImage) frankensteinBufferedImage.flip(true, false);

        BufferedImage result = flipped.getImpl();
        Assert.assertEquals(result.getRGB(0, 0), Color.WHITE.getRGB());
        Assert.assertEquals(result.getRGB(0, TEST_IMAGE_SIZE - 1), Color.WHITE.getRGB());
        Assert.assertEquals(result.getRGB(TEST_IMAGE_SIZE - 1, 0), Color.RED.getRGB());
        Assert.assertEquals(result.getRGB(TEST_IMAGE_SIZE - 1, TEST_IMAGE_SIZE - 1), Color.WHITE.getRGB());
    }

    @Test
    public void testFlipVertical()
    {
        BufferedImage bi = createBlankImage();
        bi.setRGB(0, 0, Color.RED.getRGB());

        FrankensteinBufferedImage frankensteinBufferedImage = new FrankensteinBufferedImage(bi);
        FrankensteinBufferedImage flipped = (FrankensteinBufferedImage) frankensteinBufferedImage.flip(false, true);

        BufferedImage result = flipped.getImpl();
        Assert.assertEquals(result.getRGB(0, 0), Color.WHITE.getRGB());
        Assert.assertEquals(result.getRGB(0, TEST_IMAGE_SIZE - 1), Color.RED.getRGB());
        Assert.assertEquals(result.getRGB(TEST_IMAGE_SIZE - 1, 0), Color.WHITE.getRGB());
        Assert.assertEquals(result.getRGB(TEST_IMAGE_SIZE - 1, TEST_IMAGE_SIZE - 1), Color.WHITE.getRGB());
    }

    @Test
    public void testFlipBoth()
    {
        BufferedImage bi = createBlankImage();
        bi.setRGB(0, 0, Color.RED.getRGB());

        FrankensteinBufferedImage frankensteinBufferedImage = new FrankensteinBufferedImage(bi);
        FrankensteinBufferedImage flipped = (FrankensteinBufferedImage) frankensteinBufferedImage.flip(true, true);

        BufferedImage result = flipped.getImpl();
        Assert.assertEquals(result.getRGB(0, 0), Color.WHITE.getRGB());
        Assert.assertEquals(result.getRGB(0, TEST_IMAGE_SIZE - 1), Color.WHITE.getRGB());
        Assert.assertEquals(result.getRGB(TEST_IMAGE_SIZE - 1, 0), Color.WHITE.getRGB());
        Assert.assertEquals(result.getRGB(TEST_IMAGE_SIZE - 1, TEST_IMAGE_SIZE - 1), Color.RED.getRGB());
    }
}
