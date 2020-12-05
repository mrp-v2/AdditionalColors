package mrp_v2.additionalcolors.particle.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class Color3B
{
    public static final Codec<Color3B> CODEC = RecordCodecBuilder.create(builder -> builder
            .group(Codec.BYTE.fieldOf("red").forGetter(Color3B::getRed),
                    Codec.BYTE.fieldOf("green").forGetter(Color3B::getGreen),
                    Codec.BYTE.fieldOf("blue").forGetter(Color3B::getBlue)).apply(builder, Color3B::new));
    protected final byte red, green, blue;

    public Color3B(byte red, byte green, byte blue)
    {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public byte getRed()
    {
        return this.red;
    }

    public byte getGreen()
    {
        return this.green;
    }

    public byte getBlue()
    {
        return this.blue;
    }
}
