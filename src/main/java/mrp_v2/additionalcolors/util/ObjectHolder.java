package mrp_v2.additionalcolors.util;

import mrp_v2.additionalcolors.AdditionalColors;
import mrp_v2.additionalcolors.block.ColoredIceBlock;
import mrp_v2.additionalcolors.block.ColoredSoulSandBlock;
import mrp_v2.additionalcolors.datagen.TextureGenerator;
import mrp_v2.additionalcolors.particle.ColorParticleData;
import mrp_v2.additionalcolors.util.colored_block_data.*;
import mrp_v2.additionalcolors.util.colored_block_data.singles.*;
import mrp_v2.mrplibrary.datagen.providers.TextureProvider;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleType;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.util.TriConsumer;

import javax.annotation.Nullable;
import java.util.*;
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
        COLORIZED_BLOCK_DATAS.add(new BasicColoredBlockData(Blocks.OBSIDIAN, Util.makeTagArray(Tags.Blocks.OBSIDIAN),
                Util.makeTagArray(Tags.Items.OBSIDIAN))
        {
            @Override protected double getLevelAdjustment()
            {
                return 0.5d;
            }
        });
        COLORIZED_BLOCK_DATAS
                .add(new BasicColoredBlockData(Blocks.COBBLESTONE, Util.makeTagArray(Tags.Blocks.COBBLESTONE),
                        Util.makeTagArray(Tags.Items.COBBLESTONE)));
        COLORIZED_BLOCK_DATAS.add(new BasicColoredBlockData(Blocks.STONE, Util.makeTagArray(Tags.Blocks.STONE),
                Util.makeTagArray(Tags.Items.STONE)));
        COLORIZED_BLOCK_DATAS.add(new BasicColoredBlockData(Blocks.SMOOTH_STONE));
        COLORIZED_BLOCK_DATAS.add(new VerticalPillarBasicBlockData(Blocks.QUARTZ_BLOCK,
                Util.makeTagArray(Tags.Blocks.STORAGE_BLOCKS_QUARTZ),
                Util.makeTagArray(Tags.Items.STORAGE_BLOCKS_QUARTZ)));
        COLORIZED_BLOCK_DATAS.add(new VerticalPillarBasicBlockData(Blocks.BLACKSTONE)
        {
            @Override protected String getSideSuffix()
            {
                return "";
            }
        });
        COLORIZED_BLOCK_DATAS
                .add(new BottomTopBasicBlockData(Blocks.SANDSTONE, Util.makeTagArray(Tags.Blocks.SANDSTONE),
                        Util.makeTagArray(Tags.Items.SANDSTONE)));
        COLORIZED_BLOCK_DATAS.add(new VerticalPillarBasicBlockData(Blocks.ANCIENT_DEBRIS,
                Util.makeTagArray(Tags.Blocks.ORES_NETHERITE_SCRAP),
                Util.makeTagArray(Tags.Items.ORES_NETHERITE_SCRAP)));
        COLORIZED_BLOCK_DATAS.add(new AbstractColoredBlockData<ColoredSoulSandBlock>(Blocks.SOUL_SAND,
                Util.makeTagArray(BlockTags.SOUL_SPEED_BLOCKS))
        {
            @Override protected ColoredSoulSandBlock makeNewBlock(DyeColor color)
            {
                return new ColoredSoulSandBlock(color, AbstractBlock.Properties.from(Blocks.SOUL_SAND));
            }
        });
        COLORIZED_BLOCK_DATAS
                .add(new BasicColoredBlockData(Blocks.SOUL_SOIL, Util.makeTagArray(BlockTags.SOUL_SPEED_BLOCKS)));
        COLORIZED_BLOCK_DATAS
                .add(new AbstractColoredBlockData<ColoredIceBlock>(Blocks.ICE, Util.makeTagArray(BlockTags.ICE))
                {
                    @Override protected boolean hasSpecialRenderType()
                    {
                        return true;
                    }

                    @Override protected RenderType getSpecialRenderType()
                    {
                        return RenderType.getTranslucent();
                    }

                    @Override protected ColoredIceBlock makeNewBlock(DyeColor color)
                    {
                        return new ColoredIceBlock(color, AbstractBlock.Properties.from(Blocks.ICE));
                    }
                });
        COLORIZED_BLOCK_DATAS.add(new BasicColoredBlockData(Blocks.HONEYCOMB_BLOCK));
        COLORIZED_BLOCK_DATAS.add(new BasicColoredBlockData(Blocks.NETHER_BRICKS));
        COLORIZED_BLOCK_DATAS.add(new BasicColoredBlockData(Blocks.PRISMARINE)
        {
            @Override
            public void registerTextures(TextureGenerator generator, TextureProvider.FinishedTextureConsumer consumer)
            {
                super.registerTextures(generator, consumer);
                generator.finish(generator.getTextureMeta(
                        new ResourceLocation(getBaseBlockLoc().getNamespace(), "block/" + getBaseBlockLoc().getPath())),
                        new ResourceLocation(AdditionalColors.ID, "block/" + baseBlock.getId().getPath()), consumer);
            }
        });
        COLORIZED_BLOCK_DATAS.add(new BasicColoredBlockData(Blocks.PRISMARINE_BRICKS));
        COLORIZED_BLOCK_DATAS.add(new BasicColoredBlockData(Blocks.DARK_PRISMARINE));
        TriConsumer<Block, Block, Block> coloredWoodMaker = (plankBase, slabBase, stairsBase) ->
        {
            IColoredBlockData<?> coloredPlankBlockData =
                    new BasicColoredBlockData(plankBase, Util.makeTagArray(BlockTags.PLANKS),
                            Util.makeTagArray(ItemTags.PLANKS));
            COLORIZED_BLOCK_DATAS.add(coloredPlankBlockData);
            COLORIZED_BLOCK_DATAS.add(new BasicColoredSlabBlockData(slabBase,
                    Util.makeTagArray(BlockTags.SLABS, BlockTags.WOODEN_SLABS),
                    Util.makeTagArray(ItemTags.SLABS, ItemTags.WOODEN_SLABS), coloredPlankBlockData));
            COLORIZED_BLOCK_DATAS.add(new BasicColoredStairsBlockData(stairsBase,
                    Util.makeTagArray(BlockTags.STAIRS, BlockTags.WOODEN_STAIRS),
                    Util.makeTagArray(ItemTags.STAIRS, ItemTags.WOODEN_STAIRS), coloredPlankBlockData));
        };
        coloredWoodMaker.accept(Blocks.OAK_PLANKS, Blocks.OAK_SLAB, Blocks.OAK_STAIRS);
        coloredWoodMaker.accept(Blocks.ACACIA_PLANKS, Blocks.ACACIA_SLAB, Blocks.ACACIA_STAIRS);
        coloredWoodMaker.accept(Blocks.BIRCH_PLANKS, Blocks.BIRCH_SLAB, Blocks.BIRCH_STAIRS);
        coloredWoodMaker.accept(Blocks.CRIMSON_PLANKS, Blocks.CRIMSON_SLAB, Blocks.CRIMSON_STAIRS);
        coloredWoodMaker.accept(Blocks.DARK_OAK_PLANKS, Blocks.DARK_OAK_SLAB, Blocks.DARK_OAK_STAIRS);
        coloredWoodMaker.accept(Blocks.JUNGLE_PLANKS, Blocks.JUNGLE_SLAB, Blocks.JUNGLE_STAIRS);
        coloredWoodMaker.accept(Blocks.SPRUCE_PLANKS, Blocks.SPRUCE_SLAB, Blocks.SPRUCE_STAIRS);
        coloredWoodMaker.accept(Blocks.WARPED_PLANKS, Blocks.WARPED_SLAB, Blocks.WARPED_STAIRS);
        // crying obsidian section
        IColoredBlockData<?> coloredCryingObsidianBlockData = new CryingObsidianBlockData(Blocks.CRYING_OBSIDIAN);
        COLORIZED_BLOCK_DATAS.add(coloredCryingObsidianBlockData);
        COLORIZED_BLOCK_DATAS.add(new CryingObsidianSlabBlockData(
                new ResourceLocation(AdditionalColors.OBSIDIAN_EXPANSION_ID,
                        Blocks.CRYING_OBSIDIAN.getRegistryName().getPath() + "_slab"),
                Util.makeTagArray(BlockTags.SLABS), Util.makeTagArray(ItemTags.SLABS), coloredCryingObsidianBlockData));
        COLORIZED_BLOCK_DATAS.add(new CryingObsidianStairsBlockData(
                new ResourceLocation(AdditionalColors.OBSIDIAN_EXPANSION_ID,
                        Blocks.CRYING_OBSIDIAN.getRegistryName().getPath() + "_stairs"),
                Util.makeTagArray(BlockTags.STAIRS), Util.makeTagArray(ItemTags.STAIRS),
                coloredCryingObsidianBlockData));
        COLORIZED_BLOCK_DATAS.add(new CryingObsidianDoorBlockData(
                new ResourceLocation(AdditionalColors.OBSIDIAN_EXPANSION_ID,
                        Blocks.CRYING_OBSIDIAN.getRegistryName().getPath() + "_door"),
                Util.makeTagArray(BlockTags.DOORS), Util.makeTagArray(ItemTags.DOORS)));
        COLORIZED_BLOCK_DATAS.add(new CryingObsidianGlassBlockData(
                new ResourceLocation(AdditionalColors.OBSIDIAN_EXPANSION_ID,
                        Blocks.CRYING_OBSIDIAN.getRegistryName().getPath() + "_glass"),
                Util.makeTagArray(Tags.Blocks.GLASS, Tags.Blocks.GLASS_COLORLESS),
                Util.makeTagArray(Tags.Items.GLASS, Tags.Items.GLASS_COLORLESS)));
        COLORIZED_BLOCK_DATAS.add(new CryingObsidianFenceBlockData(
                new ResourceLocation(AdditionalColors.OBSIDIAN_EXPANSION_ID,
                        Blocks.CRYING_OBSIDIAN.getRegistryName().getPath() + "_fence"),
                Util.makeTagArray(BlockTags.FENCES, Tags.Blocks.FENCES),
                Util.makeTagArray(ItemTags.FENCES, Tags.Items.FENCES)));
        COLORIZED_BLOCK_DATAS.add(new CryingObsidianFenceGateBlockData(
                new ResourceLocation(AdditionalColors.OBSIDIAN_EXPANSION_ID,
                        Blocks.CRYING_OBSIDIAN.getRegistryName().getPath() + "_fence_gate"),
                Util.makeTagArray(BlockTags.FENCE_GATES, Tags.Blocks.FENCE_GATES),
                Util.makeTagArray(Tags.Items.FENCE_GATES)));
    }

    @Nullable public static ItemGroup getObsidianExpansionItemGroup()
    {
        if (!AdditionalColors.isObsidianExpansionPresent())
        {
            return null;
        }
        List<ItemGroup> labelMatches =
                Arrays.stream(ItemGroup.GROUPS).filter((group) -> group.getPath().equals("expansion_tab"))
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
