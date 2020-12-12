package mrp_v2.additionalcolors.datagen;

import mrp_v2.additionalcolors.item.ColoredBlockItem;
import mrp_v2.additionalcolors.util.ObjectHolder;
import mrp_v2.mrplibrary.datagen.LanguageProvider;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.fml.RegistryObject;
import org.apache.commons.lang3.tuple.Pair;

public class EN_USTranslationGenerator extends LanguageProvider
{
    public EN_USTranslationGenerator(DataGenerator gen, String modid)
    {
        super(gen, modid, "en_us");
    }

    @Override protected void addTranslations()
    {
        ObjectHolder.CRYING_OBSIDIAN_HANDLER.generateTranslations(this);
        ObjectHolder.COLORIZED_BLOCK_MAP.values().forEach(set -> set.forEach(this::addSimpleBlock));
    }

    public <T extends Block> void addSimpleBlock(Pair<RegistryObject<T>, RegistryObject<ColoredBlockItem>> objPair)
    {
        this.addBlock(objPair.getLeft(), objToName(objPair.getLeft()));
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

    public <T extends Block> void addSimpleBlock(RegistryObject<T> blockObj)
    {
        this.addBlock(blockObj, objToName(blockObj));
    }
}
