/**
 * Created with IntelliJ IDEA.
 * User: Egor.Smirnov
 * Date: 24.07.13
 * Time: 14:49
 */
package ru.game.frankenstein.impl;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import ru.game.frankenstein.MonsterPart;
import ru.game.frankenstein.MonsterPartsSet;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * Various methods for loading monster part collection descriptions
 */
public class MonsterPartsLoader
{

    public static class MonsterPartsSetJSONDescription
    {
        public final Map<Integer, String> defaultColors;

        public final String[] partFiles;

        public final String[] bloodImages;

        public final String[] shadowImages;

        public MonsterPartsSetJSONDescription(Map<Integer, String> defaultColors, String[] partFiles, String[] bloodImages, String[] shadowImages) {
            this.defaultColors = defaultColors;
            this.partFiles = partFiles;
            this.bloodImages = bloodImages;
            this.shadowImages = shadowImages;
        }
    }

    public static MonsterPartsSet loadFromJSON(InputStream json) throws FileNotFoundException {
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

        return result;
    }
}
