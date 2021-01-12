package mrp_v2.additionalcolors.client.renderer.color;

import mrp_v2.additionalcolors.util.IColored;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;

import javax.annotation.Nullable;

public class BlockAndItemColorer implements IBlockColor, IItemColor
{
    public static final BlockAndItemColorer INSTANCE = new BlockAndItemColorer();
    public static int DEFAULT_COLOR = 0x888888;

    @Override public int getColor(BlockState p_getColor_1_, @Nullable IBlockDisplayReader p_getColor_2_,
            @Nullable BlockPos p_getColor_3_, int p_getColor_4_)
    {
        return getColor(p_getColor_1_.getBlock());
    }

    private int getColor(Object obj)
    {
        if (obj instanceof IColored)
        {
            return ((IColored) obj).getColor().getColorValue();
        }
        return DEFAULT_COLOR;
    }

    @Override public int getColor(ItemStack p_getColor_1_, int p_getColor_2_)
    {
        return getColor(p_getColor_1_.getItem());
    }
}
