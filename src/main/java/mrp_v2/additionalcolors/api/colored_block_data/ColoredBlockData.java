package mrp_v2.additionalcolors.api.colored_block_data;

import mrp_v2.additionalcolors.block.ColoredBlock;
import mrp_v2.mrplibrary.util.IModLocProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.DyeColor;
import net.minecraftforge.registries.RegistryObject;

public class ColoredBlockData extends AbstractColoredBlockData<ColoredBlock>
{
    public ColoredBlockData(RegistryObject<? extends Block> baseBlock, IModLocProvider modLocProvider)
    {
        super(baseBlock, modLocProvider);
    }

    @Override protected ColoredBlock makeNewBlock(DyeColor color)
    {
        return new ColoredBlock(getBlockProperties(color), color);
    }
}
