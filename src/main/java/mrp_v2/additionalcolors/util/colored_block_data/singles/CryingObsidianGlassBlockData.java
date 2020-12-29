package mrp_v2.additionalcolors.util.colored_block_data.singles;

import mrp_v2.additionalcolors.AdditionalColors;
import mrp_v2.additionalcolors.block.ColoredObsidianGlassBlock;
import mrp_v2.additionalcolors.datagen.BlockStateGenerator;
import mrp_v2.additionalcolors.datagen.LootTableGenerator;
import mrp_v2.additionalcolors.datagen.TextureGenerator;
import mrp_v2.additionalcolors.util.colored_block_data.AbstractCryingObsidianBlockData;
import mrp_v2.mrplibrary.datagen.providers.TextureProvider;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.awt.image.BufferedImage;

public class CryingObsidianGlassBlockData extends AbstractCryingObsidianBlockData<ColoredObsidianGlassBlock>
{
    public CryingObsidianGlassBlockData(ResourceLocation baseBlockLoc, ITag.INamedTag<Block>[] blockTagsToAddTo,
            ITag.INamedTag<Item>[] itemTagsToAddTo)
    {
        super(baseBlockLoc, blockTagsToAddTo, itemTagsToAddTo);
    }

    @Override public void clientSetup(FMLClientSetupEvent event)
    {
        for (RegistryObject<ColoredObsidianGlassBlock> blockObject : blockObjectMap.values())
        {
            RenderTypeLookup.setRenderLayer(blockObject.get(), RenderType.getCutout());
        }
    }

    @Override public void registerBlockStatesAndModels(BlockStateGenerator generator)
    {
        for (RegistryObject<ColoredObsidianGlassBlock> blockObject : blockObjectMap.values())
        {
            generator.simpleBlock(blockObject.get());
        }
    }

    @Override protected ColoredObsidianGlassBlock makeNewBlock(DyeColor color)
    {
        return new ColoredObsidianGlassBlock(getBlockProperties().sound(SoundType.GLASS).notSolid(), color);
    }

    @Override public void makeTextureGenerationPromises(TextureGenerator generator)
    {
        for (RegistryObject<ColoredObsidianGlassBlock> blockObject : blockObjectMap.values())
        {
            generator.promiseGeneration(
                    new ResourceLocation(AdditionalColors.ID, "block/" + blockObject.getId().getPath()));
        }
    }

    @Override public void registerTextures(TextureGenerator generator, TextureProvider.FinishedTextureConsumer consumer)
    {
        for (RegistryObject<ColoredObsidianGlassBlock> blockObject : blockObjectMap.values())
        {
            BufferedImage texture = generator.getTexture(new ResourceLocation(AdditionalColors.ID,
                    "block/" + blockObject.getId().getPath().replace("_glass", "")));
            int[] newColors = TextureProvider.color(TextureProvider.color(0, 0, 0, 0), 14 * 14);
            newColors[14 + 3] = texture.getRGB(4, 2);
            newColors[14 * 2 + 2] = texture.getRGB(3, 3);
            newColors[14 * 3 + 1] = texture.getRGB(2, 4);
            newColors[14 * 11 + 12] = texture.getRGB(13, 12);
            newColors[14 * 12 + 11] = texture.getRGB(12, 13);
            texture.setRGB(1, 1, 14, 14, newColors, 0, 14);
            generator.finish(texture,
                    new ResourceLocation(AdditionalColors.ID, "block/" + blockObject.getId().getPath()), consumer);
        }
    }

    @Override public void registerLootTables(LootTableGenerator generator)
    {
        for (RegistryObject<ColoredObsidianGlassBlock> blockObject : blockObjectMap.values())
        {
            generator.addLootTable(blockObject.get(), generator::registerSilkTouch);
        }
    }
}
