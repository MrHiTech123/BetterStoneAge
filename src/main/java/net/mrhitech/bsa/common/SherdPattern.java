package net.mrhitech.bsa.common;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.RegistryObject;
import net.mrhitech.bsa.common.item.BetterStoneAgeItems;

import java.util.Locale;
import java.util.function.Supplier;

public enum SherdPattern {
    ANGLER(),
    ARCHER(),
    ARMS_UP(),
    BLADE(),
    BLANK(),
    BREWER(),
    BURN(),
    DANGER(),
    EXPLORER(),
    FRIEND(),
    HEART(),
    HEARTBREAK(),
    HOWL(),
    MINER(),
    MOURNER(),
    PLENTY(),
    PRIZE(),
    SHEAF(),
    SHELTER(),
    SKULL(),
    SNORT();
    
    public String getSerializedName() {
        return name().toLowerCase(Locale.ROOT);
    }
    
}
