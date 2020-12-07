package mrp_v2.additionalcolors.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mrp_v2.additionalcolors.particle.util.Color3B;
import mrp_v2.additionalcolors.util.ObjectHolder;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;

import java.util.Locale;
import java.util.function.Function;

public abstract class ColorParticleData implements IParticleData
{
    protected final Color3B color;

    public ColorParticleData(Color3B color)
    {
        this.color = color;
    }

    public ColorParticleData(byte red, byte green, byte blue)
    {
        this.color = new Color3B(red, green, blue);
    }

    public static <T extends ColorParticleData> ParticleType<T> createParticleType(
            Function<Color3B, T> colorConstructor)
    {
        return new ParticleType<T>(false, makeDeserializer(colorConstructor))
        {
            private final Codec<T> codec = makeCodec(colorConstructor);

            @Override public Codec<T> func_230522_e_()
            {
                return this.codec;
            }
        };
    }

    protected static <T extends ColorParticleData> Codec<T> makeCodec(Function<Color3B, T> constructor)
    {
        return RecordCodecBuilder
                .create(builder -> builder.group(Color3B.CODEC.fieldOf("color").forGetter(ColorParticleData::getColor))
                        .apply(builder, constructor));
    }

    public Color3B getColor()
    {
        return this.color;
    }

    protected static <T extends ColorParticleData> IDeserializer<T> makeDeserializer(
            Function<Color3B, T> colorConstructor)
    {
        return new IDeserializer<T>()
        {
            @Override public T deserialize(ParticleType<T> particleTypeIn, StringReader reader)
                    throws CommandSyntaxException
            {
                reader.expect(' ');
                int color = reader.readInt() & 0xFFFFFF;
                return colorConstructor.apply(new Color3B((byte) ((color >> 16) & 0xFF), (byte) ((color >> 8) & 0xFF),
                        (byte) (color & 0xFF)));
            }

            @Override public T read(ParticleType<T> particleTypeIn, PacketBuffer buffer)
            {
                return colorConstructor.apply(new Color3B(buffer.readByte(), buffer.readByte(), buffer.readByte()));
            }
        };
    }

    @Override public void write(PacketBuffer buffer)
    {
        buffer.writeByte(this.color.getRed());
        buffer.writeByte(this.color.getGreen());
        buffer.writeByte(this.color.getBlue());
    }

    @Override public String getParameters()
    {
        return String.format(Locale.ROOT, "%s, %d", getType().getRegistryName().getPath(), this.colorAsInt());
    }

    private int colorAsInt()
    {
        return (this.color.getRed() << 16) | (this.color.getGreen() << 8) | this.color.getBlue();
    }

    public float getRedF()
    {
        return this.color.getRed() / 255F;
    }

    public float getGreenF()
    {
        return this.color.getGreen() / 255F;
    }

    public float getBlueF()
    {
        return this.color.getBlue() / 255F;
    }

    public static class DrippingObsidianTear extends ColorParticleData
    {
        public DrippingObsidianTear(Color3B color)
        {
            super(color);
        }

        @Override public ParticleType<?> getType()
        {
            return ObjectHolder.COLORED_DRIPPING_OBSIDIAN_TEAR_PARTICLE_TYPE.get();
        }
    }

    public static class FallingObsidianTear extends ColorParticleData
    {
        public FallingObsidianTear(Color3B color)
        {
            super(color);
        }

        @Override public ParticleType<?> getType()
        {
            return ObjectHolder.COLORED_FALLING_OBSIDIAN_TEAR_PARTICLE_TYPE.get();
        }
    }

    public static class LandingObsidianTear extends ColorParticleData
    {
        public LandingObsidianTear(Color3B color)
        {
            super(color);
        }

        @Override public ParticleType<?> getType()
        {
            return ObjectHolder.COLORED_LANDING_OBSIDIAN_TEAR_PARTICLE_TYPE.get();
        }
    }
}
