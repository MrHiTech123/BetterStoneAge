package net.mrhitech.bsa.common.recipes.ingredients;

import net.dries007.tfc.util.Helpers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import net.mrhitech.bsa.BetterStoneAge;

public class BetterStoneAgeIngredients {
    
    public static void registerIngredientTypes() {
        register("must_be_empty", MustBeEmptyIngredient.Serializer.INSTANCE);
    }
    
    private static <T extends Ingredient> void register(String name, IIngredientSerializer<T> serializer)
    {
        CraftingHelper.register(new ResourceLocation(BetterStoneAge.MOD_ID, name), serializer);
    }
}
