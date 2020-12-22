package mrp_v2.additionalcolors.block;

import mrp_v2.additionalcolors.util.IColored;
import net.minecraft.block.Block;
import net.minecraft.item.DyeColor;

public class ColoredBlock extends Block implements IColored
{
    private final DyeColor color;

    public ColoredBlock(DyeColor color, Properties properties)
    {
        super(properties);
        this.color = color;
    }

    public DyeColor getColor()
    {
        return this.color;
    }
}
