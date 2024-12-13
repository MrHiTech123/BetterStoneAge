package net.mrhitech.bsa.common.recipes;

import net.dries007.tfc.common.recipes.PotRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.mrhitech.bsa.BetterStoneAge;
import net.minecraft.world.item.crafting.Recipe;

public class BetterStoneAgeRecipeTypes {
    
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, BetterStoneAge.MOD_ID);
    
    public static void registerPotRecipeOutputTypes() {
        PotRecipe.register(new ResourceLocation(BetterStoneAge.MOD_ID, "porridge"), PorridgePotRecipe.OUTPUT_TYPE);
    }
    
    private static <R extends Recipe<?>> RegistryObject<RecipeType<R>> register(String name)
    {
        return RECIPE_TYPES.register(name, () -> new RecipeType<>() {
            @Override
            public String toString()
            {
                return name;
            }
        });
    }
}
