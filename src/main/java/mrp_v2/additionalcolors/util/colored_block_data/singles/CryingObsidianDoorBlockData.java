package mrp_v2.additionalcolors.util.colored_block_data.singles;

import mrp_v2.additionalcolors.AdditionalColors;
import mrp_v2.additionalcolors.block.ColoredDoorBlock;
import mrp_v2.additionalcolors.datagen.BlockStateGenerator;
import mrp_v2.additionalcolors.datagen.ItemModelGenerator;
import mrp_v2.additionalcolors.datagen.LootTableGenerator;
import mrp_v2.additionalcolors.datagen.TextureGenerator;
import mrp_v2.additionalcolors.util.colored_block_data.AbstractCryingObsidianBlockData;
import mrp_v2.mrplibrary.datagen.providers.TextureProvider;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;

import java.awt.image.BufferedImage;
import java.util.function.Function;
import java.util.function.Supplier;

public class CryingObsidianDoorBlockData extends AbstractCryingObsidianBlockData<ColoredDoorBlock>
{
    public CryingObsidianDoorBlockData(ResourceLocation baseBlockLoc, ITag.INamedTag<Block>[] blockTagsToAddTo,
            ITag.INamedTag<Item>[] itemTagsToAddTo)
    {
        super(baseBlockLoc, blockTagsToAddTo, itemTagsToAddTo);
    }

    @Override public void registerBlockStatesAndModels(BlockStateGenerator generator)
    {
        for (RegistryObject<ColoredDoorBlock> blockObject : blockObjectSet)
        {
            Function<String, ResourceLocation> doorPartLocFunction = (str) -> new ResourceLocation(AdditionalColors.ID,
                    "block/" + blockObject.getId().getPath() + "_" + str);
            generator.doorBlock(blockObject.get(), doorPartLocFunction.apply("bottom"),
                    doorPartLocFunction.apply("top"));
        }
    }

    @Override protected ColoredDoorBlock makeNewBlock(DyeColor color)
    {
        return new ColoredDoorBlock(AbstractBlock.Properties.from(Blocks.CRYING_OBSIDIAN), color);
    }

    @Override public void makeTextureGenerationPromises(TextureGenerator generator)
    {
        for (RegistryObject<ColoredDoorBlock> blockObject : blockObjectSet)
        {
            generator.promiseGeneration(
                    new ResourceLocation(AdditionalColors.ID, "block/" + blockObject.getId().getPath() + "_top"));
            generator.promiseGeneration(
                    new ResourceLocation(AdditionalColors.ID, "block/" + blockObject.getId().getPath() + "_bottom"));
            generator.promiseGeneration(
                    new ResourceLocation(AdditionalColors.ID, "item/" + blockObject.getId().getPath()));
        }
    }

    @Override public void registerTextures(TextureGenerator generator, TextureProvider.FinishedTextureConsumer consumer)
    {
        for (RegistryObject<ColoredDoorBlock> blockObject : blockObjectSet)
        {
            Supplier<BufferedImage> baseTextureSupplier = () -> generator.getTexture(
                    new ResourceLocation(AdditionalColors.ID,
                            "block/" + blockObject.getId().getPath().replace("_door", "")));
            BufferedImage doorTop = baseTextureSupplier.get(), doorBottom = baseTextureSupplier.get();
            int hingeTop = TextureProvider.color(140, 103, 184), hingeBottom = TextureProvider.color(103, 88, 159),
                    handleEdge = TextureProvider.color(101, 88, 162);
            doorTop.setRGB(0, 4, hingeTop);
            doorTop.setRGB(0, 5, hingeBottom);
            doorTop.setRGB(0, 15, hingeTop);
            doorBottom.setRGB(0, 0, hingeBottom);
            doorBottom.setRGB(0, 10, hingeTop);
            doorBottom.setRGB(0, 11, hingeBottom);
            generator.finish(doorBottom,
                    new ResourceLocation(AdditionalColors.ID, "block/" + blockObject.getId().getPath() + "_bottom"),
                    consumer);
            doorTop.setRGB(11, 14, 2, 1, TextureProvider.color(hingeTop, 2), 0, 2);
            doorTop.setRGB(13, 14, handleEdge);
            doorTop.setRGB(11, 15, handleEdge);
            int[] clear = TextureProvider.color(TextureProvider.color(0, 0, 0, 0), 12);
            doorTop.setRGB(3, 3, 4, 3, clear, 0, 4);
            doorTop.setRGB(9, 3, 4, 3, clear, 0, 4);
            doorTop.setRGB(3, 8, 4, 3, clear, 0, 4);
            doorTop.setRGB(9, 8, 4, 3, clear, 0, 4);
            generator.finish(doorTop,
                    new ResourceLocation(AdditionalColors.ID, "block/" + blockObject.getId().getPath() + "_top"),
                    consumer);
            BufferedImage itemTexture = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
            itemTexture.setRGB(8, 0, 16, 16, doorTop.getRGB(0, 0, 16, 16, null, 0, 16), 0, 16);
            itemTexture.setRGB(8, 16, 16, 16, doorBottom.getRGB(0, 0, 16, 16, null, 0, 16), 0, 16);
            generator.finish(itemTexture,
                    new ResourceLocation(AdditionalColors.ID, "item/" + blockObject.getId().getPath()), consumer);
        }
    }

    @Override public void registerItemModels(ItemModelGenerator generator)
    {
        for (RegistryObject<ColoredDoorBlock> blockObject : blockObjectSet)
        {
            generator.singleTexture(blockObject.getId().getPath(), generator.mcLoc("item/generated"), "layer0",
                    generator.modLoc("item/" + blockObject.getId().getPath()));
        }
    }

    @Override public void registerLootTables(LootTableGenerator generator)
    {
        for (RegistryObject<ColoredDoorBlock> blockObject : blockObjectSet)
        {
            generator.addLootTable(blockObject.get(),
                    (block) -> generator.registerLootTable(block, LootTableGenerator::droppingDoor));
        }
    }
}
