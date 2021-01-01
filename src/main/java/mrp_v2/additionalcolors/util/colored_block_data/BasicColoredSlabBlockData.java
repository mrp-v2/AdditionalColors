package mrp_v2.additionalcolors.util.colored_block_data;

import mrp_v2.additionalcolors.block.ColoredSlabBlock;
import mrp_v2.additionalcolors.datagen.BlockStateGenerator;
import mrp_v2.additionalcolors.datagen.LootTableGenerator;
import mrp_v2.additionalcolors.datagen.TextureGenerator;
import mrp_v2.additionalcolors.util.Util;
import mrp_v2.mrplibrary.datagen.providers.TextureProvider;
import net.minecraft.block.Block;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.fml.RegistryObject;

public class BasicColoredSlabBlockData extends AbstractColoredBlockData<ColoredSlabBlock>
{
    protected final IColoredBlockData<?> baseBlockData;

    public BasicColoredSlabBlockData(Block baseBlock, ITag.INamedTag<Block>[] blockTagsToAddTo,
            ITag.INamedTag<Item>[] itemTagsToAddTo, IColoredBlockData<?> baseBlockData)
    {
        super(baseBlock, addBlockTags(blockTagsToAddTo), addItemTags(itemTagsToAddTo));
        this.baseBlockData = baseBlockData;
    }

    protected static ITag.INamedTag<Block>[] addBlockTags(ITag.INamedTag<Block>[] blockTags)
    {
        return Util.combineTagArrays(blockTags, BlockTags.SLABS);
    }

    protected static ITag.INamedTag<Item>[] addItemTags(ITag.INamedTag<Item>[] itemTags)
    {
        return Util.combineTagArrays(itemTags, ItemTags.SLABS);
    }

    public BasicColoredSlabBlockData(Block baseBlock, IColoredBlockData<?> baseBlockData)
    {
        this(baseBlock, Util.makeTagArray(), Util.makeTagArray(), baseBlockData);
    }

    public BasicColoredSlabBlockData(ResourceLocation baseBlockLoc, ITag.INamedTag<Block>[] blockTagsToAddTo,
            ITag.INamedTag<Item>[] itemTagsToAddTo, IColoredBlockData<?> baseBlockData)
    {
        super(baseBlockLoc, addBlockTags(blockTagsToAddTo), addItemTags(itemTagsToAddTo));
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
        for (RegistryObject<ColoredSlabBlock> blockObject : getBlockObjects())
        {
            generator.addLootTable(blockObject.get(),
                    (block) -> generator.registerLootTable(block, LootTableGenerator::droppingSlab));
        }
    }

    @Override public void registerBlockStatesAndModels(BlockStateGenerator generator)
    {
        ModelFile bottomSlabModel = getBottomSlabModel(generator);
        ModelFile topSlabModel = getTopSlabModel(generator);
        ModelFile doubleSlabModel = getDoubleSlabModel(generator);
        for (RegistryObject<ColoredSlabBlock> blockObject : getBlockObjects())
        {
            generator.slabBlock(blockObject.get(), bottomSlabModel, topSlabModel, doubleSlabModel);
        }
    }

    protected ModelFile getBottomSlabModel(BlockStateGenerator generator)
    {
        return generator.models().withExistingParent(baseBlock.getId().getPath(), BlockStateGenerator.SLAB_TINTED)
                .texture("bottom", getSlabModelBottomTexture(generator))
                .texture("top", getSlabModelTopTexture(generator)).texture("side", getSlabModelSideTexture(generator));
    }

    protected ResourceLocation getSlabModelBottomTexture(BlockStateGenerator generator)
    {
        return generator.modLoc("block/" + baseBlockData.getBaseBlockLoc().getPath());
    }

    protected ResourceLocation getSlabModelTopTexture(BlockStateGenerator generator)
    {
        return generator.modLoc("block/" + baseBlockData.getBaseBlockLoc().getPath());
    }

    protected ResourceLocation getSlabModelSideTexture(BlockStateGenerator generator)
    {
        return generator.modLoc("block/" + baseBlockData.getBaseBlockLoc().getPath());
    }

    protected ModelFile getTopSlabModel(BlockStateGenerator generator)
    {
        return generator.models()
                .withExistingParent(baseBlock.getId().getPath() + "_top", BlockStateGenerator.SLAB_TOP_TINTED)
                .texture("bottom", getSlabModelBottomTexture(generator))
                .texture("top", getSlabModelTopTexture(generator)).texture("side", getSlabModelSideTexture(generator));
    }

    protected ModelFile getDoubleSlabModel(BlockStateGenerator generator)
    {
        return generator.models()
                .getExistingFile(generator.modLoc("block/" + baseBlockData.getBaseBlockLoc().getPath()));
    }

    @Override protected ColoredSlabBlock makeNewBlock(DyeColor color)
    {
        return new ColoredSlabBlock(getBlockProperties(), color);
    }
}
