package mrp_v2.additionalcolors.util;

import mrp_v2.additionalcolors.AdditionalColors;
import mrp_v2.additionalcolors.block.*;
import mrp_v2.additionalcolors.datagen.ItemModelGenerator;
import mrp_v2.additionalcolors.datagen.LootTableGenerator;
import mrp_v2.additionalcolors.item.ColoredBlockItem;
import mrp_v2.additionalcolors.particle.ColorParticleData;
import mrp_v2.mrplibrary.datagen.TextureProvider;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleType;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.Tags;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

public class ObjectHolder
{
    public static final DeferredRegister<Block> BLOCKS;
    public static final DeferredRegister<Item> ITEMS;
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES;
    public static final RegistryObject<ParticleType<ColorParticleData>> COLORED_DRIPPING_OBSIDIAN_TEAR_PARTICLE_TYPE;
    public static final RegistryObject<ParticleType<ColorParticleData>> COLORED_FALLING_OBSIDIAN_TEAR_PARTICLE_TYPE;
    public static final RegistryObject<ParticleType<ColorParticleData>> COLORED_LANDING_OBSIDIAN_TEAR_PARTICLE_TYPE;
    public static final List<ColoredBlockData<?>> COLORIZED_BLOCK_DATAS = new ArrayList<>();
    public static final Map<ResourceLocation, Map<DyeColor, RegistryObject<? extends Block>>> COLORIZED_BLOCK_MAP =
            new HashMap<>();
    public static final DyeColor[] CRYING_OBSIDIAN_COLORS =
            new DyeColor[]{DyeColor.WHITE, DyeColor.ORANGE, DyeColor.YELLOW, DyeColor.GRAY, DyeColor.LIGHT_GRAY,
                    DyeColor.CYAN, DyeColor.BLUE, DyeColor.BROWN, DyeColor.GREEN, DyeColor.RED, DyeColor.BLACK};
    public static final HashMap<DyeColor, RegistryObject<? extends IColoredBlock>> CRYING_OBSIDIAN_BLOCK_MAP =
            new HashMap<>();

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
        addColorizedBlockDatas();
        for (ColoredBlockData<?> blockEntry : COLORIZED_BLOCK_DATAS)
        {
            COLORIZED_BLOCK_MAP.put(blockEntry.getBaseBlock(), new HashMap<>(blockEntry.register()));
        }
    }

    private static void addColorizedBlockDatas()
    {
        COLORIZED_BLOCK_DATAS.add(new ColoredBlockData.ColoredBasicBlockData(Blocks.OBSIDIAN, 0.5d,
                Util.makeTagArray(Tags.Blocks.OBSIDIAN), Util.makeTagArray(Tags.Items.OBSIDIAN)));
        COLORIZED_BLOCK_DATAS.add(new ColoredBlockData.ColoredBasicBlockData(Blocks.COBBLESTONE,
                Util.makeTagArray(Tags.Blocks.COBBLESTONE), Util.makeTagArray(Tags.Items.COBBLESTONE)));
        COLORIZED_BLOCK_DATAS
                .add(new ColoredBlockData.ColoredBasicBlockData(Blocks.STONE, Util.makeTagArray(Tags.Blocks.STONE),
                        Util.makeTagArray(Tags.Items.STONE)));
        COLORIZED_BLOCK_DATAS.add(new ColoredBlockData.ColoredBasicBlockData(Blocks.SMOOTH_STONE, Util.makeTagArray(),
                Util.makeTagArray()));
        COLORIZED_BLOCK_DATAS
                .add(new ColoredBlockData.ColoredVerticalPillarBlockData(Blocks.QUARTZ_BLOCK, "_top", "_side",
                        Util.makeTagArray(Tags.Blocks.STORAGE_BLOCKS_QUARTZ),
                        Util.makeTagArray(Tags.Items.STORAGE_BLOCKS_QUARTZ)));
        // crying obsidian section
        final BiConsumer<Block, ItemModelGenerator> basicItemModelMaker = (block, generator) ->
        {
            String path = block.getBlock().getRegistryName().getPath();
            generator.withExistingParent(path, generator.modLoc("block/" + path));
        };
        final BiFunction<Supplier<? extends IColoredBlock>, ItemGroup, Supplier<ColoredBlockItem>>
                basicItemConstructor =
                (blockSupplier, itemGroup) -> () -> new ColoredBlockItem(blockSupplier.get().getColor(),
                        blockSupplier.get().getBlock(), new Item.Properties().group(itemGroup));
        final ItemGroup obsidianExpansionGroup = ObjectHolder.getObsidianExpansionItemGroup();
        final Supplier<AbstractBlock.Properties> basicProperties =
                () -> AbstractBlock.Properties.from(Blocks.CRYING_OBSIDIAN);
        final Map<DyeColor, Consumer<BufferedImage>> cryingObsidianHueChange = new HashMap<>();
        cryingObsidianHueChange.put(DyeColor.WHITE, (texture) ->
        {
            TextureProvider.makeGrayscale(texture, 0, 0, 16, 16);
            TextureProvider.adjustLevels(texture, 0, 0, 16, 16, 0.6d, 0, 70, 0, 255);
        });
        cryingObsidianHueChange.put(DyeColor.LIGHT_GRAY, (texture) ->
        {
            TextureProvider.makeGrayscale(texture, 0, 0, 16, 16);
            TextureProvider.adjustLevels(texture, 0, 0, 16, 16, 0.5d);
        });
        cryingObsidianHueChange.put(DyeColor.GRAY, (texture) -> TextureProvider.makeGrayscale(texture, 0, 0, 16, 16));
        cryingObsidianHueChange.put(DyeColor.BLACK, (texture) ->
        {
            TextureProvider.makeGrayscale(texture, 0, 0, 16, 16);
            TextureProvider.adjustLevels(texture, 0, 0, 16, 16, 1.5d);
        });
        cryingObsidianHueChange
                .put(DyeColor.RED, (texture) -> TextureProvider.adjustHSB(texture, 0, 0, 16, 16, 90, 100, 0));
        cryingObsidianHueChange
                .put(DyeColor.ORANGE, (texture) -> TextureProvider.adjustHSB(texture, 0, 0, 16, 16, 120, 100, 0));
        cryingObsidianHueChange
                .put(DyeColor.YELLOW, (texture) -> TextureProvider.adjustHSB(texture, 0, 0, 16, 16, 150, 100, 0));
        cryingObsidianHueChange
                .put(DyeColor.GREEN, (texture) -> TextureProvider.adjustHSB(texture, 0, 0, 16, 16, -165, 100, 0));
        cryingObsidianHueChange
                .put(DyeColor.CYAN, (texture) -> TextureProvider.adjustHSB(texture, 0, 0, 16, 16, -90, 100, 0));
        cryingObsidianHueChange
                .put(DyeColor.BLUE, (texture) -> TextureProvider.adjustHSB(texture, 0, 0, 16, 16, -30, 100, 0));
        cryingObsidianHueChange
                .put(DyeColor.BROWN, (texture) -> TextureProvider.adjustHSB(texture, 0, 0, 16, 16, 120, 50, -40));
        COLORIZED_BLOCK_DATAS.add(new ColoredBlockData<ColoredCryingObsidianBlock>(
                Blocks.CRYING_OBSIDIAN.getRegistryName().getPath(), ObjectHolder.CRYING_OBSIDIAN_COLORS,
                (color) -> () -> new ColoredCryingObsidianBlock(basicProperties.get(), color),
                (blockSupplier) -> basicItemConstructor.apply(blockSupplier, ItemGroup.BUILDING_BLOCKS), null,
                (block, generator) -> generator.simpleBlock(block.getBlock()), basicItemModelMaker::accept,
                (block, generator) -> generator.addLootTable(block, generator::registerDropSelfLootTable), null,
                (block, generator, consumer) ->
                {
                    BufferedImage texture = generator.getTexture(
                            new ResourceLocation("block/" + Blocks.CRYING_OBSIDIAN.getRegistryName().getPath()));
                    cryingObsidianHueChange.get(block.getColor()).accept(texture);
                    generator.finish(texture,
                            new ResourceLocation(AdditionalColors.ID, "block/" + block.getRegistryName().getPath()),
                            consumer);
                }, null, false, Items.CRYING_OBSIDIAN.getRegistryName(), true, ItemTags.createOptional(
                new ResourceLocation(AdditionalColors.ID, Blocks.CRYING_OBSIDIAN.getRegistryName().getPath())),
                Util.makeTagArray(), Util.makeTagArray())
        {
            @Override public Map<DyeColor, RegistryObject<ColoredCryingObsidianBlock>> register()
            {
                Map<DyeColor, RegistryObject<ColoredCryingObsidianBlock>> map = super.register();
                ObjectHolder.CRYING_OBSIDIAN_BLOCK_MAP.putAll(map);
                return map;
            }
        });
        COLORIZED_BLOCK_DATAS.add(new ColoredBlockData<>(Blocks.CRYING_OBSIDIAN.getRegistryName().getPath() + "_slab",
                ObjectHolder.CRYING_OBSIDIAN_COLORS,
                (color) -> () -> new ColoredSlabBlock(basicProperties.get(), color),
                (blockSupplier) -> basicItemConstructor.apply(blockSupplier, obsidianExpansionGroup), null,
                (block, generator) ->
                {
                    ResourceLocation blockLoc = new ResourceLocation(AdditionalColors.ID,
                            "block/" + block.getRegistryName().getPath().replace("_slab", ""));
                    generator.slabBlock(block, blockLoc, blockLoc);
                }, basicItemModelMaker::accept, (block, generator) -> generator
                .addLootTable(block, (block2) -> generator.registerLootTable(block2, LootTableGenerator::droppingSlab)),
                null, null, null, false,
                new ResourceLocation(AdditionalColors.OBSIDIAN_EXPANSION_ID, "crying_obsidian_slab"), false,
                ItemTags.createOptional(
                        new ResourceLocation(AdditionalColors.OBSIDIAN_EXPANSION_ID, "crying_obsidian_slab")),
                Util.makeTagArray(BlockTags.SLABS), Util.makeTagArray(ItemTags.SLABS)));
        COLORIZED_BLOCK_DATAS.add(new ColoredBlockData<>(Blocks.CRYING_OBSIDIAN.getRegistryName().getPath() + "_stairs",
                ObjectHolder.CRYING_OBSIDIAN_COLORS, (color) -> () -> new ColoredStairsBlock(
                () -> ObjectHolder.CRYING_OBSIDIAN_BLOCK_MAP.get(color).get().getBlock().getDefaultState(),
                basicProperties.get(), color),
                (blockSupplier) -> basicItemConstructor.apply(blockSupplier, obsidianExpansionGroup), null,
                (block, generator) -> generator.stairsBlock(block, new ResourceLocation(AdditionalColors.ID,
                        "block/" + block.getRegistryName().getPath().replace("_stairs", ""))),
                basicItemModelMaker::accept,
                (block, generator) -> generator.addLootTable(block, generator::registerDropSelfLootTable), null, null,
                null, false, new ResourceLocation(AdditionalColors.OBSIDIAN_EXPANSION_ID, "crying_obsidian_stairs"),
                false, ItemTags.createOptional(
                new ResourceLocation(AdditionalColors.OBSIDIAN_EXPANSION_ID, "crying_obsidian_stairs")),
                Util.makeTagArray(BlockTags.STAIRS), Util.makeTagArray(ItemTags.STAIRS)));
        COLORIZED_BLOCK_DATAS.add(new ColoredBlockData<>(Blocks.CRYING_OBSIDIAN.getRegistryName().getPath() + "_door",
                ObjectHolder.CRYING_OBSIDIAN_COLORS,
                (color) -> () -> new ColoredDoorBlock(basicProperties.get().notSolid(), color),
                (blockSupplier) -> basicItemConstructor.apply(blockSupplier, obsidianExpansionGroup), null,
                (block, generator) ->
                {
                    Function<String, ResourceLocation> doorPartLocFunction =
                            (str) -> new ResourceLocation(AdditionalColors.ID,
                                    "block/" + block.getRegistryName().getPath() + "_" + str);
                    generator.doorBlock(block, doorPartLocFunction.apply("bottom"), doorPartLocFunction.apply("top"));
                }, (block, generator) -> generator
                .singleTexture(block.getRegistryName().getPath(), generator.mcLoc("item/generated"), "layer0",
                        generator.modLoc("item/" + block.getRegistryName().getPath())), (block, generator) -> generator
                .addLootTable(block, (block2) -> generator.registerLootTable(block2, LootTableGenerator::droppingDoor)),
                null, (block, generator, consumer) ->
        {
            Supplier<BufferedImage> baseTextureSupplier = () -> generator.getTexture(
                    new ResourceLocation(AdditionalColors.ID,
                            "block/" + block.getRegistryName().getPath().replace("_door", "")));
            BufferedImage doorTop = baseTextureSupplier.get(), doorBottom = baseTextureSupplier.get();
            int hingeTop = TextureProvider.color(140, 103, 184), hingeBottom = TextureProvider.color(103, 88, 159),
                    handleEdge = TextureProvider.color(101, 88, 162);
            doorTop.setRGB(0, 4, hingeTop);
            doorTop.setRGB(0, 5, hingeBottom);
            doorTop.setRGB(0, 15, hingeTop);
            doorBottom.setRGB(0, 0, hingeBottom);
            doorBottom.setRGB(0, 10, hingeTop);
            doorBottom.setRGB(0, 11, hingeBottom);
            generator.finish(doorBottom,
                    new ResourceLocation(AdditionalColors.ID, "block/" + block.getRegistryName().getPath() + "_bottom"),
                    consumer);
            doorTop.setRGB(11, 14, 2, 1, TextureProvider.color(hingeTop, 2), 0, 2);
            doorTop.setRGB(13, 14, handleEdge);
            doorTop.setRGB(11, 15, handleEdge);
            int[] clear = TextureProvider.color(TextureProvider.color(0, 0, 0, 0), 12);
            doorTop.setRGB(3, 3, 4, 3, clear, 0, 4);
            doorTop.setRGB(9, 3, 4, 3, clear, 0, 4);
            doorTop.setRGB(3, 8, 4, 3, clear, 0, 4);
            doorTop.setRGB(9, 8, 4, 3, clear, 0, 4);
            generator.finish(doorTop,
                    new ResourceLocation(AdditionalColors.ID, "block/" + block.getRegistryName().getPath() + "_top"),
                    consumer);
            BufferedImage itemTexture = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
            itemTexture.setRGB(8, 0, 16, 16, doorTop.getRGB(0, 0, 16, 16, null, 0, 16), 0, 16);
            itemTexture.setRGB(8, 16, 16, 16, doorBottom.getRGB(0, 0, 16, 16, null, 0, 16), 0, 16);
            generator.finish(itemTexture,
                    new ResourceLocation(AdditionalColors.ID, "item/" + block.getRegistryName().getPath()), consumer);
        }, (block, event) -> RenderTypeLookup.setRenderLayer(block, RenderType.getCutout()), false,
                new ResourceLocation(AdditionalColors.OBSIDIAN_EXPANSION_ID, "crying_obsidian_door"), false,
                ItemTags.createOptional(
                        new ResourceLocation(AdditionalColors.OBSIDIAN_EXPANSION_ID, "crying_obsidian_door")),
                Util.makeTagArray(BlockTags.DOORS), Util.makeTagArray(ItemTags.DOORS)));
        COLORIZED_BLOCK_DATAS.add(new ColoredBlockData<>(Blocks.CRYING_OBSIDIAN.getRegistryName().getPath() + "_glass",
                ObjectHolder.CRYING_OBSIDIAN_COLORS, (color) -> () -> new ColoredObsidianGlassBlock(
                AbstractBlock.Properties.from(Blocks.CRYING_OBSIDIAN).sound(SoundType.GLASS).notSolid(), color),
                (blockSupplier) -> basicItemConstructor.apply(blockSupplier, obsidianExpansionGroup), null,
                (block, generator) -> generator.simpleBlock(block), basicItemModelMaker::accept,
                (block, generator) -> generator.addLootTable(block, generator::registerSilkTouch), null,
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
                    generator.finish(texture,
                            new ResourceLocation(AdditionalColors.ID, "block/" + block.getRegistryName().getPath()),
                            consumer);
                }, (block, event) -> RenderTypeLookup.setRenderLayer(block, RenderType.getCutout()), false,
                new ResourceLocation(AdditionalColors.OBSIDIAN_EXPANSION_ID, "crying_obsidian_glass"), false,
                ItemTags.createOptional(
                        new ResourceLocation(AdditionalColors.OBSIDIAN_EXPANSION_ID, "crying_obsidian_glass")),
                Util.makeTagArray(Tags.Blocks.GLASS, Tags.Blocks.GLASS_COLORLESS),
                Util.makeTagArray(Tags.Items.GLASS, Tags.Items.GLASS_COLORLESS)));
        COLORIZED_BLOCK_DATAS.add(new ColoredBlockData<>(Blocks.CRYING_OBSIDIAN.getRegistryName().getPath() + "_fence",
                ObjectHolder.CRYING_OBSIDIAN_COLORS,
                (color) -> () -> new ColoredFenceBlock(AbstractBlock.Properties.from(Blocks.CRYING_OBSIDIAN), color),
                (blockSupplier) -> basicItemConstructor.apply(blockSupplier, obsidianExpansionGroup), null,
                (block, generator) -> generator.fenceBlock(block, new ResourceLocation(AdditionalColors.ID,
                        "block/" + block.getRegistryName().getPath().replace("_fence", ""))),
                (block, generator) -> generator.fenceInventory(block.getRegistryName().getPath(),
                        new ResourceLocation(AdditionalColors.ID,
                                "block/" + block.getRegistryName().getPath().replace("_fence", ""))),
                (block, generator) -> generator.addLootTable(block, generator::registerDropSelfLootTable), null, null,
                null, false, new ResourceLocation(AdditionalColors.OBSIDIAN_EXPANSION_ID, "crying_obsidian_fence"),
                false, ItemTags.createOptional(
                new ResourceLocation(AdditionalColors.OBSIDIAN_EXPANSION_ID, "crying_obsidian_fence")),
                Util.makeTagArray(BlockTags.FENCES, Tags.Blocks.FENCES),
                Util.makeTagArray(ItemTags.FENCES, Tags.Items.FENCES)));
        COLORIZED_BLOCK_DATAS
                .add(new ColoredBlockData<>(Blocks.CRYING_OBSIDIAN.getRegistryName().getPath() + "_fence_gate",
                        ObjectHolder.CRYING_OBSIDIAN_COLORS, (color) -> () -> new ColoredFenceGateBlock(
                        AbstractBlock.Properties.from(Blocks.CRYING_OBSIDIAN), color),
                        (blockSupplier) -> basicItemConstructor.apply(blockSupplier, obsidianExpansionGroup), null,
                        (block, generator) -> generator.fenceGateBlock(block, new ResourceLocation(AdditionalColors.ID,
                                "block/" + block.getRegistryName().getPath().replace("_fence_gate", ""))),
                        basicItemModelMaker::accept,
                        (block, generator) -> generator.addLootTable(block, generator::registerDropSelfLootTable), null,
                        null, null, false,
                        new ResourceLocation(AdditionalColors.OBSIDIAN_EXPANSION_ID, "crying_obsidian_fence_gate"),
                        false, ItemTags.createOptional(
                        new ResourceLocation(AdditionalColors.OBSIDIAN_EXPANSION_ID, "crying_obsidian_fence_gate")),
                        Util.makeTagArray(BlockTags.FENCE_GATES, Tags.Blocks.FENCE_GATES),
                        Util.makeTagArray(Tags.Items.FENCE_GATES)));
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
