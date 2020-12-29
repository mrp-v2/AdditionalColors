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
        for (RegistryObject<ColoredBlock> blockObject : blockObjectMap.values())
        {
            generator.simpleBlock(blockObject.get(),
                    generator.models().getExistingFile(generator.modLoc("block/" + baseBlock.getId().getPath())));
        }
    }
}

