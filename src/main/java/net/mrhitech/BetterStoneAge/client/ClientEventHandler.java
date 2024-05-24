package net.mrhitech.BetterStoneAge.client;


import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import net.dries007.tfc.client.model.ContainedFluidModel;
import net.mrhitech.BetterStoneAge.common.item.BetterStoneAgeItems;

public class ClientEventHandler {

    public static void init() {
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.addListener(ClientEventHandler::registerColorHandlerItems);
    }

    public static void registerColorHandlerItems(RegisterColorHandlersEvent.Item event) {
        BetterStoneAgeItems.GLAZED_CERAMIC_JUGS.values().forEach(reg -> event.register(new ContainedFluidModel.Colors(), reg.get()));
    }

}
