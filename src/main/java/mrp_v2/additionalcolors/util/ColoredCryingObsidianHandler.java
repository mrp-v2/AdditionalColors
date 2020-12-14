package mrp_v2.additionalcolors.util;

import mrp_v2.additionalcolors.AdditionalColors;
import mrp_v2.additionalcolors.block.*;
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
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.RegistryObject;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.function.*;
import java.util.stream.Collectors;

public class ColoredCryingObsidianHandler
{
    private final DyeColor[] cryingObsidianColors =
            new DyeColor[]{DyeColor.WHITE, DyeColor.ORANGE, DyeColor.YELLOW, DyeColor.GRAY, DyeColor.LIGHT_GRAY,
                    DyeColor.CYAN, DyeColor.BLUE, DyeColor.BROWN, DyeColor.GREEN, DyeColor.RED, DyeColor.BLACK};
    private final BlockData<?>[] blockDatas;
    private final HashMap<DyeColor, RegistryObject<? extends IColoredBlock>> baseBlocksMap = new HashMap<>();

    public ColoredCryingObsidianHandler()
    {
        BiConsumer<Block, ItemModelGenerator> basicItemModelMaker = (block, generator) ->
        {
            String path = block.getBlock().getRegistryName().getPath();
            generator.withExistingParent(path, generator.modLoc("block/" + path));
        };
        BiFunction<Supplier<? extends IColoredBlock>, ItemGroup, Supplier<ColoredBlockItem>> basicItemConstructor =
                (blockSupplier, itemGroup) -> () -> new ColoredBlockItem(blockSupplier.get().getColor(),
                        blockSupplier.get().getBlock(), new Item.Properties().group(itemGroup));
        final ItemGroup obsidianExpansionGroup = getObsidianExpansionItemGroup();
        final Supplier<AbstractBlock.Properties> basicProperties =
                () -> AbstractBlock.Properties.from(Blocks.CRYING_OBSIDIAN);
        blockDatas = new BlockData[]{new BlockData<>(Blocks.CRYING_OBSIDIAN.getRegistryName().getPath(),
                (color) -> () -> new ColoredCryingObsidianBlock(basicProperties.get(), color),
                (blockSupplier) -> basicItemConstructor.apply(blockSupplier, ItemGroup.BUILDING_BLOCKS),
                (block, generator) -> generator.simpleBlock(block.getBlock()), basicItemModelMaker::accept,
                Items.CRYING_OBSIDIAN.getRegistryName(), true, ItemTags.createOptional(
                new ResourceLocation(AdditionalColors.ID, Blocks.CRYING_OBSIDIAN.getRegistryName().getPath()))),
                new BlockData<>(Blocks.CRYING_OBSIDIAN.getRegistryName().getPath() + "_slab",
                        (color) -> () -> new ColoredSlabBlock(basicProperties.get(), color),
                        (blockSupplier) -> basicItemConstructor.apply(blockSupplier, obsidianExpansionGroup),
                        (block, generator) ->
                        {
                            ResourceLocation blockLoc = new ResourceLocation(AdditionalColors.ID,
                                    "block/" + block.getRegistryName().getPath().replace("_slab", ""));
                            generator.slabBlock(block, blockLoc, blockLoc);
                        }, basicItemModelMaker::accept,
                        new ResourceLocation(AdditionalColors.OBSIDIAN_EXPANSION_ID, "crying_obsidian_slab"), false,
                        ItemTags.createOptional(
                                new ResourceLocation(AdditionalColors.OBSIDIAN_EXPANSION_ID, "crying_obsidian_slab"))),
                new BlockData<>(Blocks.CRYING_OBSIDIAN.getRegistryName().getPath() + "_stairs",
                        (color) -> () -> new ColoredStairsBlock(
                                () -> baseBlocksMap.get(color).get().getBlock().getDefaultState(),
                                basicProperties.get(), color),
                        (blockSupplier) -> basicItemConstructor.apply(blockSupplier, obsidianExpansionGroup),
                        (block, generator) -> generator.stairsBlock(block, new ResourceLocation(AdditionalColors.ID,
                                "block/" + block.getRegistryName().getPath().replace("_stairs", ""))),
                        basicItemModelMaker::accept,
                        new ResourceLocation(AdditionalColors.OBSIDIAN_EXPANSION_ID, "crying_obsidian_stairs"), false,
                        ItemTags.createOptional(new ResourceLocation(AdditionalColors.OBSIDIAN_EXPANSION_ID,
                                "crying_obsidian_stairs"))),
                new BlockData<>(Blocks.CRYING_OBSIDIAN.getRegistryName().getPath() + "_door",
                        (color) -> () -> new ColoredDoorBlock(basicProperties.get().notSolid(), color),
                        (blockSupplier) -> basicItemConstructor.apply(blockSupplier, obsidianExpansionGroup),
                        (block, generator) ->
                        {
                            ResourceLocation blockLoc = new ResourceLocation(AdditionalColors.ID,
                                    "block/" + block.getRegistryName().getPath().replace("_door", ""));
                            generator.doorBlock(block, blockLoc, blockLoc);
                        }, (block, generator) ->
                {
                }, new ResourceLocation(AdditionalColors.OBSIDIAN_EXPANSION_ID, "crying_obsidian_door"), false,
                        ItemTags.createOptional(
                                new ResourceLocation(AdditionalColors.OBSIDIAN_EXPANSION_ID, "crying_obsidian_door")))};
    }

    @Nullable private ItemGroup getObsidianExpansionItemGroup()
    {
        if (!AdditionalColors.isObsidianExpansionPresent())
        {
            return null;
        }
        List<ItemGroup> labelMatches = Arrays.stream(ItemGroup.GROUPS)
                .filter((group) -> ((TranslationTextComponent) group.getGroupName()).getKey()
                        .equals("itemGroup.expansionTab")).collect(Collectors.toList());
        if (labelMatches.size() == 0)
        {
            return null;
        }
        if (labelMatches.size() == 1)
        {
            return labelMatches.get(0);
        }
        final ResourceLocation obsidianExpansionGroupItemIcon =
                new ResourceLocation(AdditionalColors.OBSIDIAN_EXPANSION_ID, "weak_obsidian");
        labelMatches = labelMatches.stream()
                .filter((group) -> group.getIcon().getItem().getRegistryName().equals(obsidianExpansionGroupItemIcon))
                .collect(Collectors.toList());
        if (labelMatches.size() == 1)
        {
            return labelMatches.get(0);
        }
        return null;
    }

    public void registerBlockStatesAndModels(BlockStateGenerator generator)
    {
        for (BlockData<? extends IColoredBlock> data : blockDatas)
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

    private class BlockData<T extends Block & IColoredBlock>
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
            this.register();
        }

        private void register()
        {
            for (DyeColor color : cryingObsidianColors)
            {
                String id = color.getTranslationKey() + "_" + this.id;
                RegistryObject<T> blockObj = ObjectHolder.BLOCKS.register(id, blockConstructor.apply(color));
                objSet.add(blockObj);
                ObjectHolder.ITEMS.register(id, itemConstructor.apply(blockObj));
                if (this.id.equals(Blocks.CRYING_OBSIDIAN.getRegistryName().getPath()))
                {
                    baseBlocksMap.put(color, blockObj);
                }
            }
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
