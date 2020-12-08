package mrp_v2.additionalcolors.item;

import mrp_v2.additionalcolors.block.ColoredBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeColor;

public class ColoredBlockItem extends BlockItem
{
    private final DyeColor color;

    public ColoredBlockItem(ColoredBlock blockIn, Properties builder)
    {
        super(blockIn, builder);
        this.color = blockIn.getColor();
    }

    public DyeColor getColor()
    {
        return this.color;
    }
}
