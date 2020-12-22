package mrp_v2.additionalcolors.util;

import mrp_v2.additionalcolors.AdditionalColors;
import mrp_v2.additionalcolors.block.ColoredBlock;
import mrp_v2.additionalcolors.datagen.*;
import mrp_v2.additionalcolors.item.ColoredBlockItem;
import mrp_v2.mrplibrary.datagen.providers.TextureProvider;
import mrp_v2.mrplibrary.datagen.recipe.ShapelessRecipeBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.TagsProvider;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Direction;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.apache.logging.log4j.util.TriConsumer;

import javax.annotation.Nullable;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.function.*;

public class ColoredBlockData<T extends Block & IColored>
{
    private final String id;
    private final DyeColor[] colors;
    private final Function<DyeColor, Supplier<T>> blockConstructor;
    private final Function<Supplier<T>, Supplier<ColoredBlockItem>> itemConstructor;
    @Nullable private final Consumer<BlockStateGenerator> generalStateAndModelGenerator;
    private final BiConsumer<T, BlockStateGenerator> blockStateAndModelGenerator;
    private final BiConsumer<T, ItemModelGenerator> itemModelGenerator;
    private final BiConsumer<T, LootTableGenerator> lootTableGenerator;
    @Nullable private final GeneralTextureGenerator generalTextureGenerator;
    @Nullable private final BlockTextureGenerator<T> blockTextureGenerator;
    @Nullable private final BiConsumer<T, FMLClientSetupEvent> clientSetupStuff;
    private final boolean requiresTinting;
    private final ResourceLocation baseBlockLoc;
    private final ResourceLocation baseItemLoc;
    private final boolean required;
    private final ITag.INamedTag<Item> craftingTag;
    private final ITag.INamedTag<Item> baseCraftingTag;
    private final ITag.INamedTag<Block>[] blockTagsToAddTo;
    private final ITag.INamedTag<Item>[] itemTagsToAddTo;
    private final HashSet<RegistryObject<T>> objSet = new HashSet<>();

    public ColoredBlockData(String id, DyeColor[] colors, Function<DyeColor, Supplier<T>> blockConstructor,
            Function<Supplier<T>, Supplier<ColoredBlockItem>> itemConstructor,
            @Nullable Consumer<BlockStateGenerator> generalStateAndModelGenerator,
            BiConsumer<T, BlockStateGenerator> blockStateAndModelGenerator,
            BiConsumer<T, ItemModelGenerator> itemModelGenerator, BiConsumer<T, LootTableGenerator> lootTableGenerator,
            @Nullable GeneralTextureGenerator generalTextureGenerator,
            @Nullable BlockTextureGenerator<T> blockTextureGenerator,
            @Nullable BiConsumer<T, FMLClientSetupEvent> clientSetupStuff, boolean requiresTinting,
            ResourceLocation baseBlockLoc, ResourceLocation baseItemLoc, boolean required)
    {
        this(id, colors, blockConstructor, itemConstructor, generalStateAndModelGenerator, blockStateAndModelGenerator,
                itemModelGenerator, lootTableGenerator, generalTextureGenerator, blockTextureGenerator,
                clientSetupStuff, requiresTinting, baseBlockLoc, baseItemLoc, required, Util.makeTagArray(),
                Util.makeTagArray());
    }

    public ColoredBlockData(String id, DyeColor[] colors, Function<DyeColor, Supplier<T>> blockConstructor,
            Function<Supplier<T>, Supplier<ColoredBlockItem>> itemConstructor,
            @Nullable Consumer<BlockStateGenerator> generalStateAndModelGenerator,
            BiConsumer<T, BlockStateGenerator> blockStateAndModelGenerator,
            BiConsumer<T, ItemModelGenerator> itemModelGenerator, BiConsumer<T, LootTableGenerator> lootTableGenerator,
            @Nullable GeneralTextureGenerator generalTextureGenerator,
            @Nullable BlockTextureGenerator<T> blockTextureGenerator,
            @Nullable BiConsumer<T, FMLClientSetupEvent> clientSetupStuff, boolean requiresTinting,
            ResourceLocation baseBlockLoc, ResourceLocation baseItemLoc, boolean required,
            ITag.INamedTag<Block>[] blockTagsToAddTo, ITag.INamedTag<Item>[] itemTagsToAddTo)
    {
        this.id = id;
        this.colors = colors;
        this.blockConstructor = blockConstructor;
        this.itemConstructor = itemConstructor;
        this.generalStateAndModelGenerator = generalStateAndModelGenerator;
        this.blockStateAndModelGenerator = blockStateAndModelGenerator;
        this.itemModelGenerator = itemModelGenerator;
        this.lootTableGenerator = lootTableGenerator;
        this.generalTextureGenerator = generalTextureGenerator;
        this.blockTextureGenerator = blockTextureGenerator;
        this.clientSetupStuff = clientSetupStuff;
        this.requiresTinting = requiresTinting;
        this.baseBlockLoc = baseBlockLoc;
        this.baseItemLoc = baseItemLoc;
        this.required = required;
        this.craftingTag = ItemTags.createOptional(new ResourceLocation(AdditionalColors.ID, baseBlockLoc.getPath()));
        this.baseCraftingTag =
                ItemTags.createOptional(new ResourceLocation(AdditionalColors.ID, baseBlockLoc.getPath() + "_base"));
        this.blockTagsToAddTo = blockTagsToAddTo;
        this.itemTagsToAddTo = itemTagsToAddTo;
    }

    public void makeTextureGenerationPromises(TextureGenerator generator)
    {
        if (generalTextureGenerator != null)
        {
            generalTextureGenerator.makeTextureGenerationPromises(generator);
        }
        if (blockTextureGenerator != null)
        {
            for (RegistryObject<T> obj : objSet)
            {
                blockTextureGenerator.makeTextureGenerationPromises(obj.get(), generator);
            }
        }
    }

    public Map<DyeColor, RegistryObject<T>> register()
    {
        Map<DyeColor, RegistryObject<T>> map = new HashMap<>();
        for (DyeColor color : colors)
        {
            String id = color.getTranslationKey() + "_" + this.id;
            RegistryObject<T> blockObj = ObjectHolder.BLOCKS.register(id, blockConstructor.apply(color));
            objSet.add(blockObj);
            ObjectHolder.ITEMS.register(id, itemConstructor.apply(blockObj));
            map.put(color, blockObj);
        }
        return map;
    }

    public boolean requiresTinting()
    {
        return requiresTinting;
    }

    public void forEachBlock(Consumer<T> consumer)
    {
        for (RegistryObject<T> obj : objSet)
        {
            consumer.accept(obj.get());
        }
    }

    public ResourceLocation getBaseBlockLoc()
    {
        return baseBlockLoc;
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

    public void registerTextures(TextureGenerator generator, BiConsumer<BufferedImage, ResourceLocation> consumer)
    {
        if (generalTextureGenerator != null)
        {
            generalTextureGenerator.generateTextures(generator, consumer);
        }
        if (blockTextureGenerator != null)
        {
            for (RegistryObject<T> obj : objSet)
            {
                blockTextureGenerator.generateTextures(obj.get(), generator, consumer);
            }
        }
    }

    public static class GeneralTextureGenerator
    {
        private final Consumer<TextureGenerator> textureGenerationPromiser;
        private final BiConsumer<TextureGenerator, BiConsumer<BufferedImage, ResourceLocation>> textureGenerator;

        public GeneralTextureGenerator(Consumer<TextureGenerator> textureGenerationPromiser,
                BiConsumer<TextureGenerator, BiConsumer<BufferedImage, ResourceLocation>> textureGenerator)
        {
            this.textureGenerationPromiser = textureGenerationPromiser;
            this.textureGenerator = textureGenerator;
        }

        private void makeTextureGenerationPromises(TextureGenerator generator)
        {
            textureGenerationPromiser.accept(generator);
        }

        private void generateTextures(TextureGenerator generator, BiConsumer<BufferedImage, ResourceLocation> consumer)
        {
            textureGenerator.accept(generator, consumer);
        }
    }

    public static class BlockTextureGenerator<T extends Block & IColored>
    {
        private final BiConsumer<T, TextureGenerator> textureGenerationPromiser;
        private final TriConsumer<T, TextureGenerator, BiConsumer<BufferedImage, ResourceLocation>> textureGenerator;

        public BlockTextureGenerator(BiConsumer<T, TextureGenerator> textureGenerationPromiser,
                TriConsumer<T, TextureGenerator, BiConsumer<BufferedImage, ResourceLocation>> textureGenerator)
        {
            this.textureGenerationPromiser = textureGenerationPromiser;
            this.textureGenerator = textureGenerator;
        }

        private void makeTextureGenerationPromises(T block, TextureGenerator generator)
        {
            textureGenerationPromiser.accept(block, generator);
        }

        private void generateTextures(T block, TextureGenerator generator,
                BiConsumer<BufferedImage, ResourceLocation> consumer)
        {
            textureGenerator.accept(block, generator, consumer);
        }
    }

    public void registerItemModels(ItemModelGenerator generator)
    {
        for (RegistryObject<T> obj : objSet)
        {
            itemModelGenerator.accept(obj.get(), generator);
        }
    }

    public void registerLootTables(LootTableGenerator generator)
    {
        for (RegistryObject<T> obj : objSet)
        {
            lootTableGenerator.accept(obj.get(), generator);
        }
    }

    public void registerBlockStatesAndModels(BlockStateGenerator generator)
    {
        if (generalStateAndModelGenerator != null)
        {
            generalStateAndModelGenerator.accept(generator);
        }
        for (RegistryObject<T> obj : objSet)
        {
            blockStateAndModelGenerator.accept(obj.get(), generator);
        }
    }

    public void registerRecipes(Consumer<IFinishedRecipe> consumer)
    {
        ShapelessRecipeBuilder.shapelessRecipe(baseItemLoc).addIngredient(baseCraftingTag)
                .addCriterion("has_crafted", RecipeGenerator.makeHasItemCriterion(baseCraftingTag)).build(consumer);
        for (RegistryObject<T> obj : objSet)
        {
            ShapelessRecipeBuilder.shapelessRecipe(obj.get()).addIngredient(craftingTag)
                    .addIngredient(obj.get().getColor().getTag())
                    .addCriterion("has_base", RecipeGenerator.makeHasItemCriterion(craftingTag)).build(consumer);
        }
    }

    public void registerBlockTags(BlockTagGenerator generator)
    {
        for (ITag.INamedTag<Block> tagToAddTo : blockTagsToAddTo)
        {
            TagsProvider.Builder<Block> tagBuilder = generator.getOrCreateBuilder(tagToAddTo);
            objSet.forEach(obj -> tagBuilder.add(obj.get()));
            if (required)
            {
                tagBuilder.add(RegistryKey.getOrCreateKey(Registry.BLOCK_KEY, baseBlockLoc));
            } else
            {
                tagBuilder.addOptional(baseBlockLoc);
            }
        }
    }

    public void registerItemTags(ItemTagGenerator generator)
    {
        TagsProvider.Builder<Item> craftingTagBuilder = generator.getOrCreateBuilder(this.craftingTag);
        TagsProvider.Builder<Item> baseCraftingTagBuilder = generator.getOrCreateBuilder(this.baseCraftingTag);
        objSet.forEach((obj) -> craftingTagBuilder.add(obj.get().asItem()));
        objSet.forEach((obj) -> baseCraftingTagBuilder.add(obj.get().asItem()));
        if (required)
        {
            craftingTagBuilder.add(RegistryKey.getOrCreateKey(Registry.ITEM_KEY, baseItemLoc));
        } else
        {
            craftingTagBuilder.addOptional(baseItemLoc);
        }
        for (ITag.INamedTag<Item> tagToAddTo : itemTagsToAddTo)
        {
            TagsProvider.Builder<Item> tagBuilder = generator.getOrCreateBuilder(tagToAddTo);
            objSet.forEach(obj -> tagBuilder.add(obj.get().asItem()));
            if (required)
            {
                tagBuilder.add(RegistryKey.getOrCreateKey(Registry.ITEM_KEY, baseItemLoc));
            } else
            {
                tagBuilder.addOptional(baseItemLoc);
            }
        }
    }

    public void generateTranslations(EN_USTranslationGenerator generator)
    {
        objSet.forEach(generator::addSimpleBlock);
    }

    public static class TypedBasic<U extends Block & IColored> extends ColoredBlockData<U>
    {
        public TypedBasic(Block baseBlock, BiFunction<DyeColor, AbstractBlock.Properties, U> blockConstructor,
                @Nullable BiConsumer<U, FMLClientSetupEvent> clientSetupStuff,
                ITag.INamedTag<Block>[] additionalBlockTags)
        {
            this(baseBlock, blockConstructor, additionalBlockTags, Util.makeTagArray(), clientSetupStuff);
        }

        public TypedBasic(Block baseBlock, BiFunction<DyeColor, AbstractBlock.Properties, U> blockConstructor,
                ITag.INamedTag<Block>[] additionalBlockTags, ITag.INamedTag<Item>[] additionalItemTags,
                @Nullable BiConsumer<U, FMLClientSetupEvent> clientSetupStuff)
        {
            this(baseBlock, blockConstructor, (generator ->
            {
                ResourceLocation textureLoc = generator.modLoc("block/" + baseBlock.getRegistryName().getPath());
                generator.models().getBuilder(baseBlock.getRegistryName().getPath())
                        .parent(generator.models().getExistingFile(generator.mcLoc("block/block")))
                        .texture("all", textureLoc).texture("particle", textureLoc).element().from(0, 0, 0)
                        .to(16, 16, 16)
                        .allFaces((face, faceBuilder) -> faceBuilder.tintindex(0).texture("#all").cullface(face).end())
                        .end();
            }), new GeneralTextureGenerator((generator) -> generator.promiseGeneration(
                    new ResourceLocation(AdditionalColors.ID, "block/" + baseBlock.getRegistryName().getPath())),
                    (generator, consumer) ->
                    {
                        BufferedImage texture = generator.getTexture(
                                new ResourceLocation(baseBlock.getRegistryName().getNamespace(),
                                        "block/" + baseBlock.getRegistryName().getPath()));
                        TextureProvider.makeGrayscale(texture, 0, 0, 16, 16);
                        TextureProvider.adjustLevels(texture, 0, 0, 16, 16, 0.75d);
                        generator.finish(texture, new ResourceLocation(AdditionalColors.ID,
                                "block/" + baseBlock.getRegistryName().getPath()), consumer);
                    }), null, clientSetupStuff, additionalBlockTags, additionalItemTags);
        }

        private TypedBasic(Block baseBlock, BiFunction<DyeColor, AbstractBlock.Properties, U> blockConstructor,
                @Nullable Consumer<BlockStateGenerator> generalStateAndModelGenerator,
                @Nullable GeneralTextureGenerator generalTextureGenerator,
                @Nullable BlockTextureGenerator<U> blockTextureGenerator,
                @Nullable BiConsumer<U, FMLClientSetupEvent> clientSetupStuff,
                ITag.INamedTag<Block>[] additionalBlockTags, ITag.INamedTag<Item>[] additionalItemTags)
        {
            super(baseBlock.getRegistryName().getPath(), DyeColor.values(),
                    (color) -> () -> blockConstructor.apply(color, AbstractBlock.Properties.from(baseBlock)),
                    (blockSupplier) -> () -> new ColoredBlockItem(blockSupplier.get(),
                            new Item.Properties().group(ObjectHolder.MAIN_ITEM_GROUP)), generalStateAndModelGenerator,
                    (block, generator) -> generator.simpleBlock(block, generator.models()
                            .getExistingFile(generator.modLoc("block/" + baseBlock.getRegistryName().getPath()))),
                    (block, generator) -> generator.withExistingParent(block.getRegistryName().getPath(),
                            generator.modLoc("block/" + baseBlock.getRegistryName().getPath())),
                    (block, generator) -> generator.addLootTable(block, generator::registerDropSelfLootTable),
                    generalTextureGenerator, blockTextureGenerator, clientSetupStuff, true, baseBlock.getRegistryName(),
                    baseBlock.asItem().getRegistryName(), true, additionalBlockTags, additionalItemTags);
        }

        private TypedBasic(Block baseBlock, BiFunction<DyeColor, AbstractBlock.Properties, U> blockConstructor,
                @Nullable Consumer<BlockStateGenerator> generalStateAndModelGenerator,
                @Nullable GeneralTextureGenerator generalTextureGenerator,
                @Nullable BlockTextureGenerator<U> blockTextureGenerator, ITag.INamedTag<Block>[] additionalBlockTags,
                ITag.INamedTag<Item>[] additionalItemTags)
        {
            this(baseBlock, blockConstructor, generalStateAndModelGenerator, generalTextureGenerator,
                    blockTextureGenerator, null, additionalBlockTags, additionalItemTags);
        }

        public TypedBasic(Block baseBlock, BiFunction<DyeColor, AbstractBlock.Properties, U> blockConstructor,
                ITag.INamedTag<Block>[] additionalBlockTags)
        {
            this(baseBlock, blockConstructor, additionalBlockTags, Util.makeTagArray());
        }

        public TypedBasic(Block baseBlock, BiFunction<DyeColor, AbstractBlock.Properties, U> blockConstructor,
                ITag.INamedTag<Block>[] additionalBlockTags, ITag.INamedTag<Item>[] additionalItemTags)
        {
            this(baseBlock, blockConstructor, additionalBlockTags, additionalItemTags, null);
        }
    }

    public static class Basic extends TypedBasic<ColoredBlock>
    {
        public Basic(Block baseBlock, ITag.INamedTag<Block>[] additionalBlockTags,
                ITag.INamedTag<Item>[] additionalItemTags)
        {
            this(baseBlock, 0.75d, additionalBlockTags, additionalItemTags);
        }

        public Basic(Block baseBlock)
        {
            this(baseBlock, Util.makeTagArray());
        }

        public Basic(Block baseBlock, ITag.INamedTag<Block>[] additionalBlockTags)
        {
            this(baseBlock, additionalBlockTags, Util.makeTagArray());
        }

        public Basic(Block baseBlock, double textureLevelAdjustment, ITag.INamedTag<Block>[] additionalBlockTags,
                ITag.INamedTag<Item>[] additionalItemTags)
        {
            super(baseBlock, ColoredBlock::new, (generator ->
            {
                ResourceLocation textureLoc = generator.modLoc("block/" + baseBlock.getRegistryName().getPath());
                generator.models().getBuilder(baseBlock.getRegistryName().getPath())
                        .parent(generator.models().getExistingFile(generator.mcLoc("block/block")))
                        .texture("all", textureLoc).texture("particle", textureLoc).element().from(0, 0, 0)
                        .to(16, 16, 16)
                        .allFaces((face, faceBuilder) -> faceBuilder.tintindex(0).texture("#all").cullface(face).end())
                        .end();
            }), new GeneralTextureGenerator((generator) -> generator.promiseGeneration(
                    new ResourceLocation(AdditionalColors.ID, "block/" + baseBlock.getRegistryName().getPath())),
                    (generator, consumer) ->
                    {
                        BufferedImage texture = generator.getTexture(
                                new ResourceLocation(baseBlock.getRegistryName().getNamespace(),
                                        "block/" + baseBlock.getRegistryName().getPath()));
                        TextureProvider.makeGrayscale(texture, 0, 0, 16, 16);
                        TextureProvider.adjustLevels(texture, 0, 0, 16, 16, textureLevelAdjustment);
                        generator.finish(texture, new ResourceLocation(AdditionalColors.ID,
                                "block/" + baseBlock.getRegistryName().getPath()), consumer);
                    }), null, additionalBlockTags, additionalItemTags);
        }

        public Basic(Block baseBlock, @Nullable Consumer<BlockStateGenerator> generalStateAndModelGenerator,
                @Nullable GeneralTextureGenerator generalTextureGenerator,
                @Nullable BlockTextureGenerator<ColoredBlock> blockTextureGenerator,
                ITag.INamedTag<Block>[] additionalBlockTags, ITag.INamedTag<Item>[] additionalItemTags)
        {
            super(baseBlock, ColoredBlock::new, generalStateAndModelGenerator, generalTextureGenerator,
                    blockTextureGenerator, additionalBlockTags, additionalItemTags);
        }
    }

    public static class BottomTop extends Basic
    {
        public BottomTop(Block baseBlock, ITag.INamedTag<Block>[] additionalBlockTags,
                ITag.INamedTag<Item>[] additionalItemTags)
        {
            super(baseBlock, (generator) -> generator.models().getBuilder(baseBlock.getRegistryName().getPath())
                            .parent(generator.models().getExistingFile(generator.mcLoc("block/block")))
                            .texture("top", generator.modLoc("block/" + baseBlock.getRegistryName().getPath() + "_top"))
                            .texture("bottom", generator.modLoc("block/" + baseBlock.getRegistryName().getPath() + "_bottom"))
                            .texture("side", generator.modLoc("block/" + baseBlock.getRegistryName().getPath())).element()
                            .from(0, 0, 0).to(16, 16, 16)
                            .allFaces((face, builder) -> builder.texture("#side").cullface(face).tintindex(0).end())
                            .face(Direction.UP).texture("#top").end().face(Direction.DOWN).texture("#bottom").end().end(),
                    new GeneralTextureGenerator((generator) ->
                    {
                        generator.promiseGeneration(new ResourceLocation(AdditionalColors.ID,
                                "block/" + baseBlock.getRegistryName().getPath()));
                        generator.promiseGeneration(new ResourceLocation(AdditionalColors.ID,
                                "block/" + baseBlock.getRegistryName().getPath() + "_top"));
                        generator.promiseGeneration(new ResourceLocation(AdditionalColors.ID,
                                "block/" + baseBlock.getRegistryName().getPath() + "_bottom"));
                    }, (generator, consumer) ->
                    {
                        BufferedImage topTexture = generator.getTexture(
                                new ResourceLocation(baseBlock.getRegistryName().getNamespace(),
                                        "block/" + baseBlock.getRegistryName().getPath() + "_top"));
                        TextureGenerator.makeGrayscale(topTexture, 0, 0, 16, 16);
                        TextureGenerator.adjustLevels(topTexture, 0, 0, 16, 16, 0.5d);
                        generator.finish(topTexture, new ResourceLocation(AdditionalColors.ID,
                                "block/" + baseBlock.getRegistryName().getPath() + "_top"), consumer);
                        BufferedImage sideTexture = generator.getTexture(
                                new ResourceLocation(baseBlock.getRegistryName().getNamespace(),
                                        "block/" + baseBlock.getRegistryName().getPath()));
                        TextureGenerator.makeGrayscale(sideTexture, 0, 0, 16, 16);
                        TextureGenerator.adjustLevels(sideTexture, 0, 0, 16, 16, 0.5d);
                        generator.finish(sideTexture, new ResourceLocation(AdditionalColors.ID,
                                "block/" + baseBlock.getRegistryName().getPath()), consumer);
                        BufferedImage bottomTexture = generator.getTexture(
                                new ResourceLocation(baseBlock.getRegistryName().getNamespace(),
                                        "block/" + baseBlock.getRegistryName().getPath() + "_bottom"));
                        TextureGenerator.makeGrayscale(bottomTexture, 0, 0, 16, 16);
                        TextureGenerator.adjustLevels(bottomTexture, 0, 0, 16, 16, 0.5d);
                        generator.finish(bottomTexture, new ResourceLocation(AdditionalColors.ID,
                                "block/" + baseBlock.getRegistryName().getPath() + "_bottom"), consumer);
                    }), null, additionalBlockTags, additionalItemTags);
        }
    }

    public static class VerticalPillar extends Basic
    {
        public VerticalPillar(Block baseBlock, ITag.INamedTag<Block>[] additionalBlockTags,
                ITag.INamedTag<Item>[] additionalItemTags)
        {
            this(baseBlock, "_top", "_side", additionalBlockTags, additionalItemTags);
        }

        public VerticalPillar(Block baseBlock, String endSuffix, String sideSuffix)
        {
            this(baseBlock, endSuffix, sideSuffix, Util.makeTagArray(), Util.makeTagArray());
        }

        public VerticalPillar(Block baseBlock, String endSuffix, String sideSuffix,
                ITag.INamedTag<Block>[] additionalBlockTags, ITag.INamedTag<Item>[] additionalItemTags)
        {
            super(baseBlock, (generator) -> generator.models().getBuilder(baseBlock.getRegistryName().getPath())
                            .parent(generator.models().getExistingFile(generator.mcLoc("block/block")))
                            .texture("end", generator.modLoc("block/" + baseBlock.getRegistryName().getPath() + "_end"))
                            .texture("side", generator.modLoc("block/" + baseBlock.getRegistryName().getPath() + "_side"))
                            .texture("particle", generator.modLoc("block/" + baseBlock.getRegistryName().getPath() + "_side"))
                            .element().from(0, 0, 0).to(16, 16, 16).allFaces((face, faceBuilder) -> faceBuilder.tintindex(0)
                                    .texture(face.getAxis() == Direction.Axis.Y ? "#end" : "#side").cullface(face).end()).end(),
                    new GeneralTextureGenerator((generator) ->
                    {
                        generator.promiseGeneration(new ResourceLocation(AdditionalColors.ID,
                                "block/" + baseBlock.getRegistryName().getPath() + "_end"));
                        generator.promiseGeneration(new ResourceLocation(AdditionalColors.ID,
                                "block/" + baseBlock.getRegistryName().getPath() + "_side"));
                    }, (generator, consumer) ->
                    {
                        BufferedImage endTexture =
                                generator.getTexture(new ResourceLocation(baseBlock.getRegistryName().getNamespace(),
                                        "block/" + baseBlock.getRegistryName().getPath() + endSuffix));
                        TextureProvider.makeGrayscale(endTexture, 0, 0, 16, 16);
                        TextureProvider.adjustLevels(endTexture, 0, 0, 16, 16, 0.5d);
                        generator.finish(endTexture, new ResourceLocation(AdditionalColors.ID,
                                "block/" + baseBlock.getRegistryName().getPath() + "_end"), consumer);
                        BufferedImage sideTexture = generator.getTexture(
                                new ResourceLocation(baseBlock.getRegistryName().getNamespace(),
                                        "block/" + baseBlock.getRegistryName().getPath() + sideSuffix));
                        TextureProvider.makeGrayscale(sideTexture, 0, 0, 16, 16);
                        TextureProvider.adjustLevels(sideTexture, 0, 0, 16, 16, 0.5d);
                        generator.finish(sideTexture, new ResourceLocation(AdditionalColors.ID,
                                "block/" + baseBlock.getRegistryName().getPath() + "_side"), consumer);
                    }), null, additionalBlockTags, additionalItemTags);
        }
    }
}
