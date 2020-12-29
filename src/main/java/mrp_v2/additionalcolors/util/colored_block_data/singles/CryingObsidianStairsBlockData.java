package mrp_v2.additionalcolors.util.colored_block_data.singles;

import mrp_v2.additionalcolors.AdditionalColors;
import mrp_v2.additionalcolors.block.ColoredStairsBlock;
import mrp_v2.additionalcolors.datagen.BlockStateGenerator;
import mrp_v2.additionalcolors.util.ObjectHolder;
import mrp_v2.additionalcolors.util.colored_block_data.AbstractCryingObsidianBlockData;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;

public class CryingObsidianStairsBlockData extends AbstractCryingObsidianBlockData<ColoredStairsBlock>
{
    public CryingObsidianStairsBlockData(ResourceLocation baseBlockLoc, ITag.INamedTag<Block>[] blockTagsToAddTo,
            ITag.INamedTag<Item>[] itemTagsToAddTo)
    {
        super(baseBlockLoc, blockTagsToAddTo, itemTagsToAddTo);
    }

    @Override public void registerBlockStatesAndModels(BlockStateGenerator generator)
    {
        for (RegistryObject<ColoredStairsBlock> blockObject : blockObjectSet)
        {
            generator.stairsBlock(blockObject.get(), new ResourceLocation(AdditionalColors.ID,
                    "block/" + blockObject.getId().getPath().replace("_stairs", "")));
        }
    }

    @Override protected ColoredStairsBlock makeNewBlock(DyeColor color)
    {
        return new ColoredStairsBlock(() -> ObjectHolder.CRYING_OBSIDIAN_BLOCK_MAP.get(color).get().getDefaultState(),
                AbstractBlock.Properties.from(Blocks.CRYING_OBSIDIAN), color);
    }
}
