package net.mrhitech.bsa.common;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.RegistryObject;
import net.mrhitech.bsa.common.item.BetterStoneAgeItems;

import java.util.Locale;
import java.util.function.Supplier;

public enum SherdPattern {
    ANGLER(() -> Items.ANGLER_POTTERY_SHERD),
    ARCHER(() -> Items.ARCHER_POTTERY_SHERD),
    ARMS_UP(() -> Items.ARMS_UP_POTTERY_SHERD),
    BLADE(() -> Items.BLADE_POTTERY_SHERD),
    BLANK(() -> BetterStoneAgeItems.FIRED_BLANK_SHERD.get()),
    BREWER(() -> Items.BREWER_POTTERY_SHERD),
    BURN(() -> Items.BURN_POTTERY_SHERD),
    DANGER(() -> Items.DANGER_POTTERY_SHERD),
    EXPLORER(() -> Items.EXPLORER_POTTERY_SHERD),
    FRIEND(() -> Items.FRIEND_POTTERY_SHERD),
    HEART(() -> Items.HEART_POTTERY_SHERD),
    HEARTBREAK(() -> Items.HEARTBREAK_POTTERY_SHERD),
    HOWL(() -> Items.HOWL_POTTERY_SHERD),
    MINER(() -> Items.MINER_POTTERY_SHERD),
    MOURNER(() -> Items.MOURNER_POTTERY_SHERD),
    PLENTY(() -> Items.PLENTY_POTTERY_SHERD),
    PRIZE(() -> Items.PRIZE_POTTERY_SHERD),
    SHEAF(() -> Items.SHEAF_POTTERY_SHERD),
    SHELTER(() -> Items.SHELTER_POTTERY_SHERD),
    SKULL(() -> Items.SKULL_POTTERY_SHERD),
    SNORT(() -> Items.SNORT_POTTERY_SHERD);
    
    private final Supplier<Item> cookedSherd;
    
    SherdPattern(Supplier<Item> f_cookedSherd) {
        cookedSherd = f_cookedSherd;
    }
    
    public Item getCookedSherd() {
        return cookedSherd.get();
    }
    
    public String getSerializedName() {
        return name().toLowerCase(Locale.ROOT);
    }
    
}
