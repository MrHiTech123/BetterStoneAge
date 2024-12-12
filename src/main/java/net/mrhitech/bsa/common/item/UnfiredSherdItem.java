package net.mrhitech.bsa.common.item;

import net.minecraft.world.item.Item;
import net.mrhitech.bsa.common.SherdPattern;

public class UnfiredSherdItem extends Item {
    
    private final SherdPattern pattern;
    
    public UnfiredSherdItem(Properties pProperties, SherdPattern f_pattern) {
        super(pProperties);
        pattern = f_pattern;
    }
    
    public SherdPattern getPattern() {
        return pattern;
    }
    
}
