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
