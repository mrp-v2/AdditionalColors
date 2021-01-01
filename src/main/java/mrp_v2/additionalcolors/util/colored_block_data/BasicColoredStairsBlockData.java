package mrp_v2.additionalcolors.util.colored_block_data;

import mrp_v2.additionalcolors.block.ColoredStairsBlock;
import mrp_v2.additionalcolors.datagen.BlockStateGenerator;
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

public class BasicColoredStairsBlockData extends AbstractColoredBlockData<ColoredStairsBlock>
{
    protected final AbstractColoredBlockData<?> baseBlockData;

    public BasicColoredStairsBlockData(Block baseBlock, AbstractColoredBlockData<?> baseBlockData)
    {
        this(baseBlock, Util.makeTagArray(), Util.makeTagArray(), baseBlockData);
    }

    public BasicColoredStairsBlockData(ResourceLocation baseBlockLoc, ITag.INamedTag<Block>[] blockTagsToAddTo,
            ITag.INamedTag<Item>[] itemTagsToAddTo, AbstractColoredBlockData<?> baseBlockData)
    {
        super(baseBlockLoc, addBlockTags(blockTagsToAddTo), addItemTags(itemTagsToAddTo));
        this.baseBlockData = baseBlockData;
    }

    protected static ITag.INamedTag<Block>[] addBlockTags(ITag.INamedTag<Block>[] blockTags)
    {
        return Util.combineTagArrays(blockTags, BlockTags.STAIRS);
    }

    protected static ITag.INamedTag<Item>[] addItemTags(ITag.INamedTag<Item>[] itemTags)
    {
        return Util.combineTagArrays(itemTags, ItemTags.STAIRS);
    }

    public BasicColoredStairsBlockData(Block baseBlock, ITag.INamedTag<Block>[] blockTagsToAddTo,
            ITag.INamedTag<Item>[] itemTagsToAddTo, AbstractColoredBlockData<?> baseBlockData)
    {
        super(baseBlock, addBlockTags(blockTagsToAddTo), addItemTags(itemTagsToAddTo));
        this.baseBlockData = baseBlockData;
    }

    @Override public void makeTextureGenerationPromises(TextureGenerator generator)
    {
    }

    @Override public void registerTextures(TextureGenerator generator, TextureProvider.FinishedTextureConsumer consumer)
    {
    }

    @Override public void registerBlockStatesAndModels(BlockStateGenerator generator)
    {
        ModelFile stairsModel = getStairsModel(generator);
        ModelFile innerStairsModel = getInnerStairsModel(generator);
        ModelFile outerStairsModel = getOuterStairsModel(generator);
        for (RegistryObject<ColoredStairsBlock> blockObject : getBlockObjects())
        {
            generator.stairsBlock(blockObject.get(), stairsModel, innerStairsModel, outerStairsModel);
        }
    }

    protected ModelFile getStairsModel(BlockStateGenerator generator)
    {
        return generator.models().withExistingParent(baseBlock.getId().getPath(), BlockStateGenerator.STAIRS_TINTED)
                .texture("bottom", getStairsModelBottomTexture(generator))
                .texture("top", getStairsModelTopTexture(generator))
                .texture("side", getStairsModelSideTexture(generator));
    }

    protected ResourceLocation getStairsModelBottomTexture(BlockStateGenerator generator)
    {
        return generator.modLoc("block/" + baseBlockData.getBaseBlockLoc().getPath());
    }

    protected ResourceLocation getStairsModelTopTexture(BlockStateGenerator generator)
    {
        return generator.modLoc("block/" + baseBlockData.getBaseBlockLoc().getPath());
    }

    protected ResourceLocation getStairsModelSideTexture(BlockStateGenerator generator)
    {
        return generator.modLoc("block/" + baseBlockData.getBaseBlockLoc().getPath());
    }

    protected ModelFile getInnerStairsModel(BlockStateGenerator generator)
    {
        return generator.models()
                .withExistingParent(baseBlock.getId().getPath() + "_inner", BlockStateGenerator.STAIRS_INNER_TINTED)
                .texture("bottom", getStairsModelBottomTexture(generator))
                .texture("top", getStairsModelTopTexture(generator))
                .texture("side", getStairsModelSideTexture(generator));
    }

    protected ModelFile getOuterStairsModel(BlockStateGenerator generator)
    {
        return generator.models()
                .withExistingParent(baseBlock.getId().getPath() + "_outer", BlockStateGenerator.STAIRS_OUTER_TINTED)
                .texture("bottom", getStairsModelBottomTexture(generator))
                .texture("top", getStairsModelTopTexture(generator))
                .texture("side", getStairsModelSideTexture(generator));
    }

    @Override protected ColoredStairsBlock makeNewBlock(DyeColor color)
    {
        return new ColoredStairsBlock(() -> baseBlockData.getBlockObject(color).get().getDefaultState(),
                getBlockProperties(), color);
    }
}
