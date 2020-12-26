package mrp_v2.additionalcolors.util;

import mrp_v2.additionalcolors.AdditionalColors;
import mrp_v2.additionalcolors.block.*;
import mrp_v2.additionalcolors.datagen.BlockStateGenerator;
import mrp_v2.additionalcolors.datagen.ItemModelGenerator;
import mrp_v2.additionalcolors.datagen.LootTableGenerator;
import mrp_v2.additionalcolors.datagen.TextureGenerator;
import mrp_v2.additionalcolors.particle.ColorParticleData;
import mrp_v2.additionalcolors.util.colored_block_data.*;
import mrp_v2.mrplibrary.datagen.providers.TextureProvider;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleType;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.Tags;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ObjectHolder
{
    public static final DeferredRegister<Block> BLOCKS;
    public static final DeferredRegister<Item> ITEMS;
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES;
    public static final RegistryObject<ParticleType<ColorParticleData>> COLORED_DRIPPING_OBSIDIAN_TEAR_PARTICLE_TYPE;
    public static final RegistryObject<ParticleType<ColorParticleData>> COLORED_FALLING_OBSIDIAN_TEAR_PARTICLE_TYPE;
    public static final RegistryObject<ParticleType<ColorParticleData>> COLORED_LANDING_OBSIDIAN_TEAR_PARTICLE_TYPE;
    public static final List<IColoredBlockData<?>> COLORIZED_BLOCK_DATAS = new ArrayList<>();
    public static final Map<ResourceLocation, Map<DyeColor, RegistryObject<? extends Block>>> COLORIZED_BLOCK_MAP =
            new HashMap<>();
    public static final DyeColor[] CRYING_OBSIDIAN_COLORS =
            new DyeColor[]{DyeColor.WHITE, DyeColor.ORANGE, DyeColor.YELLOW, DyeColor.GRAY, DyeColor.LIGHT_GRAY,
                    DyeColor.CYAN, DyeColor.BLUE, DyeColor.BROWN, DyeColor.GREEN, DyeColor.RED, DyeColor.BLACK};
    public static final HashMap<DyeColor, RegistryObject<? extends Block>> CRYING_OBSIDIAN_BLOCK_MAP = new HashMap<>();
    public static final ItemGroup MAIN_ITEM_GROUP = new ItemGroup(AdditionalColors.ID)
    {
        @Override public ItemStack createIcon()
        {
            return new ItemStack(
                    COLORIZED_BLOCK_MAP.get(Blocks.COBBLESTONE.getRegistryName()).get(DyeColor.GREEN).get());
        }
    };
    @Nullable public static final ItemGroup OBSIDIAN_EXPANSION_ITEM_GROUP;

    static
    {
        BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, AdditionalColors.ID);
        ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, AdditionalColors.ID);
        PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, AdditionalColors.ID);
        COLORED_DRIPPING_OBSIDIAN_TEAR_PARTICLE_TYPE = PARTICLE_TYPES.register("colored_dripping_obsidian_tear",
                () -> ColorParticleData.createParticleType(ColorParticleData.DrippingObsidianTear::new));
        COLORED_FALLING_OBSIDIAN_TEAR_PARTICLE_TYPE = PARTICLE_TYPES.register("colored_falling_obsidian_tear",
                () -> ColorParticleData.createParticleType(ColorParticleData.FallingObsidianTear::new));
        COLORED_LANDING_OBSIDIAN_TEAR_PARTICLE_TYPE = PARTICLE_TYPES.register("colored_landing_obsidian_tear",
                () -> ColorParticleData.createParticleType(ColorParticleData.LandingObsidianTear::new));
        OBSIDIAN_EXPANSION_ITEM_GROUP = getObsidianExpansionItemGroup();
        addColorizedBlockDatas();
        for (IColoredBlockData<?> blockEntry : COLORIZED_BLOCK_DATAS)
        {
            COLORIZED_BLOCK_MAP.put(blockEntry.getBaseBlockLoc(), new HashMap<>(blockEntry.register()));
        }
    }

    private static void addColorizedBlockDatas()
    {
        COLORIZED_BLOCK_DATAS
                .add(new BasicColoredBlockData(Blocks.OBSIDIAN, 0.5d, Util.makeTagArray(Tags.Blocks.OBSIDIAN),
                        Util.makeTagArray(Tags.Items.OBSIDIAN)));
        COLORIZED_BLOCK_DATAS
                .add(new BasicColoredBlockData(Blocks.COBBLESTONE, Util.makeTagArray(Tags.Blocks.COBBLESTONE),
                        Util.makeTagArray(Tags.Items.COBBLESTONE)));
        COLORIZED_BLOCK_DATAS.add(new BasicColoredBlockData(Blocks.STONE, Util.makeTagArray(Tags.Blocks.STONE),
                Util.makeTagArray(Tags.Items.STONE)));
        COLORIZED_BLOCK_DATAS.add(new BasicColoredBlockData(Blocks.SMOOTH_STONE));
        COLORIZED_BLOCK_DATAS.add(new VerticalPillarBasicBlockData(Blocks.QUARTZ_BLOCK,
                Util.makeTagArray(Tags.Blocks.STORAGE_BLOCKS_QUARTZ),
                Util.makeTagArray(Tags.Items.STORAGE_BLOCKS_QUARTZ)));
        COLORIZED_BLOCK_DATAS.add(new VerticalPillarBasicBlockData(Blocks.BLACKSTONE, "_top", ""));
        COLORIZED_BLOCK_DATAS
                .add(new BottomTopBasicBlockData(Blocks.SANDSTONE, Util.makeTagArray(Tags.Blocks.SANDSTONE),
                        Util.makeTagArray(Tags.Items.SANDSTONE)));
        COLORIZED_BLOCK_DATAS.add(new VerticalPillarBasicBlockData(Blocks.ANCIENT_DEBRIS,
                Util.makeTagArray(Tags.Blocks.ORES_NETHERITE_SCRAP),
                Util.makeTagArray(Tags.Items.ORES_NETHERITE_SCRAP)));
        COLORIZED_BLOCK_DATAS.add(new TypedBasicColoredBlockData<>(Blocks.SOUL_SAND, ColoredSoulSandBlock::new,
                Util.makeTagArray(BlockTags.SOUL_SPEED_BLOCKS)));
        COLORIZED_BLOCK_DATAS
                .add(new BasicColoredBlockData(Blocks.SOUL_SOIL, Util.makeTagArray(BlockTags.SOUL_SPEED_BLOCKS)));
        COLORIZED_BLOCK_DATAS.add(new TypedBasicColoredBlockData<ColoredIceBlock>(Blocks.ICE, ColoredIceBlock::new,
                Util.makeTagArray(BlockTags.ICE))
        {
            @Override public void clientSetup(FMLClientSetupEvent event)
            {
                for (RegistryObject<ColoredIceBlock> blockObject : blockObjectSet)
                {
                    RenderTypeLookup.setRenderLayer(blockObject.get(), RenderType.getTranslucent());
                }
            }
        });
        COLORIZED_BLOCK_DATAS.add(new BasicColoredBlockData(Blocks.HONEYCOMB_BLOCK));
        COLORIZED_BLOCK_DATAS.add(new BasicColoredBlockData(Blocks.NETHER_BRICKS));
        COLORIZED_BLOCK_DATAS.add(new AnimatedBasicBlockData(Blocks.PRISMARINE));
        // crying obsidian section
        final Supplier<AbstractBlock.Properties> basicProperties =
                () -> AbstractBlock.Properties.from(Blocks.CRYING_OBSIDIAN);
        final Map<DyeColor, Consumer<BufferedImage>> cryingObsidianTextureMakerMap = new HashMap<>();
        cryingObsidianTextureMakerMap.put(DyeColor.WHITE, (texture) ->
        {
            TextureProvider.makeGrayscale(texture);
            TextureProvider.adjustLevels(texture, 0.6d, 0, 70, 0, 255);
        });
        cryingObsidianTextureMakerMap.put(DyeColor.LIGHT_GRAY, (texture) ->
        {
            TextureProvider.makeGrayscale(texture);
            TextureProvider.adjustLevels(texture, 0.5d);
        });
        cryingObsidianTextureMakerMap.put(DyeColor.GRAY, TextureProvider::makeGrayscale);
        cryingObsidianTextureMakerMap.put(DyeColor.BLACK, (texture) ->
        {
            TextureProvider.makeGrayscale(texture);
            TextureProvider.adjustLevels(texture, 1.5d);
        });
        cryingObsidianTextureMakerMap.put(DyeColor.RED, (texture) -> TextureProvider.adjustHSB(texture, 90, 100, 0));
        cryingObsidianTextureMakerMap
                .put(DyeColor.ORANGE, (texture) -> TextureProvider.adjustHSB(texture, 120, 100, 0));
        cryingObsidianTextureMakerMap
                .put(DyeColor.YELLOW, (texture) -> TextureProvider.adjustHSB(texture, 150, 100, 0));
        cryingObsidianTextureMakerMap
                .put(DyeColor.GREEN, (texture) -> TextureProvider.adjustHSB(texture, -165, 100, 0));
        cryingObsidianTextureMakerMap.put(DyeColor.CYAN, (texture) -> TextureProvider.adjustHSB(texture, -90, 100, 0));
        cryingObsidianTextureMakerMap.put(DyeColor.BLUE, (texture) -> TextureProvider.adjustHSB(texture, -30, 100, 0));
        cryingObsidianTextureMakerMap
                .put(DyeColor.BROWN, (texture) -> TextureProvider.adjustHSB(texture, 120, 50, -40));
        COLORIZED_BLOCK_DATAS.add(new TypedBasicColoredBlockData<ColoredCryingObsidianBlock>(Blocks.CRYING_OBSIDIAN,
                ColoredCryingObsidianBlock::new)
        {
            @Override public DyeColor[] getColors()
            {
                return CRYING_OBSIDIAN_COLORS;
            }

            @Override public void makeTextureGenerationPromises(TextureGenerator generator)
            {
                for (RegistryObject<ColoredCryingObsidianBlock> blockObject : blockObjectSet)
                {
                    generator.promiseGeneration(
                            new ResourceLocation(AdditionalColors.ID, "block/" + blockObject.getId().getPath()));
                }
            }

            @Override public boolean requiresTinting()
            {
                return false;
            }

            @Override
            public void registerTextures(TextureGenerator generator, TextureProvider.FinishedTextureConsumer consumer)
            {
                for (RegistryObject<ColoredCryingObsidianBlock> blockObject : blockObjectSet)
                {
                    ColoredCryingObsidianBlock block = blockObject.get();
                    BufferedImage texture = generator.getTexture(
                            new ResourceLocation("block/" + Blocks.CRYING_OBSIDIAN.getRegistryName().getPath()));
                    cryingObsidianTextureMakerMap.get(block.getColor()).accept(texture);
                    generator.finish(texture, null,
                            new ResourceLocation(AdditionalColors.ID, "block/" + blockObject.getId().getPath()),
                            consumer);
                }
            }

            @Override public void registerItemModels(ItemModelGenerator generator)
            {
                for (RegistryObject<ColoredCryingObsidianBlock> blockObject : blockObjectSet)
                {
                    String path = blockObject.get().getRegistryName().getPath();
                    generator.withExistingParent(path, generator.modLoc("block/" + path));
                }
            }

            @Override public void registerBlockStatesAndModels(BlockStateGenerator generator)
            {
                for (RegistryObject<ColoredCryingObsidianBlock> blockObject : blockObjectSet)
                {
                    generator.simpleBlock(blockObject.get());
                }
            }

            @Override public Map<DyeColor, RegistryObject<ColoredCryingObsidianBlock>> register()
            {
                Map<DyeColor, RegistryObject<ColoredCryingObsidianBlock>> map = super.register();
                ObjectHolder.CRYING_OBSIDIAN_BLOCK_MAP.putAll(map);
                return map;
            }
        });
        COLORIZED_BLOCK_DATAS.add(new CryingObsidianBasedBlockData<ColoredSlabBlock>(
                new ResourceLocation(AdditionalColors.OBSIDIAN_EXPANSION_ID,
                        Blocks.CRYING_OBSIDIAN.getRegistryName().getPath() + "_slab"),
                (color) -> new ColoredSlabBlock(basicProperties.get(), color), Util.makeTagArray(BlockTags.SLABS),
                Util.makeTagArray(ItemTags.SLABS))
        {
            @Override public void registerLootTables(LootTableGenerator generator)
            {
                for (RegistryObject<ColoredSlabBlock> blockObject : blockObjectSet)
                {
                    generator.addLootTable(blockObject.get(),
                            (block) -> generator.registerLootTable(block, LootTableGenerator::droppingSlab));
                }
            }

            @Override public void registerBlockStatesAndModels(BlockStateGenerator generator)
            {
                for (RegistryObject<ColoredSlabBlock> blockObject : blockObjectSet)
                {
                    ResourceLocation blockLoc = new ResourceLocation(AdditionalColors.ID,
                            "block/" + blockObject.getId().getPath().replace("_slab", ""));
                    generator.slabBlock(blockObject.get(), blockLoc, blockLoc);
                }
            }
        });
        COLORIZED_BLOCK_DATAS.add(new CryingObsidianBasedBlockData<ColoredStairsBlock>(
                new ResourceLocation(AdditionalColors.OBSIDIAN_EXPANSION_ID,
                        Blocks.CRYING_OBSIDIAN.getRegistryName().getPath() + "_stairs"),
                (color) -> new ColoredStairsBlock(
                        () -> ObjectHolder.CRYING_OBSIDIAN_BLOCK_MAP.get(color).get().getDefaultState(),
                        basicProperties.get(), color), Util.makeTagArray(BlockTags.STAIRS),
                Util.makeTagArray(ItemTags.STAIRS))
        {
            @Override public void registerBlockStatesAndModels(BlockStateGenerator generator)
            {
                for (RegistryObject<ColoredStairsBlock> blockObject : blockObjectSet)
                {
                    generator.stairsBlock(blockObject.get(), new ResourceLocation(AdditionalColors.ID,
                            "block/" + blockObject.getId().getPath().replace("_stairs", "")));
                }
            }
        });
        COLORIZED_BLOCK_DATAS.add(new CryingObsidianBasedBlockData<ColoredDoorBlock>(
                new ResourceLocation(AdditionalColors.OBSIDIAN_EXPANSION_ID,
                        Blocks.CRYING_OBSIDIAN.getRegistryName().getPath() + "_door"),
                (color) -> new ColoredDoorBlock(basicProperties.get(), color), Util.makeTagArray(BlockTags.DOORS),
                Util.makeTagArray(ItemTags.DOORS))
        {
            @Override public void registerBlockStatesAndModels(BlockStateGenerator generator)
            {
                for (RegistryObject<ColoredDoorBlock> blockObject : blockObjectSet)
                {
                    Function<String, ResourceLocation> doorPartLocFunction =
                            (str) -> new ResourceLocation(AdditionalColors.ID,
                                    "block/" + blockObject.getId().getPath() + "_" + str);
                    generator.doorBlock(blockObject.get(), doorPartLocFunction.apply("bottom"),
                            doorPartLocFunction.apply("top"));
                }
            }

            @Override public void makeTextureGenerationPromises(TextureGenerator generator)
            {
                for (RegistryObject<ColoredDoorBlock> blockObject : blockObjectSet)
                {
                    generator.promiseGeneration(new ResourceLocation(AdditionalColors.ID,
                            "block/" + blockObject.getId().getPath() + "_top"));
                    generator.promiseGeneration(new ResourceLocation(AdditionalColors.ID,
                            "block/" + blockObject.getId().getPath() + "_bottom"));
                    generator.promiseGeneration(
                            new ResourceLocation(AdditionalColors.ID, "item/" + blockObject.getId().getPath()));
                }
            }

            @Override
            public void registerTextures(TextureGenerator generator, TextureProvider.FinishedTextureConsumer consumer)
            {
                for (RegistryObject<ColoredDoorBlock> blockObject : blockObjectSet)
                {
                    Supplier<BufferedImage> baseTextureSupplier = () -> generator.getTexture(
                            new ResourceLocation(AdditionalColors.ID,
                                    "block/" + blockObject.getId().getPath().replace("_door", "")));
                    BufferedImage doorTop = baseTextureSupplier.get(), doorBottom = baseTextureSupplier.get();
                    int hingeTop = TextureProvider.color(140, 103, 184), hingeBottom =
                            TextureProvider.color(103, 88, 159), handleEdge = TextureProvider.color(101, 88, 162);
                    doorTop.setRGB(0, 4, hingeTop);
                    doorTop.setRGB(0, 5, hingeBottom);
                    doorTop.setRGB(0, 15, hingeTop);
                    doorBottom.setRGB(0, 0, hingeBottom);
                    doorBottom.setRGB(0, 10, hingeTop);
                    doorBottom.setRGB(0, 11, hingeBottom);
                    generator.finish(doorBottom, null, new ResourceLocation(AdditionalColors.ID,
                            "block/" + blockObject.getId().getPath() + "_bottom"), consumer);
                    doorTop.setRGB(11, 14, 2, 1, TextureProvider.color(hingeTop, 2), 0, 2);
                    doorTop.setRGB(13, 14, handleEdge);
                    doorTop.setRGB(11, 15, handleEdge);
                    int[] clear = TextureProvider.color(TextureProvider.color(0, 0, 0, 0), 12);
                    doorTop.setRGB(3, 3, 4, 3, clear, 0, 4);
                    doorTop.setRGB(9, 3, 4, 3, clear, 0, 4);
                    doorTop.setRGB(3, 8, 4, 3, clear, 0, 4);
                    doorTop.setRGB(9, 8, 4, 3, clear, 0, 4);
                    generator.finish(doorTop, null, new ResourceLocation(AdditionalColors.ID,
                            "block/" + blockObject.getId().getPath() + "_top"), consumer);
                    BufferedImage itemTexture = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
                    itemTexture.setRGB(8, 0, 16, 16, doorTop.getRGB(0, 0, 16, 16, null, 0, 16), 0, 16);
                    itemTexture.setRGB(8, 16, 16, 16, doorBottom.getRGB(0, 0, 16, 16, null, 0, 16), 0, 16);
                    generator.finish(itemTexture, null,
                            new ResourceLocation(AdditionalColors.ID, "item/" + blockObject.getId().getPath()),
                            consumer);
                }
            }

            @Override public void registerItemModels(ItemModelGenerator generator)
            {
                for (RegistryObject<ColoredDoorBlock> blockObject : blockObjectSet)
                {
                    generator.singleTexture(blockObject.getId().getPath(), generator.mcLoc("item/generated"), "layer0",
                            generator.modLoc("item/" + blockObject.getId().getPath()));
                }
            }

            @Override public void registerLootTables(LootTableGenerator generator)
            {
                for (RegistryObject<ColoredDoorBlock> blockObject : blockObjectSet)
                {
                    generator.addLootTable(blockObject.get(),
                            (block) -> generator.registerLootTable(block, LootTableGenerator::droppingDoor));
                }
            }
        });
        COLORIZED_BLOCK_DATAS.add(new CryingObsidianBasedBlockData<ColoredObsidianGlassBlock>(
                new ResourceLocation(AdditionalColors.OBSIDIAN_EXPANSION_ID,
                        Blocks.CRYING_OBSIDIAN.getRegistryName().getPath() + "_glass"),
                (color) -> new ColoredObsidianGlassBlock(basicProperties.get().sound(SoundType.GLASS).notSolid(),
                        color), Util.makeTagArray(Tags.Blocks.GLASS, Tags.Blocks.GLASS_COLORLESS),
                Util.makeTagArray(Tags.Items.GLASS, Tags.Items.GLASS_COLORLESS))
        {
            @Override public void registerBlockStatesAndModels(BlockStateGenerator generator)
            {
                for (RegistryObject<ColoredObsidianGlassBlock> blockObject : blockObjectSet)
                {
                    generator.simpleBlock(blockObject.get());
                }
            }

            @Override public void clientSetup(FMLClientSetupEvent event)
            {
                for (RegistryObject<ColoredObsidianGlassBlock> blockObject : blockObjectSet)
                {
                    RenderTypeLookup.setRenderLayer(blockObject.get(), RenderType.getCutout());
                }
            }

            @Override public void makeTextureGenerationPromises(TextureGenerator generator)
            {
                for (RegistryObject<ColoredObsidianGlassBlock> blockObject : blockObjectSet)
                {
                    generator.promiseGeneration(
                            new ResourceLocation(AdditionalColors.ID, "block/" + blockObject.getId().getPath()));
                }
            }

            @Override
            public void registerTextures(TextureGenerator generator, TextureProvider.FinishedTextureConsumer consumer)
            {
                for (RegistryObject<ColoredObsidianGlassBlock> blockObject : blockObjectSet)
                {
                    BufferedImage texture = generator.getTexture(new ResourceLocation(AdditionalColors.ID,
                            "block/" + blockObject.getId().getPath().replace("_glass", "")));
                    int[] newColors = TextureProvider.color(TextureProvider.color(0, 0, 0, 0), 14 * 14);
                    newColors[14 + 3] = texture.getRGB(4, 2);
                    newColors[14 * 2 + 2] = texture.getRGB(3, 3);
                    newColors[14 * 3 + 1] = texture.getRGB(2, 4);
                    newColors[14 * 11 + 12] = texture.getRGB(13, 12);
                    newColors[14 * 12 + 11] = texture.getRGB(12, 13);
                    texture.setRGB(1, 1, 14, 14, newColors, 0, 14);
                    generator.finish(texture, null,
                            new ResourceLocation(AdditionalColors.ID, "block/" + blockObject.getId().getPath()),
                            consumer);
                }
            }

            @Override public void registerLootTables(LootTableGenerator generator)
            {
                for (RegistryObject<ColoredObsidianGlassBlock> blockObject : blockObjectSet)
                {
                    generator.addLootTable(blockObject.get(), generator::registerSilkTouch);
                }
            }
        });
        COLORIZED_BLOCK_DATAS.add(new CryingObsidianBasedBlockData<ColoredFenceBlock>(
                new ResourceLocation(AdditionalColors.OBSIDIAN_EXPANSION_ID,
                        Blocks.CRYING_OBSIDIAN.getRegistryName().getPath() + "_fence"),
                (color) -> new ColoredFenceBlock(basicProperties.get(), color),
                Util.makeTagArray(BlockTags.FENCES, Tags.Blocks.FENCES),
                Util.makeTagArray(ItemTags.FENCES, Tags.Items.FENCES))
        {
            @Override public void registerItemModels(ItemModelGenerator generator)
            {
                for (RegistryObject<ColoredFenceBlock> blockObject : blockObjectSet)
                {
                    generator.fenceInventory(blockObject.getId().getPath(), new ResourceLocation(AdditionalColors.ID,
                            "block/" + blockObject.getId().getPath().replace("_fence", "")));
                }
            }

            @Override public void registerBlockStatesAndModels(BlockStateGenerator generator)
            {
                for (RegistryObject<ColoredFenceBlock> blockObject : blockObjectSet)
                {
                    generator.fenceBlock(blockObject.get(), new ResourceLocation(AdditionalColors.ID,
                            "block/" + blockObject.getId().getPath().replace("_fence", "")));
                }
            }
        });
        COLORIZED_BLOCK_DATAS.add(new CryingObsidianBasedBlockData<ColoredFenceGateBlock>(
                new ResourceLocation(AdditionalColors.OBSIDIAN_EXPANSION_ID,
                        Blocks.CRYING_OBSIDIAN.getRegistryName().getPath() + "_fence_gate"),
                (color) -> new ColoredFenceGateBlock(basicProperties.get(), color),
                Util.makeTagArray(BlockTags.FENCE_GATES, Tags.Blocks.FENCE_GATES),
                Util.makeTagArray(Tags.Items.FENCE_GATES))
        {
            @Override public void registerBlockStatesAndModels(BlockStateGenerator generator)
            {
                for (RegistryObject<ColoredFenceGateBlock> blockObject : blockObjectSet)
                {
                    generator.fenceGateBlock(blockObject.get(), new ResourceLocation(AdditionalColors.ID,
                            "block/" + blockObject.getId().getPath().replace("_fence_gate", "")));
                }
            }
        });
    }

    @Nullable public static ItemGroup getObsidianExpansionItemGroup()
    {
        if (!AdditionalColors.isObsidianExpansionPresent())
        {
            return null;
        }
        List<ItemGroup> labelMatches = Arrays.stream(ItemGroup.GROUPS)
                .filter((group) -> ((TranslationTextComponent) group.getGroupName()).getKey()
                        .equals("itemGroup.expansion_tab")).collect(Collectors.toList());
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

    public static void registerListeners(IEventBus bus)
    {
        BLOCKS.register(bus);
        ITEMS.register(bus);
        PARTICLE_TYPES.register(bus);
    }
}
