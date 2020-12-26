package mrp_v2.additionalcolors.util.colored_block_data;

import mrp_v2.additionalcolors.AdditionalColors;
import mrp_v2.additionalcolors.datagen.TextureGenerator;
import mrp_v2.mrplibrary.datagen.providers.TextureProvider;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;

import java.awt.image.BufferedImage;

public class AnimatedBasicBlockData extends BasicColoredBlockData
{
    public AnimatedBasicBlockData(Block baseBlock)
    {
        super(baseBlock);
    }

    public AnimatedBasicBlockData(Block baseBlock, ITag.INamedTag<Block>[] additionalBlockTags)
    {
        super(baseBlock, additionalBlockTags);
    }

    public AnimatedBasicBlockData(Block baseBlock, ITag.INamedTag<Block>[] additionalBlockTags,
            ITag.INamedTag<Item>[] additionalItemTags)
    {
        super(baseBlock, additionalBlockTags, additionalItemTags);
    }

    public AnimatedBasicBlockData(Block baseBlock, double levelAdjustment, ITag.INamedTag<Block>[] additionalBlockTags,
            ITag.INamedTag<Item>[] additionalItemTags)
    {
        super(baseBlock, levelAdjustment, additionalBlockTags, additionalItemTags);
    }

    @Override public void registerTextures(TextureGenerator generator, TextureProvider.FinishedTextureConsumer consumer)
    {
        BufferedImage texture = generator.getTexture(new ResourceLocation(baseBlock.getRegistryName().getNamespace(),
                "block/" + baseBlock.getRegistryName().getPath()));
        TextureProvider.makeGrayscale(texture);
        TextureProvider.adjustLevels(texture, levelAdjustment);
        generator.finish(texture, generator.getTextureMeta(
                new ResourceLocation(getBaseBlockLoc().getNamespace(), "block/" + getBaseBlockLoc().getPath())),
                new ResourceLocation(AdditionalColors.ID, "block/" + baseBlock.getRegistryName().getPath()), consumer);
    }
}
