package mrp_v2.additionalcolors.util;

import mrp_v2.additionalcolors.AdditionalColors;
import mrp_v2.additionalcolors.block.ColoredBlock;
import mrp_v2.additionalcolors.item.ColoredBlockItem;
import mrp_v2.additionalcolors.particle.ColorParticleData;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.particles.ParticleType;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.HashSet;

public class ObjectHolder
{
    public static final DeferredRegister<Block> BLOCKS;
    public static final DeferredRegister<Item> ITEMS;
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES;
    public static final ColoredCryingObsidianHandler CRYING_OBSIDIAN_HANDLER;
    public static final RegistryObject<ParticleType<ColorParticleData>> COLORED_DRIPPING_OBSIDIAN_TEAR_PARTICLE_TYPE;
    public static final RegistryObject<ParticleType<ColorParticleData>> COLORED_FALLING_OBSIDIAN_TEAR_PARTICLE_TYPE;
    public static final RegistryObject<ParticleType<ColorParticleData>> COLORED_LANDING_OBSIDIAN_TEAR_PARTICLE_TYPE;
    public static final ColorizedBlockEntry[] BLOCKS_TO_COLORIZE = new ColorizedBlockEntry[]{
            new ColorizedBlockEntry(Blocks.OBSIDIAN, Tags.Items.OBSIDIAN, Util.makeTagArray(Tags.Blocks.OBSIDIAN),
                    Util.makeTagArray()), new ColorizedBlockEntry(Blocks.COBBLESTONE, Tags.Items.COBBLESTONE,
            Util.makeTagArray(Tags.Blocks.COBBLESTONE), Util.makeTagArray()),
            new ColorizedBlockEntry(Blocks.STONE, Tags.Items.STONE, Util.makeTagArray(Tags.Blocks.STONE),
                    Util.makeTagArray()), new ColorizedBlockEntry(Blocks.SMOOTH_STONE,
            ItemTags.createOptional(new ResourceLocation(AdditionalColors.ID, "smooth_stone")), Util.makeTagArray(),
            Util.makeTagArray())};
    public static final HashMap<Block, HashSet<Pair<RegistryObject<ColoredBlock>, RegistryObject<ColoredBlockItem>>>>
            COLORIZED_BLOCK_MAP = new HashMap<>();

    static
    {
        BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, AdditionalColors.ID);
        ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, AdditionalColors.ID);
        PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, AdditionalColors.ID);
        CRYING_OBSIDIAN_HANDLER = new ColoredCryingObsidianHandler();
        COLORED_DRIPPING_OBSIDIAN_TEAR_PARTICLE_TYPE = PARTICLE_TYPES.register("colored_dripping_obsidian_tear",
                () -> ColorParticleData.createParticleType(ColorParticleData.DrippingObsidianTear::new));
        COLORED_FALLING_OBSIDIAN_TEAR_PARTICLE_TYPE = PARTICLE_TYPES.register("colored_falling_obsidian_tear",
                () -> ColorParticleData.createParticleType(ColorParticleData.FallingObsidianTear::new));
        COLORED_LANDING_OBSIDIAN_TEAR_PARTICLE_TYPE = PARTICLE_TYPES.register("colored_landing_obsidian_tear",
                () -> ColorParticleData.createParticleType(ColorParticleData.LandingObsidianTear::new));
        for (ColorizedBlockEntry blockEntry : BLOCKS_TO_COLORIZE)
        {
            Block block = blockEntry.getBlock();
            HashSet<Pair<RegistryObject<ColoredBlock>, RegistryObject<ColoredBlockItem>>> objPairSet = new HashSet<>();
            for (DyeColor color : DyeColor.values())
            {
                final String id = color.getTranslationKey() + '_' + block.getRegistryName().getPath();
                RegistryObject<ColoredBlock> blockObj =
                        BLOCKS.register(id, () -> new ColoredBlock(color, AbstractBlock.Properties.from(block)));
                objPairSet.add(Pair.of(blockObj, ITEMS.register(id, () -> new ColoredBlockItem(blockObj.get(),
                        new Item.Properties().group(block.asItem().getGroup())))));
            }
            COLORIZED_BLOCK_MAP.put(block, objPairSet);
        }
    }

    public static void registerListeners(IEventBus bus)
    {
        BLOCKS.register(bus);
        ITEMS.register(bus);
        PARTICLE_TYPES.register(bus);
    }
}
