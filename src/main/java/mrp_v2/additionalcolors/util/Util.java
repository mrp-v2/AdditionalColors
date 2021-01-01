package mrp_v2.additionalcolors.util;

import net.minecraft.tags.ITag;

public class Util
{
    @SafeVarargs public static <T> ITag.INamedTag<T>[] makeTagArray(ITag.INamedTag<T>... tags)
    {
        return tags;
    }

    @SafeVarargs public static <T> ITag.INamedTag<T>[] combineTagArrays(ITag.INamedTag<T>[] tagArray,
            ITag.INamedTag<T>... additionalTags)
    {
        ITag.INamedTag<T>[] newTagArray = new ITag.INamedTag[tagArray.length + additionalTags.length];
        int i = 0;
        for (ITag.INamedTag<T> tag : tagArray)
        {
            newTagArray[i++] = tag;
        }
        for (ITag.INamedTag<T> tag : additionalTags)
        {
            newTagArray[i++] = tag;
        }
        return newTagArray;
    }
}
