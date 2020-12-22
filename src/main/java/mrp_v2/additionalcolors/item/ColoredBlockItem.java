package mrp_v2.additionalcolors.item;

import mrp_v2.additionalcolors.util.IColored;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeColor;

public class ColoredBlockItem extends BlockItem implements IColored
{
    private final DyeColor color;

    public <T extends Block & IColored> ColoredBlockItem(T blockIn, Properties builder)
    {
        super(blockIn, builder);
        this.color = blockIn.getColor();
    }

    @Override public DyeColor getColor()
    {
        return this.color;
    }
}
