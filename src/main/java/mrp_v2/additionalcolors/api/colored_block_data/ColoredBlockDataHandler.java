package mrp_v2.additionalcolors.api.colored_block_data;

import mrp_v2.mrplibrary.datagen.DataGeneratorHelper;
import mrp_v2.mrplibrary.datagen.providers.RecipeProvider;
import mrp_v2.mrplibrary.datagen.providers.TextureProvider;
import mrp_v2.mrplibrary.util.IModLocProvider;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.loot.LootTable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class ColoredBlockDataHandler implements IModLocProvider
{
    public static final String DATA_PROVIDER_NAME_SUFFIX = ": Colored Block Data Handler";
    private final String modId;
    private final ItemGroup itemGroup;
    private final DeferredRegister<Block> blocks;
    private final DeferredRegister<Item> items;
    private final Map<ResourceLocation, AbstractColoredBlockData<?>> coloredBlockDatasMap;

    public ColoredBlockDataHandler(String modId, ItemGroup itemGroup)
    {
        this.modId = modId;
        this.itemGroup = itemGroup;
        blocks = DeferredRegister.create(ForgeRegistries.BLOCKS, this.modId);
        items = DeferredRegister.create(ForgeRegistries.ITEMS, this.modId);
        coloredBlockDatasMap = new LinkedHashMap<>();
    }

    @Override public String getModId()
    {
        return modId;
    }

    public AbstractColoredBlockData<?> getColoredBlockData(ResourceLocation baseBlockLoc)
    {
        if (!coloredBlockDatasMap.containsKey(baseBlockLoc))
        {
            throw new IllegalArgumentException(
                    "This handler does not contain a colored block data for '" + baseBlockLoc + "'");
        }
        return coloredBlockDatasMap.get(baseBlockLoc);
    }

    protected Collection<AbstractColoredBlockData<?>> getColoredBlockDatas()
    {
        return coloredBlockDatasMap.values();
    }

    public void register(IEventBus modEventBus)
    {
        modEventBus.register(this);
        blocks.register(modEventBus);
        items.register(modEventBus);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                () -> () -> modEventBus.register(new ClientColoredBlockDataHandler(this)));
    }

    protected void add(AbstractColoredBlockData<?> coloredBlockData)
    {
        coloredBlockDatasMap.put(coloredBlockData.getBaseBlockLoc(), coloredBlockData);
        coloredBlockData.register(blocks, items, itemGroup);
    }

    @SubscribeEvent public void gatherData(GatherDataEvent event)
    {
        DataGeneratorHelper helper = new DataGeneratorHelper(event, modId);
        if (event.includeClient())
        {
            helper.addTextureProvider(this::makeTextureGenerator);
            helper.addBlockStateProvider(this::makeBlockStateGenerator);
            helper.addItemModelProvider(this::makeItemModelGenerator);
            helper.addLanguageProvider(this::makeLanguageGenerator);
        }
        if (event.includeServer())
        {
            helper.addBlockTagsProvider(this::makeBlockTagGenerator);
            helper.addItemTagsProvider(this::makeItemTagGenerator);
            helper.addRecipeProvider(this::makeRecipeGenerator);
            helper.addLootTables(makeLootTableGenerator());
        }
    }

    protected TextureGenerator makeTextureGenerator(DataGenerator generator, ExistingFileHelper existingFileHelper,
            String modId)
    {
        return new TextureGenerator(generator, existingFileHelper, modId);
    }

    protected LootTableGenerator makeLootTableGenerator()
    {
        return new LootTableGenerator();
    }

    protected ItemModelGenerator makeItemModelGenerator(DataGenerator generator, String modid,
            ExistingFileHelper existingFileHelper)
    {
        return new ItemModelGenerator(generator, modid, existingFileHelper);
    }

    protected BlockStateGenerator makeBlockStateGenerator(DataGenerator gen, String modid,
            ExistingFileHelper exFileHelper)
    {
        return new BlockStateGenerator(gen, modid, exFileHelper);
    }

    protected RecipeGenerator makeRecipeGenerator(DataGenerator dataGeneratorIn, String modId)
    {
        return new RecipeGenerator(dataGeneratorIn, modId);
    }

    protected BlockTagGenerator makeBlockTagGenerator(DataGenerator generatorIn, String modId,
            @Nullable ExistingFileHelper existingFileHelper)
    {
        return new BlockTagGenerator(generatorIn, modId, existingFileHelper);
    }

    protected ItemTagGenerator makeItemTagGenerator(DataGenerator dataGenerator,
            net.minecraft.data.BlockTagsProvider blockTagProvider, String modId,
            @Nullable ExistingFileHelper existingFileHelper)
    {
        return new ItemTagGenerator(dataGenerator, blockTagProvider, modId, existingFileHelper);
    }

    protected LanguageGenerator makeLanguageGenerator(DataGenerator gen, String modid)
    {
        return new LanguageGenerator(gen, modid);
    }

    protected class TextureGenerator extends TextureProvider
    {
        public TextureGenerator(DataGenerator generator, ExistingFileHelper existingFileHelper, String modId)
        {
            super(generator, existingFileHelper, modId);
            for (AbstractColoredBlockData<?> coloredBlockData : coloredBlockDatasMap.values())
            {
                coloredBlockData.makeTextureGenerationPromises(this);
            }
        }

        @Override public String getName()
        {
            return super.getName() + DATA_PROVIDER_NAME_SUFFIX;
        }

        @Override protected void addTextures(FinishedTextureConsumer finishedTextureConsumer)
        {
            for (AbstractColoredBlockData<?> coloredBlockData : coloredBlockDatasMap.values())
            {
                coloredBlockData.registerTextures(this, finishedTextureConsumer);
            }
        }
    }

    protected class LootTableGenerator extends mrp_v2.additionalcolors.api.datagen.LootTableGenerator
    {
        public LootTableGenerator()
        {
            for (AbstractColoredBlockData<?> coloredBlockData : coloredBlockDatasMap.values())
            {
                coloredBlockData.registerLootTables(this);
            }
        }

        @Override public void add(Block blockIn, Function<Block, LootTable.Builder> factory)
        {
            super.add(blockIn, factory);
        }
    }

    protected class ItemModelGenerator extends mrp_v2.additionalcolors.api.datagen.ItemModelGenerator
    {
        public ItemModelGenerator(DataGenerator generator, String modid, ExistingFileHelper existingFileHelper)
        {
            super(generator, modid, existingFileHelper);
        }

        @Override protected void registerModels()
        {
            for (AbstractColoredBlockData<?> coloredBlockData : coloredBlockDatasMap.values())
            {
                coloredBlockData.registerItemModels(this);
            }
        }

        @Override public String getName()
        {
            return super.getName() + DATA_PROVIDER_NAME_SUFFIX;
        }
    }

    protected class BlockStateGenerator extends mrp_v2.additionalcolors.api.datagen.BlockStateGenerator
    {
        public BlockStateGenerator(DataGenerator gen, String modid, ExistingFileHelper exFileHelper)
        {
            super(gen, modid, exFileHelper);
        }

        @Override protected void registerStatesAndModels()
        {
            for (AbstractColoredBlockData<?> coloredBlockData : coloredBlockDatasMap.values())
            {
                coloredBlockData.registerBlockStatesAndModels(this);
            }
        }

        @Override public String getName()
        {
            return super.getName() + DATA_PROVIDER_NAME_SUFFIX;
        }
    }

    protected class RecipeGenerator extends RecipeProvider
    {
        public RecipeGenerator(DataGenerator dataGeneratorIn, String modId)
        {
            super(dataGeneratorIn, modId);
        }

        @Override protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer)
        {
            for (AbstractColoredBlockData<?> coloredBlockData : coloredBlockDatasMap.values())
            {
                coloredBlockData.registerRecipes(consumer);
            }
        }

        @Override public String getName()
        {
            return super.getName() + DATA_PROVIDER_NAME_SUFFIX;
        }
    }

    protected class BlockTagGenerator extends mrp_v2.additionalcolors.api.datagen.BlockTagGenerator
    {
        public BlockTagGenerator(DataGenerator generatorIn, String modId,
                @Nullable ExistingFileHelper existingFileHelper)
        {
            super(generatorIn, modId, existingFileHelper);
        }

        @Override protected void addTags()
        {
            for (AbstractColoredBlockData<?> coloredBlockData : coloredBlockDatasMap.values())
            {
                coloredBlockData.registerBlockTags(this);
            }
        }

        @Override public String getName()
        {
            return super.getName() + DATA_PROVIDER_NAME_SUFFIX;
        }
    }

    protected class ItemTagGenerator extends mrp_v2.additionalcolors.api.datagen.ItemTagGenerator
    {
        protected ItemTagGenerator(DataGenerator dataGenerator, net.minecraft.data.BlockTagsProvider blockTagProvider,
                String modId, @Nullable ExistingFileHelper existingFileHelper)
        {
            super(dataGenerator, blockTagProvider, modId, existingFileHelper);
        }

        @Override protected void addTags()
        {
            for (AbstractColoredBlockData<?> coloredBlockData : coloredBlockDatasMap.values())
            {
                coloredBlockData.registerItemTags(this);
            }
        }

        @Override public String getName()
        {
            return super.getName() + DATA_PROVIDER_NAME_SUFFIX;
        }
    }

    protected class LanguageGenerator extends mrp_v2.additionalcolors.api.datagen.LanguageGenerator
    {
        public LanguageGenerator(DataGenerator gen, String modid)
        {
            super(gen, modid, "en_us");
        }

        @Override protected void addTranslations()
        {
            super.addTranslations();
            for (AbstractColoredBlockData<?> coloredBlockData : coloredBlockDatasMap.values())
            {
                coloredBlockData.generateTranslations(this);
            }
        }

        @Override public String getName()
        {
            return super.getName() + DATA_PROVIDER_NAME_SUFFIX;
        }
    }
}
