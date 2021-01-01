package mrp_v2.additionalcolors.util.colored_block_data;

import mrp_v2.additionalcolors.AdditionalColors;
import mrp_v2.additionalcolors.block.ColoredBlock;
import mrp_v2.additionalcolors.datagen.BlockStateGenerator;
import mrp_v2.additionalcolors.datagen.TextureGenerator;
import mrp_v2.additionalcolors.util.Util;
import mrp_v2.mrplibrary.datagen.providers.TextureProvider;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;

import java.awt.image.BufferedImage;

public class VerticalPillarBasicBlockData extends BasicColoredBlockData
{
    public VerticalPillarBasicBlockData(Block baseBlock)
    {
        this(baseBlock, Util.makeTagArray(), Util.makeTagArray());
    }

    public VerticalPillarBasicBlockData(Block baseBlock, ITag.INamedTag<Block>[] additionalBlockTags,
            ITag.INamedTag<Item>[] additionalItemTags)
    {
        super(baseBlock, additionalBlockTags, additionalItemTags);
    }

    public VerticalPillarBasicBlockData(Block baseBlock, ITag.INamedTag<Block> additionalBlockTag,
            ITag.INamedTag<Item> additionalItemTag)
    {
        this(baseBlock, Util.makeTagArray(additionalBlockTag), Util.makeTagArray(additionalItemTag));
    }

    @Override public void makeTextureGenerationPromises(TextureGenerator generator)
    {
        generator.promiseGeneration(
                new ResourceLocation(AdditionalColors.ID, "block/" + baseBlock.getId().getPath() + "_end"));
        generator.promiseGeneration(
                new ResourceLocation(AdditionalColors.ID, "block/" + baseBlock.getId().getPath() + "_side"));
    }

    @Override public void registerTextures(TextureGenerator generator, TextureProvider.FinishedTextureConsumer consumer)
    {
        BufferedImage endTexture = generator.getTexture(new ResourceLocation(baseBlock.getId().getNamespace(),
                "block/" + baseBlock.getId().getPath() + getEndSuffix()));
        TextureProvider.makeGrayscale(endTexture, 0, 0, 16, 16);
        TextureProvider.adjustLevels(endTexture, 0, 0, 16, 16, 0.5d);
        generator.finish(endTexture,
                new ResourceLocation(AdditionalColors.ID, "block/" + baseBlock.getId().getPath() + "_end"), consumer);
        BufferedImage sideTexture = generator.getTexture(new ResourceLocation(baseBlock.getId().getNamespace(),
                "block/" + baseBlock.getId().getPath() + getSideSuffix()));
        TextureProvider.makeGrayscale(sideTexture, 0, 0, 16, 16);
        TextureProvider.adjustLevels(sideTexture, 0, 0, 16, 16, 0.5d);
        generator.finish(sideTexture,
                new ResourceLocation(AdditionalColors.ID, "block/" + baseBlock.getId().getPath() + "_side"), consumer);
    }

    protected String getEndSuffix()
    {
        return "_top";
    }

    protected String getSideSuffix()
    {
        return "_side";
    }

    @Override public void registerBlockStatesAndModels(BlockStateGenerator generator)
    {
        generator.models().getBuilder(baseBlock.getId().getPath())
                .parent(generator.models().getExistingFile(generator.mcLoc("block/block")))
                .texture("end", generator.modLoc("block/" + baseBlock.getId().getPath() + "_end"))
                .texture("side", generator.modLoc("block/" + baseBlock.getId().getPath() + "_side"))
                .texture("particle", generator.modLoc("block/" + baseBlock.getId().getPath() + "_side")).element()
                .from(0, 0, 0).to(16, 16, 16).allFaces((face, faceBuilder) -> faceBuilder.tintindex(0)
                .texture(face.getAxis() == Direction.Axis.Y ? "#end" : "#side").cullface(face).end()).end();
        for (RegistryObject<ColoredBlock> blockObject : getBlockObjects())
        {
            generator.simpleBlock(blockObject.get(),
                    generator.models().getExistingFile(generator.modLoc("block/" + baseBlock.getId().getPath())));
        }
    }

    public static class Slab extends BasicColoredSlabBlockData
    {
        public Slab(Block baseBlock, ITag.INamedTag<Block>[] blockTagsToAddTo, ITag.INamedTag<Item>[] itemTagsToAddTo,
                IColoredBlockData<?> baseBlockData)
        {
            super(baseBlock, blockTagsToAddTo, itemTagsToAddTo, baseBlockData);
        }

        @Override protected ResourceLocation getSlabModelBottomTexture(BlockStateGenerator generator)
        {
            return getSlabModelTopTexture(generator);
        }

        @Override protected ResourceLocation getSlabModelTopTexture(BlockStateGenerator generator)
        {
            return generator.modLoc("block/" + baseBlockData.getBaseBlockLoc().getPath() + "_end");
        }

        @Override protected ResourceLocation getSlabModelSideTexture(BlockStateGenerator generator)
        {
            return generator.modLoc("block/" + baseBlockData.getBaseBlockLoc().getPath() + "_side");
        }
    }

    public static class Stairs extends BasicColoredStairsBlockData
    {
        public Stairs(Block baseBlock, ITag.INamedTag<Block>[] blockTagsToAddTo, ITag.INamedTag<Item>[] itemTagsToAddTo,
                AbstractColoredBlockData<?> baseBlockData)
        {
            super(baseBlock, blockTagsToAddTo, itemTagsToAddTo, baseBlockData);
        }

        @Override protected ResourceLocation getStairsModelBottomTexture(BlockStateGenerator generator)
        {
            return getStairsModelTopTexture(generator);
        }

        @Override protected ResourceLocation getStairsModelTopTexture(BlockStateGenerator generator)
        {
            return generator.modLoc("block/" + baseBlockData.getBaseBlockLoc().getPath() + "_end");
        }

        @Override protected ResourceLocation getStairsModelSideTexture(BlockStateGenerator generator)
        {
            return generator.modLoc("block/" + baseBlockData.getBaseBlockLoc().getPath() + "_side");
        }
    }

    @Override public void createBlockSlabAndStairWrapper(Block baseSlabBlock, Block baseStairsBlock)
    {
        new BlockSlabAndStairWrapper(baseSlabBlock, baseStairsBlock);
    }

    public class BlockSlabAndStairWrapper
            extends mrp_v2.additionalcolors.util.colored_block_data.BlockSlabAndStairWrapper
    {
        public BlockSlabAndStairWrapper(Block baseSlabBlock, Block baseStairsBlock)
        {
            super(VerticalPillarBasicBlockData.this.baseBlock.get(), baseSlabBlock, baseStairsBlock);
        }

        @Override protected BasicColoredBlockData makeNewColoredBlockData()
        {
            return VerticalPillarBasicBlockData.this;
        }

        @Override
        protected AbstractColoredBlockData<?> makeNewColoredSlabBlockData(AbstractColoredBlockData<?> baseBlockData)
        {
            return new Slab(baseSlabBlock, slabBlockTags, slabBlockItemTags, VerticalPillarBasicBlockData.this);
        }

        @Override
        protected AbstractColoredBlockData<?> makeNewColoredStairsBlockData(AbstractColoredBlockData<?> baseBlockData)
        {
            return new Stairs(baseStairsBlock, stairsBlockTags, stairsBlockItemTags, VerticalPillarBasicBlockData.this);
        }
    }
}

