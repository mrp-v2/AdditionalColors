package mrp_v2.additionalcolors.datagen.texture;

import com.google.common.base.Preconditions;
import com.google.common.hash.Hasher;
import mrp_v2.additionalcolors.AdditionalColors;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.resources.IResource;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;

public abstract class TextureProvider implements IDataProvider
{
    private static final Logger LOGGER = LogManager.getLogger();
    private final DataGenerator generator;
    private final ExistingFileHelper existingFileHelper;
    private final String modId;

    public TextureProvider(DataGenerator generator, ExistingFileHelper existingFileHelper, String modId)
    {
        this.generator = generator;
        this.existingFileHelper = existingFileHelper;
        this.modId = modId;
    }

    public static int color(int r, int g, int b)
    {
        Preconditions.checkArgument(r < 256 && r >= 0);
        Preconditions.checkArgument(g < 256 && g >= 0);
        Preconditions.checkArgument(b < 256 && b >= 0);
        return color(255, r, g, b);
    }

    public static int color(int a, int r, int g, int b)
    {
        Preconditions.checkArgument(a < 256 && a >= 0);
        Preconditions.checkArgument(r < 256 && r >= 0);
        Preconditions.checkArgument(g < 256 && g >= 0);
        Preconditions.checkArgument(b < 256 && b >= 0);
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    public static void adjustHSB(BufferedImage texture, int startX, int startY, int w, int h, float hueChange,
            float saturationChange, float brightnessChange)
    {
        for (int x = startX; x < startX + w; x++)
        {
            for (int y = startY; y < startY + h; y++)
            {
                Color color = new Color(texture.getRGB(x, y), true);
                float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
                hsb[0] += hueChange;
                hsb[1] += saturationChange;
                hsb[1] = (float) (hsb[1] - Math.floor(hsb[1]));
                hsb[2] += brightnessChange;
                hsb[2] = (float) (hsb[2] - Math.floor(hsb[2]));
                int rgb = Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
                int argb = (color.getAlpha() << 24) | rgb;
                texture.setRGB(x, y, argb);
            }
        }
    }

    public static int[] color(int color, int length)
    {
        int[] array = new int[length];
        for (int i = 0; i < length; i++)
        {
            array[i] = color;
        }
        return array;
    }

    @Override public void act(DirectoryCache cache)
    {
        Set<ResourceLocation> locationSet = new HashSet<>();
        addTextures((texture, location) ->
        {
            if (!locationSet.add(location))
            {
                throw new IllegalStateException("Duplicate texture " + location);
            } else
            {
                saveTexture(cache, texture, getTexturePath(location));
            }
        });
    }

    @Override public String getName()
    {
        return "Textures: " + modId;
    }

    protected abstract void addTextures(BiConsumer<BufferedImage, ResourceLocation> consumer);

    private void saveTexture(DirectoryCache cache, BufferedImage texture, Path path)
    {
        Hasher hasher = HASH_FUNCTION.newHasher();
        for (int i : texture.getRGB(0, 0, texture.getWidth(), texture.getHeight(), null, 0, texture.getWidth()))
        {
            hasher.putInt(i);
        }
        String hash = hasher.hash().toString();
        if (!Objects.equals(cache.getPreviousHash(path), hash) || !Files.exists(path))
        {
            try
            {
                Files.createDirectories(path.getParent());
            } catch (IOException ioException)
            {
                LOGGER.error("Couldn't create directory for texture {}", path, ioException);
            }
            try
            {
                ImageIO.write(texture, "png", path.toFile());
            } catch (IOException ioException)
            {
                LOGGER.error("Couldn't save texture {}", path, ioException);
            }
        }
        cache.recordHash(path, hash);
    }

    private Path getTexturePath(ResourceLocation texture)
    {
        return getTexturePath(texture, this.generator.getOutputFolder());
    }

    private Path getTexturePath(ResourceLocation texture, Path folder)
    {
        return folder.resolve("assets/" + texture.getNamespace() + "/textures/" + texture.getPath() + ".png");
    }

    public void finish(BufferedImage texture, ResourceLocation id, BiConsumer<BufferedImage, ResourceLocation> consumer)
    {
        consumer.accept(texture, id);
    }

    @Nullable public BufferedImage getTexture(ResourceLocation textureLoc)
    {
        ResourceLocation loc = new ResourceLocation(AdditionalColors.ID, "textures/" + textureLoc.getPath() + ".png");
        Preconditions.checkArgument(existingFileHelper.exists(loc, ResourcePackType.CLIENT_RESOURCES),
                "Texture %s does not exist in any known resource pack", loc);
        try
        {
            IResource resource = existingFileHelper.getResource(loc, ResourcePackType.CLIENT_RESOURCES);
            return ImageIO.read(resource.getInputStream());
        } catch (IOException ioException)
        {
            LOGGER.error("Couldn't read texture {}", textureLoc, ioException);
        }
        return null;
    }
}
