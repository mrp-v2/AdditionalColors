package mrp_v2.additionalcolors.util.colored_block_data;

import mrp_v2.additionalcolors.datagen.ItemModelGenerator;
import mrp_v2.additionalcolors.datagen.LootTableGenerator;
import mrp_v2.additionalcolors.datagen.TextureGenerator;
import mrp_v2.additionalcolors.util.IColored;
import mrp_v2.additionalcolors.util.ObjectHolder;
import mrp_v2.mrplibrary.datagen.providers.TextureProvider;
import net.minecraft.block.Block;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;

import javax.annotation.Nullable;

public abstract class AbstractCryingObsidianBlockData<T extends Block & IColored> extends AbstractColoredBlockData<T>
{
    protected AbstractCryingObsidianBlockData(ResourceLocation baseBlockLoc, ITag.INamedTag<Block>[] blockTagsToAddTo,
            ITag.INamedTag<Item>[] itemTagsToAddTo)
    {
        super(baseBlockLoc, blockTagsToAddTo, itemTagsToAddTo);
    }

    @Override public DyeColor[] getColors()
    {
        return ObjectHolder.CRYING_OBSIDIAN_COLORS;
    }

    @Nullable @Override public ItemGroup getItemGroup()
    {
        return ObjectHolder.OBSIDIAN_EXPANSION_ITEM_GROUP;
    }

    @Override public void makeTextureGenerationPromises(TextureGenerator generator)
    {
    }

    @Override public void registerTextures(TextureGenerator generator, TextureProvider.FinishedTextureConsumer consumer)
    {
    }

    @Override public void registerItemModels(ItemModelGenerator generator)
    {
        for (RegistryObject<T> blockObject : blockObjectSet)
        {
            String path = blockObject.get().getRegistryName().getPath();
            generator.withExistingParent(path, generator.modLoc("block/" + path));
        }
    }

    @Override public void registerLootTables(LootTableGenerator generator)
    {
        for (RegistryObject<T> blockObject : blockObjectSet)
        {
            generator.addLootTable(blockObject.get(), generator::registerDropSelfLootTable);
        }
    }

    @Override public boolean requiresTinting()
    {
        return false;
    }
}
