package mrp_v2.additionalcolors.util.colored_block_data;

import mrp_v2.additionalcolors.AdditionalColors;
import mrp_v2.additionalcolors.block.ColoredBlock;
import mrp_v2.additionalcolors.datagen.BlockStateGenerator;
import mrp_v2.additionalcolors.datagen.TextureGenerator;
import mrp_v2.mrplibrary.datagen.providers.TextureProvider;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.Tag;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;

import java.awt.image.BufferedImage;

public class BottomTopBasicBlockData extends BasicColoredBlockData
{
    public BottomTopBasicBlockData(Block baseBlock, Tag<Block>[] additionalBlockTags, Tag<Item>[] additionalItemTags)
    {
        super(baseBlock, additionalBlockTags, additionalItemTags);
    }

    @Override public void registerTextures(TextureGenerator generator, TextureProvider.FinishedTextureConsumer consumer)
    {
        BufferedImage topTexture = generator.getTexture(new ResourceLocation(baseBlock.getId().getNamespace(),
                "block/" + baseBlock.getId().getPath() + "_top"));
        TextureGenerator.makeGrayscale(topTexture, 0, 0, 16, 16);
        TextureGenerator.adjustLevels(topTexture, 0, 0, 16, 16, 0.5d);
        generator.finish(topTexture,
                new ResourceLocation(AdditionalColors.ID, "block/" + baseBlock.getId().getPath() + "_top"), consumer);
        BufferedImage sideTexture = generator.getTexture(
                new ResourceLocation(baseBlock.getId().getNamespace(), "block/" + baseBlock.getId().getPath()));
        TextureGenerator.makeGrayscale(sideTexture, 0, 0, 16, 16);
        TextureGenerator.adjustLevels(sideTexture, 0, 0, 16, 16, 0.5d);
        generator.finish(sideTexture, new ResourceLocation(AdditionalColors.ID, "block/" + baseBlock.getId().getPath()),
                consumer);
        BufferedImage bottomTexture = generator.getTexture(new ResourceLocation(baseBlock.getId().getNamespace(),
                "block/" + baseBlock.getId().getPath() + "_bottom"));
        TextureGenerator.makeGrayscale(bottomTexture, 0, 0, 16, 16);
        TextureGenerator.adjustLevels(bottomTexture, 0, 0, 16, 16, 0.5d);
        generator.finish(bottomTexture,
                new ResourceLocation(AdditionalColors.ID, "block/" + baseBlock.getId().getPath() + "_bottom"),
                consumer);
    }

    @Override public void registerBlockStatesAndModels(BlockStateGenerator generator)
    {
        generator.models().getBuilder(baseBlock.getId().getPath())
                .parent(generator.models().getExistingFile(generator.mcLoc("block/block")))
                .texture("top", generator.modLoc("block/" + baseBlock.getId().getPath() + "_top"))
                .texture("bottom", generator.modLoc("block/" + baseBlock.getId().getPath() + "_bottom"))
                .texture("side", generator.modLoc("block/" + baseBlock.getId().getPath())).element()
                .from(0, 0, 0).to(16, 16, 16)
                .allFaces((face, builder) -> builder.texture("#side").cullface(face).tintindex(0).end())
                .face(Direction.UP).texture("#top").end().face(Direction.DOWN).texture("#bottom").end().end();
        for (RegistryObject<ColoredBlock> blockObject : blockObjectMap.values())
        {
            generator.simpleBlock(blockObject.get(),
                    generator.models().getExistingFile(generator.modLoc("block/" + baseBlock.getId().getPath())));
        }
    }
}
