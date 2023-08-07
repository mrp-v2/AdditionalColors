package mrp_v2.additionalcolors.api.datagen;

import mrp_v2.mrplibrary.datagen.providers.LanguageProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.registries.RegistryObject;

public abstract class LanguageGenerator extends LanguageProvider
{
    public LanguageGenerator(DataGenerator gen, String modid, String locale)
    {
        super(gen, modid, locale);
    }

    public <T extends Block> void addSimpleBlock(RegistryObject<T> blockObj)
    {
        this.addBlock(blockObj, objToName(blockObj));
    }

    public static String objToName(RegistryObject<?> obj)
    {
        String[] words = obj.getId().getPath().split("_");
        StringBuilder name = new StringBuilder();
        for (int i = 0; i < words.length; i++)
        {
            String word = words[i];
            if (i != 0)
            {
                name.append(' ');
            }
            name.append(word.substring(0, 1).toUpperCase());
            name.append(word.substring(1));
        }
        return name.toString();
    }
}
