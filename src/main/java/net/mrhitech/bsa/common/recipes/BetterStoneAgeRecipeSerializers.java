package net.mrhitech.bsa.common.recipes;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.mrhitech.bsa.BetterStoneAge;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public class BetterStoneAgeRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, BetterStoneAge.MOD_ID);
    
    public static final RegistryObject<PorridgePotRecipe.Serializer> POT_PORRIDGE = RECIPE_SERIALIZERS.register("pot_porridge", PorridgePotRecipe.Serializer::new);
    
    public static void register(IEventBus bus) {
        RECIPE_SERIALIZERS.register(bus);
    }
    
}
