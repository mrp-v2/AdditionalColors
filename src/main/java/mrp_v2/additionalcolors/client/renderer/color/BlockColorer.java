package mrp_v2.additionalcolors.client.renderer.color;

import mrp_v2.additionalcolors.AdditionalColors;
import mrp_v2.additionalcolors.util.IColored;
import mrp_v2.additionalcolors.util.ObjectHolder;
import mrp_v2.additionalcolors.util.colored_block_data.IColoredBlockData;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid = AdditionalColors.ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BlockColorer implements IBlockColor, IItemColor
{
    public static final BlockColorer INSTANCE = new BlockColorer();
    public static int DEFAULT_COLOR = 0x888888;

    @SubscribeEvent public static void registerBlockColors(ColorHandlerEvent.Block event)
    {
        for (IColoredBlockData<? extends Block> data : ObjectHolder.COLORIZED_BLOCK_DATAS)
        {
            if (data.requiresTinting())
            {
                data.forEachBlock((block) -> event.getBlockColors().register(INSTANCE, block));
            }
        }
    }

    @SubscribeEvent public static void registerItemColors(ColorHandlerEvent.Item event)
    {
        for (IColoredBlockData<? extends Block> data : ObjectHolder.COLORIZED_BLOCK_DATAS)
        {
            if (data.requiresTinting())
            {
                data.forEachBlock((block) -> event.getItemColors().register(INSTANCE, block.asItem()));
            }
        }
    }

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
