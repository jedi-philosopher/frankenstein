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

import junit.framework.Assert;
import org.junit.Test;
import ru.game.frankenstein.*;
import ru.game.frankenstein.impl.imageio.BufferedImageFactory;
import ru.game.frankenstein.impl.imageio.FrankensteinBufferedImage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: User
 * Date: 15.08.13
 * Time: 23:10
 */
public class TestMonsterGenerator
{
    public BufferedImage createBlankImageWithBorder(int width, int height)
    {
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.getGraphics();
        g.setColor(Color.black);
        g.drawRect(0, 0, bi.getWidth() - 1, bi.getHeight() - 1);
        return bi;
    }
    @Test
    public void testGenerateLimbWithDecoration() throws FrankensteinException, IOException {
        BufferedImageFactory imageFactory = new BufferedImageFactory();
        TestImageFactory testImageFactory = new TestImageFactory(imageFactory);

        MonsterPartsSet set = new MonsterPartsSet();
        set.addParts(new MonsterPart(MonsterPartType.MONSTER_BODY, new AttachmentPoint[]{new AttachmentPoint(2, 8, 0, new MonsterPartType[]{MonsterPartType.MONSTER_LIMB})}, "body", null, null)
                ,new MonsterPart(MonsterPartType.MONSTER_LIMB, new AttachmentPoint[]{
                            new AttachmentPoint(1, 2, 0, new MonsterPartType[]{MonsterPartType.MONSTER_BODY})
                            , new AttachmentPoint(7, 2, 0, new MonsterPartType[]{MonsterPartType.MONSTER_DECORATION})
                }, "limb", null, null)
                ,new MonsterPart(MonsterPartType.MONSTER_DECORATION, new AttachmentPoint[]{new AttachmentPoint(1, 1, 0, new MonsterPartType[]{MonsterPartType.MONSTER_LIMB})}, "decor", null, null));


        BufferedImage bodyImage = createBlankImageWithBorder(16, 16);
        BufferedImage limbImage = createBlankImageWithBorder(8, 4);
        BufferedImage decorImage = createBlankImageWithBorder(3, 3);
        decorImage.setRGB(1, 1, Color.RED.getRGB());

        testImageFactory.addImage("body", new FrankensteinBufferedImage(bodyImage));
        testImageFactory.addImage("limb", new FrankensteinBufferedImage(limbImage));
        testImageFactory.addImage("decor", new FrankensteinBufferedImage(decorImage));

        MonsterGenerator generator = new MonsterGenerator(testImageFactory, set);

        Monster m = generator.generateMonster(new MonsterGenerationParams(false, false, null));

        BufferedImage resultImage = ((FrankensteinBufferedImage)m.monsterImage).getImpl();

        ImageIO.write(resultImage, "png", new File("out/test.png"));
        Assert.assertEquals(16, resultImage.getWidth());
        Assert.assertEquals(16, resultImage.getHeight());

        Assert.assertEquals(Color.red.getRGB(), resultImage.getRGB(8, 8));
    }

}
