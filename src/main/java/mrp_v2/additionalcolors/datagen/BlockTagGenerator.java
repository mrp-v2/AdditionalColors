package mrp_v2.additionalcolors.datagen;

import mrp_v2.additionalcolors.util.ObjectHolder;
import mrp_v2.mrplibrary.datagen.providers.BlockTagsProvider;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.Tag;
import net.minecraftforge.client.model.generators.ExistingFileHelper;

import javax.annotation.Nullable;

public class BlockTagGenerator extends BlockTagsProvider
{
    public BlockTagGenerator(DataGenerator generatorIn, String modId, @Nullable ExistingFileHelper existingFileHelper)
    {
        super(generatorIn, modId, existingFileHelper);
    }

    @Override protected void registerTags()
    {
        ObjectHolder.COLORIZED_BLOCK_DATAS.forEach((data) -> data.registerBlockTags(this));
    }

    public Tag.Builder<Block> getOrCreateBuilder(Tag<Block> tag)
    {
        return super.getBuilder(tag);
    }
}
