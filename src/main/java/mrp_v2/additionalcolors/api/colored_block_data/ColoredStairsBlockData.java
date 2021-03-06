package mrp_v2.additionalcolors.api.colored_block_data;

import mrp_v2.additionalcolors.api.datagen.BlockStateGenerator;
import mrp_v2.additionalcolors.block.ColoredStairsBlock;
import mrp_v2.mrplibrary.datagen.providers.BlockStateProvider;
import mrp_v2.mrplibrary.datagen.providers.TextureProvider;
import net.minecraft.block.Block;
import net.minecraft.item.DyeColor;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.fml.RegistryObject;

public class ColoredStairsBlockData extends AbstractColoredBlockData<ColoredStairsBlock>
{
    protected final AbstractColoredBlockData<?> baseBlockData;

    public ColoredStairsBlockData(RegistryObject<? extends Block> baseBlock, AbstractColoredBlockData<?> baseBlockData)
    {
        super(baseBlock, baseBlockData);
        this.baseBlockData = baseBlockData;
        addBlockTags(BlockTags.STAIRS);
        addItemTags(ItemTags.STAIRS);
    }

    @Override public void makeTextureGenerationPromises(TextureProvider generator)
    {
    }

    @Override protected ColoredStairsBlock makeNewBlock(DyeColor color)
    {
        return new ColoredStairsBlock(() -> baseBlockData.getBlockObject(color).get().getDefaultState(),
                getBlockProperties(color), color);
    }

    @Override public void registerTextures(TextureProvider generator, TextureProvider.FinishedTextureConsumer consumer)
    {
    }

    @Override public void registerBlockStatesAndModels(BlockStateGenerator generator)
    {
        BlockStateProvider.StairsBlockModels models = generator
                .stairsBlockTinted(baseBlock.getId().getPath(), baseBlockData.getSideTextureLoc(false),
                        baseBlockData.getBottomTextureLoc(false), baseBlockData.getTopTextureLoc(false));
        for (RegistryObject<ColoredStairsBlock> blockObject : getBlockObjects())
        {
            generator.stairsBlock(blockObject.get(), models.getStairs(), models.getStairsInner(),
                    models.getStairsOuter());
        }
    }
}
