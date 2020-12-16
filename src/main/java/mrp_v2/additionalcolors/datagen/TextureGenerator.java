package mrp_v2.additionalcolors.datagen;

import mrp_v2.additionalcolors.util.ObjectHolder;
import mrp_v2.mrplibrary.datagen.TextureProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.awt.image.BufferedImage;
import java.util.function.BiConsumer;

public class TextureGenerator extends TextureProvider
{
    public TextureGenerator(DataGenerator generator, ExistingFileHelper existingFileHelper, String modId)
    {
        super(generator, existingFileHelper, modId);
    }

    @Override protected void addTextures(BiConsumer<BufferedImage, ResourceLocation> consumer)
    {
        ObjectHolder.CRYING_OBSIDIAN_HANDLER.registerTextures(this, consumer);
    }
}
