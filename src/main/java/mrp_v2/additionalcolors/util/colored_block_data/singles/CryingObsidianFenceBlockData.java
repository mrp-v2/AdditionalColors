package mrp_v2.additionalcolors.util.colored_block_data.singles;

import mrp_v2.additionalcolors.AdditionalColors;
import mrp_v2.additionalcolors.block.ColoredFenceBlock;
import mrp_v2.additionalcolors.datagen.BlockStateGenerator;
import mrp_v2.additionalcolors.datagen.ItemModelGenerator;
import mrp_v2.additionalcolors.util.colored_block_data.AbstractCryingObsidianBlockData;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;

public class CryingObsidianFenceBlockData extends AbstractCryingObsidianBlockData<ColoredFenceBlock>
{
    public CryingObsidianFenceBlockData(ResourceLocation baseBlockLoc, ITag.INamedTag<Block>[] blockTagsToAddTo,
            ITag.INamedTag<Item>[] itemTagsToAddTo)
    {
        super(baseBlockLoc, blockTagsToAddTo, itemTagsToAddTo);
    }

    @Override public void registerItemModels(ItemModelGenerator generator)
    {
        for (RegistryObject<ColoredFenceBlock> blockObject : blockObjectSet)
        {
            generator.fenceInventory(blockObject.getId().getPath(), new ResourceLocation(AdditionalColors.ID,
                    "block/" + blockObject.getId().getPath().replace("_fence", "")));
        }
    }

    @Override public void registerBlockStatesAndModels(BlockStateGenerator generator)
    {
        for (RegistryObject<ColoredFenceBlock> blockObject : blockObjectSet)
        {
            generator.fenceBlock(blockObject.get(), new ResourceLocation(AdditionalColors.ID,
                    "block/" + blockObject.getId().getPath().replace("_fence", "")));
        }
    }

    @Override protected ColoredFenceBlock makeNewBlock(DyeColor color)
    {
        return new ColoredFenceBlock(AbstractBlock.Properties.from(Blocks.CRYING_OBSIDIAN), color);
    }
}
