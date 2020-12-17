package mrp_v2.additionalcolors.datagen;

import mrp_v2.additionalcolors.block.ColoredBlock;
import mrp_v2.additionalcolors.item.ColoredBlockItem;
import mrp_v2.additionalcolors.util.ColorizedBlockEntry;
import mrp_v2.additionalcolors.util.ObjectHolder;
import mrp_v2.mrplibrary.datagen.ItemTagsProvider;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.TagsProvider;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;
import org.apache.commons.lang3.tuple.Pair;

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
        ObjectHolder.CRYING_OBSIDIAN_HANDLER.registerItemTags(this);
        for (ColorizedBlockEntry entry : ObjectHolder.BLOCKS_TO_COLORIZE)
        {
            TagsProvider.Builder<Item> builder = this.getOrCreateBuilder(entry.getCraftingTag());
            builder.add(entry.getBlock().asItem());
            for (Pair<RegistryObject<ColoredBlock>, RegistryObject<ColoredBlockItem>> objPair : ObjectHolder.COLORIZED_BLOCK_MAP
                    .get(entry.getBlock()))
            {
                builder.add(objPair.getRight().get());
            }
            for (ITag.INamedTag<Item> additionalTag : entry.getAdditionalItemTags())
            {
                builder = this.getOrCreateBuilder(additionalTag);
                for (Pair<RegistryObject<ColoredBlock>, RegistryObject<ColoredBlockItem>> objPair : ObjectHolder.COLORIZED_BLOCK_MAP
                        .get(entry.getBlock()))
                {
                    builder.add(objPair.getRight().get());
                }
            }
        }
    }

    @Override public Builder<Item> getOrCreateBuilder(ITag.INamedTag<Item> tag)
    {
        return super.getOrCreateBuilder(tag);
    }
}
