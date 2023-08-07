package mrp_v2.additionalcolors.api.colored_block_data;

import mrp_v2.additionalcolors.api.block_properties.IBlockPropertiesProvider;
import mrp_v2.additionalcolors.api.datagen.*;
import mrp_v2.additionalcolors.item.ColoredBlockItem;
import mrp_v2.additionalcolors.util.IColored;
import mrp_v2.mrplibrary.datagen.providers.TextureProvider;
import mrp_v2.mrplibrary.util.IModLocProvider;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;

public abstract class AbstractColoredBlockData<T extends Block & IColored> implements IModLocProvider
{
    protected final RegistryObject<? extends Block> baseBlock;
    protected final Map<DyeColor, RegistryObject<T>> blockObjectMap = new LinkedHashMap<>(22);
    protected final TagKey<Item> craftingTag;
    protected final List<TagKey<Block>> blockTagsToAddTo;
    protected final List<TagKey<Item>> itemTagsToAddTo;
    protected final TagKey<Item> craftingTagIncludingBase;
    @Nullable protected IBlockPropertiesProvider blockPropertiesProvider = null;

    public AbstractColoredBlockData(RegistryObject<? extends Block> baseBlock, IModLocProvider modLocProvider)
    {
        this.baseBlock = baseBlock;
        this.craftingTagIncludingBase = ItemTags.create(modLocProvider.modLoc(baseBlock.getId().getPath()));
        this.craftingTag = ItemTags.create(modLocProvider.modLoc(baseBlock.getId().getPath() + "_base"));
        this.blockTagsToAddTo = new ArrayList<>();
        this.itemTagsToAddTo = new ArrayList<>();
    }

    public AbstractColoredBlockData<?> setBlockPropertiesProvider(IBlockPropertiesProvider blockPropertiesProvider)
    {
        this.blockPropertiesProvider = blockPropertiesProvider;
        return this;
    }

    public AbstractColoredBlockData<?> addBlockTags(TagKey<Block> blockTag)
    {
        blockTagsToAddTo.add(blockTag);
        return this;
    }

    public AbstractColoredBlockData<?> addItemTags(TagKey<Item> itemTag)
    {
        itemTagsToAddTo.add(itemTag);
        return this;
    }

    @SafeVarargs public final AbstractColoredBlockData<?> addBlockTags(TagKey<Block>... blockTags)
    {
        Collections.addAll(blockTagsToAddTo, blockTags);
        return this;
    }

    @SafeVarargs public final AbstractColoredBlockData<?> addItemTags(TagKey<Item>... itemTags)
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
        return craftingTag.location().getNamespace();
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

    public void register(DeferredRegister<Block> blocks, DeferredRegister<Item> items, CreativeModeTab itemGroup)
    {
        for (DyeColor color : getColors())
        {
            String id = color.getName() + "_" + baseBlock.getId().getPath();
            RegistryObject<T> blockObject = blocks.register(id, () -> makeNewBlock(color));
            blockObjectMap.put(color, blockObject);
            items.register(id, () -> new ColoredBlockItem(blockObject.get(), new Item.Properties().tab(itemGroup)));
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
                ItemBlockRenderTypes.setRenderLayer(blockObject.get(), getSpecialRenderType());
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
            generator.addLootTable(blockObject.get(), generator::dropSelf);
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

    public void registerRecipes(Consumer<FinishedRecipe> consumer)
    {
        Block baseBlock = this.baseBlock.get();
        ShapelessRecipeBuilder.shapeless(baseBlock).requires(craftingTag)
                .unlockedBy("has_block", ExtendedRecipeProvider.makeHasItemCriterion(craftingTag))
                .save(consumer, modLoc("normal_crafting/" + baseBlock.asItem().getRegistryName().getPath()));
        ExtendedRecipeProvider.coloredCraftingRecipe(Ingredient.of(craftingTag), baseBlock)
                .unlockedBy("has_block", ExtendedRecipeProvider.makeHasItemCriterion(craftingTag))
                .save(consumer, modLoc("colored_crafting/" + baseBlock.asItem().getRegistryName().getPath()));
        for (RegistryObject<T> blockObject : getBlockObjects())
        {
            T block = blockObject.get();
            ShapelessRecipeBuilder.shapeless(block).requires(craftingTagIncludingBase)
                    .requires(block.getColor().getTag())
                    .unlockedBy("has_base", ExtendedRecipeProvider.makeHasItemCriterion(craftingTagIncludingBase))
                    .save(consumer, modLoc("normal_crafting/" + block.asItem().getRegistryName().getPath()));
            ExtendedRecipeProvider.coloredCraftingRecipe(Ingredient.of(craftingTagIncludingBase), block)
                    .unlockedBy("has_base", ExtendedRecipeProvider.makeHasItemCriterion(craftingTagIncludingBase))
                    .save(consumer, modLoc("colored_crafting/" + block.asItem().getRegistryName().getPath()));
        }
    }

    public void registerBlockTags(BlockTagGenerator generator)
    {
        for (TagKey<Block> tagToAddTo : blockTagsToAddTo)
        {
            TagsProvider.TagAppender<Block> tagBuilder = generator.tag(tagToAddTo);
            for (RegistryObject<T> blockObject : getBlockObjects())
            {
                tagBuilder.add(blockObject.get());
            }
        }
    }

    public void registerItemTags(ItemTagGenerator generator)
    {
        TagsProvider.TagAppender<Item> craftingTagBuilder = generator.tag(craftingTag);
        TagsProvider.TagAppender<Item> craftingTagIncludingBaseBuilder =
                generator.tag(craftingTagIncludingBase);
        for (RegistryObject<T> blockObject : getBlockObjects())
        {
            Item item = blockObject.get().asItem();
            craftingTagBuilder.add(item);
            craftingTagIncludingBaseBuilder.add(item);
        }
        craftingTagIncludingBaseBuilder.add(baseBlock.get().asItem());
        for (TagKey<Item> tagToAddTo : itemTagsToAddTo)
        {
            TagsProvider.TagAppender<Item> tagBuilder = generator.tag(tagToAddTo);
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

    protected final BlockBehaviour.Properties getBlockProperties(DyeColor color)
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
                "Colored Block Data '" + getBaseBlockLoc() + "' has no parent!");
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
