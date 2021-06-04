package mrp_v2.additionalcolors.api.datagen;

import mrp_v2.mrplibrary.datagen.providers.ItemTagsProvider;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.TagsProvider;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public abstract class ItemTagGenerator extends ItemTagsProvider
{
    protected ItemTagGenerator(DataGenerator dataGenerator, BlockTagsProvider blockTagProvider, String modId,
            @Nullable ExistingFileHelper existingFileHelper)
    {
        super(dataGenerator, blockTagProvider, modId, existingFileHelper);
    }

    @Override public TagsProvider.Builder<Item> tag(ITag.INamedTag<Item> tag)
    {
        return super.tag(tag);
    }
}
