package mrp_v2.additionalcolors.api.colored_block_data;

import mrp_v2.additionalcolors.api.datagen.BlockStateGenerator;
import mrp_v2.additionalcolors.api.datagen.ItemModelGenerator;
import mrp_v2.additionalcolors.block.ColoredTrapDoorBlock;
import mrp_v2.mrplibrary.datagen.providers.BlockStateProvider;
import mrp_v2.mrplibrary.util.IModLocProvider;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.item.DyeColor;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.fml.RegistryObject;

public class ColoredTrapDoorBlockData extends AbstractColoredBlockData<ColoredTrapDoorBlock>
{
    private final boolean orientable;

    public ColoredTrapDoorBlockData(RegistryObject<? extends Block> baseBlock, IModLocProvider modLocProvider,
            boolean orientable, boolean wooden)
    {
        super(baseBlock, modLocProvider);
        addBlockTags(BlockTags.TRAPDOORS);
        addItemTags(ItemTags.TRAPDOORS);
        this.orientable = orientable;
        if (wooden)
        {
            addBlockTags(BlockTags.WOODEN_TRAPDOORS);
            addItemTags(ItemTags.WOODEN_TRAPDOORS);
        }
    }

    @Override protected ColoredTrapDoorBlock makeNewBlock(DyeColor color)
    {
        return new ColoredTrapDoorBlock(getBlockProperties(color), color);
    }

    @Override protected boolean hasSpecialRenderType()
    {
        return true;
    }

    @Override protected RenderType getSpecialRenderType()
    {
        return RenderType.getCutout();
    }

    @Override public void registerItemModels(ItemModelGenerator generator)
    {
        for (RegistryObject<ColoredTrapDoorBlock> blockObject : getBlockObjects())
        {
            generator.withExistingParent(blockObject.getId().getPath(),
                    generator.modLoc("block/" + baseBlock.getId().getPath() + "_bottom"));
        }
    }

    @Override public void registerBlockStatesAndModels(BlockStateGenerator generator)
    {
        BlockStateProvider.TrapDoorBlockModels models = orientable ?
                generator.trapDoorBlockOrientableTinted(baseBlock.getId().getPath(), this.getSideTextureLoc(false)) :
                generator.trapDoorBlockTinted(baseBlock.getId().getPath(), this.getSideTextureLoc(false));
        for (RegistryObject<ColoredTrapDoorBlock> blockObject : getBlockObjects())
        {
            generator.trapdoorBlock(blockObject.get(), models.getBottom(), models.getTop(), models.getOpen(), true);
        }
    }
}
