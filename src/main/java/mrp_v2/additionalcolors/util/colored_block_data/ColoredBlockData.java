package mrp_v2.additionalcolors.util.colored_block_data;

import mrp_v2.additionalcolors.AdditionalColors;
import mrp_v2.additionalcolors.datagen.BlockTagGenerator;
import mrp_v2.additionalcolors.datagen.EN_USTranslationGenerator;
import mrp_v2.additionalcolors.datagen.ItemTagGenerator;
import mrp_v2.additionalcolors.datagen.RecipeGenerator;
import mrp_v2.additionalcolors.util.IColored;
import mrp_v2.additionalcolors.util.ObjectHolder;
import mrp_v2.mrplibrary.datagen.recipe.ShapelessRecipeBuilder;
import net.minecraft.block.Block;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.TagsProvider;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public abstract class ColoredBlockData<T extends Block & IColored> implements IColoredBlockData<T>
{
    protected final Set<RegistryObject<T>> blockObjectSet = new HashSet<>();
    protected final ITag.INamedTag<Item> craftingTag;
    protected final ITag.INamedTag<Block>[] blockTagsToAddTo;
    protected final ITag.INamedTag<Item>[] itemTagsToAddTo;
    protected final ITag.INamedTag<Item> craftingTagIncludingBase;

    protected ColoredBlockData(String baseBlockPath, ITag.INamedTag<Block>[] blockTagsToAddTo,
            ITag.INamedTag<Item>[] itemTagsToAddTo)
    {
        this.craftingTagIncludingBase =
                ItemTags.createOptional(new ResourceLocation(AdditionalColors.ID, baseBlockPath));
        this.craftingTag = ItemTags.createOptional(new ResourceLocation(AdditionalColors.ID, baseBlockPath + "_base"));
        this.blockTagsToAddTo = blockTagsToAddTo;
        this.itemTagsToAddTo = itemTagsToAddTo;
    }

    @Override public void forEachBlock(Consumer<T> consumer)
    {
        for (RegistryObject<T> blockObject : blockObjectSet)
        {
            consumer.accept(blockObject.get());
        }
    }

    @Override public void clientSetup(FMLClientSetupEvent event)
    {
    }

    @Override public void registerRecipes(Consumer<IFinishedRecipe> consumer)
    {
        ShapelessRecipeBuilder.shapelessRecipe(getBaseItemLoc()).addIngredient(craftingTag)
                .addCriterion("has_block", RecipeGenerator.makeHasItemCriterion(craftingTag)).build(consumer);
        for (RegistryObject<T> blockObject : blockObjectSet)
        {
            T block = blockObject.get();
            ShapelessRecipeBuilder.shapelessRecipe(block).addIngredient(craftingTagIncludingBase)
                    .addIngredient(block.getColor().getTag())
                    .addCriterion("has_base", RecipeGenerator.makeHasItemCriterion(craftingTagIncludingBase))
                    .build(consumer);
        }
    }

    @Override public void registerBlockTags(BlockTagGenerator generator)
    {
        for (ITag.INamedTag<Block> tagToAddTo : blockTagsToAddTo)
        {
            TagsProvider.Builder<Block> tagBuilder = generator.getOrCreateBuilder(tagToAddTo);
            for (RegistryObject<T> blockObject : blockObjectSet)
            {
                tagBuilder.add(blockObject.get());
            }
            if (doesBaseBlockAlwaysExist())
            {
                tagBuilder.add(getBaseBlock());
            } else
            {
                tagBuilder.addOptional(getBaseBlockLoc());
            }
        }
    }

    @Override public void registerItemTags(ItemTagGenerator generator)
    {
        TagsProvider.Builder<Item> craftingTagBuilder = generator.getOrCreateBuilder(craftingTag);
        TagsProvider.Builder<Item> craftingTagIncludingBaseBuilder =
                generator.getOrCreateBuilder(craftingTagIncludingBase);
        for (RegistryObject<T> blockObject : blockObjectSet)
        {
            Item item = blockObject.get().asItem();
            craftingTagBuilder.add(item);
            craftingTagIncludingBaseBuilder.add(item);
        }
        if (doesBaseBlockAlwaysExist())
        {
            craftingTagIncludingBaseBuilder.add(getBaseBlock().asItem());
        } else
        {
            craftingTagIncludingBaseBuilder.addOptional(getBaseItemLoc());
        }
        for (ITag.INamedTag<Item> tagToAddTo : itemTagsToAddTo)
        {
            TagsProvider.Builder<Item> tagBuilder = generator.getOrCreateBuilder(tagToAddTo);
            for (RegistryObject<T> blockObject : blockObjectSet)
            {
                tagBuilder.add(blockObject.get().asItem());
            }
            if (doesBaseBlockAlwaysExist())
            {
                tagBuilder.add(getBaseBlock().asItem());
            } else
            {
                tagBuilder.addOptional(getBaseItemLoc());
            }
        }
    }

    @Override public void generateTranslations(EN_USTranslationGenerator generator)
    {
        for (RegistryObject<T> blockObject : blockObjectSet)
        {
            generator.addSimpleBlock(blockObject);
        }
    }

    @Override public DyeColor[] getColors()
    {
        return DyeColor.values();
    }

    @Override public ItemGroup getItemGroup()
    {
        return ObjectHolder.MAIN_ITEM_GROUP;
    }
}
