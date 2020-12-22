package mrp_v2.additionalcolors.client.renderer.color;

import mrp_v2.additionalcolors.AdditionalColors;
import mrp_v2.additionalcolors.util.ColoredBlockData;
import mrp_v2.additionalcolors.util.IColored;
import mrp_v2.additionalcolors.util.ObjectHolder;
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
        for (ColoredBlockData<? extends Block> data : ObjectHolder.COLORIZED_BLOCK_DATAS)
        {
            if (data.requiresTinting())
            {
                data.forEachBlock((block) -> event.getBlockColors().register(INSTANCE, block));
            }
        }
    }

    @SubscribeEvent public static void registerItemColors(ColorHandlerEvent.Item event)
    {
        for (ColoredBlockData<? extends Block> data : ObjectHolder.COLORIZED_BLOCK_DATAS)
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
        if (p_getColor_1_.getBlock() instanceof IColored)
        {
            return getColor((IColored) p_getColor_1_.getBlock());
        }
        return DEFAULT_COLOR;
    }

    private int getColor(IColored colored)
    {
        return colored.getColor().getColorValue();
    }

    @Override public int getColor(ItemStack p_getColor_1_, int p_getColor_2_)
    {
        if (p_getColor_1_.getItem() instanceof IColored)
        {
            return getColor((IColored) p_getColor_1_.getItem());
        }
        return DEFAULT_COLOR;
    }
}
