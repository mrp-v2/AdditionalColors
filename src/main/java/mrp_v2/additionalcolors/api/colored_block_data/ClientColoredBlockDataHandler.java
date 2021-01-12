package mrp_v2.additionalcolors.api.colored_block_data;

import mrp_v2.additionalcolors.client.renderer.color.BlockAndItemColorer;
import net.minecraft.block.Block;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientColoredBlockDataHandler
{
    private final ColoredBlockDataHandler parent;

    public ClientColoredBlockDataHandler(ColoredBlockDataHandler parent)
    {
        this.parent = parent;
    }

    @SubscribeEvent public void itemColors(ColorHandlerEvent.Item event)
    {
        for (AbstractColoredBlockData<?> coloredBlockData : parent.getColoredBlockDatas())
        {
            if (coloredBlockData.requiresTinting())
            {
                for (RegistryObject<? extends Block> itemObject : coloredBlockData.getBlockObjects())
                {
                    event.getItemColors().register(BlockAndItemColorer.INSTANCE, itemObject.get());
                }
            }
        }
    }

    @SubscribeEvent public void blockColors(ColorHandlerEvent.Block event)
    {
        for (AbstractColoredBlockData<?> coloredBlockData : parent.getColoredBlockDatas())
        {
            if (coloredBlockData.requiresTinting())
            {
                for (RegistryObject<? extends Block> blockObject : coloredBlockData.getBlockObjects())
                {
                    event.getBlockColors().register(BlockAndItemColorer.INSTANCE, blockObject.get());
                }
            }
        }
    }

    @SubscribeEvent public void clientSetup(FMLClientSetupEvent event)
    {
        for (AbstractColoredBlockData<?> coloredBlockData : parent.getColoredBlockDatas())
        {
            coloredBlockData.clientSetup(event);
        }
    }
}
