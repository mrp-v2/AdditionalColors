package mrp_v2.additionalcolors.util;

import mrp_v2.additionalcolors.AdditionalColors;
import mrp_v2.additionalcolors.api.block_properties.BlockBasedPropertiesProvider;
import mrp_v2.additionalcolors.api.colored_block_data.*;
import mrp_v2.additionalcolors.api.datagen.ExtendedRecipeProvider;
import mrp_v2.additionalcolors.block.ColoredCraftingTableBlock;
import mrp_v2.additionalcolors.block.ColoredIceBlock;
import mrp_v2.additionalcolors.block.ColoredSoulSandBlock;
import mrp_v2.additionalcolors.inventory.container.ColoredWorkbenchContainer;
import mrp_v2.additionalcolors.item.crafting.ColoredCraftingRecipe;
import mrp_v2.additionalcolors.particle.ColorParticleData;
import mrp_v2.additionalcolors.util.colored_block_data.CryingObsidianBlockData;
import mrp_v2.mrplibrary.datagen.providers.TextureProvider;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.*;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.SingleItemRecipe;
import net.minecraft.particles.ParticleType;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Consumer;

public class ObjectHolder
{
    public static final DeferredRegister<Block> BLOCKS;
    public static final DeferredRegister<Item> ITEMS;
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES;
    public static final DeferredRegister<ContainerType<?>> CONTAINER_TYPES;
    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS;
    public static final RegistryObject<Block> COLORED_CRAFTING_TABLE;
    public static final RegistryObject<ParticleType<ColorParticleData>> COLORED_DRIPPING_OBSIDIAN_TEAR_PARTICLE_TYPE;
    public static final RegistryObject<ParticleType<ColorParticleData>> COLORED_FALLING_OBSIDIAN_TEAR_PARTICLE_TYPE;
    public static final RegistryObject<ParticleType<ColorParticleData>> COLORED_LANDING_OBSIDIAN_TEAR_PARTICLE_TYPE;
    public static final RegistryObject<ContainerType<ColoredWorkbenchContainer>> COLORED_WORKBENCH_CONTAINER_TYPE;
    public static final IRecipeType<ColoredCraftingRecipe> COLORED_CRAFTING_RECIPE_TYPE =
            ColoredCraftingRecipe.createRecipeType();
    public static final RegistryObject<IRecipeSerializer<ColoredCraftingRecipe>> COLORED_CRAFTING_RECIPE_SERIALIZER;
    public static final ColoredBlockDataHandler COLORED_BLOCK_DATA_HANDLER;
    public static final ItemGroup MAIN_ITEM_GROUP = new ItemGroup(AdditionalColors.ID)
    {
        private final RegistryObject<Item> iconItem = RegistryObject
                .of(new ResourceLocation(AdditionalColors.ID, "green_cobblestone"), ForgeRegistries.ITEMS);

        @Override public ItemStack createIcon()
        {
            return new ItemStack(iconItem.get());
        }
    };

    static
    {
        BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, AdditionalColors.ID);
        ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, AdditionalColors.ID);
        PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, AdditionalColors.ID);
        CONTAINER_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, AdditionalColors.ID);
        RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, AdditionalColors.ID);
        COLORED_CRAFTING_TABLE = BLOCKS.register(ColoredCraftingTableBlock.ID,
                () -> new ColoredCraftingTableBlock(AbstractBlock.Properties.from(Blocks.CRAFTING_TABLE)));
        ITEMS.register(ColoredCraftingTableBlock.ID,
                () -> new BlockItem(COLORED_CRAFTING_TABLE.get(), new Item.Properties().group(MAIN_ITEM_GROUP)));
        COLORED_DRIPPING_OBSIDIAN_TEAR_PARTICLE_TYPE = PARTICLE_TYPES.register("colored_dripping_obsidian_tear",
                () -> ColorParticleData.createParticleType(ColorParticleData.DrippingObsidianTear::new));
        COLORED_FALLING_OBSIDIAN_TEAR_PARTICLE_TYPE = PARTICLE_TYPES.register("colored_falling_obsidian_tear",
                () -> ColorParticleData.createParticleType(ColorParticleData.FallingObsidianTear::new));
        COLORED_LANDING_OBSIDIAN_TEAR_PARTICLE_TYPE = PARTICLE_TYPES.register("colored_landing_obsidian_tear",
                () -> ColorParticleData.createParticleType(ColorParticleData.LandingObsidianTear::new));
        COLORED_CRAFTING_RECIPE_SERIALIZER = RECIPE_SERIALIZERS
                .register("crafting_colored", () -> new SingleItemRecipe.Serializer<>(ColoredCraftingRecipe::new));
        COLORED_BLOCK_DATA_HANDLER = new ColoredBlockDataHandler(AdditionalColors.ID, MAIN_ITEM_GROUP)
        {
            @Override protected LanguageGenerator makeLanguageGenerator(DataGenerator gen, String modid)
            {
                return new LanguageGenerator(gen, modid)
                {
                    @Override protected void addTranslations()
                    {
                        ColoredWorkbenchContainer.init();
                        super.addTranslations();
                        add(ObjectHolder.MAIN_ITEM_GROUP, AdditionalColors.DISPLAY_NAME);
                        addBlock(ObjectHolder.COLORED_CRAFTING_TABLE, "Colored Crafting Table");
                    }
                };
            }

            @Override protected RecipeGenerator makeRecipeGenerator(DataGenerator dataGeneratorIn, String modId)
            {
                return new RecipeGenerator(dataGeneratorIn, modId)
                {
                    @Override protected void registerRecipes(Consumer<IFinishedRecipe> consumer)
                    {
                        super.registerRecipes(consumer);
                        ShapedRecipeBuilder.shapedRecipe(COLORED_CRAFTING_TABLE.get()).patternLine("RGB")
                                .patternLine(" C ").patternLine("D W").key('R', Tags.Items.DYES_RED)
                                .key('G', Tags.Items.DYES_GREEN).key('B', Tags.Items.DYES_BLUE)
                                .key('C', Items.CRAFTING_TABLE).key('D', Tags.Items.DYES_BLACK)
                                .key('W', Tags.Items.DYES_WHITE).addCriterion("has_crafting_table",
                                ExtendedRecipeProvider.makeHasItemCriterion(Items.CRAFTING_TABLE))
                                .addCriterion("has_dye", ExtendedRecipeProvider.makeHasItemCriterion(Tags.Items.DYES))
                                .build(consumer);
                    }
                };
            }
        };
        COLORED_WORKBENCH_CONTAINER_TYPE =
                CONTAINER_TYPES.register("colored_workbench", ColoredWorkbenchContainer::createContainerType);
        addColorizedBlockDatas();
    }

    private static void addColorizedBlockDatas()
    {
        // Obsidian
        new ColoredBlockData(Util.makeRegistryObject(Blocks.OBSIDIAN), COLORED_BLOCK_DATA_HANDLER)
        {
            @Override protected double getLevelAdjustment()
            {
                return 0.5d;
            }
        }.setBlockPropertiesProvider(new BlockBasedPropertiesProvider(Blocks.OBSIDIAN))
                .addBlockTags(Tags.Blocks.OBSIDIAN).addItemTags(Tags.Items.OBSIDIAN).add(COLORED_BLOCK_DATA_HANDLER);
        // Cobblestone (Slab, Stairs, Wall)
        new ColoredBlockData(Util.makeRegistryObject(Blocks.COBBLESTONE), COLORED_BLOCK_DATA_HANDLER)
                .setBlockPropertiesProvider(new BlockBasedPropertiesProvider(Blocks.COBBLESTONE))
                .addBlockTags(Tags.Blocks.COBBLESTONE).addItemTags(Tags.Items.COBBLESTONE)
                .add(COLORED_BLOCK_DATA_HANDLER).makeSlabBlock(Util.makeRegistryObject(Blocks.COBBLESTONE_SLAB))
                .add(COLORED_BLOCK_DATA_HANDLER).parent()
                .makeStairsBlock(Util.makeRegistryObject(Blocks.COBBLESTONE_STAIRS)).add(COLORED_BLOCK_DATA_HANDLER)
                .parent().makeWallBlock(Util.makeRegistryObject(Blocks.COBBLESTONE_WALL))
                .add(COLORED_BLOCK_DATA_HANDLER).parent();
        // Stone (Slab, Stairs)
        new ColoredBlockData(Util.makeRegistryObject(Blocks.STONE), COLORED_BLOCK_DATA_HANDLER)
                .setBlockPropertiesProvider(new BlockBasedPropertiesProvider(Blocks.STONE))
                .addBlockTags(Tags.Blocks.STONE).addItemTags(Tags.Items.STONE).add(COLORED_BLOCK_DATA_HANDLER)
                .makeSlabBlock(Util.makeRegistryObject(Blocks.STONE_SLAB)).add(COLORED_BLOCK_DATA_HANDLER).parent()
                .makeStairsBlock(Util.makeRegistryObject(Blocks.STONE_STAIRS)).add(COLORED_BLOCK_DATA_HANDLER).parent();
        // Smooth Stone (Slab)
        new ColoredBlockData(Util.makeRegistryObject(Blocks.SMOOTH_STONE), COLORED_BLOCK_DATA_HANDLER)
                .setBlockPropertiesProvider(new BlockBasedPropertiesProvider(Blocks.SMOOTH_STONE))
                .add(COLORED_BLOCK_DATA_HANDLER).makeSlabBlock(Util.makeRegistryObject(Blocks.SMOOTH_STONE_SLAB))
                .add(COLORED_BLOCK_DATA_HANDLER).parent();
        // Quartz Block (Slab, Stairs)
        new VerticalPillarBlockData(Util.makeRegistryObject(Blocks.QUARTZ_BLOCK), COLORED_BLOCK_DATA_HANDLER)
        {
            @Override protected String getSideSuffix()
            {
                return "_side";
            }
        }.setBlockPropertiesProvider(new BlockBasedPropertiesProvider(Blocks.QUARTZ_BLOCK))
                .addBlockTags(Tags.Blocks.STORAGE_BLOCKS_QUARTZ).addItemTags(Tags.Items.STORAGE_BLOCKS_QUARTZ)
                .add(COLORED_BLOCK_DATA_HANDLER).makeSlabBlock(Util.makeRegistryObject(Blocks.QUARTZ_SLAB))
                .add(COLORED_BLOCK_DATA_HANDLER).parent().makeStairsBlock(Util.makeRegistryObject(Blocks.QUARTZ_STAIRS))
                .add(COLORED_BLOCK_DATA_HANDLER).parent();
        // Smooth Quartz (Slab, Stairs)
        new ColoredBlockData(Util.makeRegistryObject(Blocks.SMOOTH_QUARTZ), COLORED_BLOCK_DATA_HANDLER)
        {
            @Override protected ResourceLocation getSideTextureLoc(boolean base)
            {
                return new ResourceLocation(base ? baseBlock.getId().getNamespace() : getModId(),
                        "block/quartz_block_bottom");
            }
        }.setBlockPropertiesProvider(new BlockBasedPropertiesProvider(Blocks.SMOOTH_QUARTZ))
                .add(COLORED_BLOCK_DATA_HANDLER).makeSlabBlock(Util.makeRegistryObject(Blocks.SMOOTH_QUARTZ_SLAB))
                .add(COLORED_BLOCK_DATA_HANDLER).parent()
                .makeStairsBlock(Util.makeRegistryObject(Blocks.SMOOTH_QUARTZ_STAIRS)).add(COLORED_BLOCK_DATA_HANDLER)
                .parent();
        // Blackstone (Slab, Stairs, Wall)
        new VerticalPillarBlockData(Util.makeRegistryObject(Blocks.BLACKSTONE), COLORED_BLOCK_DATA_HANDLER)
                .setBlockPropertiesProvider(new BlockBasedPropertiesProvider(Blocks.BLACKSTONE))
                .add(COLORED_BLOCK_DATA_HANDLER).makeSlabBlock(Util.makeRegistryObject(Blocks.BLACKSTONE_SLAB))
                .add(COLORED_BLOCK_DATA_HANDLER).parent()
                .makeStairsBlock(Util.makeRegistryObject(Blocks.BLACKSTONE_STAIRS)).add(COLORED_BLOCK_DATA_HANDLER)
                .parent().makeWallBlock(Util.makeRegistryObject(Blocks.BLACKSTONE_WALL)).add(COLORED_BLOCK_DATA_HANDLER)
                .parent();
        // Sandstone (Slab, Stairs, Wall)
        new BottomTopBlockData(Util.makeRegistryObject(Blocks.SANDSTONE), COLORED_BLOCK_DATA_HANDLER)
                .setBlockPropertiesProvider(new BlockBasedPropertiesProvider(Blocks.SANDSTONE))
                .addBlockTags(Tags.Blocks.SANDSTONE).addItemTags(Tags.Items.SANDSTONE).add(COLORED_BLOCK_DATA_HANDLER)
                .makeSlabBlock(Util.makeRegistryObject(Blocks.SANDSTONE_SLAB)).add(COLORED_BLOCK_DATA_HANDLER).parent()
                .makeStairsBlock(Util.makeRegistryObject(Blocks.SANDSTONE_STAIRS)).add(COLORED_BLOCK_DATA_HANDLER)
                .parent().makeWallBlock(Util.makeRegistryObject(Blocks.SANDSTONE_WALL));
        // Smooth Sandstone
        new ColoredBlockData(Util.makeRegistryObject(Blocks.SMOOTH_SANDSTONE), COLORED_BLOCK_DATA_HANDLER)
        {
            @Override public void makeTextureGenerationPromises(TextureProvider generator)
            {
            }

            @Override protected ResourceLocation getSideTextureLoc(boolean base)
            {
                return new ResourceLocation(base ? baseBlock.getId().getNamespace() : getModId(),
                        "block/sandstone_top");
            }

            @Override
            public void registerTextures(TextureProvider generator, TextureProvider.FinishedTextureConsumer consumer)
            {
            }
        }.setBlockPropertiesProvider(new BlockBasedPropertiesProvider(Blocks.SMOOTH_SANDSTONE))
                .add(COLORED_BLOCK_DATA_HANDLER);
        // Soul Sand
        new AbstractColoredBlockData<ColoredSoulSandBlock>(Util.makeRegistryObject(Blocks.SOUL_SAND),
                COLORED_BLOCK_DATA_HANDLER)
        {
            @Override protected ColoredSoulSandBlock makeNewBlock(DyeColor color)
            {
                return new ColoredSoulSandBlock(getBlockProperties(color), color);
            }
        }.setBlockPropertiesProvider(new BlockBasedPropertiesProvider(Blocks.SOUL_SAND))
                .addBlockTags(BlockTags.SOUL_SPEED_BLOCKS).add(COLORED_BLOCK_DATA_HANDLER);
        // Soul Soil
        new ColoredBlockData(Util.makeRegistryObject(Blocks.SOUL_SOIL), COLORED_BLOCK_DATA_HANDLER)
                .setBlockPropertiesProvider(new BlockBasedPropertiesProvider(Blocks.SOUL_SOIL))
                .addBlockTags(BlockTags.SOUL_SPEED_BLOCKS).add(COLORED_BLOCK_DATA_HANDLER);
        // Ice
        new AbstractColoredBlockData<ColoredIceBlock>(Util.makeRegistryObject(Blocks.ICE), COLORED_BLOCK_DATA_HANDLER)
        {
            @Override protected ColoredIceBlock makeNewBlock(DyeColor color)
            {
                return new ColoredIceBlock(getBlockProperties(color), color);
            }

            @Override protected boolean hasSpecialRenderType()
            {
                return true;
            }

            @Override protected RenderType getSpecialRenderType()
            {
                return RenderType.getTranslucent();
            }
        }.setBlockPropertiesProvider(new BlockBasedPropertiesProvider(Blocks.ICE)).addBlockTags(BlockTags.ICE)
                .add(COLORED_BLOCK_DATA_HANDLER);
        // Honeycomb Block
        new ColoredBlockData(Util.makeRegistryObject(Blocks.HONEYCOMB_BLOCK), COLORED_BLOCK_DATA_HANDLER)
                .setBlockPropertiesProvider(new BlockBasedPropertiesProvider(Blocks.HONEYCOMB_BLOCK))
                .add(COLORED_BLOCK_DATA_HANDLER);
        // Nether Bricks (Slab, Stairs, Wall)
        new ColoredBlockData(Util.makeRegistryObject(Blocks.NETHER_BRICKS), COLORED_BLOCK_DATA_HANDLER)
                .setBlockPropertiesProvider(new BlockBasedPropertiesProvider(Blocks.NETHER_BRICKS))
                .add(COLORED_BLOCK_DATA_HANDLER).makeSlabBlock(Util.makeRegistryObject(Blocks.NETHER_BRICK_SLAB))
                .add(COLORED_BLOCK_DATA_HANDLER).parent()
                .makeStairsBlock(Util.makeRegistryObject(Blocks.NETHER_BRICK_STAIRS)).add(COLORED_BLOCK_DATA_HANDLER)
                .parent().makeWallBlock(Util.makeRegistryObject(Blocks.NETHER_BRICK_WALL));
        // Prismarine (Slab, Stairs, Wall)
        new ColoredBlockData(Util.makeRegistryObject(Blocks.PRISMARINE), COLORED_BLOCK_DATA_HANDLER)
                .setBlockPropertiesProvider(new BlockBasedPropertiesProvider(Blocks.PRISMARINE))
                .add(COLORED_BLOCK_DATA_HANDLER).makeSlabBlock(Util.makeRegistryObject(Blocks.PRISMARINE_SLAB))
                .add(COLORED_BLOCK_DATA_HANDLER).parent()
                .makeStairsBlock(Util.makeRegistryObject(Blocks.PRISMARINE_STAIRS)).add(COLORED_BLOCK_DATA_HANDLER)
                .parent().makeWallBlock(Util.makeRegistryObject(Blocks.PRISMARINE_WALL));
        // Prismarine Bricks (Slab, Stairs)
        new ColoredBlockData(Util.makeRegistryObject(Blocks.PRISMARINE_BRICKS), COLORED_BLOCK_DATA_HANDLER)
                .setBlockPropertiesProvider(new BlockBasedPropertiesProvider(Blocks.PRISMARINE_BRICKS))
                .add(COLORED_BLOCK_DATA_HANDLER).makeSlabBlock(Util.makeRegistryObject(Blocks.PRISMARINE_BRICK_SLAB))
                .add(COLORED_BLOCK_DATA_HANDLER).parent()
                .makeStairsBlock(Util.makeRegistryObject(Blocks.PRISMARINE_BRICK_STAIRS))
                .add(COLORED_BLOCK_DATA_HANDLER).parent();
        // Dark Prismarine (Slab, Stairs)
        new ColoredBlockData(Util.makeRegistryObject(Blocks.DARK_PRISMARINE), COLORED_BLOCK_DATA_HANDLER)
                .setBlockPropertiesProvider(new BlockBasedPropertiesProvider(Blocks.DARK_PRISMARINE))
                .add(COLORED_BLOCK_DATA_HANDLER).makeSlabBlock(Util.makeRegistryObject(Blocks.DARK_PRISMARINE_SLAB))
                .add(COLORED_BLOCK_DATA_HANDLER).parent()
                .makeStairsBlock(Util.makeRegistryObject(Blocks.DARK_PRISMARINE_STAIRS)).add(COLORED_BLOCK_DATA_HANDLER)
                .parent();
        Consumer5<Block, Block, Block, Block, Block> coloredWoodMaker =
                (plankBase, slabBase, stairsBase, fenceBase, fenceGateBase) -> new ColoredBlockData(
                        Util.makeRegistryObject(plankBase), COLORED_BLOCK_DATA_HANDLER)
                        .setBlockPropertiesProvider(new BlockBasedPropertiesProvider(plankBase))
                        .addBlockTags(BlockTags.PLANKS).addItemTags(ItemTags.PLANKS).add(COLORED_BLOCK_DATA_HANDLER)
                        .makeSlabBlock(Util.makeRegistryObject(slabBase)).addBlockTags(BlockTags.WOODEN_SLABS)
                        .addItemTags(ItemTags.WOODEN_SLABS).add(COLORED_BLOCK_DATA_HANDLER).parent()
                        .makeStairsBlock(Util.makeRegistryObject(stairsBase)).addBlockTags(BlockTags.WOODEN_STAIRS)
                        .addItemTags(ItemTags.WOODEN_STAIRS).add(COLORED_BLOCK_DATA_HANDLER).parent()
                        .makeFenceBlock(Util.makeRegistryObject(fenceBase)).addBlockTags(BlockTags.WOODEN_FENCES)
                        .addItemTags(ItemTags.WOODEN_FENCES).add(COLORED_BLOCK_DATA_HANDLER).parent()
                        .makeFenceGateBlock(Util.makeRegistryObject(fenceGateBase)).add(COLORED_BLOCK_DATA_HANDLER)
                        .parent();
        // Oak Planks (Slab, Stairs, Fence, Fence Gate)
        coloredWoodMaker
                .accept(Blocks.OAK_PLANKS, Blocks.OAK_SLAB, Blocks.OAK_STAIRS, Blocks.OAK_FENCE, Blocks.OAK_FENCE_GATE);
        // Acacia Planks (Slab, Stairs, Fence, Fence Gate)
        coloredWoodMaker.accept(Blocks.ACACIA_PLANKS, Blocks.ACACIA_SLAB, Blocks.ACACIA_STAIRS, Blocks.ACACIA_FENCE,
                Blocks.ACACIA_FENCE_GATE);
        // Birch Planks (Slab, Stairs, Fence, Fence Gate)
        coloredWoodMaker.accept(Blocks.BIRCH_PLANKS, Blocks.BIRCH_SLAB, Blocks.BIRCH_STAIRS, Blocks.BIRCH_FENCE,
                Blocks.BIRCH_FENCE_GATE);
        // Crimson Planks (Slab, Stairs, Fence, Fence Gate)
        coloredWoodMaker.accept(Blocks.CRIMSON_PLANKS, Blocks.CRIMSON_SLAB, Blocks.CRIMSON_STAIRS, Blocks.CRIMSON_FENCE,
                Blocks.CRIMSON_FENCE_GATE);
        // Dark Oak Planks (Slab, Stairs, Fence, Fence Gate)
        coloredWoodMaker
                .accept(Blocks.DARK_OAK_PLANKS, Blocks.DARK_OAK_SLAB, Blocks.DARK_OAK_STAIRS, Blocks.DARK_OAK_FENCE,
                        Blocks.DARK_OAK_FENCE_GATE);
        // Jungle Planks (Slab, Stairs, Fence, Fence Gate)
        coloredWoodMaker.accept(Blocks.JUNGLE_PLANKS, Blocks.JUNGLE_SLAB, Blocks.JUNGLE_STAIRS, Blocks.JUNGLE_FENCE,
                Blocks.JUNGLE_FENCE_GATE);
        // Spruce Planks (Slab, Stairs, Fence, Fence Gate)
        coloredWoodMaker.accept(Blocks.SPRUCE_PLANKS, Blocks.SPRUCE_SLAB, Blocks.SPRUCE_STAIRS, Blocks.SPRUCE_FENCE,
                Blocks.SPRUCE_FENCE_GATE);
        // Warped Planks (Slab, Stairs, Fence, Fence Gate)
        coloredWoodMaker.accept(Blocks.WARPED_PLANKS, Blocks.WARPED_SLAB, Blocks.WARPED_STAIRS, Blocks.WARPED_FENCE,
                Blocks.WARPED_FENCE_GATE);
        // Granite (Slab, Stairs, Wall)
        new ColoredBlockData(Util.makeRegistryObject(Blocks.GRANITE), COLORED_BLOCK_DATA_HANDLER)
                .setBlockPropertiesProvider(new BlockBasedPropertiesProvider(Blocks.GRANITE))
                .addBlockTags(Tags.Blocks.STONE).addItemTags(Tags.Items.STONE).add(COLORED_BLOCK_DATA_HANDLER)
                .makeSlabBlock(Util.makeRegistryObject(Blocks.GRANITE_SLAB)).add(COLORED_BLOCK_DATA_HANDLER).parent()
                .makeStairsBlock(Util.makeRegistryObject(Blocks.GRANITE_STAIRS)).add(COLORED_BLOCK_DATA_HANDLER)
                .parent().makeWallBlock(Util.makeRegistryObject(Blocks.GRANITE_WALL));
        // Polished Granite (Slab, Stairs)
        new ColoredBlockData(Util.makeRegistryObject(Blocks.POLISHED_GRANITE), COLORED_BLOCK_DATA_HANDLER)
                .setBlockPropertiesProvider(new BlockBasedPropertiesProvider(Blocks.POLISHED_GRANITE))
                .addBlockTags(Tags.Blocks.STONE).addItemTags(Tags.Items.STONE).add(COLORED_BLOCK_DATA_HANDLER)
                .makeSlabBlock(Util.makeRegistryObject(Blocks.POLISHED_GRANITE_SLAB)).add(COLORED_BLOCK_DATA_HANDLER)
                .parent().makeStairsBlock(Util.makeRegistryObject(Blocks.POLISHED_GRANITE_STAIRS))
                .add(COLORED_BLOCK_DATA_HANDLER).parent();
        // Andesite (Slab, Stairs, Wall)
        new ColoredBlockData(Util.makeRegistryObject(Blocks.ANDESITE), COLORED_BLOCK_DATA_HANDLER)
                .setBlockPropertiesProvider(new BlockBasedPropertiesProvider(Blocks.ANDESITE))
                .addBlockTags(Tags.Blocks.STONE).addItemTags(Tags.Items.STONE).add(COLORED_BLOCK_DATA_HANDLER)
                .makeSlabBlock(Util.makeRegistryObject(Blocks.ANDESITE_SLAB)).add(COLORED_BLOCK_DATA_HANDLER).parent()
                .makeStairsBlock(Util.makeRegistryObject(Blocks.ANDESITE_STAIRS)).add(COLORED_BLOCK_DATA_HANDLER)
                .parent().makeWallBlock(Util.makeRegistryObject(Blocks.ANDESITE_WALL)).add(COLORED_BLOCK_DATA_HANDLER)
                .parent();
        // Polished Andesite (Slab, Stairs)
        new ColoredBlockData(Util.makeRegistryObject(Blocks.POLISHED_ANDESITE), COLORED_BLOCK_DATA_HANDLER)
                .setBlockPropertiesProvider(new BlockBasedPropertiesProvider(Blocks.POLISHED_ANDESITE))
                .addBlockTags(Tags.Blocks.STONE).addItemTags(Tags.Items.STONE).add(COLORED_BLOCK_DATA_HANDLER)
                .makeSlabBlock(Util.makeRegistryObject(Blocks.POLISHED_ANDESITE_SLAB)).add(COLORED_BLOCK_DATA_HANDLER)
                .parent().makeStairsBlock(Util.makeRegistryObject(Blocks.POLISHED_ANDESITE_STAIRS))
                .add(COLORED_BLOCK_DATA_HANDLER).parent();
        // Diorite (Slab, Stairs, Wall)
        new ColoredBlockData(Util.makeRegistryObject(Blocks.DIORITE), COLORED_BLOCK_DATA_HANDLER)
                .setBlockPropertiesProvider(new BlockBasedPropertiesProvider(Blocks.DIORITE))
                .addBlockTags(Tags.Blocks.STONE).addItemTags(Tags.Items.STONE).add(COLORED_BLOCK_DATA_HANDLER)
                .makeSlabBlock(Util.makeRegistryObject(Blocks.DIORITE_SLAB)).add(COLORED_BLOCK_DATA_HANDLER).parent()
                .makeStairsBlock(Util.makeRegistryObject(Blocks.DIORITE_STAIRS)).add(COLORED_BLOCK_DATA_HANDLER)
                .parent().makeWallBlock(Util.makeRegistryObject(Blocks.DIORITE_WALL)).add(COLORED_BLOCK_DATA_HANDLER)
                .parent();
        // Polished Diorite (Slab, Stairs)
        new ColoredBlockData(Util.makeRegistryObject(Blocks.POLISHED_DIORITE), COLORED_BLOCK_DATA_HANDLER)
                .setBlockPropertiesProvider(new BlockBasedPropertiesProvider(Blocks.POLISHED_DIORITE))
                .addBlockTags(Tags.Blocks.STONE).addItemTags(Tags.Items.STONE).add(COLORED_BLOCK_DATA_HANDLER)
                .makeSlabBlock(Util.makeRegistryObject(Blocks.POLISHED_DIORITE_SLAB)).add(COLORED_BLOCK_DATA_HANDLER)
                .parent().makeStairsBlock(Util.makeRegistryObject(Blocks.POLISHED_DIORITE_STAIRS))
                .add(COLORED_BLOCK_DATA_HANDLER).parent();
        // Basalt
        new ColoredRotatedPillarBlockData(Util.makeRegistryObject(Blocks.BASALT), COLORED_BLOCK_DATA_HANDLER)
                .setBlockPropertiesProvider(new BlockBasedPropertiesProvider(Blocks.BASALT))
                .add(COLORED_BLOCK_DATA_HANDLER);
        // Polished Basalt
        new ColoredRotatedPillarBlockData(Util.makeRegistryObject(Blocks.POLISHED_BASALT), COLORED_BLOCK_DATA_HANDLER)
                .setBlockPropertiesProvider(new BlockBasedPropertiesProvider(Blocks.POLISHED_BASALT))
                .add(COLORED_BLOCK_DATA_HANDLER);
        // Crying Obsidian
        new CryingObsidianBlockData(Util.makeRegistryObject(Blocks.CRYING_OBSIDIAN), COLORED_BLOCK_DATA_HANDLER)
                .setBlockPropertiesProvider(new BlockBasedPropertiesProvider(Blocks.CRYING_OBSIDIAN))
                .add(COLORED_BLOCK_DATA_HANDLER);
        // Netherrack
        new ColoredBlockData(Util.makeRegistryObject(Blocks.NETHERRACK), COLORED_BLOCK_DATA_HANDLER)
                .setBlockPropertiesProvider(new BlockBasedPropertiesProvider(Blocks.NETHERRACK))
                .add(COLORED_BLOCK_DATA_HANDLER);
        // End Stone
        new ColoredBlockData(Util.makeRegistryObject(Blocks.END_STONE), COLORED_BLOCK_DATA_HANDLER)
                .setBlockPropertiesProvider(new BlockBasedPropertiesProvider(Blocks.END_STONE))
                .add(COLORED_BLOCK_DATA_HANDLER);
        // End Stone Brick (Slab, Stairs, Wall)
        new ColoredBlockData(Util.makeRegistryObject(Blocks.END_STONE_BRICKS), COLORED_BLOCK_DATA_HANDLER)
                .setBlockPropertiesProvider(new BlockBasedPropertiesProvider(Blocks.END_STONE_BRICKS))
                .add(COLORED_BLOCK_DATA_HANDLER).makeSlabBlock(Util.makeRegistryObject(Blocks.END_STONE_BRICK_SLAB))
                .add(COLORED_BLOCK_DATA_HANDLER).parent()
                .makeStairsBlock(Util.makeRegistryObject(Blocks.END_STONE_BRICK_STAIRS)).add(COLORED_BLOCK_DATA_HANDLER)
                .parent().makeWallBlock(Util.makeRegistryObject(Blocks.END_STONE_BRICK_WALL))
                .add(COLORED_BLOCK_DATA_HANDLER).parent();
        // Purpur (Slab, Stairs)
        new ColoredBlockData(Util.makeRegistryObject(Blocks.PURPUR_BLOCK), COLORED_BLOCK_DATA_HANDLER)
                .setBlockPropertiesProvider(new BlockBasedPropertiesProvider(Blocks.PURPUR_BLOCK))
                .add(COLORED_BLOCK_DATA_HANDLER).makeSlabBlock(Util.makeRegistryObject(Blocks.PURPUR_SLAB))
                .add(COLORED_BLOCK_DATA_HANDLER).parent().makeStairsBlock(Util.makeRegistryObject(Blocks.PURPUR_STAIRS))
                .add(COLORED_BLOCK_DATA_HANDLER).parent();
        // Purpur Pillar
        new ColoredRotatedPillarBlockData(Util.makeRegistryObject(Blocks.PURPUR_PILLAR), COLORED_BLOCK_DATA_HANDLER)
        {
            @Override protected String getSideSuffix()
            {
                return "";
            }
        }.setBlockPropertiesProvider(new BlockBasedPropertiesProvider(Blocks.PURPUR_PILLAR))
                .add(COLORED_BLOCK_DATA_HANDLER);
    }

    public static void registerListeners(IEventBus bus)
    {
        BLOCKS.register(bus);
        ITEMS.register(bus);
        PARTICLE_TYPES.register(bus);
        COLORED_BLOCK_DATA_HANDLER.register(bus);
        RECIPE_SERIALIZERS.register(bus);
        CONTAINER_TYPES.register(bus);
    }
}
