package mrp_v2.additionalcolors.api.colored_block_data;

import mrp_v2.additionalcolors.api.datagen.BlockStateGenerator;
import mrp_v2.additionalcolors.block.ColoredFenceGateBlock;
import mrp_v2.mrplibrary.datagen.providers.BlockStateProvider;
import mrp_v2.mrplibrary.datagen.providers.TextureProvider;
import net.minecraft.block.Block;
import net.minecraft.item.DyeColor;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fml.RegistryObject;

public class ColoredFenceGateBlockData extends AbstractColoredBlockData<ColoredFenceGateBlock>
{
    protected final AbstractColoredBlockData<?> baseBlockData;

    public ColoredFenceGateBlockData(RegistryObject<? extends Block> baseBlock,
            AbstractColoredBlockData<?> baseBlockData)
    {
        super(baseBlock, baseBlockData);
        this.baseBlockData = baseBlockData;
        addBlockTags(BlockTags.FENCE_GATES, Tags.Blocks.FENCE_GATES);
        addItemTags(Tags.Items.FENCE_GATES);
    }

    @Override public void makeTextureGenerationPromises(TextureProvider generator)
    {
    }

    @Override protected ColoredFenceGateBlock makeNewBlock(DyeColor color)
    {
        return new ColoredFenceGateBlock(getBlockProperties(color), color);
    }

    @Override public void registerTextures(TextureProvider generator, TextureProvider.FinishedTextureConsumer consumer)
    {
    }

    @Override public void registerBlockStatesAndModels(BlockStateGenerator generator)
    {
        BlockStateProvider.FenceGateBlockModels models =
                generator.fenceGateBlockTinted(baseBlock.getId().getPath(), baseBlockData.getSideTextureLoc(false));
        for (RegistryObject<ColoredFenceGateBlock> blockObject : blockObjectMap.values())
        {
            generator.fenceGateBlock(blockObject.get(), models.getGate(), models.getGateOpen(), models.getGateWall(),
                    models.getGateWallOpen());
        }
    }
}
