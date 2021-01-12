package mrp_v2.additionalcolors.api.colored_block_data;

import mrp_v2.additionalcolors.api.datagen.BlockStateGenerator;
import mrp_v2.additionalcolors.block.ColoredBlock;
import mrp_v2.mrplibrary.datagen.providers.TextureProvider;
import mrp_v2.mrplibrary.util.IModLocProvider;
import net.minecraft.block.Block;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;

public class BottomTopBlockData extends ColoredBlockData
{
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
                "block/" + baseBlock.getId().getPath() + getTopSuffix());
    }

    protected String getTopSuffix()
    {
        return "_top";
    }

    public ResourceLocation getBottomTextureLoc(boolean base)
    {
        return new ResourceLocation(base ? baseBlock.getId().getNamespace() : getModId(),
                "block/" + baseBlock.getId().getPath() + getBottomSuffix());
    }

    @Override public void registerTextures(TextureProvider generator, TextureProvider.FinishedTextureConsumer consumer)
    {
        TextureProvider.Texture topTexture = generator.getTexture(getTopTextureLoc(true));
        TextureProvider.makeGrayscale(topTexture.getTexture(), 0, 0, 16, 16);
        TextureProvider.adjustLevels(topTexture.getTexture(), 0, 0, 16, 16, getLevelAdjustment());
        generator.finish(topTexture, getTopTextureLoc(false), consumer);
        TextureProvider.Texture sideTexture = generator.getTexture(getSideTextureLoc(true));
        TextureProvider.makeGrayscale(sideTexture.getTexture(), 0, 0, 16, 16);
        TextureProvider.adjustLevels(sideTexture.getTexture(), 0, 0, 16, 16, getLevelAdjustment());
        generator.finish(sideTexture, getSideTextureLoc(false), consumer);
        TextureProvider.Texture bottomTexture = generator.getTexture(getBottomTextureLoc(true));
        TextureProvider.makeGrayscale(bottomTexture.getTexture(), 0, 0, 16, 16);
        TextureProvider.adjustLevels(bottomTexture.getTexture(), 0, 0, 16, 16, getLevelAdjustment());
        generator.finish(bottomTexture, getBottomTextureLoc(false), consumer);
    }

    @Override public void registerBlockStatesAndModels(BlockStateGenerator generator)
    {
        generator.models().getBuilder(baseBlock.getId().getPath())
                .parent(generator.models().getExistingFile(generator.mcLoc("block/block")))
                .texture("top", getTopTextureLoc(false)).texture("bottom", getBottomTextureLoc(false))
                .texture("side", getSideTextureLoc(false)).element().from(0, 0, 0).to(16, 16, 16)
                .allFaces((face, builder) -> builder.texture("#side").cullface(face).tintindex(0).end())
                .face(Direction.UP).texture("#top").end().face(Direction.DOWN).texture("#bottom").end().end();
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
