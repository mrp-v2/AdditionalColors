package mrp_v2.additionalcolors.util;

import mrp_v2.additionalcolors.AdditionalColors;
import mrp_v2.additionalcolors.block.ColoredBlock;
import mrp_v2.additionalcolors.block.ColoredCryingObsidianBlock;
import mrp_v2.additionalcolors.item.ColoredBlockItem;
import mrp_v2.additionalcolors.particle.ColorParticleData;
import mrp_v2.additionalcolors.particle.util.Color3B;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.HashSet;

public class ObjectHolder
{
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, AdditionalColors.ID);
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, AdditionalColors.ID);
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, AdditionalColors.ID);
    public static final HashMap<DyeColor, RegistryObject<ColoredCryingObsidianBlock>> COLORED_CRYING_OBSIDIAN_BLOCKS =
            new HashMap<>();
    public static final RegistryObject<ParticleType<ColorParticleData>> COLORED_DRIPPING_OBSIDIAN_TEAR_PARTICLE_TYPE;
    public static final RegistryObject<ParticleType<ColorParticleData>> COLORED_FALLING_OBSIDIAN_TEAR_PARTICLE_TYPE;
    public static final RegistryObject<ParticleType<ColorParticleData>> COLORED_LANDING_OBSIDIAN_TEAR_PARTICLE_TYPE;
    public static final Block[] BLOCKS_TO_COLORIZE = new Block[]{Blocks.OBSIDIAN};
    public static final HashMap<RegistryObject<ColoredBlock>, DyeColor> COLORIZED_BLOCKS = new HashMap<>();
    public static final HashMap<RegistryObject<ColoredBlockItem>, DyeColor> COLORIZED_BLOCK_ITEMS = new HashMap<>();
    public static final HashMap<Block, HashSet<RegistryObject<ColoredBlock>>> COLORIZED_BLOCK_MAP = new HashMap<>();

    static
    {
        DyeColor[] cryingObsidianColors =
                new DyeColor[]{DyeColor.WHITE, DyeColor.ORANGE, DyeColor.YELLOW, DyeColor.GRAY, DyeColor.LIGHT_GRAY,
                        DyeColor.CYAN, DyeColor.BLUE, DyeColor.BROWN, DyeColor.GREEN, DyeColor.RED, DyeColor.BLACK};
        for (DyeColor color : cryingObsidianColors)
        {
            String id = color.getTranslationKey() + "_crying_obsidian";
            COLORED_CRYING_OBSIDIAN_BLOCKS.put(color, BLOCKS.register(id,
                    () -> new ColoredCryingObsidianBlock(AbstractBlock.Properties.from(Blocks.CRYING_OBSIDIAN),
                            Color3B.fromInt(color.getColorValue()))));
            ITEMS.register(id, () -> new BlockItem(COLORED_CRYING_OBSIDIAN_BLOCKS.get(color).get(),
                    new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)));
        }
        COLORED_DRIPPING_OBSIDIAN_TEAR_PARTICLE_TYPE = PARTICLE_TYPES.register("colored_dripping_obsidian_tear",
                () -> ColorParticleData.createParticleType(ColorParticleData.DrippingObsidianTear::new));
        COLORED_FALLING_OBSIDIAN_TEAR_PARTICLE_TYPE = PARTICLE_TYPES.register("colored_falling_obsidian_tear",
                () -> ColorParticleData.createParticleType(ColorParticleData.FallingObsidianTear::new));
        COLORED_LANDING_OBSIDIAN_TEAR_PARTICLE_TYPE = PARTICLE_TYPES.register("colored_landing_obsidian_tear",
                () -> ColorParticleData.createParticleType(ColorParticleData.LandingObsidianTear::new));
        for (Block block : BLOCKS_TO_COLORIZE)
        {
            HashSet<RegistryObject<ColoredBlock>> set = new HashSet<>();
            for (DyeColor color : DyeColor.values())
            {
                final String id = color.getTranslationKey() + '_' + block.getRegistryName().getPath();
                RegistryObject<ColoredBlock> blockObj =
                        BLOCKS.register(id, () -> new ColoredBlock(color, AbstractBlock.Properties.from(block)));
                set.add(blockObj);
                COLORIZED_BLOCKS.put(blockObj, color);
                COLORIZED_BLOCK_ITEMS.put(ITEMS.register(id, () -> new ColoredBlockItem(blockObj.get(),
                        new Item.Properties().group(block.asItem().getGroup()))), color);
            }
            COLORIZED_BLOCK_MAP.put(block, set);
        }
    }

    public static void registerListeners(IEventBus bus)
    {
        BLOCKS.register(bus);
        ITEMS.register(bus);
        PARTICLE_TYPES.register(bus);
    }
}
