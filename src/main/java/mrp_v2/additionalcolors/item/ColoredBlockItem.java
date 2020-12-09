package mrp_v2.additionalcolors.item;

import mrp_v2.additionalcolors.block.ColoredBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeColor;

public class ColoredBlockItem extends BlockItem
{
    private final DyeColor color;

    public ColoredBlockItem(ColoredBlock blockIn, Properties builder)
    {
        this(blockIn.getColor(), blockIn, builder);
    }

    public ColoredBlockItem(DyeColor color, Block blockIn, Properties builder)
    {
        super(blockIn, builder);
        this.color = color;
    }

    public DyeColor getColor()
    {
        return this.color;
    }
}
