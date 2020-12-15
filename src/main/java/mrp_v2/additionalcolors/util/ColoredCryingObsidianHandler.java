package mrp_v2.additionalcolors.util;

import mrp_v2.additionalcolors.AdditionalColors;
import mrp_v2.additionalcolors.block.*;
import mrp_v2.additionalcolors.datagen.*;
import mrp_v2.additionalcolors.datagen.texture.IFinishedTexture;
import mrp_v2.additionalcolors.datagen.texture.TextureGenerator;
import mrp_v2.additionalcolors.datagen.texture.TextureProvider;
import mrp_v2.additionalcolors.item.ColoredBlockItem;
import mrp_v2.mrplibrary.datagen.ShapelessRecipeBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.data.IFinishedRecipe;
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
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.apache.logging.log4j.util.TriConsumer;

import javax.annotation.Nullable;
import java.awt.image.BufferedImage;
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
                (block, generator) -> generator.simpleBlock(block.getBlock()), basicItemModelMaker::accept, null, null,
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
                        }, basicItemModelMaker::accept, null, null,
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
                        basicItemModelMaker::accept, null, null,
                        new ResourceLocation(AdditionalColors.OBSIDIAN_EXPANSION_ID, "crying_obsidian_stairs"), false,
                        ItemTags.createOptional(new ResourceLocation(AdditionalColors.OBSIDIAN_EXPANSION_ID,
                                "crying_obsidian_stairs"))),
                new BlockData<>(Blocks.CRYING_OBSIDIAN.getRegistryName().getPath() + "_door",
                        (color) -> () -> new ColoredDoorBlock(basicProperties.get().notSolid(), color),
                        (blockSupplier) -> basicItemConstructor.apply(blockSupplier, obsidianExpansionGroup),
                        (block, generator) ->
                        {
                            Function<String, ResourceLocation> doorPartLocFunction =
                                    (str) -> new ResourceLocation(AdditionalColors.ID,
                                            "block/" + block.getRegistryName().getPath() + "_" + str);
                            generator.doorBlock(block, doorPartLocFunction.apply("bottom"),
                                    doorPartLocFunction.apply("top"));
                        }, (block, generator) -> generator
                        .singleTexture(block.getRegistryName().getPath(), generator.mcLoc("item/generated"), "layer0",
                                generator.modLoc("item/" + block.getRegistryName().getPath())),
                        (block, generator, consumer) ->
                        {
                            Supplier<BufferedImage> baseTextureSupplier = () -> generator.getTexture(
                                    new ResourceLocation(AdditionalColors.ID,
                                            "block/" + block.getRegistryName().getPath().replace("_door", "")));
                            BufferedImage doorTop = baseTextureSupplier.get(), doorBottom = baseTextureSupplier.get();
                            int hingeTop = TextureProvider.color(140, 103, 184), hingeBottom =
                                    TextureProvider.color(103, 88, 159), handleEdge =
                                    TextureProvider.color(101, 88, 162);
                            doorTop.setRGB(0, 4, hingeTop);
                            doorTop.setRGB(0, 5, hingeBottom);
                            doorTop.setRGB(0, 15, hingeTop);
                            doorBottom.setRGB(0, 0, hingeBottom);
                            doorBottom.setRGB(0, 10, hingeTop);
                            doorBottom.setRGB(0, 11, hingeBottom);
                            generator.finish(new ResourceLocation(AdditionalColors.ID,
                                    "block/" + block.getRegistryName().getPath() + "_bottom"), doorBottom, consumer);
                            doorTop.setRGB(11, 14, 2, 1, TextureProvider.color(hingeTop, 2), 0, 2);
                            doorTop.setRGB(13, 14, handleEdge);
                            doorTop.setRGB(11, 15, handleEdge);
                            int[] clear = TextureProvider.color(TextureProvider.color(0, 0, 0, 0), 12);
                            doorTop.setRGB(3, 3, 4, 3, clear, 0, 4);
                            doorTop.setRGB(9, 3, 4, 3, clear, 0, 4);
                            doorTop.setRGB(3, 8, 4, 3, clear, 0, 4);
                            doorTop.setRGB(9, 8, 4, 3, clear, 0, 4);
                            generator.finish(new ResourceLocation(AdditionalColors.ID,
                                    "block/" + block.getRegistryName().getPath() + "_top"), doorTop, consumer);
                            BufferedImage itemTexture = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
                            itemTexture.setRGB(8, 0, 16, 16, doorTop.getRGB(0, 0, 16, 16, null, 0, 16), 0, 16);
                            itemTexture.setRGB(8, 16, 16, 16, doorBottom.getRGB(0, 0, 16, 16, null, 0, 16), 0, 16);
                            generator.finish(new ResourceLocation(AdditionalColors.ID,
                                    "item/" + block.getRegistryName().getPath()), itemTexture, consumer);
                        }, (block, event) -> RenderTypeLookup.setRenderLayer(block, RenderType.getCutout()),
                        new ResourceLocation(AdditionalColors.OBSIDIAN_EXPANSION_ID, "crying_obsidian_door"), false,
                        ItemTags.createOptional(
                                new ResourceLocation(AdditionalColors.OBSIDIAN_EXPANSION_ID, "crying_obsidian_door"))),
                new BlockData<>(Blocks.CRYING_OBSIDIAN.getRegistryName().getPath() + "_glass",
                        (color) -> () -> new ColoredObsidianGlassBlock(
                                AbstractBlock.Properties.from(Blocks.CRYING_OBSIDIAN).sound(SoundType.GLASS).notSolid(),
                                color),
                        (blockSupplier) -> basicItemConstructor.apply(blockSupplier, obsidianExpansionGroup),
                        (block, generator) -> generator.simpleBlock(block), basicItemModelMaker::accept,
                        (block, generator, consumer) ->
                        {
                            BufferedImage texture = generator.getTexture(new ResourceLocation(AdditionalColors.ID,
                                    "block/" + block.getRegistryName().getPath().replace("_glass", "")));
                            int[] newColors = TextureProvider.color(TextureProvider.color(0, 0, 0, 0), 14 * 14);
                            newColors[14 + 3] = texture.getRGB(4, 2);
                            newColors[14 * 2 + 2] = texture.getRGB(3, 3);
                            newColors[14 * 3 + 1] = texture.getRGB(2, 4);
                            newColors[14 * 11 + 12] = texture.getRGB(13, 12);
                            newColors[14 * 12 + 11] = texture.getRGB(12, 13);
                            texture.setRGB(1, 1, 14, 14, newColors, 0, 14);
                            generator.finish(new ResourceLocation(AdditionalColors.ID,
                                    "block/" + block.getRegistryName().getPath()), texture, consumer);
                        }, (block, event) -> RenderTypeLookup.setRenderLayer(block, RenderType.getCutout()),
                        new ResourceLocation(AdditionalColors.OBSIDIAN_EXPANSION_ID, "crying_obsidian_glass"), false,
                        ItemTags.createOptional(new ResourceLocation(AdditionalColors.OBSIDIAN_EXPANSION_ID,
                                "crying_obsidian_glass")))};
    }

    @Nullable private ItemGroup getObsidianExpansionItemGroup()
    {
        if (true)
        {
            return null;
        }
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

    @SubscribeEvent public void clientSetup(FMLClientSetupEvent event)
    {
        for (BlockData<?> data : blockDatas)
        {
            data.clientSetup(event);
        }
    }

    public void registerTextures(TextureGenerator generator, Consumer<IFinishedTexture> consumer)
    {
        for (BlockData<?> data : blockDatas)
        {
            data.registerTextures(generator, consumer);
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
        @Nullable private final TriConsumer<T, TextureGenerator, Consumer<IFinishedTexture>> textureGenerator;
        @Nullable private final BiConsumer<T, FMLClientSetupEvent> clientSetupStuff;
        private final ResourceLocation baseItem;
        private final boolean required;
        private final ITag.INamedTag<Item> craftingTag;
        private final ITag.INamedTag<Item>[] tagsToAddTo;
        private final HashSet<RegistryObject<T>> objSet = new HashSet<>();

        @SafeVarargs private BlockData(String id, Function<DyeColor, Supplier<T>> blockConstructor,
                Function<Supplier<T>, Supplier<ColoredBlockItem>> itemConstructor,
                BiConsumer<T, BlockStateGenerator> blockStateAndModelGenerator,
                BiConsumer<T, ItemModelGenerator> itemModelGenerator,
                @Nullable TriConsumer<T, TextureGenerator, Consumer<IFinishedTexture>> textureGenerator,
                @Nullable BiConsumer<T, FMLClientSetupEvent> clientSetupStuff, ResourceLocation baseItem,
                boolean required, ITag.INamedTag<Item> craftingTag, ITag.INamedTag<Item>... tagsToAddTo)
        {
            this.id = id;
            this.blockConstructor = blockConstructor;
            this.itemConstructor = itemConstructor;
            this.blockStateAndModelGenerator = blockStateAndModelGenerator;
            this.itemModelGenerator = itemModelGenerator;
            this.clientSetupStuff = clientSetupStuff;
            this.textureGenerator = textureGenerator;
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

        public void clientSetup(FMLClientSetupEvent event)
        {
            if (clientSetupStuff != null)
            {
                for (RegistryObject<T> obj : objSet)
                {
                    clientSetupStuff.accept(obj.get(), event);
                }
            }
        }

        private void registerTextures(TextureGenerator generator, Consumer<IFinishedTexture> consumer)
        {
            if (textureGenerator != null)
            {
                for (RegistryObject<T> obj : objSet)
                {
                    textureGenerator.accept(obj.get(), generator, consumer);
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
