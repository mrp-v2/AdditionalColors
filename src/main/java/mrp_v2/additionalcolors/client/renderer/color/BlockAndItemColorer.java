package mrp_v2.additionalcolors.client.renderer.color;

import mrp_v2.additionalcolors.util.IColored;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;

import javax.annotation.Nullable;

public class BlockAndItemColorer implements BlockColor, ItemColor
{
    public static final BlockAndItemColorer INSTANCE = new BlockAndItemColorer();
    public static int DEFAULT_COLOR = 0x888888;

    @Override public int getColor(BlockState p_getColor_1_, @Nullable BlockAndTintGetter p_getColor_2_,
            @Nullable BlockPos p_getColor_3_, int p_getColor_4_)
    {
        return getColor(p_getColor_1_.getBlock());
    }

    private int getColor(Object obj)
    {
        if (obj instanceof IColored)
        {
            return ((IColored) obj).getColor().getMaterialColor().col;
        }
        return DEFAULT_COLOR;
    }

    @Override public int getColor(ItemStack p_getColor_1_, int p_getColor_2_)
    {
        return getColor(p_getColor_1_.getItem());
    }
}
