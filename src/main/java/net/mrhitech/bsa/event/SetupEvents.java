package net.mrhitech.bsa.event;

import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegistryObject;
import net.mrhitech.bsa.common.blocks.BetterStoneAgeBlocks;
import net.mrhitech.bsa.mixin.BlockEntityTypeAccessor;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class SetupEvents {
    public static void init() {
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        
        bus.addListener(SetupEvents::setup);
    }
    
    private static void setup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            modifyBlockEntityTypes();
        });
    }
    private static void modifyBlockEntityTypes() {
        modifyBlockEntityType(TFCBlockEntities.POT.get(), BetterStoneAgeBlocks.GLAZED_POTS.values().stream().map(RegistryObject::get));
    }
    private static void modifyBlockEntityType(BlockEntityType<?> type, Stream<Block> extraBlocks)
    {
        Set<Block> blocks = ((BlockEntityTypeAccessor) (Object) type).accessor$getValidBlocks();
        blocks = new HashSet<>(blocks);
        blocks.addAll(extraBlocks.toList());
        ((BlockEntityTypeAccessor) (Object) type).accessor$setValidBlocks(blocks);
    }
}
