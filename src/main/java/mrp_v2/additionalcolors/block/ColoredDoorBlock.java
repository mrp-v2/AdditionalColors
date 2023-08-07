package mrp_v2.additionalcolors.block;

import mrp_v2.additionalcolors.util.IColored;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.item.DyeColor;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

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
