package mrp_v2.additionalcolors.client.renderer.color;

import mrp_v2.additionalcolors.AdditionalColors;
import mrp_v2.additionalcolors.block.ColoredBlock;
import mrp_v2.additionalcolors.item.ColoredBlockItem;
import mrp_v2.additionalcolors.util.ObjectHolder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.HashSet;

@Mod.EventBusSubscriber(modid = AdditionalColors.ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BlockColorer implements IBlockColor, IItemColor
{
    public static final BlockColorer INSTANCE = new BlockColorer();
    public static int DEFAULT_COLOR = 0x888888;

    @SubscribeEvent public static void registerBlockColors(ColorHandlerEvent.Block event)
    {
        Block[] blocks = new Block[ObjectHolder.BLOCKS_TO_COLORIZE.length * DyeColor.values().length];
        int i = 0;
        for (HashSet<RegistryObject<ColoredBlock>> set : ObjectHolder.COLORIZED_BLOCK_MAP.values())
        {
            for (RegistryObject<ColoredBlock> blockObj : set)
            {
                blocks[i++] = blockObj.get();
            }
        }
        event.getBlockColors().register(INSTANCE, blocks);
    }

    @SubscribeEvent public static void registerItemColors(ColorHandlerEvent.Item event)
    {
        Item[] items = new Item[ObjectHolder.BLOCKS_TO_COLORIZE.length * DyeColor.values().length];
        int i = 0;
        for (HashSet<RegistryObject<ColoredBlockItem>> set : ObjectHolder.COLORIZED_BLOCK_ITEM_MAP.values())
        {
            for (RegistryObject<ColoredBlockItem> itemObj : set)
            {
                items[i++] = itemObj.get();
            }
        }
        event.getItemColors().register(INSTANCE, items);
    }

    @Override public int getColor(BlockState p_getColor_1_, @Nullable IBlockDisplayReader p_getColor_2_,
            @Nullable BlockPos p_getColor_3_, int p_getColor_4_)
    {
        if (p_getColor_1_.getBlock() instanceof ColoredBlock)
        {
            return ((ColoredBlock) p_getColor_1_.getBlock()).getColor().getColorValue();
        }
        return DEFAULT_COLOR;
    }

    @Override public int getColor(ItemStack p_getColor_1_, int p_getColor_2_)
    {
        if (p_getColor_1_.getItem() instanceof ColoredBlockItem)
        {
            return ((ColoredBlockItem) p_getColor_1_.getItem()).getColor().getColorValue();
        }
        return DEFAULT_COLOR;
    }
}
