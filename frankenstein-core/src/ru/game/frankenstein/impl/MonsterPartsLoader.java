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
import ru.game.frankenstein.*;

import java.io.*;
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

        public final String[] shadowImages;

        public MonsterPartsSetJSONDescription(Map<String, Integer> defaultColors, String[] partFiles, String[] bloodImages, String[] shadowImages) {
            this.defaultColors = defaultColors;
            this.partFiles = partFiles;
            this.bloodImages = bloodImages;
            this.shadowImages = shadowImages;
        }
    }

    public static MonsterPartsSet loadFromJSON(ImageFactory imageFactory, File json) throws FrankensteinException {
        Gson gson = new Gson();
        final File root = json.getParentFile();
        MonsterPartsSetJSONDescription descr = null;
        try {
            descr = gson.fromJson(new FileReader(json), MonsterPartsSetJSONDescription.class);
        } catch (FileNotFoundException e) {
            throw new FrankensteinException("Failed to find parts library file", e);
        }

        MonsterPartsSet result = new MonsterPartsSet();
        for (String filePath : descr.partFiles) {
            try {
                MonsterPart[] parts = gson.fromJson(new FileReader(new File(root, filePath)), MonsterPart[].class);
                result.addParts(parts);
            } catch (Exception ex) {
                System.err.println("Failed to parse json file " + filePath);
                throw new FrankensteinException("Exception while parsing " + filePath, ex);
            }
        }

        if (descr.defaultColors != null) {
            Map<FrankensteinColor, Integer> colorMap = new HashMap<FrankensteinColor, Integer>();
            for (Map.Entry<String, Integer> e : descr.defaultColors.entrySet()) {
                colorMap.put(imageFactory.decodeColor(e.getKey()), e.getValue());
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

        if (descr.shadowImages != null) {
            for (String s : descr.shadowImages) {
                try {
                    result.addShadowImage(imageFactory.loadImage(s));
                } catch (FrankensteinException e) {
                    System.err.println("Failed to load shadow image from " + s);
                    e.printStackTrace();
                }
            }
        }



        return result;
    }
}
