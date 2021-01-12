package mrp_v2.additionalcolors.api.datagen;

import mrp_v2.mrplibrary.datagen.providers.ItemModelProvider;
import mrp_v2.mrplibrary.util.IModLocProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

public abstract class ItemModelGenerator extends ItemModelProvider implements IModLocProvider
{
    public ItemModelGenerator(DataGenerator generator, String modid, ExistingFileHelper existingFileHelper)
    {
        super(generator, modid, existingFileHelper);
    }
}
