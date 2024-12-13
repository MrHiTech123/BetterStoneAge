package net.mrhitech.bsa;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
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
import net.mrhitech.bsa.client.BetterStoneAgeSounds;
import net.mrhitech.bsa.client.ClientEventHandler;
import net.mrhitech.bsa.common.blockentities.BetterStoneAgeBlockEntities;
import net.mrhitech.bsa.common.blocks.BetterStoneAgeBlocks;
import net.mrhitech.bsa.common.creative.BetterStoneAgeCreativeTabs;
import net.mrhitech.bsa.common.item.BetterStoneAgeItems;
import net.mrhitech.bsa.common.recipes.BetterStoneAgeRecipeSerializers;
import net.mrhitech.bsa.common.recipes.BetterStoneAgeRecipeTypes;
import net.mrhitech.bsa.common.recipes.ingredients.BetterStoneAgeIngredients;
import net.mrhitech.bsa.common.recipes.outputs.BetterStoneAgeItemStackModifiers;
import net.mrhitech.bsa.event.ForgeEventHandler;
import net.mrhitech.bsa.event.SetupEvents;
import net.mrhitech.bsa.loot.BetterStoneAgeLootModifiers;
import org.slf4j.Logger;

import net.dries007.tfc.common.TFCCreativeTabs;




// The value here should match an entry in the META-INF/mods.toml file
@Mod(BetterStoneAge.MOD_ID)
public class BetterStoneAge
{
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "bsa";
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
        BetterStoneAgeSounds.register(modEventBus);
        BetterStoneAgeRecipeTypes.registerPotRecipeOutputTypes();
        BetterStoneAgeIngredients.registerIngredientTypes();
        BetterStoneAgeItemStackModifiers.registerItemStackModifierTypes();
        ForgeEventHandler.init();
        SetupEvents.init();
        
        


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
        BetterStoneAgeCreativeTabs.addCreative(event);
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
            
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}


