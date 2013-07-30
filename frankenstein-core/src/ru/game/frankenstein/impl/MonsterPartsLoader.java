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

/**
 * Created with IntelliJ IDEA.
 * User: Egor.Smirnov
 * Date: 24.07.13
 * Time: 14:49
 */
package ru.game.frankenstein.impl;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import ru.game.frankenstein.FrankensteinException;
import ru.game.frankenstein.ImageFactory;
import ru.game.frankenstein.MonsterPart;
import ru.game.frankenstein.MonsterPartsSet;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Various methods for loading monster part collection descriptions
 */
public class MonsterPartsLoader
{

    public static class MonsterPartsSetJSONDescription
    {
        public final Map<String, Integer> defaultColors;

        public final String[] partFiles;

        public final String[] bloodImages;

        public final String shadowImage;

        public MonsterPartsSetJSONDescription(Map<String, Integer> defaultColors, String[] partFiles, String[] bloodImages, String shadowImage) {
            this.defaultColors = defaultColors;
            this.partFiles = partFiles;
            this.bloodImages = bloodImages;
            this.shadowImage = shadowImage;
        }
    }

    public static MonsterPartsSet loadFromJSON(ImageFactory imageFactory, InputStream json) throws FileNotFoundException {
        Gson gson = new Gson();
        MonsterPartsSetJSONDescription descr = gson.fromJson(new InputStreamReader(json), MonsterPartsSetJSONDescription.class);

        MonsterPartsSet result = new MonsterPartsSet();
        for (String filePath : descr.partFiles) {
            try {
                MonsterPart[] parts = gson.fromJson(new FileReader(filePath), MonsterPart[].class);
                result.addParts(parts);
            } catch (JsonSyntaxException ex) {
                System.err.println("Failed to parse json file " + filePath);
                throw ex;
            }
        }

        if (descr.defaultColors != null) {
            Map<Color, Integer> colorMap = new HashMap<Color, Integer>();
            for (Map.Entry<String, Integer> e : descr.defaultColors.entrySet()) {
                colorMap.put(Color.decode(e.getKey()), e.getValue());
            }
            result.setBaseColors(colorMap);
        }


        if (descr.bloodImages != null) {
            for (String s : descr.bloodImages) {
                try {
                    result.addBloodImage(imageFactory.loadImage(s));
                } catch (FrankensteinException e) {
                    System.err.println("Failed to load blood image from " + s);
                    e.printStackTrace();
                }
            }
        }

        if (descr.shadowImage != null) {
            try {
                result.setShadowImage(imageFactory.loadImage(descr.shadowImage));
            } catch (FrankensteinException e) {
                System.err.println("Failed to load shadow image from " + descr.shadowImage);
                e.printStackTrace();
            }
        }



        return result;
    }
}
