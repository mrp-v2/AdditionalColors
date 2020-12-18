package mrp_v2.additionalcolors.datagen;

import mrp_v2.additionalcolors.util.ObjectHolder;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ItemModelGenerator extends ItemModelProvider
{
    public ItemModelGenerator(DataGenerator generator, String modid, ExistingFileHelper existingFileHelper)
    {
        super(generator, modid, existingFileHelper);
    }

    @Override protected void registerModels()
    {
        ObjectHolder.COLORIZED_BLOCK_DATAS.forEach((data) -> data.registerItemModels(this));
    }
}
