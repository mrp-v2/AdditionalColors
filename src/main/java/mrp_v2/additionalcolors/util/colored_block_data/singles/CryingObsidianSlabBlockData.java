package mrp_v2.additionalcolors.util.colored_block_data.singles;

import mrp_v2.additionalcolors.AdditionalColors;
import mrp_v2.additionalcolors.block.ColoredSlabBlock;
import mrp_v2.additionalcolors.datagen.BlockStateGenerator;
import mrp_v2.additionalcolors.datagen.ItemModelGenerator;
import mrp_v2.additionalcolors.util.ObjectHolder;
import mrp_v2.additionalcolors.util.colored_block_data.BasicColoredSlabBlockData;
import mrp_v2.additionalcolors.util.colored_block_data.IColoredBlockData;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;

public class CryingObsidianSlabBlockData extends BasicColoredSlabBlockData
{
    public CryingObsidianSlabBlockData(ResourceLocation baseBlockLoc, ITag.INamedTag<Block>[] blockTagsToAddTo,
            ITag.INamedTag<Item>[] itemTagsToAddTo, IColoredBlockData<?> baseBlockData)
    {
        super(baseBlockLoc, blockTagsToAddTo, itemTagsToAddTo, baseBlockData);
    }

    @Override public boolean requiresTinting()
    {
        return false;
    }

    @Override public void registerItemModels(ItemModelGenerator generator)
    {
        for (RegistryObject<ColoredSlabBlock> blockObject : blockObjectMap.values())
        {
            String path = blockObject.getId().getPath();
            generator.withExistingParent(path, generator.modLoc("block/" + path));
        }
    }

    @Override public DyeColor[] getColors()
    {
        return ObjectHolder.CRYING_OBSIDIAN_COLORS;
    }

    @Override public ItemGroup getItemGroup()
    {
        return ObjectHolder.OBSIDIAN_EXPANSION_ITEM_GROUP;
    }

    @Override protected AbstractBlock.Properties getBlockProperties()
    {
        return AbstractBlock.Properties.from(Blocks.CRYING_OBSIDIAN);
    }

    @Override public void registerBlockStatesAndModels(BlockStateGenerator generator)
    {
        for (RegistryObject<ColoredSlabBlock> blockObject : blockObjectMap.values())
        {
            ResourceLocation blockLoc = new ResourceLocation(AdditionalColors.ID,
                    "block/" + blockObject.getId().getPath().replace("_slab", ""));
            generator.slabBlock(blockObject.get(), blockLoc, blockLoc);
        }
    }
}
