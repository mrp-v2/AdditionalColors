package mrp_v2.additionalcolors.datagen.texture;

import mrp_v2.additionalcolors.util.ObjectHolder;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

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
}
