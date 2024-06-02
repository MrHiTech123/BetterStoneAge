package net.mrhitech.BetterStoneAge;

import com.mojang.logging.LogUtils;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.plant.TFCBushBlock;
import net.dries007.tfc.common.blocks.rock.RockCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.ForgeRegistries;
import net.mrhitech.BetterStoneAge.client.ClientEventHandler;
import net.mrhitech.BetterStoneAge.client.screen.ClayTabletEditScreen;
import net.mrhitech.BetterStoneAge.common.blockentities.BetterStoneAgeBlockEntities;
import net.mrhitech.BetterStoneAge.common.blocks.BetterStoneAgeBlocks;
import net.mrhitech.BetterStoneAge.common.food.BetterStoneAgeFoods;
import net.mrhitech.BetterStoneAge.common.item.BetterStoneAgeItems;
import net.mrhitech.BetterStoneAge.common.blockentities.BetterStoneAgeBlockEntities;
import net.mrhitech.BetterStoneAge.common.recipes.BetterStoneAgeRecipeSerializers;
import net.mrhitech.BetterStoneAge.loot.BetterStoneAgeLootModifiers;
import org.slf4j.Logger;

import net.dries007.tfc.common.TFCCreativeTabs;



// The value here should match an entry in the META-INF/mods.toml file
@Mod(BetterStoneAge.MOD_ID)
public class BetterStoneAge
{
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "better_stone_age";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    public BetterStoneAge()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        BetterStoneAgeItems.register(modEventBus);
        BetterStoneAgeBlocks.register(modEventBus);
        BetterStoneAgeBlockEntities.register(modEventBus);
        BetterStoneAgeLootModifiers.register(modEventBus);
        BetterStoneAgeRecipeSerializers.RECIPE_SERIALIZERS.register(modEventBus);


        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);


        if (FMLEnvironment.dist == Dist.CLIENT) {
            ClientEventHandler.init();
        }
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");
        LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if (event.getTabKey() == TFCCreativeTabs.EARTH.tab().getKey()) {
            for (DyeColor color : DyeColor.values()) {
                event.accept(BetterStoneAgeItems.UNFIRED_CERAMIC_JUGS.get(color));
                event.accept(BetterStoneAgeItems.GLAZED_CERAMIC_JUGS.get(color));
                event.accept(BetterStoneAgeItems.GLAZED_POTS.get(color));
            }

            for (RockCategory rock_category : RockCategory.values()) {
                event.accept(BetterStoneAgeItems.MULTITOOL_HEADS.get(rock_category));
            }
        }
        if (event.getTabKey() == TFCCreativeTabs.MISC.tab().getKey()) {
            event.accept(BetterStoneAgeItems.SINEW);
            event.accept(BetterStoneAgeItems.DRIED_SINEW);
            event.accept(BetterStoneAgeItems.POUNDED_SINEW);
            event.accept(BetterStoneAgeItems.SINEW_STRING);
        }
        if (event.getTabKey() == TFCCreativeTabs.FOOD.tab().getKey()) {
            event.accept(BetterStoneAgeItems.CRUSHED_BARLEY_GRAIN);
            event.accept(BetterStoneAgeItems.CRUSHED_OAT_GRAIN);
            event.accept(BetterStoneAgeItems.CRUSHED_RYE_GRAIN);
            event.accept(BetterStoneAgeItems.CRUSHED_WHEAT_GRAIN);

            event.accept(BetterStoneAgeItems.COARSE_BARLEY_FLOUR);
            event.accept(BetterStoneAgeItems.COARSE_MAIZE_FLOUR);
            event.accept(BetterStoneAgeItems.COARSE_OAT_FLOUR);
            event.accept(BetterStoneAgeItems.COARSE_RICE_FLOUR);
            event.accept(BetterStoneAgeItems.COARSE_RYE_FLOUR);
            event.accept(BetterStoneAgeItems.COARSE_WHEAT_FLOUR);

            event.accept(BetterStoneAgeItems.PORRIDGE);

        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            ItemBlockRenderTypes.setRenderLayer(BetterStoneAgeBlocks.SINEW.get(), RenderType.translucent());
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}


