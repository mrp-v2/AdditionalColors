package mrp_v2.additionalcolors.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;

public class ColorizedBlockEntry
{
    private final Block block;
    private final ITag.INamedTag<Item> tag;

    public ColorizedBlockEntry(Block block, ITag.INamedTag<Item> tag)
    {
        this.block = block;
        this.tag = tag;
    }

    public Block getBlock()
    {
        return this.block;
    }

    public ITag.INamedTag<Item> getTag()
    {
        return this.tag;
    }
}
