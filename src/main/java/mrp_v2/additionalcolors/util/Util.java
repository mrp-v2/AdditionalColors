package mrp_v2.additionalcolors.util;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class Util
{
    @SafeVarargs public static <T> TagKey<T>[] makeTagArray(TagKey<T>... tags)
    {
        return tags;
    }

    @SafeVarargs public static <T> TagKey<T>[] combineTagArrays(TagKey<T>[] tagArray,
            TagKey<T>... additionalTags)
    {
        TagKey<T>[] newTagArray = new TagKey[tagArray.length + additionalTags.length];
        int i = 0;
        for (TagKey<T> tag : tagArray)
        {
            newTagArray[i++] = tag;
        }
        for (TagKey<T> tag : additionalTags)
        {
            newTagArray[i++] = tag;
        }
        return newTagArray;
    }

    public static RegistryObject<Block> makeRegistryObject(Block block)
    {
        return RegistryObject.create(block.getRegistryName(), ForgeRegistries.BLOCKS);
    }
}
