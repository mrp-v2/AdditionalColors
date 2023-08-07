package mrp_v2.additionalcolors.api.colored_block_data;

import mrp_v2.additionalcolors.api.datagen.BlockStateGenerator;
import mrp_v2.additionalcolors.api.datagen.ItemModelGenerator;
import mrp_v2.additionalcolors.block.ColoredFenceBlock;
import mrp_v2.mrplibrary.datagen.providers.BlockStateProvider;
import mrp_v2.mrplibrary.datagen.providers.TextureProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.DyeColor;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.RegistryObject;

public class ColoredFenceBlockData extends AbstractColoredBlockData<ColoredFenceBlock>
{
    protected final AbstractColoredBlockData<?> baseBlockData;

    public ColoredFenceBlockData(RegistryObject<? extends Block> baseBlock, AbstractColoredBlockData<?> baseBlockData)
    {
        super(baseBlock, baseBlockData);
        this.baseBlockData = baseBlockData;
        addBlockTags(BlockTags.FENCES, Tags.Blocks.FENCES);
        addItemTags(ItemTags.FENCES, Tags.Items.FENCES);
    }

    @Override public void makeTextureGenerationPromises(TextureProvider generator)
    {
    }

    @Override protected ColoredFenceBlock makeNewBlock(DyeColor color)
    {
        return new ColoredFenceBlock(getBlockProperties(color), color);
    }

    @Override public void registerTextures(TextureProvider generator, TextureProvider.FinishedTextureConsumer consumer)
    {
    }

    @Override public void registerItemModels(ItemModelGenerator generator)
    {
        for (RegistryObject<ColoredFenceBlock> blockObject : blockObjectMap.values())
        {
            generator.fenceInventoryTinted(blockObject.getId().getPath(), baseBlockData.getSideTextureLoc(false));
        }
    }

    @Override public void registerBlockStatesAndModels(BlockStateGenerator generator)
    {
        BlockStateProvider.FenceBlockModels models =
                generator.fenceBlockTinted(baseBlock.getId().getPath(), baseBlockData.getSideTextureLoc(false));
        for (RegistryObject<ColoredFenceBlock> blockObject : blockObjectMap.values())
        {
            generator.fourWayBlock(blockObject.get(), models.getFencePost(), models.getFenceSide());
        }
    }
}
