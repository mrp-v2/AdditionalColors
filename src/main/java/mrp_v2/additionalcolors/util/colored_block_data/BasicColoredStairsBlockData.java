package mrp_v2.additionalcolors.util.colored_block_data;

import mrp_v2.additionalcolors.block.ColoredStairsBlock;
import mrp_v2.additionalcolors.datagen.BlockStateGenerator;
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

public class BasicColoredStairsBlockData extends AbstractColoredBlockData<ColoredStairsBlock>
{
    protected final IColoredBlockData<?> baseBlockData;

    public BasicColoredStairsBlockData(ResourceLocation baseBlockLoc, ITag.INamedTag<Block>[] blockTagsToAddTo,
            ITag.INamedTag<Item>[] itemTagsToAddTo, IColoredBlockData<?> baseBlockData)
    {
        super(baseBlockLoc, blockTagsToAddTo, itemTagsToAddTo);
        this.baseBlockData = baseBlockData;
    }

    public BasicColoredStairsBlockData(Block baseBlock, ITag.INamedTag<Block>[] blockTagsToAddTo,
            ITag.INamedTag<Item>[] itemTagsToAddTo, IColoredBlockData<?> baseBlockData)
    {
        super(baseBlock, blockTagsToAddTo, itemTagsToAddTo);
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
        Triple<ModelFile, ModelFile, ModelFile> stairModels = generator
                .tintedSimpleStairsBlock(baseBlock.getId().getPath(),
                        generator.modLoc("block/" + baseBlockData.getBaseBlockLoc().getPath()));
        for (RegistryObject<ColoredStairsBlock> blockObject : blockObjectMap.values())
        {
            generator.stairsBlock(blockObject.get(), stairModels.getLeft(), stairModels.getMiddle(),
                    stairModels.getRight());
        }
    }

    @Override protected ColoredStairsBlock makeNewBlock(DyeColor color)
    {
        return new ColoredStairsBlock(() -> baseBlockData.getBlockObject(color).get().getDefaultState(),
                getBlockProperties(), color);
    }
}
