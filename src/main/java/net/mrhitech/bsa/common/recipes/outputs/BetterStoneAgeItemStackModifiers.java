package net.mrhitech.bsa.common.recipes.outputs;

import net.dries007.tfc.common.recipes.outputs.ItemStackModifier;
import net.dries007.tfc.common.recipes.outputs.ItemStackModifiers;
import net.dries007.tfc.util.Helpers;
import net.minecraft.resources.ResourceLocation;
import net.mrhitech.bsa.BetterStoneAge;

public class BetterStoneAgeItemStackModifiers {
    public static void registerItemStackModifierTypes() {
        register("copy_sherds", CopySherdsModifier.INSTANCE);
    }
    
    
    private static void register(String name, ItemStackModifier.Serializer<?> serializer) {
        ItemStackModifiers.register(new ResourceLocation(BetterStoneAge.MOD_ID, name), serializer);
    }
}
