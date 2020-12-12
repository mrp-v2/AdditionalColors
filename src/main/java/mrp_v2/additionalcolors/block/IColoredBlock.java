package mrp_v2.additionalcolors.block;

import net.minecraft.item.DyeColor;
import net.minecraftforge.common.extensions.IForgeBlock;

public interface IColoredBlock extends IForgeBlock
{
    DyeColor getColor();
}
