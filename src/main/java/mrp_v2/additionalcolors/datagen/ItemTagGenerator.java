package mrp_v2.additionalcolors.datagen;

import mrp_v2.additionalcolors.item.ColoredBlockItem;
import mrp_v2.additionalcolors.util.ColorizedBlockEntry;
import mrp_v2.additionalcolors.util.ObjectHolder;
import mrp_v2.mrplibrary.datagen.ItemTagsProvider;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.TagsProvider;
import net.minecraft.item.Item;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;

import javax.annotation.Nullable;

public class ItemTagGenerator extends ItemTagsProvider
{
    protected ItemTagGenerator(DataGenerator dataGenerator, BlockTagsProvider blockTagProvider, String modId,
            @Nullable ExistingFileHelper existingFileHelper)
    {
        super(dataGenerator, blockTagProvider, modId, existingFileHelper);
    }

    @Override protected void registerTags()
    {
        TagsProvider.Builder<Item> cryingObsidianBuilder = this.getOrCreateBuilder(ObjectHolder.CRYING_OBSIDIAN_TAG);
        for (RegistryObject<ColoredBlockItem> itemObj : ObjectHolder.COLORED_CRYING_OBSIDIAN_ITEMS.values())
        {
            cryingObsidianBuilder.add(itemObj.get());
        }
        for (ColorizedBlockEntry entry : ObjectHolder.BLOCKS_TO_COLORIZE)
        {
            TagsProvider.Builder<Item> builder = this.getOrCreateBuilder(entry.getTag());
            for (RegistryObject<ColoredBlockItem> itemObj : ObjectHolder.COLORIZED_BLOCK_ITEM_MAP.get(entry.getBlock()))
            {
                builder.add(itemObj.get());
            }
        }
    }
}
