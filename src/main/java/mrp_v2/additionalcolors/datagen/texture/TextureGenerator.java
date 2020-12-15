package mrp_v2.additionalcolors.datagen.texture;

import mrp_v2.additionalcolors.util.ObjectHolder;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.awt.image.BufferedImage;
import java.util.function.Consumer;

public class TextureGenerator extends TextureProvider
{
    public TextureGenerator(DataGenerator generator, ExistingFileHelper existingFileHelper, String modId)
    {
        super(generator, existingFileHelper, modId);
    }

    @Override protected void addTextures(Consumer<IFinishedTexture> consumer)
    {
        ObjectHolder.CRYING_OBSIDIAN_HANDLER.registerTextures(this, consumer);
    }

    public Result finish(ResourceLocation id, BufferedImage texture)
    {
        return new Result(id, texture);
    }

    public class Result implements IFinishedTexture
    {
        private final ResourceLocation id;
        private final BufferedImage texture;

        private Result(ResourceLocation id, BufferedImage texture)
        {
            this.id = id;
            this.texture = texture;
        }

        @Override public ResourceLocation getID()
        {
            return id;
        }

        @Override public BufferedImage getTexture()
        {
            return texture;
        }
    }
}
