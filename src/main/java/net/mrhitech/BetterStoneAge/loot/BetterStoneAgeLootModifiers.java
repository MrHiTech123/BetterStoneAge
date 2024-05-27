package net.mrhitech.BetterStoneAge.loot;

import com.mojang.serialization.Codec;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.mrhitech.BetterStoneAge.BetterStoneAge;

public class BetterStoneAgeLootModifiers {

    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIER_SERIALIZERS = DeferredRegister.create(
            ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, BetterStoneAge.MOD_ID);

    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> ADD_ITEM_STACK = LOOT_MODIFIER_SERIALIZERS.register("add_itemstack", AddItemStackModifier.CODEC);
    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> ADD_ITEM_STACK_MIN_MAX = LOOT_MODIFIER_SERIALIZERS.register("add_itemstack_min_max", AddItemStackMinMaxModifier.CODEC);

    public static void register(IEventBus bus) {LOOT_MODIFIER_SERIALIZERS.register(bus);}
}
