package mrp_v2.additionalcolors.util.colored_block_data;

import mrp_v2.additionalcolors.AdditionalColors;
import mrp_v2.additionalcolors.datagen.*;
import mrp_v2.additionalcolors.item.ColoredBlockItem;
import mrp_v2.additionalcolors.util.IColored;
import mrp_v2.additionalcolors.util.ObjectHolder;
import mrp_v2.additionalcolors.util.Util;
import mrp_v2.mrplibrary.datagen.providers.TextureProvider;
import mrp_v2.mrplibrary.datagen.recipe.ShapelessRecipeBuilder;
import mrp_v2.mrplibrary.util.Possible;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.TagsProvider;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public abstract class AbstractColoredBlockData<T extends Block & IColored> implements IColoredBlockData<T>
{
    protected final Possible<Block> baseBlock;
    protected final Possible<Item> baseItem;
    protected final Map<DyeColor, RegistryObject<T>> blockObjectMap = new HashMap<>();
    protected final ITag.INamedTag<Item> craftingTag;
    protected final ITag.INamedTag<Block>[] blockTagsToAddTo;
    protected final ITag.INamedTag<Item>[] itemTagsToAddTo;
    protected final ITag.INamedTag<Item> craftingTagIncludingBase;

    @Override public Map<DyeColor, RegistryObject<T>> register()
    {
        for (DyeColor color : getColors())
        {
            String id = color.getTranslationKey() + "_" + baseBlock.getId().getPath();
            RegistryObject<T> blockObject = ObjectHolder.BLOCKS.register(id, () -> makeNewBlock(color));
            blockObjectMap.put(color, blockObject);
            ObjectHolder.ITEMS.register(id,
                    () -> new ColoredBlockItem(blockObject.get(), new Item.Properties().group(getItemGroup())));
        }
        return blockObjectMap;
    }

    protected AbstractColoredBlockData(ResourceLocation baseBlockLoc, ITag.INamedTag<Block>[] blockTagsToAddTo,
            ITag.INamedTag<Item>[] itemTagsToAddTo)
    {
        this.baseBlock = Possible.of(baseBlockLoc);
        this.baseItem = Possible.of(baseBlockLoc);
        this.craftingTagIncludingBase =
                ItemTags.createOptional(new ResourceLocation(AdditionalColors.ID, baseBlockLoc.getPath()));
        this.craftingTag =
                ItemTags.createOptional(new ResourceLocation(AdditionalColors.ID, baseBlockLoc.getPath() + "_base"));
        this.blockTagsToAddTo = blockTagsToAddTo;
        this.itemTagsToAddTo = itemTagsToAddTo;
    }

    public AbstractColoredBlockData(Block baseBlock)
    {
        this(baseBlock, Util.makeTagArray());
    }

    public AbstractColoredBlockData(Block baseBlock, ITag.INamedTag<Block>[] blockTagsToAddTo)
    {
        this(baseBlock, blockTagsToAddTo, Util.makeTagArray());
    }

    protected AbstractColoredBlockData(Block baseBlock, ITag.INamedTag<Block>[] blockTagsToAddTo,
            ITag.INamedTag<Item>[] itemTagsToAddTo)
    {
        this.baseBlock = Possible.of(baseBlock);
        this.baseItem = Possible.of(baseBlock.asItem());
        this.craftingTagIncludingBase = ItemTags.createOptional(
                new ResourceLocation(AdditionalColors.ID, baseBlock.getRegistryName().getPath()));
        this.craftingTag = ItemTags.createOptional(
                new ResourceLocation(AdditionalColors.ID, baseBlock.getRegistryName().getPath() + "_base"));
        this.blockTagsToAddTo = blockTagsToAddTo;
        this.itemTagsToAddTo = itemTagsToAddTo;
    }

    @Override public void makeTextureGenerationPromises(TextureGenerator generator)
    {
        generator.promiseGeneration(new ResourceLocation(AdditionalColors.ID, "block/" + baseBlock.getId().getPath()));
    }

    @Override public void forEachBlock(Consumer<T> consumer)
    {
        for (RegistryObject<T> blockObject : blockObjectMap.values())
        {
            consumer.accept(blockObject.get());
        }
    }

    @Override public boolean requiresTinting()
    {
        return true;
    }

    @Override public ResourceLocation getBaseBlockLoc()
    {
        return baseBlock.getId();
    }

    @Override public ResourceLocation getBaseItemLoc()
    {
        return baseItem.getId();
    }

    @Override public void clientSetup(FMLClientSetupEvent event)
    {
        if (hasSpecialRenderType())
        {
            for (RegistryObject<T> blockObject : blockObjectMap.values())
            {
                RenderTypeLookup.setRenderLayer(blockObject.get(), getSpecialRenderType());
            }
        }
    }

    @Override public void registerTextures(TextureGenerator generator, TextureProvider.FinishedTextureConsumer consumer)
    {
        BufferedImage texture = generator.getTexture(
                new ResourceLocation(baseBlock.getId().getNamespace(), "block/" + baseBlock.getId().getPath()));
        TextureProvider.makeGrayscale(texture);
        TextureProvider.adjustLevels(texture, getLevelAdjustment());
        generator.finish(texture, new ResourceLocation(AdditionalColors.ID, "block/" + baseBlock.getId().getPath()),
                consumer);
    }

    @Override public void registerItemModels(ItemModelGenerator generator)
    {
        for (RegistryObject<T> blockObject : blockObjectMap.values())
        {
            generator.withExistingParent(blockObject.getId().getPath(),
                    generator.modLoc("block/" + baseBlock.getId().getPath()));
        }
    }

    @Override public void registerLootTables(LootTableGenerator generator)
    {
        for (RegistryObject<T> blockObject : blockObjectMap.values())
        {
            generator.addLootTable(blockObject.get(), generator::registerDropSelfLootTable);
        }
    }

    @Override public void registerBlockStatesAndModels(BlockStateGenerator generator)
    {
        ResourceLocation textureLoc = generator.modLoc("block/" + baseBlock.getId().getPath());
        ModelFile modelFile = generator.tintedSimpleBlock("block/" + baseBlock.getId().getPath(), textureLoc);
        for (RegistryObject<T> blockObject : blockObjectMap.values())
        {
            generator.simpleBlock(blockObject.get(), modelFile);
        }
    }

    @Override public void registerRecipes(Consumer<IFinishedRecipe> consumer)
    {
        ShapelessRecipeBuilder.shapelessRecipe(getBaseItemLoc()).addIngredient(craftingTag)
                .addCriterion("has_block", RecipeGenerator.makeHasItemCriterion(craftingTag)).build(consumer);
        for (Map.Entry<DyeColor, RegistryObject<T>> blockObjectEntry : blockObjectMap.entrySet())
        {
            ShapelessRecipeBuilder.shapelessRecipe(blockObjectEntry.getValue().get())
                    .addIngredient(craftingTagIncludingBase).addIngredient(blockObjectEntry.getKey().getTag())
                    .addCriterion("has_base", RecipeGenerator.makeHasItemCriterion(craftingTagIncludingBase))
                    .build(consumer);
        }
    }

    @Override public void registerBlockTags(BlockTagGenerator generator)
    {
        for (ITag.INamedTag<Block> tagToAddTo : blockTagsToAddTo)
        {
            TagsProvider.Builder<Block> tagBuilder = generator.getOrCreateBuilder(tagToAddTo);
            for (RegistryObject<T> blockObject : blockObjectMap.values())
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
        for (RegistryObject<T> blockObject : blockObjectMap.values())
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
            for (RegistryObject<T> blockObject : blockObjectMap.values())
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
        for (RegistryObject<T> blockObject : blockObjectMap.values())
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

    @Override public RegistryObject<T> getBlockObject(DyeColor color)
    {
        return blockObjectMap.get(color);
    }

    @Override public Block getBaseBlock()
    {
        return baseBlock.get();
    }

    @Override public boolean doesBaseBlockAlwaysExist()
    {
        return baseBlock.exists();
    }

    protected double getLevelAdjustment()
    {
        return 0.75d;
    }

    protected boolean hasSpecialRenderType()
    {
        return false;
    }

    protected RenderType getSpecialRenderType()
    {
        throw new UnsupportedOperationException();
    }

    protected abstract T makeNewBlock(DyeColor color);

    protected AbstractBlock.Properties getBlockProperties()
    {
        return AbstractBlock.Properties.from(baseBlock.get());
    }
}
