package net.mrhitech.bsa.client;


import net.dries007.tfc.client.render.blockentity.PotBlockEntityRenderer;
import net.dries007.tfc.common.items.TFCFishingRodItem;
import net.dries007.tfc.util.Helpers;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import net.dries007.tfc.client.model.ContainedFluidModel;
import net.mrhitech.bsa.common.blockentities.BetterStoneAgeBlockEntities;
import net.mrhitech.bsa.common.item.BetterStoneAgeItems;

import java.util.function.Predicate;


public class ClientEventHandler {

    public static void init() {
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.addListener(ClientEventHandler::registerColorHandlerItems);
        bus.addListener(ClientEventHandler::ClientSetup);
        bus.addListener(ClientEventHandler::registerEntityRenderers);
    }
    
    @SuppressWarnings("deprecation")
    public static void ClientSetup(FMLClientSetupEvent event) {
        
        event.enqueueWork(
                () -> {
                    Item rod = BetterStoneAgeItems.BONE_FISHING_ROD.get();
                    
                    ItemProperties.register(rod, Helpers.identifier("cast"), (stack, level, entity, unused) -> {
                        if (entity == null)
                        {
                            return 0.0F;
                        }
                        else
                        {
                            return entity instanceof Player player && TFCFishingRodItem.isThisTheHeldRod(player, stack) && player.fishing != null ? 1.0F : 0.0F;
                        }
                    });
                    
                }
        );
        final RenderType cutoutMipped = RenderType.cutoutMipped();
        final Predicate<RenderType> ghostBlock = rt -> rt == cutoutMipped || rt == Sheets.translucentCullBlockSheet();
        
        
        
    }
    
    
    public static void registerColorHandlerItems(RegisterColorHandlersEvent.Item event) {
        BetterStoneAgeItems.GLAZED_CERAMIC_JUGS.values().forEach(reg -> event.register(new ContainedFluidModel.Colors(), reg.get()));
    }
    
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        for (DyeColor color : DyeColor.values()) {
            event.registerBlockEntityRenderer(BetterStoneAgeBlockEntities.GLAZED_POTS.get(color).get(), ctx -> new PotBlockEntityRenderer());
        }
    }

}
