/**
 * Created with IntelliJ IDEA.
 * User: Egor.Smirnov
 * Date: 24.07.13
 * Time: 13:42
 */
package ru.game.frankenstein.testapp;

import org.apache.commons.cli.*;
import ru.game.frankenstein.*;
import ru.game.frankenstein.impl.MonsterPartsLoader;
import ru.game.frankenstein.impl.imageio.BufferedImageFactory;
import ru.game.frankenstein.impl.imageio.FrankensteinBufferedImage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Test application that creates some monster images using test image set
 */
public class FrankensteinTestApp
{
    public static void main(String[] args)
    {
        PosixParser parser = new PosixParser();
        HelpFormatter formatter = new HelpFormatter();

        Options options = new Options().addOption("c", "count", true, "Number of monster images to generate. Defaults to 3")
                .addOption("o", "output", true, "Output dir")
                .addOption("i", "input", true, "Input json file with part library description");

        CommandLine commandLine;
        try {
            commandLine = parser.parse(options, args);
        } catch (ParseException e) {
            formatter.printHelp("FrankensteinTestApp", options);
            return;
        }

        if (!commandLine.hasOption('i') || !commandLine.hasOption('o')) {
            System.err.println("Both -o and -i must be specified");
            formatter.printHelp("FrankensteinTestApp", options);
            return;
        }

        String inputLibrary = commandLine.getOptionValue('i');
        String outputDirPath = commandLine.getOptionValue('o');
        int count = Integer.parseInt(commandLine.getOptionValue('c', "3"));

        MonsterPartsSet partsSet;
        try {
            partsSet = MonsterPartsLoader.loadFromJSON(new FileInputStream(inputLibrary));
        } catch (FileNotFoundException e) {
            System.err.println("Failed to load monster parts collection");
            e.printStackTrace();
            return;
        }

        MonsterGenerator generator = new MonsterGenerator(new BufferedImageFactory(), partsSet);

        File outputDir = new File(outputDirPath);
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        } else {
            if (!outputDir.isDirectory()) {
                System.err.println("Output path is not a directory");
                return;
            }
        }

        MonsterGenerationParams params = new MonsterGenerationParams(false, false, null);
        for (int i = 0; i < count; ++i)
        {
            Monster m;
            try {
                m = generator.generateMonster(params);
            } catch (FrankensteinException e) {
                System.err.println("Failed to generate monster image");
                e.printStackTrace();
                continue;
            }
            final File outFile = new File(outputDir, i + ".png");
            try {
                ImageIO.write((((FrankensteinBufferedImage)m.monsterImage).getImpl()), "png", outFile);
                System.out.println("Generated " + (i + 1) + " out of " + count);
            } catch (IOException e) {
                System.err.println("Failed to write output file " + outFile.getPath());
                e.printStackTrace();
            }
        }

    }
}
