package mrp_v2.additionalcolors.util;

import mrp_v2.additionalcolors.block.IColoredBlock;
import mrp_v2.additionalcolors.datagen.*;
import mrp_v2.additionalcolors.item.ColoredBlockItem;
import mrp_v2.mrplibrary.datagen.ShapelessRecipeBuilder;
import net.minecraft.block.Block;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.TagsProvider;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
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
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ColorizedBlockData<T extends Block & IColoredBlock>
{
    private final String id;
    private final DyeColor[] colors;
    private final Function<DyeColor, Supplier<T>> blockConstructor;
    private final Function<Supplier<T>, Supplier<ColoredBlockItem>> itemConstructor;
    @Nullable private final Consumer<BlockStateGenerator> generalStateAndModelGenerator;
    private final BiConsumer<T, BlockStateGenerator> blockStateAndModelGenerator;
    private final BiConsumer<T, ItemModelGenerator> itemModelGenerator;
    private final BiConsumer<T, LootTableGenerator> lootTableGenerator;
    @Nullable private final TriConsumer<T, TextureGenerator, BiConsumer<BufferedImage, ResourceLocation>>
            textureGenerator;
    @Nullable private final BiConsumer<T, FMLClientSetupEvent> clientSetupStuff;
    private final boolean requiresTinting;
    private final ResourceLocation baseBlock;
    private final boolean required;
    private final ITag.INamedTag<Item> craftingTag;
    private final ITag.INamedTag<Block>[] blockTagsToAddTo;
    private final ITag.INamedTag<Item>[] itemTagsToAddTo;
    private final HashSet<RegistryObject<T>> objSet = new HashSet<>();

    public ColorizedBlockData(String id, DyeColor[] colors, Function<DyeColor, Supplier<T>> blockConstructor,
            Function<Supplier<T>, Supplier<ColoredBlockItem>> itemConstructor,
            @Nullable Consumer<BlockStateGenerator> generalStateAndModelGenerator,
            BiConsumer<T, BlockStateGenerator> blockStateAndModelGenerator,
            BiConsumer<T, ItemModelGenerator> itemModelGenerator, BiConsumer<T, LootTableGenerator> lootTableGenerator,
            @Nullable TriConsumer<T, TextureGenerator, BiConsumer<BufferedImage, ResourceLocation>> textureGenerator,
            @Nullable BiConsumer<T, FMLClientSetupEvent> clientSetupStuff, boolean requiresTinting,
            ResourceLocation baseBlock, boolean required, ITag.INamedTag<Item> craftingTag,
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
        this.clientSetupStuff = clientSetupStuff;
        this.textureGenerator = textureGenerator;
        this.requiresTinting = requiresTinting;
        this.baseBlock = baseBlock;
        this.required = required;
        this.craftingTag = craftingTag;
        this.blockTagsToAddTo = blockTagsToAddTo;
        this.itemTagsToAddTo = itemTagsToAddTo;
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

    public ResourceLocation getBaseBlock()
    {
        return baseBlock;
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
        if (textureGenerator != null)
        {
            for (RegistryObject<T> obj : objSet)
            {
                textureGenerator.accept(obj.get(), generator, consumer);
            }
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
                tagBuilder.add(RegistryKey.getOrCreateKey(Registry.BLOCK_KEY, baseBlock));
            } else
            {
                tagBuilder.addOptional(baseBlock);
            }
        }
    }

    public void registerItemTags(ItemTagGenerator generator)
    {
        TagsProvider.Builder<Item> recipeTagBuilder = generator.getOrCreateBuilder(this.craftingTag);
        objSet.forEach(obj -> recipeTagBuilder.add(obj.get().asItem()));
        if (required)
        {
            recipeTagBuilder.add(RegistryKey.getOrCreateKey(Registry.ITEM_KEY, baseBlock));
        } else
        {
            recipeTagBuilder.addOptional(baseBlock);
        }
        for (ITag.INamedTag<Item> tagToAddTo : itemTagsToAddTo)
        {
            TagsProvider.Builder<Item> tagBuilder = generator.getOrCreateBuilder(tagToAddTo);
            objSet.forEach(obj -> tagBuilder.add(obj.get().asItem()));
            if (required)
            {
                tagBuilder.add(RegistryKey.getOrCreateKey(Registry.ITEM_KEY, baseBlock));
            } else
            {
                tagBuilder.addOptional(baseBlock);
            }
        }
    }

    public void generateTranslations(EN_USTranslationGenerator generator)
    {
        objSet.forEach(generator::addSimpleBlock);
    }
}
