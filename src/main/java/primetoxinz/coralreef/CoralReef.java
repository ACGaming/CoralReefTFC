package primetoxinz.coralreef;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Created by tyler on 8/17/16.
 */
@Mod.EventBusSubscriber(modid =  CoralReef.MODID)
@Mod(modid = CoralReef.MODID, name = CoralReef.NAME, version = CoralReef.VERSION)
public class CoralReef {

    public static final String MODID = "coralreef";
    public static final String NAME = "CoralReef";
    public static final String VERSION = "2.0";
    @Config(modid = MODID)
    public static class ConfigHandler {

        @Config.Comment("Chance for a Reef to generate in a chunk")
        public static int reefChance = 30;

        @Config.Comment("Number of blocks in a Reef")
        public static int reefCount = 32;

        @Config.Comment("Light level of coral")
        public static int coralLightLevel = 15;

        @Config.Comment("Only spawn in Oceans")
        public static boolean onlyOcean = false;

        @Config.Comment("Bubble Particles from coral")
        public static boolean bubbles = true;

    }



    public static BlockCoral coral;
    public static BlockReef reef;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {

        coral = new BlockCoral();
        reef = new BlockReef();
        proxy.registerItems();
        GameRegistry.registerWorldGenerator(new GeneratorReef(), 1);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        OreDictionary.registerOre("dyeOrange", new ItemStack(coral, 1, 0));
        OreDictionary.registerOre("dyeMagenta", new ItemStack(coral, 1, 1));
        OreDictionary.registerOre("dyePink", new ItemStack(coral, 1, 2));
        OreDictionary.registerOre("dyeCyan", new ItemStack(coral, 1, 3));
        OreDictionary.registerOre("dyeGreen", new ItemStack(coral, 1, 4));
        OreDictionary.registerOre("dyeGray", new ItemStack(coral, 1, 5));
    }

    @SidedProxy(clientSide = "primetoxinz.coralreef.CoralReef$ClientProxy", serverSide = "primetoxinz.coralreef.CoralReef$CommonProxy")
    public static CommonProxy proxy;


    @SubscribeEvent
    public static void onConfigChange(ConfigChangedEvent event) {
        if (event.getModID().equals(MODID)) {
            ConfigManager.sync(MODID, Config.Type.INSTANCE);
        }
    }

    public static class CommonProxy {
        public void registerItems() {
        }
    }

    public static class ClientProxy extends CommonProxy {
        @Override
        public void registerItems() {
            for (int i = 0; i <= 5; i++) {
                registerItemModel(Item.getItemFromBlock(coral), i, "coralreef:coral" + (i + 1));
            }
            for (int i = 0; i <= 1; i++) {
                registerItemModel(Item.getItemFromBlock(reef), i, "coralreef:reef" + (i + 1));
            }
        }

        public void registerItemModel(Item item, int meta, String name) {
            ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(name, "inventory"));
        }

    }
}
