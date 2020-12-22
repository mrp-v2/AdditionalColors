package mrp_v2.additionalcolors.block;

import mrp_v2.additionalcolors.util.IColored;
import net.minecraft.block.DoorBlock;
import net.minecraft.item.DyeColor;

public class ColoredDoorBlock extends DoorBlock implements IColored
{
    private final DyeColor color;

    public ColoredDoorBlock(Properties builder, DyeColor color)
    {
        super(builder);
        this.color = color;
    }

    @Override public DyeColor getColor()
    {
        return color;
    }
}
