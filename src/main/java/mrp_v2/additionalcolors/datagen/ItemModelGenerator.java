package mrp_v2.additionalcolors.datagen;

import mrp_v2.additionalcolors.util.ObjectHolder;
import mrp_v2.mrplibrary.datagen.providers.ItemModelProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ItemModelGenerator extends ItemModelProvider
{
    public ItemModelGenerator(DataGenerator generator, String modid, ExistingFileHelper existingFileHelper)
    {
        super(generator, modid, existingFileHelper);
    }

    @Override protected void registerModels()
    {
        String path = ObjectHolder.COLORED_CRAFTING_TABLE.getId().getPath();
        this.withExistingParent(path, modLoc("block/" + path));
    }
}
