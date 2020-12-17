package mrp_v2.additionalcolors.datagen;

import mrp_v2.additionalcolors.block.ColoredBlock;
import mrp_v2.additionalcolors.item.ColoredBlockItem;
import mrp_v2.additionalcolors.util.ColorizedBlockEntry;
import mrp_v2.additionalcolors.util.ObjectHolder;
import mrp_v2.mrplibrary.datagen.BlockTagsProvider;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.TagsProvider;
import net.minecraft.tags.ITag;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;

public class BlockTagGenerator extends BlockTagsProvider
{
    public BlockTagGenerator(DataGenerator generatorIn, String modId, @Nullable ExistingFileHelper existingFileHelper)
    {
        super(generatorIn, modId, existingFileHelper);
    }

    @Override protected void registerTags()
    {
        ObjectHolder.CRYING_OBSIDIAN_HANDLER.registerBlockTags(this);
        for (ColorizedBlockEntry entry : ObjectHolder.BLOCKS_TO_COLORIZE)
        {
            for (ITag.INamedTag<Block> additionalTag : entry.getAdditionalBlockTags())
            {
                TagsProvider.Builder<Block> builder = this.getOrCreateBuilder(additionalTag);
                for (Pair<RegistryObject<ColoredBlock>, RegistryObject<ColoredBlockItem>> objPair : ObjectHolder.COLORIZED_BLOCK_MAP
                        .get(entry.getBlock()))
                {
                    builder.add(objPair.getLeft().get());
                }
            }
        }
    }

    @Override public Builder<Block> getOrCreateBuilder(ITag.INamedTag<Block> tag)
    {
        return super.getOrCreateBuilder(tag);
    }
}
