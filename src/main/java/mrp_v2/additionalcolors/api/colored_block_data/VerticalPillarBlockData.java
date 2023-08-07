package mrp_v2.additionalcolors.api.colored_block_data;

import mrp_v2.additionalcolors.api.datagen.BlockStateGenerator;
import mrp_v2.additionalcolors.block.ColoredBlock;
import mrp_v2.mrplibrary.datagen.TintedBlockStateGenerator;
import mrp_v2.mrplibrary.datagen.providers.TextureProvider;
import mrp_v2.mrplibrary.util.IModLocProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.registries.RegistryObject;

public class VerticalPillarBlockData extends ColoredBlockData
{
    public VerticalPillarBlockData(RegistryObject<? extends Block> baseBlock, IModLocProvider modLocProvider)
    {
        super(baseBlock, modLocProvider);
    }

    @Override public void makeTextureGenerationPromises(TextureProvider generator)
    {
        generator.promiseGeneration(getEndTextureLoc(false));
        generator.promiseGeneration(getSideTextureLoc(false));
    }

    protected ResourceLocation getEndTextureLoc(boolean base)
    {
        return new ResourceLocation(base ? baseBlock.getId().getNamespace() : getModId(),
                "block/" + baseBlock.getId().getPath() + getEndSuffix());
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
        ModelFile model = generator.models()
                .withExistingParent(baseBlock.getId().getPath(), TintedBlockStateGenerator.CUBE_TINTED)
                .texture("end", getEndTextureLoc(false)).texture("side", getSideTextureLoc(false))
                .texture("particle", getSideTextureLoc(false)).element().from(0, 0, 0).to(16, 16, 16).allFaces(
                        (direction, faceBuilder) -> faceBuilder.tintindex(0)
                                .texture(direction.getAxis() == Direction.Axis.Y ? "#end" : "#side").cullface(direction)
                                .end()).end();
        for (RegistryObject<ColoredBlock> blockObject : getBlockObjects())
        {
            generator.simpleBlock(blockObject.get(), model);
        }
    }

    @Override
    public AbstractColoredBlockData<ColoredBlock>.Slab makeSlabBlock(RegistryObject<? extends Block> baseSlabBlock)
    {
        return new Slab(baseSlabBlock, this);
    }

    @Override
    public AbstractColoredBlockData<ColoredBlock>.Wall makeWallBlock(RegistryObject<? extends Block> baseWallBlock)
    {
        return new Wall(baseWallBlock, this);
    }

    @Override public AbstractColoredBlockData<ColoredBlock>.Stairs makeStairsBlock(
            RegistryObject<? extends Block> baseStairsBlock)
    {
        return new Stairs(baseStairsBlock, this);
    }

    protected String getEndSuffix()
    {
        return "_top";
    }

    public class Stairs extends AbstractColoredBlockData<ColoredBlock>.Stairs
    {
        public Stairs(RegistryObject<? extends Block> baseBlock, AbstractColoredBlockData<?> baseBlockData)
        {
            super(baseBlock, baseBlockData);
        }
    }

    public class Slab extends AbstractColoredBlockData<ColoredBlock>.Slab
    {
        public Slab(RegistryObject<? extends Block> baseBlock, AbstractColoredBlockData<?> baseBlockData)
        {
            super(baseBlock, baseBlockData);
        }
    }

    public class Wall extends AbstractColoredBlockData<ColoredBlock>.Wall
    {
        protected Wall(RegistryObject<? extends Block> baseBlock, AbstractColoredBlockData<?> baseBlockData)
        {
            super(baseBlock, baseBlockData);
        }
    }
}

