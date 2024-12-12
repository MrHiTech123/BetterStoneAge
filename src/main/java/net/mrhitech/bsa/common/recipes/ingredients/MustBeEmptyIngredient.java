package net.mrhitech.bsa.common.recipes.ingredients;


import com.google.gson.JsonObject;
import net.dries007.tfc.common.capabilities.Capabilities;
import net.dries007.tfc.common.recipes.ingredients.DelegateIngredient;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.JsonHelpers;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicBoolean;

public class MustBeEmptyIngredient extends DelegateIngredient {
    
    public MustBeEmptyIngredient(@Nullable Ingredient delegate) {
        super(delegate);
    }
    
    @Override
    public boolean test(@Nullable ItemStack stack) {
        AtomicBoolean toReturn = new AtomicBoolean(super.test(stack));
        
        if (toReturn.get() && stack != null && !stack.isEmpty()) {
            stack.getCapability(Capabilities.FLUID_ITEM).ifPresent(cap -> {
                toReturn.set(cap.getFluidInTank(0).getAmount() == 0);
            });
        }
        
        return toReturn.get();
    }
    
    @Override
    public IIngredientSerializer<? extends DelegateIngredient> getSerializer()
    {
        return Serializer.INSTANCE;
    }
    
    
    
    public enum Serializer implements IIngredientSerializer<MustBeEmptyIngredient>
    {
        INSTANCE;
        
        @Override
        public MustBeEmptyIngredient parse(FriendlyByteBuf buffer)
        {
            final Ingredient internal = Helpers.decodeNullable(buffer, Ingredient::fromNetwork);
            return new MustBeEmptyIngredient(internal);
        }
        
        @Override
        public MustBeEmptyIngredient parse(JsonObject json)
        {
            final Ingredient internal = json.has("ingredient") ? Ingredient.fromJson(JsonHelpers.get(json, "ingredient")) : null;
            return new MustBeEmptyIngredient(internal);
        }
        
        @Override
        public void write(FriendlyByteBuf buffer, MustBeEmptyIngredient ingredient)
        {
            Helpers.encodeNullable(ingredient.delegate, buffer, Ingredient::toNetwork);
        }
    }
    
    
}
