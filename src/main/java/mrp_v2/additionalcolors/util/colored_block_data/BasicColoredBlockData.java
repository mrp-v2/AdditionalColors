package mrp_v2.additionalcolors.util.colored_block_data;

import mrp_v2.additionalcolors.block.ColoredBlock;
import mrp_v2.additionalcolors.item.ColoredBlockItem;
import mrp_v2.additionalcolors.util.ObjectHolder;
import mrp_v2.additionalcolors.util.Util;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraftforge.fml.RegistryObject;

import java.util.HashMap;
import java.util.Map;

public class BasicColoredBlockData extends ExistingBaseBlockData<ColoredBlock>
{
    public BasicColoredBlockData(Block baseBlock)
    {
        this(baseBlock, Util.makeTagArray());
    }

    public BasicColoredBlockData(Block baseBlock, ITag.INamedTag<Block>[] additionalBlockTags)
    {
        this(baseBlock, additionalBlockTags, Util.makeTagArray());
    }

    public BasicColoredBlockData(Block baseBlock, ITag.INamedTag<Block>[] additionalBlockTags,
            ITag.INamedTag<Item>[] additionalItemTags)
    {
        super(baseBlock, additionalBlockTags, additionalItemTags);
    }

    public BasicColoredBlockData(Block baseBlock, double levelAdjustment, ITag.INamedTag<Block>[] additionalBlockTags,
            ITag.INamedTag<Item>[] additionalItemTags)
    {
        super(baseBlock, levelAdjustment, additionalBlockTags, additionalItemTags);
    }

    @Override public Map<DyeColor, RegistryObject<ColoredBlock>> register()
    {
        Map<DyeColor, RegistryObject<ColoredBlock>> map = new HashMap<>();
        for (DyeColor color : getColors())
        {
            String id = color.getTranslationKey() + "_" + this.baseBlock.getRegistryName().getPath();
            RegistryObject<ColoredBlock> blockObj = ObjectHolder.BLOCKS
                    .register(id, () -> new ColoredBlock(color, AbstractBlock.Properties.from(baseBlock)));
            blockObjectSet.add(blockObj);
            ObjectHolder.ITEMS.register(id,
                    () -> new ColoredBlockItem(blockObj.get(), new Item.Properties().group(getItemGroup())));
            map.put(color, blockObj);
        }
        return map;
    }
}
