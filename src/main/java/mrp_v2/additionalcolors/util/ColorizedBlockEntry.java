package mrp_v2.additionalcolors.util;

import mrp_v2.additionalcolors.block.ColoredBlock;
import mrp_v2.additionalcolors.item.ColoredBlockItem;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;

public class ColorizedBlockEntry extends ColorizedBlockData<ColoredBlock>
{
    public ColorizedBlockEntry(Block baseBlock, ITag.INamedTag<Item> craftingTag,
            ITag.INamedTag<Block>[] additionalBlockTags, ITag.INamedTag<Item>[] additionalItemTags)
    {
        super(baseBlock.getRegistryName().getPath(), DyeColor.values(),
                (color) -> () -> new ColoredBlock(color, AbstractBlock.Properties.from(baseBlock)),
                (blockSupplier) -> () -> new ColoredBlockItem(blockSupplier.get(),
                        new Item.Properties().group(baseBlock.asItem().getGroup())), (generator ->
                {
                    ResourceLocation textureLoc = generator.modLoc("block/" + baseBlock.getRegistryName().getPath());
                    generator.models().getBuilder(baseBlock.getRegistryName().getPath())
                            .parent(generator.models().getExistingFile(generator.mcLoc("block/block")))
                            .texture("all", textureLoc).texture("particle", textureLoc).element().from(0, 0, 0)
                            .to(16, 16, 16).allFaces(
                            (face, faceBuilder) -> faceBuilder.tintindex(0).texture("#all").cullface(face).end()).end();
                }), (block, generator) -> generator.simpleBlock(block, generator.models()
                        .getExistingFile(generator.modLoc("block/" + baseBlock.getRegistryName().getPath()))),
                (block, generator) -> generator.withExistingParent(block.getRegistryName().getPath(),
                        generator.modLoc("block/" + baseBlock.getRegistryName().getPath())),
                (block, generator) -> generator.addLootTable(block, generator::registerDropSelfLootTable), null, null,
                true, baseBlock.asItem().getRegistryName(), true, craftingTag, additionalBlockTags, additionalItemTags);
    }
}
