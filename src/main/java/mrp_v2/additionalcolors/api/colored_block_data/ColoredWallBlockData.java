package mrp_v2.additionalcolors.api.colored_block_data;

import mrp_v2.additionalcolors.api.datagen.BlockStateGenerator;
import mrp_v2.additionalcolors.api.datagen.ItemModelGenerator;
import mrp_v2.additionalcolors.block.ColoredWallBlock;
import mrp_v2.mrplibrary.datagen.providers.BlockStateProvider;
import mrp_v2.mrplibrary.datagen.providers.TextureProvider;
import net.minecraft.block.Block;
import net.minecraft.item.DyeColor;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.fml.RegistryObject;

public class ColoredWallBlockData extends AbstractColoredBlockData<ColoredWallBlock>
{
    protected final AbstractColoredBlockData<?> baseBlockData;

    public ColoredWallBlockData(RegistryObject<? extends Block> baseBlock, AbstractColoredBlockData<?> baseBlockData)
    {
        super(baseBlock, baseBlockData);
        this.baseBlockData = baseBlockData;
        addBlockTags(BlockTags.WALLS);
        addItemTags(ItemTags.WALLS);
    }

    @Override public void makeTextureGenerationPromises(TextureProvider generator)
    {
    }

    @Override protected ColoredWallBlock makeNewBlock(DyeColor color)
    {
        return new ColoredWallBlock(getBlockProperties(color), color);
    }

    @Override public void registerTextures(TextureProvider generator, TextureProvider.FinishedTextureConsumer consumer)
    {
    }

    @Override public void registerItemModels(ItemModelGenerator generator)
    {
        for (RegistryObject<ColoredWallBlock> blockObject : getBlockObjects())
        {
            generator.wallInventoryTinted(blockObject.getId().getPath(), baseBlockData.getSideTextureLoc(false));
        }
    }

    @Override public void registerBlockStatesAndModels(BlockStateGenerator generator)
    {
        BlockStateProvider.WallBlockModels models =
                generator.wallBlockTinted(baseBlock.getId().getPath(), baseBlockData.getSideTextureLoc(false));
        for (RegistryObject<ColoredWallBlock> blockObject : getBlockObjects())
        {
            generator.wallBlock(blockObject.get(), models.getPost(), models.getSide(), models.getSideTall());
        }
    }
}
