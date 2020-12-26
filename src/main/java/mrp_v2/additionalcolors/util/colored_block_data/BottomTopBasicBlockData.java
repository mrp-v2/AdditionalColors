package mrp_v2.additionalcolors.util.colored_block_data;

import mrp_v2.additionalcolors.AdditionalColors;
import mrp_v2.additionalcolors.block.ColoredBlock;
import mrp_v2.additionalcolors.datagen.BlockStateGenerator;
import mrp_v2.additionalcolors.datagen.TextureGenerator;
import mrp_v2.mrplibrary.datagen.providers.TextureProvider;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;

import java.awt.image.BufferedImage;

public class BottomTopBasicBlockData extends BasicColoredBlockData
{
    public BottomTopBasicBlockData(Block baseBlock, ITag.INamedTag<Block>[] additionalBlockTags,
            ITag.INamedTag<Item>[] additionalItemTags)
    {
        super(baseBlock, additionalBlockTags, additionalItemTags);
    }

    @Override public void makeTextureGenerationPromises(TextureGenerator generator)
    {
        generator.promiseGeneration(
                new ResourceLocation(AdditionalColors.ID, "block/" + baseBlock.getRegistryName().getPath()));
        generator.promiseGeneration(
                new ResourceLocation(AdditionalColors.ID, "block/" + baseBlock.getRegistryName().getPath() + "_top"));
        generator.promiseGeneration(new ResourceLocation(AdditionalColors.ID,
                "block/" + baseBlock.getRegistryName().getPath() + "_bottom"));
    }

    @Override public void registerTextures(TextureGenerator generator, TextureProvider.FinishedTextureConsumer consumer)
    {
        BufferedImage topTexture = generator.getTexture(new ResourceLocation(baseBlock.getRegistryName().getNamespace(),
                "block/" + baseBlock.getRegistryName().getPath() + "_top"));
        TextureGenerator.makeGrayscale(topTexture, 0, 0, 16, 16);
        TextureGenerator.adjustLevels(topTexture, 0, 0, 16, 16, 0.5d);
        generator.finish(topTexture, null,
                new ResourceLocation(AdditionalColors.ID, "block/" + baseBlock.getRegistryName().getPath() + "_top"),
                consumer);
        BufferedImage sideTexture = generator.getTexture(
                new ResourceLocation(baseBlock.getRegistryName().getNamespace(),
                        "block/" + baseBlock.getRegistryName().getPath()));
        TextureGenerator.makeGrayscale(sideTexture, 0, 0, 16, 16);
        TextureGenerator.adjustLevels(sideTexture, 0, 0, 16, 16, 0.5d);
        generator.finish(sideTexture, null,
                new ResourceLocation(AdditionalColors.ID, "block/" + baseBlock.getRegistryName().getPath()), consumer);
        BufferedImage bottomTexture = generator.getTexture(
                new ResourceLocation(baseBlock.getRegistryName().getNamespace(),
                        "block/" + baseBlock.getRegistryName().getPath() + "_bottom"));
        TextureGenerator.makeGrayscale(bottomTexture, 0, 0, 16, 16);
        TextureGenerator.adjustLevels(bottomTexture, 0, 0, 16, 16, 0.5d);
        generator.finish(bottomTexture, null,
                new ResourceLocation(AdditionalColors.ID, "block/" + baseBlock.getRegistryName().getPath() + "_bottom"),
                consumer);
    }

    @Override public void registerBlockStatesAndModels(BlockStateGenerator generator)
    {
        generator.models().getBuilder(baseBlock.getRegistryName().getPath())
                .parent(generator.models().getExistingFile(generator.mcLoc("block/block")))
                .texture("top", generator.modLoc("block/" + baseBlock.getRegistryName().getPath() + "_top"))
                .texture("bottom", generator.modLoc("block/" + baseBlock.getRegistryName().getPath() + "_bottom"))
                .texture("side", generator.modLoc("block/" + baseBlock.getRegistryName().getPath())).element()
                .from(0, 0, 0).to(16, 16, 16)
                .allFaces((face, builder) -> builder.texture("#side").cullface(face).tintindex(0).end())
                .face(Direction.UP).texture("#top").end().face(Direction.DOWN).texture("#bottom").end().end();
        for (RegistryObject<ColoredBlock> blockObject : blockObjectSet)
        {
            generator.simpleBlock(blockObject.get(), generator.models()
                    .getExistingFile(generator.modLoc("block/" + baseBlock.getRegistryName().getPath())));
        }
    }
}
