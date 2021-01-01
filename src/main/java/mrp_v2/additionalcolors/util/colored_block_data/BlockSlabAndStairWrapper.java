package mrp_v2.additionalcolors.util.colored_block_data;

import mrp_v2.additionalcolors.util.ObjectHolder;
import mrp_v2.additionalcolors.util.Util;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;

import java.util.List;

public class BlockSlabAndStairWrapper extends BlockAndSlabWrapper
{
    protected final Block baseStairsBlock;
    protected final ITag.INamedTag<Block>[] stairsBlockTags;
    protected final ITag.INamedTag<Item>[] stairsBlockItemTags;

    public BlockSlabAndStairWrapper(Block baseBlock, Block baseSlabBlock, Block baseStairsBlock)
    {
        this(baseBlock, Util.makeTagArray(), Util.makeTagArray(), baseSlabBlock, Util.makeTagArray(),
                Util.makeTagArray(), baseStairsBlock, Util.makeTagArray(), Util.makeTagArray());
    }

    public BlockSlabAndStairWrapper(Block baseBlock, ITag.INamedTag<Block>[] blockTags,
            ITag.INamedTag<Item>[] blockItemTags, Block baseSlabBlock, ITag.INamedTag<Block>[] slabBlockTags,
            ITag.INamedTag<Item>[] slabBlockItemTags, Block baseStairsBlock, ITag.INamedTag<Block>[] stairsBlockTags,
            ITag.INamedTag<Item>[] stairsBlockItemTags)
    {
        super(baseBlock, blockTags, blockItemTags, baseSlabBlock, slabBlockTags, slabBlockItemTags, false);
        this.baseStairsBlock = baseStairsBlock;
        this.stairsBlockTags = stairsBlockTags;
        this.stairsBlockItemTags = stairsBlockItemTags;
        build(ObjectHolder.COLORIZED_BLOCK_DATAS);
    }

    protected AbstractColoredBlockData<?> build(List<IColoredBlockData<?>> coloredBlockDataList)
    {
        AbstractColoredBlockData<?> baseBlockData = super.build(coloredBlockDataList);
        coloredBlockDataList.add(makeNewColoredStairsBlockData(baseBlockData));
        return baseBlockData;
    }

    public BlockSlabAndStairWrapper(Block baseBlock, ITag.INamedTag<Block> blockTag, ITag.INamedTag<Item> blockItemTag,
            Block baseSlabBlock, Block baseStairsBlock)
    {
        this(baseBlock, Util.makeTagArray(blockTag), Util.makeTagArray(blockItemTag), baseSlabBlock,
                Util.makeTagArray(), Util.makeTagArray(), baseStairsBlock, Util.makeTagArray(), Util.makeTagArray());
    }

    public BlockSlabAndStairWrapper(Block baseBlock, ITag.INamedTag<Block> blockTag, ITag.INamedTag<Item> blockItemTag,
            Block baseSlabBlock, ITag.INamedTag<Block> slabBlockTag, ITag.INamedTag<Item> slabBlockItemTag,
            Block baseStairsBlock, ITag.INamedTag<Block> stairsBlockTag, ITag.INamedTag<Item> stairsBlockItemTag)
    {
        this(baseBlock, Util.makeTagArray(blockTag), Util.makeTagArray(blockItemTag), baseSlabBlock,
                Util.makeTagArray(slabBlockTag), Util.makeTagArray(slabBlockItemTag), baseStairsBlock,
                Util.makeTagArray(stairsBlockTag), Util.makeTagArray(stairsBlockItemTag));
    }

    protected AbstractColoredBlockData<?> makeNewColoredStairsBlockData(AbstractColoredBlockData<?> baseBlockData)
    {
        return new BasicColoredStairsBlockData(baseStairsBlock, stairsBlockTags, stairsBlockItemTags, baseBlockData);
    }
}
