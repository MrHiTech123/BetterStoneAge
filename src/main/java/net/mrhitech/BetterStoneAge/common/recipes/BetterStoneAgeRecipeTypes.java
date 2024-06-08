package net.mrhitech.BetterStoneAge.common.recipes;

import net.dries007.tfc.common.recipes.PotRecipe;
import net.dries007.tfc.util.Helpers;
import net.minecraft.resources.ResourceLocation;
import net.mrhitech.BetterStoneAge.BetterStoneAge;

public class BetterStoneAgeRecipeTypes {
    public static void registerPotRecipeOutputTypes() {
        PotRecipe.register(new ResourceLocation(BetterStoneAge.MOD_ID, "porridge"), PorridgePotRecipe.OUTPUT_TYPE);
    }
}
