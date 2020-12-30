package mrp_v2.additionalcolors.util;

import net.minecraft.tags.Tag;

public class Util
{
    @SafeVarargs public static <U> Tag<U>[] makeTagArray(Tag<U>... tags)
    {
        return tags;
    }
}
