package mrp_v2.additionalcolors.util;

import net.minecraft.tags.ITag;

public class Util
{
    @SafeVarargs public static <U> ITag.INamedTag<U>[] makeTagArray(ITag.INamedTag<U>... tags)
    {
        return tags;
    }
}
