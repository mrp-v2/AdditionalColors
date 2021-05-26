package mrp_v2.additionalcolors.block;

import mrp_v2.additionalcolors.util.IColored;
import net.minecraft.block.TrapDoorBlock;
import net.minecraft.item.DyeColor;

public class ColoredTrapDoorBlock extends TrapDoorBlock implements IColored
{
    private final DyeColor color;

    public ColoredTrapDoorBlock(Properties properties, DyeColor color)
    {
        super(properties);
        this.color = color;
    }

    @Override public DyeColor getColor()
    {
        return this.color;
    }
}
