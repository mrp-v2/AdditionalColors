package mrp_v2.additionalcolors.util.colored_block_data.singles;

import mrp_v2.additionalcolors.AdditionalColors;
import mrp_v2.additionalcolors.block.ColoredSlabBlock;
import mrp_v2.additionalcolors.datagen.BlockStateGenerator;
import mrp_v2.additionalcolors.datagen.LootTableGenerator;
import mrp_v2.additionalcolors.util.colored_block_data.AbstractCryingObsidianBlockData;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;

public class CryingObsidianSlabBlockData extends AbstractCryingObsidianBlockData<ColoredSlabBlock>
{
    public CryingObsidianSlabBlockData(ResourceLocation baseBlockLoc, ITag.INamedTag<Block>[] blockTagsToAddTo,
            ITag.INamedTag<Item>[] itemTagsToAddTo)
    {
        super(baseBlockLoc, blockTagsToAddTo, itemTagsToAddTo);
    }

    @Override public void registerLootTables(LootTableGenerator generator)
    {
        for (RegistryObject<ColoredSlabBlock> blockObject : blockObjectSet)
        {
            generator.addLootTable(blockObject.get(),
                    (block) -> generator.registerLootTable(block, LootTableGenerator::droppingSlab));
        }
    }

    @Override public void registerBlockStatesAndModels(BlockStateGenerator generator)
    {
        for (RegistryObject<ColoredSlabBlock> blockObject : blockObjectSet)
        {
            ResourceLocation blockLoc = new ResourceLocation(AdditionalColors.ID,
                    "block/" + blockObject.getId().getPath().replace("_slab", ""));
            generator.slabBlock(blockObject.get(), blockLoc, blockLoc);
        }
    }

    @Override protected ColoredSlabBlock makeNewBlock(DyeColor color)
    {
        return new ColoredSlabBlock(AbstractBlock.Properties.from(Blocks.CRYING_OBSIDIAN), color);
    }
}
