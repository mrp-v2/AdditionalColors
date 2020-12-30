package mrp_v2.additionalcolors.util.colored_block_data;

import mrp_v2.additionalcolors.block.ColoredBlock;
import mrp_v2.additionalcolors.util.Util;
import net.minecraft.block.Block;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.tags.Tag;

public class BasicColoredBlockData extends AbstractColoredBlockData<ColoredBlock>
{
    public BasicColoredBlockData(Block baseBlock)
    {
        this(baseBlock, Util.makeTagArray());
    }

    public BasicColoredBlockData(Block baseBlock, Tag<Block>[] additionalBlockTags)
    {
        this(baseBlock, additionalBlockTags, Util.makeTagArray());
    }

    public BasicColoredBlockData(Block baseBlock, Tag<Block>[] additionalBlockTags, Tag<Item>[] additionalItemTags)
    {
        super(baseBlock, additionalBlockTags, additionalItemTags);
    }

    @Override protected ColoredBlock makeNewBlock(DyeColor color)
    {
        return new ColoredBlock(color, getBlockProperties());
    }
}
