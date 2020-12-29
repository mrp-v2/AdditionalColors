package mrp_v2.additionalcolors.util.colored_block_data;

import mrp_v2.additionalcolors.block.ColoredSlabBlock;
import mrp_v2.additionalcolors.datagen.BlockStateGenerator;
import mrp_v2.additionalcolors.datagen.LootTableGenerator;
import mrp_v2.additionalcolors.datagen.TextureGenerator;
import mrp_v2.mrplibrary.datagen.providers.TextureProvider;
import net.minecraft.block.Block;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.fml.RegistryObject;
import org.apache.commons.lang3.tuple.Triple;

public class BasicColoredSlabBlockData extends AbstractColoredBlockData<ColoredSlabBlock>
{
    protected final IColoredBlockData<?> baseBlockData;

    public BasicColoredSlabBlockData(Block baseBlock, ITag.INamedTag<Block>[] additionalBlockTags,
            ITag.INamedTag<Item>[] additionalItemTags, IColoredBlockData<?> baseBlockData)
    {
        super(baseBlock, additionalBlockTags, additionalItemTags);
        this.baseBlockData = baseBlockData;
    }

    public BasicColoredSlabBlockData(ResourceLocation baseBlockLoc, ITag.INamedTag<Block>[] blockTagsToAddTo,
            ITag.INamedTag<Item>[] itemTagsToAddTo, IColoredBlockData<?> baseBlockData)
    {
        super(baseBlockLoc, blockTagsToAddTo, itemTagsToAddTo);
        this.baseBlockData = baseBlockData;
    }

    @Override public void makeTextureGenerationPromises(TextureGenerator generator)
    {
    }

    @Override public void registerTextures(TextureGenerator generator, TextureProvider.FinishedTextureConsumer consumer)
    {
    }

    @Override public void registerLootTables(LootTableGenerator generator)
    {
        for (RegistryObject<ColoredSlabBlock> blockObject : blockObjectMap.values())
        {
            generator.addLootTable(blockObject.get(),
                    (block) -> generator.registerLootTable(block, LootTableGenerator::droppingSlab));
        }
    }

    @Override public void registerBlockStatesAndModels(BlockStateGenerator generator)
    {
        Triple<ModelFile, ModelFile, ModelFile> slabModels = generator
                .tintedSimpleSlabBlock(baseBlock.getId().getPath(),
                        generator.modLoc("block/" + baseBlockData.getBaseBlockLoc().getPath()),
                        baseBlockData.getBaseBlockLoc().getPath());
        for (RegistryObject<ColoredSlabBlock> blockObject : blockObjectMap.values())
        {
            generator.slabBlock(blockObject.get(), slabModels.getLeft(), slabModels.getMiddle(), slabModels.getRight());
        }
    }

    @Override protected ColoredSlabBlock makeNewBlock(DyeColor color)
    {
        return new ColoredSlabBlock(getBlockProperties(), color);
    }
}
