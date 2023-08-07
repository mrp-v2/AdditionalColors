package mrp_v2.additionalcolors.api.colored_block_data;

import mrp_v2.additionalcolors.api.datagen.BlockStateGenerator;
import mrp_v2.additionalcolors.block.ColoredBlock;
import mrp_v2.mrplibrary.datagen.TintedBlockStateGenerator;
import mrp_v2.mrplibrary.datagen.providers.TextureProvider;
import mrp_v2.mrplibrary.util.IModLocProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.registries.RegistryObject;

public class BottomTopBlockData extends ColoredBlockData
{
    private final String TOP_SUFFIX = "_top";

    public BottomTopBlockData(RegistryObject<? extends Block> baseBlock, IModLocProvider modLocProvider)
    {
        super(baseBlock, modLocProvider);
    }

    @Override public void makeTextureGenerationPromises(TextureProvider generator)
    {
        generator.promiseGeneration(getSideTextureLoc(false));
        generator.promiseGeneration(getTopTextureLoc(false));
        generator.promiseGeneration(getBottomTextureLoc(false));
    }

    public ResourceLocation getTopTextureLoc(boolean base)
    {
        return new ResourceLocation(base ? baseBlock.getId().getNamespace() : getModId(),
                "block/" + baseBlock.getId().getPath() + TOP_SUFFIX);
    }

    public ResourceLocation getBottomTextureLoc(boolean base)
    {
        return new ResourceLocation(base ? baseBlock.getId().getNamespace() : getModId(),
                "block/" + baseBlock.getId().getPath() + getBottomSuffix());
    }

    @Override public void registerTextures(TextureProvider generator, TextureProvider.FinishedTextureConsumer consumer)
    {
        TextureProvider.Texture topTexture = generator.getTexture(getTopTextureLoc(true));
        TextureProvider.makeGrayscale(topTexture.getTexture());
        TextureProvider.adjustLevels(topTexture.getTexture(), getLevelAdjustment());
        generator.finish(topTexture, getTopTextureLoc(false), consumer);
        TextureProvider.Texture sideTexture = generator.getTexture(getSideTextureLoc(true));
        TextureProvider.makeGrayscale(sideTexture.getTexture());
        TextureProvider.adjustLevels(sideTexture.getTexture(), getLevelAdjustment());
        generator.finish(sideTexture, getSideTextureLoc(false), consumer);
        TextureProvider.Texture bottomTexture = generator.getTexture(getBottomTextureLoc(true));
        TextureProvider.makeGrayscale(bottomTexture.getTexture());
        TextureProvider.adjustLevels(bottomTexture.getTexture(), getLevelAdjustment());
        generator.finish(bottomTexture, getBottomTextureLoc(false), consumer);
    }

    @Override public void registerBlockStatesAndModels(BlockStateGenerator generator)
    {
        ModelFile baseModel = generator.models().getExistingFile(TintedBlockStateGenerator.CUBE_BOTTOM_TOP_TINTED);
        generator.models().getBuilder(baseBlock.getId().getPath())
                .parent(baseModel)
                .texture("top", getTopTextureLoc(false)).texture("bottom", getBottomTextureLoc(false))
                .texture("side", getSideTextureLoc(false));
        for (RegistryObject<ColoredBlock> blockObject : getBlockObjects())
        {
            generator.simpleBlock(blockObject.get(),
                    generator.models().getExistingFile(generator.modLoc("block/" + baseBlock.getId().getPath())));
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

    protected String getBottomSuffix()
    {
        return "_bottom";
    }

    public class Slab extends AbstractColoredBlockData<ColoredBlock>.Slab
    {
        public Slab(RegistryObject<? extends Block> baseBlock, AbstractColoredBlockData<?> baseBlockData)
        {
            super(baseBlock, baseBlockData);
        }
    }

    public class Stairs extends AbstractColoredBlockData<ColoredBlock>.Stairs
    {
        public Stairs(RegistryObject<? extends Block> baseBlock, AbstractColoredBlockData<?> baseBlockData)
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
