package mrp_v2.additionalcolors.util.colored_block_data;

import mrp_v2.additionalcolors.item.ColoredBlockItem;
import mrp_v2.additionalcolors.util.IColored;
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
import java.util.function.BiFunction;

public class TypedBasicColoredBlockData<T extends Block & IColored> extends ExistingBaseBlockData<T>
{
    protected final BiFunction<DyeColor, AbstractBlock.Properties, T> blockConstructor;

    public TypedBasicColoredBlockData(Block baseBlock,
            BiFunction<DyeColor, AbstractBlock.Properties, T> blockConstructor)
    {
        this(baseBlock, blockConstructor, Util.makeTagArray());
    }

    public TypedBasicColoredBlockData(Block baseBlock,
            BiFunction<DyeColor, AbstractBlock.Properties, T> blockConstructor,
            ITag.INamedTag<Block>[] blockTagsToAddTo)
    {
        this(baseBlock, blockConstructor, blockTagsToAddTo, Util.makeTagArray());
    }

    public TypedBasicColoredBlockData(Block baseBlock,
            BiFunction<DyeColor, AbstractBlock.Properties, T> blockConstructor,
            ITag.INamedTag<Block>[] blockTagsToAddTo, ITag.INamedTag<Item>[] itemTagsToAddTo)
    {
        super(baseBlock, blockTagsToAddTo, itemTagsToAddTo);
        this.blockConstructor = blockConstructor;
    }

    @Override public Map<DyeColor, RegistryObject<T>> register()
    {
        Map<DyeColor, RegistryObject<T>> map = new HashMap<>();
        for (DyeColor color : getColors())
        {
            String id = color.getTranslationKey() + "_" + this.baseBlock.getRegistryName().getPath();
            RegistryObject<T> blockObj = ObjectHolder.BLOCKS
                    .register(id, () -> blockConstructor.apply(color, AbstractBlock.Properties.from(baseBlock)));
            blockObjectSet.add(blockObj);
            ObjectHolder.ITEMS.register(id, () -> new ColoredBlockItem(blockObj.get(),
                    new Item.Properties().group(ObjectHolder.MAIN_ITEM_GROUP)));
            map.put(color, blockObj);
        }
        return map;
    }
}
