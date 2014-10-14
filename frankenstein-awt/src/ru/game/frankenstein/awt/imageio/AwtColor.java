package ru.game.frankenstein.awt.imageio;

import ru.game.frankenstein.FrankensteinColor;

import java.awt.*;

public class AwtColor implements FrankensteinColor
{
    private final Color color;

    public AwtColor(Color color) {
        this.color = color;
    }

    @Override
    public int getR() {
        return color.getRed();
    }

    @Override
    public int getG() {
        return color.getGreen();
    }

    @Override
    public int getB() {
        return color.getBlue();
    }

    @Override
    public int getAlpha() {
        return color.getAlpha();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AwtColor awtColor = (AwtColor) o;

        return color.equals(awtColor.color);

    }

    @Override
    public int hashCode() {
        return color.hashCode();
    }
}
