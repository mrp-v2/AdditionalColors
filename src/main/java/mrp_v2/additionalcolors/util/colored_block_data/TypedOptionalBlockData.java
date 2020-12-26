package mrp_v2.additionalcolors.util.colored_block_data;

import mrp_v2.additionalcolors.item.ColoredBlockItem;
import mrp_v2.additionalcolors.util.IColored;
import mrp_v2.additionalcolors.util.ObjectHolder;
import net.minecraft.block.Block;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public abstract class TypedOptionalBlockData<T extends Block & IColored> extends ColoredBlockData<T>
{
    protected final ResourceLocation baseBlockLoc;
    protected final Function<DyeColor, T> blockConstructor;

    protected TypedOptionalBlockData(ResourceLocation baseBlockLoc, Function<DyeColor, T> blockConstructor,
            ITag.INamedTag<Block>[] blockTagsToAddTo, ITag.INamedTag<Item>[] itemTagsToAddTo)
    {
        super(baseBlockLoc.getPath(), blockTagsToAddTo, itemTagsToAddTo);
        this.baseBlockLoc = baseBlockLoc;
        this.blockConstructor = blockConstructor;
    }

    @Override public Map<DyeColor, RegistryObject<T>> register()
    {
        Map<DyeColor, RegistryObject<T>> map = new HashMap<>();
        for (DyeColor color : getColors())
        {
            String id = color.getTranslationKey() + "_" + baseBlockLoc.getPath();
            RegistryObject<T> blockObj = ObjectHolder.BLOCKS.register(id, () -> blockConstructor.apply(color));
            blockObjectSet.add(blockObj);
            ObjectHolder.ITEMS.register(id,
                    () -> new ColoredBlockItem(blockObj.get(), new Item.Properties().group(getItemGroup())));
            map.put(color, blockObj);
        }
        return map;
    }

    @Override public boolean requiresTinting()
    {
        return false;
    }

    @Override public ResourceLocation getBaseBlockLoc()
    {
        return baseBlockLoc;
    }

    @Override public ResourceLocation getBaseItemLoc()
    {
        return baseBlockLoc;
    }

    @Override public Block getBaseBlock()
    {
        throw new UnsupportedOperationException(
                "This colored block data is not based on a block that is guaranteed to exist!");
    }

    @Override public boolean doesBaseBlockAlwaysExist()
    {
        return false;
    }
}
