package mrp_v2.additionalcolors.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;

public class ColorizedBlockEntry
{
    private final Block block;
    private final ITag.INamedTag<Item> craftingTag;
    private final ITag.INamedTag<Block>[] additionalBlockTags;
    private final ITag.INamedTag<Item>[] additionalItemTags;

    public ColorizedBlockEntry(Block block, ITag.INamedTag<Item> craftingTag,
            ITag.INamedTag<Block>[] additionalBlockTags, ITag.INamedTag<Item>[] additionalItemTags)
    {
        this.block = block;
        this.craftingTag = craftingTag;
        this.additionalBlockTags = additionalBlockTags;
        this.additionalItemTags = additionalItemTags;
    }

    public Block getBlock()
    {
        return this.block;
    }

    public ITag.INamedTag<Block>[] getAdditionalBlockTags()
    {
        return additionalBlockTags;
    }

    public ITag.INamedTag<Item>[] getAdditionalItemTags()
    {
        return additionalItemTags;
    }

    public ITag.INamedTag<Item> getCraftingTag()
    {
        return this.craftingTag;
    }
}
