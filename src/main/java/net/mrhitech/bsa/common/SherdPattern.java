package net.mrhitech.bsa.common;

import java.util.Locale;

public enum SherdPattern {
    ANGLER,
    ARCHER,
    ARMS_UP,
    BLADE,
    BLANK,
    BREWER,
    BURN,
    DANGER,
    EXPLORER,
    FRIEND,
    HEART,
    HEARTBREAK,
    HOWL,
    MINER,
    MOURNER,
    PLENTY,
    PRIZE,
    SHEAF,
    SHELTER,
    SKULL,
    SNORT;
    
    public String getSerializedName() {
        return name().toLowerCase(Locale.ROOT);
    }
    
}
