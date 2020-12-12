package mrp_v2.additionalcolors.util;

import mrp_v2.additionalcolors.AdditionalColors;
import mrp_v2.additionalcolors.block.ColoredCryingObsidianBlock;
import mrp_v2.additionalcolors.block.IColoredBlock;
import mrp_v2.additionalcolors.datagen.*;
import mrp_v2.additionalcolors.item.ColoredBlockItem;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.data.TagsProvider;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fml.RegistryObject;

import java.util.HashSet;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ColoredCryingObsidianHandler
{
    public static final DyeColor[] CRYING_OBSIDIAN_COLORS =
            new DyeColor[]{DyeColor.WHITE, DyeColor.ORANGE, DyeColor.YELLOW, DyeColor.GRAY, DyeColor.LIGHT_GRAY,
                    DyeColor.CYAN, DyeColor.BLUE, DyeColor.BROWN, DyeColor.GREEN, DyeColor.RED, DyeColor.BLACK};
    private final BlockData<?>[] blockDatas;

    public ColoredCryingObsidianHandler()
    {
        blockDatas = new BlockData[]{new BlockData<>(Blocks.CRYING_OBSIDIAN.getRegistryName().getPath(),
                (color) -> () -> new ColoredCryingObsidianBlock(AbstractBlock.Properties.from(Blocks.CRYING_OBSIDIAN),
                        color), (blockSupplier) -> () -> new ColoredBlockItem(blockSupplier.get().getColor(),
                blockSupplier.get().getBlock(), new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)),
                (block, generator) -> generator.simpleBlock(block.getBlock()), (block, generator) ->
        {
            String path = block.getBlock().getRegistryName().getPath();
            generator.withExistingParent(path, generator.modLoc("block/" + path));
        }, Items.CRYING_OBSIDIAN.getRegistryName(), true, ItemTags.createOptional(
                new ResourceLocation(AdditionalColors.ID, Blocks.CRYING_OBSIDIAN.getRegistryName().getPath())))};
    }

    public void registerBlockStatesAndModels(BlockStateGenerator generator)
    {
        for (BlockData<?> data : blockDatas)
        {
            data.registerBlockStatesAndModels(generator);
        }
    }

    public void registerItemModels(ItemModelGenerator generator)
    {
        for (BlockData<?> data : blockDatas)
        {
            data.registerItemModels(generator);
        }
    }

    public void register()
    {
        for (BlockData<?> data : blockDatas)
        {
            data.register();
        }
    }

    public void registerTags(ItemTagGenerator generator)
    {
        for (BlockData<?> data : blockDatas)
        {
            data.registerTags(generator);
        }
    }

    public void registerRecipes(Consumer<IFinishedRecipe> consumer)
    {
        for (BlockData<?> data : blockDatas)
        {
            data.registerRecipes(consumer);
        }
    }

    public void generateTranslations(EN_USTranslationGenerator generator)
    {
        for (BlockData<?> data : blockDatas)
        {
            data.generateTranslations(generator);
        }
    }

    private static class BlockData<T extends Block & IColoredBlock>
    {
        private final String id;
        private final Function<DyeColor, Supplier<T>> blockConstructor;
        private final Function<Supplier<T>, Supplier<ColoredBlockItem>> itemConstructor;
        private final BiConsumer<T, BlockStateGenerator> blockStateAndModelGenerator;
        private final BiConsumer<T, ItemModelGenerator> itemModelGenerator;
        private final ResourceLocation baseItem;
        private final boolean required;
        private final ITag.INamedTag<Item> craftingTag;
        private final ITag.INamedTag<Item>[] tagsToAddTo;
        private final HashSet<RegistryObject<T>> objSet = new HashSet<>();

        @SafeVarargs private BlockData(String id, Function<DyeColor, Supplier<T>> blockConstructor,
                Function<Supplier<T>, Supplier<ColoredBlockItem>> itemConstructor,
                BiConsumer<T, BlockStateGenerator> blockStateAndModelGenerator,
                BiConsumer<T, ItemModelGenerator> itemModelGenerator, ResourceLocation baseItem, boolean required,
                ITag.INamedTag<Item> craftingTag, ITag.INamedTag<Item>... tagsToAddTo)
        {
            this.id = id;
            this.blockConstructor = blockConstructor;
            this.itemConstructor = itemConstructor;
            this.blockStateAndModelGenerator = blockStateAndModelGenerator;
            this.itemModelGenerator = itemModelGenerator;
            this.baseItem = baseItem;
            this.required = required;
            this.craftingTag = craftingTag;
            this.tagsToAddTo = tagsToAddTo;
        }

        private void registerItemModels(ItemModelGenerator generator)
        {
            for (RegistryObject<T> obj : objSet)
            {
                itemModelGenerator.accept(obj.get(), generator);
            }
        }

        private void registerBlockStatesAndModels(BlockStateGenerator generator)
        {
            for (RegistryObject<T> obj : objSet)
            {
                blockStateAndModelGenerator.accept(obj.get(), generator);
            }
        }

        private void register()
        {
            for (DyeColor color : CRYING_OBSIDIAN_COLORS)
            {
                String id = color.getTranslationKey() + "_" + this.id;
                RegistryObject<T> blockObj = ObjectHolder.BLOCKS.register(id, blockConstructor.apply(color));
                objSet.add(blockObj);
                ObjectHolder.ITEMS.register(id, itemConstructor.apply(blockObj));
            }
        }

        private void registerRecipes(Consumer<IFinishedRecipe> consumer)
        {
            for (RegistryObject<T> obj : objSet)
            {
                ShapelessRecipeBuilder.shapelessRecipe(obj.get()).addIngredient(craftingTag)
                        .addIngredient(obj.get().getColor().getTag())
                        .addCriterion("has_base", RecipeGenerator.makeHasItemCriterion(craftingTag)).build(consumer);
            }
        }

        private void registerTags(ItemTagGenerator generator)
        {
            TagsProvider.Builder<Item> recipeTagBuilder = generator.getOrCreateBuilder(this.craftingTag);
            objSet.forEach(obj -> recipeTagBuilder.add(obj.get().asItem()));
            if (required)
            {
                recipeTagBuilder.add(RegistryKey.getOrCreateKey(Registry.ITEM_KEY, baseItem));
            } else
            {
                recipeTagBuilder.addOptional(baseItem);
            }
            for (ITag.INamedTag<Item> tagToAddTo : tagsToAddTo)
            {
                TagsProvider.Builder<Item> tagBuilder = generator.getOrCreateBuilder(tagToAddTo);
                objSet.forEach(obj -> tagBuilder.add(obj.get().asItem()));
                if (required)
                {
                    recipeTagBuilder.add(RegistryKey.getOrCreateKey(Registry.ITEM_KEY, baseItem));
                } else
                {
                    recipeTagBuilder.addOptional(baseItem);
                }
            }
        }

        private void generateTranslations(EN_USTranslationGenerator generator)
        {
            objSet.forEach(generator::addSimpleBlock);
        }
    }
}
