package net.mrhitech.BetterStoneAge.common.blockentities;

import com.mojang.datafixers.types.Type;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.util.Helpers;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraftforge.registries.RegistryObject;
import net.mrhitech.BetterStoneAge.BetterStoneAge;
import net.dries007.tfc.common.blockentities.PotBlockEntity;
import net.mrhitech.BetterStoneAge.common.blocks.BetterStoneAgeBlocks;

import java.util.Map;
import java.util.function.Supplier;

public class BetterStoneAgeBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, BetterStoneAge.MOD_ID);

    public static final Map<DyeColor, RegistryObject<BlockEntityType<?>>> GLAZED_POTS = Helpers.mapOfKeys(DyeColor.class, color ->
            BLOCK_ENTITES.register("ceramic/pot/" + color, () -> BlockEntityType.Builder.of(PotBlockEntity::new, new Block[]{(Block)BetterStoneAgeBlocks.GLAZED_POTS.get(color).get()}).build((Type)null)));

    public static void register(IEventBus bus) {BLOCK_ENTITES.register(bus);}
}
