
package net.mrhitech.BetterStoneAge.common.recipes;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.JsonObject;
import net.dries007.tfc.client.render.blockentity.PotBlockEntityRenderer;
import net.dries007.tfc.common.recipes.PotRecipe;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.items.ItemHandlerHelper;
import net.mrhitech.BetterStoneAge.common.item.BetterStoneAgeItems;
import org.jetbrains.annotations.Nullable;
import net.dries007.tfc.common.recipes.SoupPotRecipe;

import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blockentities.PotBlockEntity;
import net.dries007.tfc.common.capabilities.food.DynamicBowlHandler;
import net.dries007.tfc.common.capabilities.food.FoodCapability;
import net.dries007.tfc.common.capabilities.food.FoodData;
import net.dries007.tfc.common.capabilities.food.IFood;
import net.dries007.tfc.common.capabilities.food.Nutrient;
import net.dries007.tfc.common.fluids.TFCFluids;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.common.recipes.ingredients.FluidStackIngredient;
import net.dries007.tfc.compat.jade.common.BlockEntityTooltip;
import net.dries007.tfc.compat.jade.common.BlockEntityTooltips;
import net.dries007.tfc.util.Helpers;



public class PorridgePotRecipe extends PotRecipe
{
    public static final OutputType OUTPUT_TYPE = nbt -> {
        ItemStack stack = ItemStack.of(nbt.getCompound("item"));
        return new PorridgeOutput(stack);
    };

    public static final int PORRIDGE_HUNGER_VALUE = 4;
    public static final float PORRIDGE_DECAY_MODIFIER = 3.5F;

    public PorridgePotRecipe(ResourceLocation id, List<Ingredient> itemIngredients, FluidStackIngredient fluidIngredient, int duration, float minTemp)
    {
        super(id, itemIngredients, fluidIngredient, duration, minTemp);
    }

    @Override
    public Output getOutput(PotBlockEntity.PotInventory inventory)
    {
        int ingredientCount = 0;
        float water = 20, saturation = 2;
        float[] nutrition = new float[Nutrient.TOTAL];
        ItemStack porridgeStack = ItemStack.EMPTY;
        final List<ItemStack> itemIngredients = new ArrayList<>();
        for (int i = PotBlockEntity.SLOT_EXTRA_INPUT_START; i <= PotBlockEntity.SLOT_EXTRA_INPUT_END; i++)
        {
            final ItemStack stack = inventory.getStackInSlot(i);
            final @Nullable IFood food = FoodCapability.get(stack);

            if (food != null)
            {
                itemIngredients.add(stack);
                if (food.isRotten()) // this should mostly not happen since the ingredients are not rotten to start, but worth checking
                {
                    ingredientCount = 0;
                    break;
                }
                final FoodData data = food.getData();
                water += data.water();
                saturation += data.saturation();
                for (Nutrient nutrient : Nutrient.VALUES)
                {
                    nutrition[nutrient.ordinal()] += data.nutrient(nutrient);
                }
                ingredientCount++;
            }
        }
        if (ingredientCount > 0)
        {
            float multiplier = 1 - (0.05f * ingredientCount); // per-serving multiplier of nutrition
            water *= multiplier; saturation *= multiplier;
            for (Nutrient nutrient : Nutrient.VALUES)
            {
                final int idx = nutrient.ordinal();
                nutrition[idx] *= multiplier;
            }
            FoodData data = FoodData.create(PORRIDGE_HUNGER_VALUE, water, saturation, nutrition, PORRIDGE_DECAY_MODIFIER);
            int servings = (int) (ingredientCount / 2f) + 1;
            long created = FoodCapability.getRoundedCreationDate();

            porridgeStack = new ItemStack(BetterStoneAgeItems.PORRIDGE.get(), servings);
            final @Nullable IFood food = FoodCapability.get(porridgeStack);

            if (food instanceof DynamicBowlHandler handler)
            {
                handler.setCreationDate(created);
                handler.setIngredients(itemIngredients);
                handler.setFood(data);
            }
        }

        System.out.println(porridgeStack);
        System.out.println(porridgeStack == null);
        System.out.println(new PorridgeOutput(porridgeStack));
        System.out.println(new PorridgeOutput(porridgeStack) == null);

        System.out.println("Is it empty: " + porridgeStack.isEmpty());

        return new PorridgeOutput(porridgeStack);
    }

    @Override
    public RecipeSerializer<?> getSerializer()
    {
        return BetterStoneAgeRecipeSerializers.POT_PORRIDGE.get();
    }

    public record PorridgeOutput(ItemStack stack) implements Output
    {
        @Override
        public boolean isEmpty()
        {
            return stack.isEmpty();
        }

        @Override
        public InteractionResult onInteract(PotBlockEntity entity, Player player, ItemStack clickedWith)
        {
            if (Helpers.isItem(clickedWith.getItem(), TFCTags.Items.SOUP_BOWLS) && !stack.isEmpty())
            {
                // set the internal bowl to the one we clicked with
                if (FoodCapability.get(stack) instanceof DynamicBowlHandler handler)
                {
                    handler.setBowl(clickedWith);
                }

                // take the player's bowl, give a porridge
                clickedWith.shrink(1);
                ItemHandlerHelper.giveItemToPlayer(player, stack.split(1));
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        }

        @Override
        public int getFluidColor()
        {
            return TFCFluids.ALPHA_MASK | 0xEAC97F;
        }

        @Override
        public void write(CompoundTag nbt)
        {
            nbt.put("item", stack.save(new CompoundTag()));
        }

        @Override
        public OutputType getType()
        {
            return PorridgePotRecipe.OUTPUT_TYPE;
        }

        @Override
        public BlockEntityTooltip getTooltip()
        {
            return ((level, state, pos, entity, tooltip) -> {
                final List<Component> text = new ArrayList<>();
                BlockEntityTooltips.itemWithCount(tooltip, stack);
                FoodCapability.addTooltipInfo(stack, text);
                text.forEach(tooltip);
            });
        }
    }

    public static class Serializer extends PotRecipe.Serializer<PorridgePotRecipe>
    {
        @Override
        protected PorridgePotRecipe fromJson(ResourceLocation recipeId, JsonObject json, List<Ingredient> ingredients, FluidStackIngredient fluidIngredient, int duration, float minTemp)
        {
            return new PorridgePotRecipe(recipeId, ingredients, fluidIngredient, duration, minTemp);
        }

        @Override
        protected PorridgePotRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer, List<Ingredient> ingredients, FluidStackIngredient fluidIngredient, int duration, float minTemp)
        {
            return new PorridgePotRecipe(recipeId, ingredients, fluidIngredient, duration, minTemp);
        }
    }
}