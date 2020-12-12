package mrp_v2.additionalcolors.datagen;

import mrp_v2.additionalcolors.block.ColoredBlock;
import mrp_v2.additionalcolors.item.ColoredBlockItem;
import mrp_v2.additionalcolors.util.ObjectHolder;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;
import org.apache.commons.lang3.tuple.Pair;

public class ItemModelGenerator extends ItemModelProvider
{
    public ItemModelGenerator(DataGenerator generator, String modid, ExistingFileHelper existingFileHelper)
    {
        super(generator, modid, existingFileHelper);
    }

    @Override protected void registerModels()
    {
        ObjectHolder.CRYING_OBSIDIAN_HANDLER.registerItemModels(this);
        for (Block block : ObjectHolder.COLORIZED_BLOCK_MAP.keySet())
        {
            String path = block.getRegistryName().getPath();
            for (Pair<RegistryObject<ColoredBlock>, RegistryObject<ColoredBlockItem>> blockObj : ObjectHolder.COLORIZED_BLOCK_MAP
                    .get(block))
            {
                this.withExistingParent(blockObj.getLeft().getId().getPath(), modLoc("block/" + path));
            }
        }
    }
}
