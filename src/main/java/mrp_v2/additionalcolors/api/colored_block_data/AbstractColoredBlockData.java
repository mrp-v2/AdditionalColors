package mrp_v2.additionalcolors.api.colored_block_data;

import mrp_v2.additionalcolors.api.block_properties.IBlockPropertiesProvider;
import mrp_v2.additionalcolors.api.datagen.*;
import mrp_v2.additionalcolors.item.ColoredBlockItem;
import mrp_v2.additionalcolors.util.IColored;
import mrp_v2.mrplibrary.datagen.providers.TextureProvider;
import mrp_v2.mrplibrary.util.IModLocProvider;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.data.TagsProvider;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.DeferredRegister;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;

public abstract class AbstractColoredBlockData<T extends Block & IColored> implements IModLocProvider
{
    protected final RegistryObject<? extends Block> baseBlock;
    protected final Map<DyeColor, RegistryObject<T>> blockObjectMap = new HashMap<>();
    protected final ITag.INamedTag<Item> craftingTag;
    protected final List<ITag.INamedTag<Block>> blockTagsToAddTo;
    protected final List<ITag.INamedTag<Item>> itemTagsToAddTo;
    protected final ITag.INamedTag<Item> craftingTagIncludingBase;
    @Nullable protected IBlockPropertiesProvider blockPropertiesProvider = null;

    public AbstractColoredBlockData(RegistryObject<? extends Block> baseBlock, IModLocProvider modLocProvider)
    {
        this.baseBlock = baseBlock;
        this.craftingTagIncludingBase = ItemTags.createOptional(modLocProvider.modLoc(baseBlock.getId().getPath()));
        this.craftingTag = ItemTags.createOptional(modLocProvider.modLoc(baseBlock.getId().getPath() + "_base"));
        this.blockTagsToAddTo = new ArrayList<>();
        this.itemTagsToAddTo = new ArrayList<>();
    }

    public AbstractColoredBlockData<?> setBlockPropertiesProvider(IBlockPropertiesProvider blockPropertiesProvider)
    {
        this.blockPropertiesProvider = blockPropertiesProvider;
        return this;
    }

    public AbstractColoredBlockData<?> addBlockTags(ITag.INamedTag<Block> blockTag)
    {
        blockTagsToAddTo.add(blockTag);
        return this;
    }

    public AbstractColoredBlockData<?> addItemTags(ITag.INamedTag<Item> itemTag)
    {
        itemTagsToAddTo.add(itemTag);
        return this;
    }

    @SafeVarargs public final AbstractColoredBlockData<?> addBlockTags(ITag.INamedTag<Block>... blockTags)
    {
        Collections.addAll(blockTagsToAddTo, blockTags);
        return this;
    }

    @SafeVarargs public final AbstractColoredBlockData<?> addItemTags(ITag.INamedTag<Item>... itemTags)
    {
        Collections.addAll(itemTagsToAddTo, itemTags);
        return this;
    }

    public void makeTextureGenerationPromises(TextureProvider generator)
    {
        generator.promiseGeneration(getSideTextureLoc(false));
    }

    protected ResourceLocation getSideTextureLoc(boolean base)
    {
        return new ResourceLocation(base ? baseBlock.getId().getNamespace() : getModId(),
                "block/" + baseBlock.getId().getPath() + getSideSuffix());
    }

    @Override public String getModId()
    {
        return craftingTag.getName().getNamespace();
    }

    protected String getSideSuffix()
    {
        return "";
    }

    protected ResourceLocation getTopTextureLoc(boolean base)
    {
        return getEndTextureLoc(base);
    }

    protected ResourceLocation getEndTextureLoc(boolean base)
    {
        return getSideTextureLoc(base);
    }

    protected ResourceLocation getBottomTextureLoc(boolean base)
    {
        return getEndTextureLoc(base);
    }

    public void register(DeferredRegister<Block> blocks, DeferredRegister<Item> items, ItemGroup itemGroup)
    {
        for (DyeColor color : getColors())
        {
            String id = color.getTranslationKey() + "_" + baseBlock.getId().getPath();
            RegistryObject<T> blockObject = blocks.register(id, () -> makeNewBlock(color));
            blockObjectMap.put(color, blockObject);
            items.register(id, () -> new ColoredBlockItem(blockObject.get(), new Item.Properties().group(itemGroup)));
        }
    }

    protected DyeColor[] getColors()
    {
        return DyeColor.values();
    }

    protected abstract T makeNewBlock(DyeColor color);

    public boolean requiresTinting()
    {
        return true;
    }

    public final ResourceLocation getBaseBlockLoc()
    {
        return baseBlock.getId();
    }

    public void clientSetup(FMLClientSetupEvent event)
    {
        if (hasSpecialRenderType())
        {
            for (RegistryObject<T> blockObject : getBlockObjects())
            {
                RenderTypeLookup.setRenderLayer(blockObject.get(), getSpecialRenderType());
            }
        }
    }

    public final Collection<RegistryObject<T>> getBlockObjects()
    {
        return blockObjectMap.values();
    }

    protected boolean hasSpecialRenderType()
    {
        return false;
    }

    protected RenderType getSpecialRenderType()
    {
        throw new UnsupportedOperationException();
    }

    public void registerTextures(TextureProvider generator, TextureProvider.FinishedTextureConsumer consumer)
    {
        TextureProvider.Texture texture = generator.getTexture(getSideTextureLoc(true));
        TextureProvider.makeGrayscale(texture.getTexture());
        TextureProvider.adjustLevels(texture.getTexture(), getLevelAdjustment());
        generator.finish(texture, getSideTextureLoc(false), consumer);
    }

    protected double getLevelAdjustment()
    {
        return 0.75d;
    }

    public void registerItemModels(ItemModelGenerator generator)
    {
        for (RegistryObject<T> blockObject : getBlockObjects())
        {
            generator.withExistingParent(blockObject.getId().getPath(),
                    generator.modLoc("block/" + baseBlock.getId().getPath()));
        }
    }

    public void registerLootTables(LootTableGenerator generator)
    {
        for (RegistryObject<T> blockObject : getBlockObjects())
        {
            generator.addLootTable(blockObject.get(), generator::registerDropSelfLootTable);
        }
    }

    public void registerBlockStatesAndModels(BlockStateGenerator generator)
    {
        ModelFile modelFile = generator.cubeAllTinted("block/" + baseBlock.getId().getPath(), getSideTextureLoc(false));
        for (RegistryObject<T> blockObject : getBlockObjects())
        {
            generator.simpleBlock(blockObject.get(), modelFile);
        }
    }

    public void registerRecipes(Consumer<IFinishedRecipe> consumer)
    {
        Block baseBlock = this.baseBlock.get();
        ShapelessRecipeBuilder.shapelessRecipe(baseBlock).addIngredient(craftingTag)
                .addCriterion("has_block", ExtendedRecipeProvider.makeHasItemCriterion(craftingTag))
                .build(consumer, modLoc("normal_crafting/" + baseBlock.asItem().getRegistryName().getPath()));
        ExtendedRecipeProvider.coloredCraftingRecipe(Ingredient.fromTag(craftingTag), baseBlock)
                .addCriterion("has_block", ExtendedRecipeProvider.makeHasItemCriterion(craftingTag))
                .build(consumer, modLoc("colored_crafting/" + baseBlock.asItem().getRegistryName().getPath()));
        for (RegistryObject<T> blockObject : getBlockObjects())
        {
            T block = blockObject.get();
            ShapelessRecipeBuilder.shapelessRecipe(block).addIngredient(craftingTagIncludingBase)
                    .addIngredient(block.getColor().getTag())
                    .addCriterion("has_base", ExtendedRecipeProvider.makeHasItemCriterion(craftingTagIncludingBase))
                    .build(consumer, modLoc("normal_crafting/" + block.asItem().getRegistryName().getPath()));
            ExtendedRecipeProvider.coloredCraftingRecipe(Ingredient.fromTag(craftingTagIncludingBase), block)
                    .addCriterion("has_base", ExtendedRecipeProvider.makeHasItemCriterion(craftingTagIncludingBase))
                    .build(consumer, modLoc("colored_crafting/" + block.asItem().getRegistryName().getPath()));
        }
    }

    public void registerBlockTags(BlockTagGenerator generator)
    {
        for (ITag.INamedTag<Block> tagToAddTo : blockTagsToAddTo)
        {
            TagsProvider.Builder<Block> tagBuilder = generator.getOrCreateBuilder(tagToAddTo);
            for (RegistryObject<T> blockObject : getBlockObjects())
            {
                tagBuilder.add(blockObject.get());
            }
        }
    }

    public void registerItemTags(ItemTagGenerator generator)
    {
        TagsProvider.Builder<Item> craftingTagBuilder = generator.getOrCreateBuilder(craftingTag);
        TagsProvider.Builder<Item> craftingTagIncludingBaseBuilder =
                generator.getOrCreateBuilder(craftingTagIncludingBase);
        for (RegistryObject<T> blockObject : getBlockObjects())
        {
            Item item = blockObject.get().asItem();
            craftingTagBuilder.add(item);
            craftingTagIncludingBaseBuilder.add(item);
        }
        craftingTagIncludingBaseBuilder.add(baseBlock.get().asItem());
        for (ITag.INamedTag<Item> tagToAddTo : itemTagsToAddTo)
        {
            TagsProvider.Builder<Item> tagBuilder = generator.getOrCreateBuilder(tagToAddTo);
            for (RegistryObject<T> blockObject : getBlockObjects())
            {
                tagBuilder.add(blockObject.get().asItem());
            }
        }
    }

    public void generateTranslations(LanguageGenerator generator)
    {
        for (RegistryObject<T> blockObject : getBlockObjects())
        {
            generator.addSimpleBlock(blockObject);
        }
    }

    public final RegistryObject<T> getBlockObject(DyeColor color)
    {
        return blockObjectMap.get(color);
    }

    public Slab makeSlabBlock(RegistryObject<? extends Block> baseSlabBlock)
    {
        return new Slab(baseSlabBlock, this);
    }

    public Wall makeWallBlock(RegistryObject<? extends Block> baseWallBlock)
    {
        return new Wall(baseWallBlock, this);
    }

    public Stairs makeStairsBlock(RegistryObject<? extends Block> baseStairsBlock)
    {
        return new Stairs(baseStairsBlock, this);
    }

    public Fence makeFenceBlock(RegistryObject<? extends Block> baseFenceBlock)
    {
        return new Fence(baseFenceBlock, AbstractColoredBlockData.this);
    }

    public FenceGate makeFenceGateBlock(RegistryObject<? extends Block> baseFenceGateBlock)
    {
        return new FenceGate(baseFenceGateBlock, AbstractColoredBlockData.this);
    }

    public AbstractColoredBlockData<T> add(ColoredBlockDataHandler coloredBlockDataHandler)
    {
        coloredBlockDataHandler.add(this);
        return this;
    }

    public boolean hasParent()
    {
        return false;
    }

    protected final AbstractBlock.Properties getBlockProperties(DyeColor color)
    {
        if (blockPropertiesProvider != null)
        {
            return blockPropertiesProvider.getProperties(color);
        } else if (hasParent())
        {
            return parent().getBlockProperties(color);
        } else
        {
            throw new IllegalStateException("Colored Block Data '" + getBaseBlockLoc() +
                    "' does not have a block property provider set and has no parent!");
        }
    }

    public AbstractColoredBlockData<?> parent()
    {
        throw new UnsupportedOperationException(
                "Colored Block Data '" + getBaseBlockLoc().toString() + "' has no parent!");
    }

    public class Slab extends ColoredSlabBlockData
    {
        public Slab(RegistryObject<? extends Block> baseBlock, AbstractColoredBlockData<?> baseBlockData)
        {
            super(baseBlock, baseBlockData);
        }

        @Override public boolean hasParent()
        {
            return true;
        }

        @Override public AbstractColoredBlockData<?> parent()
        {
            return AbstractColoredBlockData.this;
        }
    }

    public class Stairs extends ColoredStairsBlockData
    {
        public Stairs(RegistryObject<? extends Block> baseBlock, AbstractColoredBlockData<?> baseBlockData)
        {
            super(baseBlock, baseBlockData);
        }

        @Override public boolean hasParent()
        {
            return true;
        }

        @Override public AbstractColoredBlockData<?> parent()
        {
            return AbstractColoredBlockData.this;
        }
    }

    public class Wall extends ColoredWallBlockData
    {
        protected Wall(RegistryObject<? extends Block> baseBlock, AbstractColoredBlockData<?> baseBlockData)
        {
            super(baseBlock, baseBlockData);
        }

        @Override public boolean hasParent()
        {
            return true;
        }

        @Override public AbstractColoredBlockData<?> parent()
        {
            return AbstractColoredBlockData.this;
        }
    }

    public class Fence extends ColoredFenceBlockData
    {
        protected Fence(RegistryObject<? extends Block> baseBlock, AbstractColoredBlockData<?> modLocProvider)
        {
            super(baseBlock, modLocProvider);
        }

        @Override public boolean hasParent()
        {
            return true;
        }

        @Override public AbstractColoredBlockData<?> parent()
        {
            return AbstractColoredBlockData.this;
        }
    }

    public class FenceGate extends ColoredFenceGateBlockData
    {
        protected FenceGate(RegistryObject<? extends Block> baseBlock, AbstractColoredBlockData<?> modLocProvider)
        {
            super(baseBlock, modLocProvider);
        }

        @Override public boolean hasParent()
        {
            return true;
        }

        @Override public AbstractColoredBlockData<?> parent()
        {
            return AbstractColoredBlockData.this;
        }
    }
}
