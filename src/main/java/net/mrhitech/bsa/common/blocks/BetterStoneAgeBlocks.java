package net.mrhitech.bsa.common.blocks;

import net.dries007.tfc.common.blockentities.AbstractFirepitBlockEntity;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.GroundcoverBlock;
import net.dries007.tfc.common.blocks.devices.PotBlock;
import net.dries007.tfc.common.blocks.wood.TFCDoorBlock;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.util.registry.RegistrationHelpers;
import net.dries007.tfc.util.registry.RegistryWood;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.dries007.tfc.util.Helpers;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraft.world.item.DyeColor;
import net.minecraftforge.registries.RegistryObject;
import net.mrhitech.bsa.BetterStoneAge;
import net.mrhitech.bsa.common.blocks.devices.CustomPotBlock;
import net.mrhitech.bsa.common.blocks.devices.DryingSinewBlock;
import net.mrhitech.bsa.common.item.BetterStoneAgeItems;
import org.jetbrains.annotations.Nullable;
import net.mrhitech.bsa.common.blockentities.BetterStoneAgeBlockEntities;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import static net.dries007.tfc.common.blocks.TFCBlocks.litBlockEmission;


public class BetterStoneAgeBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, BetterStoneAge.MOD_ID);

    public static final Map<DyeColor, RegistryObject<Block>> GLAZED_POTS = Helpers.mapOfKeys(DyeColor.class, color ->
            BLOCKS.register("ceramic/pot/" + color, () -> new CustomPotBlock(
                    ExtendedProperties.of(MapColor.DIRT).strength(0.4F, 0.4F).sound(SoundType.NETHER_WART).randomTicks().noOcclusion().lightLevel(litBlockEmission(15)).blockEntity(TFCBlockEntities.POT).pathType(BlockPathTypes.DAMAGE_FIRE).<AbstractFirepitBlockEntity<?>>ticks(AbstractFirepitBlockEntity::serverTick, AbstractFirepitBlockEntity::clientTick),
                    BetterStoneAgeItems.GLAZED_POTS.get(color)
            )));
    
    public static final RegistryObject<Block> SINEW = BLOCKS.register("sinew", () -> new DryingSinewBlock(ExtendedProperties.of(MapColor.COLOR_PINK).noCollission().noOcclusion().instabreak().blockEntity(BetterStoneAgeBlockEntities.TICK_COUNTER), () -> BetterStoneAgeItems.DRIED_SINEW.get()));
    
    public static final RegistryObject<Block> HIDE_DOOR = BLOCKS.register("hide_door", () -> new TFCDoorBlock(ExtendedProperties.of(DyeColor.BLUE).sound(SoundType.WOOD).instrument(NoteBlockInstrument.BASS).strength(0.3F).noOcclusion(), BlockSetType.DARK_OAK));
    
    public static final Map<BetterStoneAgeGroundcoverBlockType, RegistryObject<Block>> GROUNDCOVER = Helpers.mapOfKeys(BetterStoneAgeGroundcoverBlockType.class, type -> 
            register("groundcover/" + type.getSerializedName(), () -> groundcover(type), type.createBlockItem()));
    
    
    private static ExtendedProperties properties(RegistryWood wood)
    {
        return ExtendedProperties.of(wood.woodColor()).sound(SoundType.WOOD).instrument(NoteBlockInstrument.BASS);
    }
    
    private static GroundcoverBlock groundcover(BetterStoneAgeGroundcoverBlockType cover) {
        return new GroundcoverBlock(ExtendedProperties.of(MapColor.PLANT).strength(0.05F, 0.0F).sound(SoundType.NETHER_WART).noCollission().pushReaction(PushReaction.DESTROY), cover.getShape(), cover.getVanillaItem());
    }
    
    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> blockSupplier) {
        return register(name, blockSupplier, (block) -> {
            return new BlockItem(block, new Item.Properties());
        });
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> blockSupplier, Item.Properties blockItemProperties) {
        return register(name, blockSupplier, (block) -> {
            return new BlockItem(block, blockItemProperties);
        });
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> blockSupplier, @Nullable Function<T, ? extends BlockItem> blockItemFactory) {
        return RegistrationHelpers.registerBlock(BLOCKS, TFCItems.ITEMS, name, blockSupplier, blockItemFactory);
    }

    public static void register(IEventBus bus) {BLOCKS.register(bus);}
}
