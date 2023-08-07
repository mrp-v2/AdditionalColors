package mrp_v2.additionalcolors.api.colored_block_data;

import mrp_v2.additionalcolors.api.datagen.ExtendedBlockLootTables;
import mrp_v2.additionalcolors.api.datagen.LootTableGenerator;
import mrp_v2.additionalcolors.block.ColoredSlabBlock;
import mrp_v2.mrplibrary.datagen.providers.BlockStateProvider;
import mrp_v2.mrplibrary.datagen.providers.TextureProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.DyeColor;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegistryObject;

public class ColoredSlabBlockData extends AbstractColoredBlockData<ColoredSlabBlock>
{
    protected final AbstractColoredBlockData<?> baseBlockData;

    public ColoredSlabBlockData(RegistryObject<? extends Block> baseBlock, AbstractColoredBlockData<?> baseBlockData)
    {
        super(baseBlock, baseBlockData);
        this.baseBlockData = baseBlockData;
        addBlockTags(BlockTags.SLABS);
        addItemTags(ItemTags.SLABS);
    }

    @Override public void makeTextureGenerationPromises(TextureProvider generator)
    {
    }

    @Override protected ColoredSlabBlock makeNewBlock(DyeColor color)
    {
        return new ColoredSlabBlock(getBlockProperties(color), color);
    }

    @Override public void registerTextures(TextureProvider generator, TextureProvider.FinishedTextureConsumer consumer)
    {
    }

    @Override public void registerLootTables(LootTableGenerator generator)
    {
        for (RegistryObject<ColoredSlabBlock> blockObject : getBlockObjects())
        {
            generator.addLootTable(blockObject.get(),
                    (block) -> generator.add(block, ExtendedBlockLootTables::droppingSlab));
        }
    }

    @Override
    public void registerBlockStatesAndModels(mrp_v2.additionalcolors.api.datagen.BlockStateGenerator generator)
    {
        BlockStateProvider.SlabBlockModels models = generator
                .slabBlockTinted(baseBlock.getId().getPath(), getDoubleSlabModel(),
                        baseBlockData.getSideTextureLoc(false), baseBlockData.getBottomTextureLoc(false),
                        baseBlockData.getTopTextureLoc(false));
        for (RegistryObject<ColoredSlabBlock> blockObject : getBlockObjects())
        {
            generator.slabBlock(blockObject.get(), models.getSlab(), models.getSlabTop(), models.getDoubleSlab());
        }
    }

    protected ResourceLocation getDoubleSlabModel()
    {
        return baseBlockData.modLoc(baseBlockData.baseBlock.getId().getPath());
    }
}
