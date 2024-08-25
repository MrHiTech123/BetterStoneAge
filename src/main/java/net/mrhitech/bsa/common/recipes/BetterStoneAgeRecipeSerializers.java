package net.mrhitech.bsa.common.recipes;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.mrhitech.bsa.BetterStoneAge;

@SuppressWarnings("unused")
public class BetterStoneAgeRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, BetterStoneAge.MOD_ID);

    public static final RegistryObject<PorridgePotRecipe.Serializer> POT_PORRIDGE = RECIPE_SERIALIZERS.register("pot_porridge", PorridgePotRecipe.Serializer::new);
    
}
