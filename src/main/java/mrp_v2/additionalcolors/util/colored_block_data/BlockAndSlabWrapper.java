package mrp_v2.additionalcolors.util.colored_block_data;

import mrp_v2.additionalcolors.util.ObjectHolder;
import mrp_v2.additionalcolors.util.Util;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;

import java.util.List;

public class BlockAndSlabWrapper
{
    protected final Block baseBlock;
    protected final ITag.INamedTag<Block>[] blockTags;
    protected final ITag.INamedTag<Item>[] blockItemTags;
    protected final Block baseSlabBlock;
    protected final ITag.INamedTag<Block>[] slabBlockTags;
    protected final ITag.INamedTag<Item>[] slabBlockItemTags;

    public BlockAndSlabWrapper(Block baseBlock, Block baseSlabBlock)
    {
        this(baseBlock, Util.makeTagArray(), Util.makeTagArray(), baseSlabBlock, Util.makeTagArray(),
                Util.makeTagArray());
    }

    public BlockAndSlabWrapper(Block baseBlock, ITag.INamedTag<Block>[] blockTags, ITag.INamedTag<Item>[] blockItemTags,
            Block baseSlabBlock, ITag.INamedTag<Block>[] slabBlockTags, ITag.INamedTag<Item>[] slabBlockItemTags)
    {
        this(baseBlock, blockTags, blockItemTags, baseSlabBlock, slabBlockTags, slabBlockItemTags, true);
    }

    protected BlockAndSlabWrapper(Block baseBlock, ITag.INamedTag<Block>[] blockTags,
            ITag.INamedTag<Item>[] blockItemTags, Block baseSlabBlock, ITag.INamedTag<Block>[] slabBlockTags,
            ITag.INamedTag<Item>[] slabBlockItemTags, boolean build)
    {
        this.baseBlock = baseBlock;
        this.blockTags = blockTags;
        this.blockItemTags = blockItemTags;
        this.baseSlabBlock = baseSlabBlock;
        this.slabBlockTags = slabBlockTags;
        this.slabBlockItemTags = slabBlockItemTags;
        if (build)
        {
            build(ObjectHolder.COLORIZED_BLOCK_DATAS);
        }
    }

    protected AbstractColoredBlockData<?> build(List<IColoredBlockData<?>> coloredBlockDataList)
    {
        AbstractColoredBlockData<?> baseBlockData = makeNewColoredBlockData();
        coloredBlockDataList.add(baseBlockData);
        coloredBlockDataList.add(makeNewColoredSlabBlockData(baseBlockData));
        return baseBlockData;
    }

    protected AbstractColoredBlockData<?> makeNewColoredBlockData()
    {
        return new BasicColoredBlockData(baseBlock, blockTags, blockItemTags);
    }

    protected AbstractColoredBlockData<?> makeNewColoredSlabBlockData(AbstractColoredBlockData<?> baseBlockData)
    {
        return new BasicColoredSlabBlockData(baseSlabBlock, slabBlockTags, slabBlockItemTags, baseBlockData);
    }
}
