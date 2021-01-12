package mrp_v2.additionalcolors.api.datagen;

import mrp_v2.mrplibrary.datagen.providers.BlockTagsProvider;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.ITag;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public abstract class BlockTagGenerator extends BlockTagsProvider
{
    public BlockTagGenerator(DataGenerator generatorIn, String modId, @Nullable ExistingFileHelper existingFileHelper)
    {
        super(generatorIn, modId, existingFileHelper);
    }

    @Override public Builder<Block> getOrCreateBuilder(ITag.INamedTag<Block> tag)
    {
        return super.getOrCreateBuilder(tag);
    }
}
