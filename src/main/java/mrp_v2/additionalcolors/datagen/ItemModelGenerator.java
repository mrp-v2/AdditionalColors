package mrp_v2.additionalcolors.datagen;

import mrp_v2.additionalcolors.block.ColoredCryingObsidianBlock;
import mrp_v2.additionalcolors.util.ObjectHolder;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;

public class ItemModelGenerator extends ItemModelProvider
{
    public ItemModelGenerator(DataGenerator generator, String modid, ExistingFileHelper existingFileHelper)
    {
        super(generator, modid, existingFileHelper);
    }

    @Override protected void registerModels()
    {
        for (RegistryObject<ColoredCryingObsidianBlock> blockObj : ObjectHolder.COLORED_CRYING_OBSIDIAN_BLOCKS.values())
        {
            String path = blockObj.get().getRegistryName().getPath();
            this.withExistingParent(path, modLoc("block/" + path));
        }
    }
}
