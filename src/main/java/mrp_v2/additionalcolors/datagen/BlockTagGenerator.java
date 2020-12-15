package mrp_v2.additionalcolors.datagen;

import mrp_v2.additionalcolors.util.ObjectHolder;
import mrp_v2.mrplibrary.datagen.BlockTagsProvider;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.ITag;
import net.minecraftforge.common.data.ExistingFileHelper;

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
    }

    @Override public Builder<Block> getOrCreateBuilder(ITag.INamedTag<Block> tag)
    {
        return super.getOrCreateBuilder(tag);
    }
}
