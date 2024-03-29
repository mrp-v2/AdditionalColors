package mrp_v2.additionalcolors.util.colored_block_data;

import mrp_v2.additionalcolors.api.colored_block_data.AbstractColoredBlockData;
import mrp_v2.additionalcolors.api.datagen.BlockStateGenerator;
import mrp_v2.additionalcolors.api.datagen.ItemModelGenerator;
import mrp_v2.additionalcolors.block.ColoredCryingObsidianBlock;
import mrp_v2.mrplibrary.datagen.providers.TextureProvider;
import mrp_v2.mrplibrary.util.IModLocProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.DyeColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegistryObject;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class CryingObsidianBlockData extends AbstractColoredBlockData<ColoredCryingObsidianBlock>
{
    public static final Map<DyeColor, Consumer<BufferedImage>> TEXTURE_MAKER_FUNCTION_MAP;
    public static final DyeColor[] CRYING_OBSIDIAN_COLORS =
            new DyeColor[]{DyeColor.WHITE, DyeColor.ORANGE, DyeColor.YELLOW, DyeColor.GRAY, DyeColor.LIGHT_GRAY,
                    DyeColor.CYAN, DyeColor.BLUE, DyeColor.BROWN, DyeColor.GREEN, DyeColor.RED, DyeColor.BLACK};

    static
    {
        TEXTURE_MAKER_FUNCTION_MAP = new HashMap<>();
        TEXTURE_MAKER_FUNCTION_MAP.put(DyeColor.WHITE, (texture) ->
        {
            TextureProvider.makeGrayscale(texture);
            TextureProvider.adjustLevels(texture, 0.6d, 0, 70, 0, 255);
        });
        TEXTURE_MAKER_FUNCTION_MAP.put(DyeColor.LIGHT_GRAY, (texture) ->
        {
            TextureProvider.makeGrayscale(texture);
            TextureProvider.adjustLevels(texture, 0.5d);
        });
        TEXTURE_MAKER_FUNCTION_MAP.put(DyeColor.GRAY, TextureProvider::makeGrayscale);
        TEXTURE_MAKER_FUNCTION_MAP.put(DyeColor.BLACK, (texture) ->
        {
            TextureProvider.makeGrayscale(texture);
            TextureProvider.adjustLevels(texture, 1.5d);
        });
        TEXTURE_MAKER_FUNCTION_MAP.put(DyeColor.RED, (texture) -> TextureProvider.adjustHSB(texture, 90, 100, 0));
        TEXTURE_MAKER_FUNCTION_MAP.put(DyeColor.ORANGE, (texture) -> TextureProvider.adjustHSB(texture, 120, 100, 0));
        TEXTURE_MAKER_FUNCTION_MAP.put(DyeColor.YELLOW, (texture) -> TextureProvider.adjustHSB(texture, 150, 100, 0));
        TEXTURE_MAKER_FUNCTION_MAP.put(DyeColor.GREEN, (texture) -> TextureProvider.adjustHSB(texture, -165, 100, 0));
        TEXTURE_MAKER_FUNCTION_MAP.put(DyeColor.CYAN, (texture) -> TextureProvider.adjustHSB(texture, -90, 100, 0));
        TEXTURE_MAKER_FUNCTION_MAP.put(DyeColor.BLUE, (texture) -> TextureProvider.adjustHSB(texture, -30, 100, 0));
        TEXTURE_MAKER_FUNCTION_MAP.put(DyeColor.BROWN, (texture) -> TextureProvider.adjustHSB(texture, 120, 50, -40));
    }

    public CryingObsidianBlockData(RegistryObject<? extends Block> baseBlock, IModLocProvider coloredBlockDataHandler)
    {
        super(baseBlock, coloredBlockDataHandler);
    }

    @Override public void makeTextureGenerationPromises(TextureProvider generator)
    {
        for (RegistryObject<ColoredCryingObsidianBlock> blockObject : blockObjectMap.values())
        {
            generator.promiseGeneration(generator.modLoc("block/" + blockObject.getId().getPath()));
        }
    }

    @Override public DyeColor[] getColors()
    {
        return CRYING_OBSIDIAN_COLORS;
    }

    @Override protected ColoredCryingObsidianBlock makeNewBlock(DyeColor color)
    {
        return new ColoredCryingObsidianBlock(getBlockProperties(color), color);
    }

    @Override public boolean requiresTinting()
    {
        return false;
    }

    @Override public void registerTextures(TextureProvider generator, TextureProvider.FinishedTextureConsumer consumer)
    {
        for (RegistryObject<ColoredCryingObsidianBlock> blockObject : blockObjectMap.values())
        {
            ColoredCryingObsidianBlock block = blockObject.get();
            TextureProvider.Texture texture = generator
                    .getTexture(new ResourceLocation("block/" + Blocks.CRYING_OBSIDIAN.getRegistryName().getPath()));
            TEXTURE_MAKER_FUNCTION_MAP.get(block.getColor()).accept(texture.getTexture());
            generator.finish(texture, generator.modLoc("block/" + blockObject.getId().getPath()), consumer);
        }
    }

    @Override public void registerItemModels(ItemModelGenerator generator)
    {
        for (RegistryObject<ColoredCryingObsidianBlock> blockObject : blockObjectMap.values())
        {
            String path = blockObject.get().getRegistryName().getPath();
            generator.withExistingParent(path, generator.modLoc("block/" + path));
        }
    }

    @Override public void registerBlockStatesAndModels(BlockStateGenerator generator)
    {
        for (RegistryObject<ColoredCryingObsidianBlock> blockObject : blockObjectMap.values())
        {
            generator.simpleBlock(blockObject.get());
        }
    }
}
