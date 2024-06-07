package net.mrhitech.BetterStoneAge.client;

import net.dries007.tfc.util.Helpers;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.mrhitech.BetterStoneAge.BetterStoneAge;

public class BetterStoneAgeSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Registries.SOUND_EVENT, BetterStoneAge.MOD_ID);
    
    public static final RegistryObject<SoundEvent> KNAP_BONE = SOUNDS.register("item.knapping.bone", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(BetterStoneAge.MOD_ID, "item.knapping.bone")));
    
    public static void register(IEventBus bus) {SOUNDS.register(bus);}
}
