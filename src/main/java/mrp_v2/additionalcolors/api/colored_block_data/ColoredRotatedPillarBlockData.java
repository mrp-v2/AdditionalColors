package mrp_v2.additionalcolors.api.colored_block_data;

import mrp_v2.additionalcolors.api.datagen.BlockStateGenerator;
import mrp_v2.additionalcolors.block.ColoredRotatedPillarBlock;
import mrp_v2.mrplibrary.datagen.providers.BlockStateProvider;
import mrp_v2.mrplibrary.datagen.providers.TextureProvider;
import mrp_v2.mrplibrary.util.IModLocProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.DyeColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegistryObject;

public class ColoredRotatedPillarBlockData extends AbstractColoredBlockData<ColoredRotatedPillarBlock>
{
    public ColoredRotatedPillarBlockData(RegistryObject<? extends Block> baseBlock, IModLocProvider modLocProvider)
    {
        super(baseBlock, modLocProvider);
    }

    @Override public void makeTextureGenerationPromises(TextureProvider generator)
    {
        generator.promiseGeneration(getEndTextureLoc(false));
        generator.promiseGeneration(getSideTextureLoc(false));
    }

    protected String getSideSuffix()
    {
        return "_side";
    }

    @Override protected ResourceLocation getEndTextureLoc(boolean base)
    {
        return new ResourceLocation(base ? baseBlock.getId().getNamespace() : getModId(),
                "block/" + baseBlock.getId().getPath() + getEndSuffix());
    }

    @Override protected ColoredRotatedPillarBlock makeNewBlock(DyeColor color)
    {
        return new ColoredRotatedPillarBlock(getBlockProperties(color), color);
    }

    @Override public void registerTextures(TextureProvider generator, TextureProvider.FinishedTextureConsumer consumer)
    {
        TextureProvider.Texture endTexture = generator.getTexture(getEndTextureLoc(true));
        TextureProvider.makeGrayscale(endTexture.getTexture());
        TextureProvider.adjustLevels(endTexture.getTexture(), getLevelAdjustment());
        generator.finish(endTexture, getEndTextureLoc(false), consumer);
        TextureProvider.Texture sideTexture = generator.getTexture(getSideTextureLoc(true));
        TextureProvider.makeGrayscale(sideTexture.getTexture());
        TextureProvider.adjustLevels(sideTexture.getTexture(), getLevelAdjustment());
        generator.finish(sideTexture, getSideTextureLoc(false), consumer);
    }

    @Override public void registerBlockStatesAndModels(BlockStateGenerator generator)
    {
        BlockStateProvider.AxisBlockModels models = generator
                .axisBlockTinted(baseBlock.getId().getPath(), getSideTextureLoc(false), getEndTextureLoc(false));
        for (RegistryObject<ColoredRotatedPillarBlock> blockObject : getBlockObjects())
        {
            generator.axisBlock(blockObject.get(), models.getCubeColumn(), models.getCubeColumnHorizontal());
        }
    }

    @Override public AbstractColoredBlockData<ColoredRotatedPillarBlock>.Slab makeSlabBlock(
            RegistryObject<? extends Block> baseSlabBlock)
    {
        return new Slab(baseSlabBlock, this);
    }

    @Override public AbstractColoredBlockData<ColoredRotatedPillarBlock>.Wall makeWallBlock(
            RegistryObject<? extends Block> baseWallBlock)
    {
        return new Wall(baseWallBlock, this);
    }

    @Override public AbstractColoredBlockData<ColoredRotatedPillarBlock>.Stairs makeStairsBlock(
            RegistryObject<? extends Block> baseStairsBlock)
    {
        return new Stairs(baseStairsBlock, this);
    }

    protected String getEndSuffix()
    {
        return "_top";
    }

    public class Stairs extends AbstractColoredBlockData<ColoredRotatedPillarBlock>.Stairs
    {
        public Stairs(RegistryObject<? extends Block> baseBlock, AbstractColoredBlockData<?> baseBlockData)
        {
            super(baseBlock, baseBlockData);
        }
    }

    public class Slab extends AbstractColoredBlockData<ColoredRotatedPillarBlock>.Slab
    {
        public Slab(RegistryObject<? extends Block> baseBlock, AbstractColoredBlockData<?> baseBlockData)
        {
            super(baseBlock, baseBlockData);
        }
    }

    public class Wall extends AbstractColoredBlockData<ColoredRotatedPillarBlock>.Wall
    {
        protected Wall(RegistryObject<? extends Block> baseBlock, AbstractColoredBlockData<?> baseBlockData)
        {
            super(baseBlock, baseBlockData);
        }
    }
}
