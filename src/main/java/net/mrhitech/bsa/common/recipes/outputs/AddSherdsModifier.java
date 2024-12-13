package net.mrhitech.bsa.common.recipes.outputs;

import com.google.common.collect.Lists;
import net.dries007.tfc.common.recipes.RecipeHelpers;
import net.dries007.tfc.common.recipes.outputs.ItemStackModifier;
import net.dries007.tfc.util.Helpers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import net.mrhitech.bsa.common.BetterStoneAgeTags;
import net.mrhitech.bsa.common.item.BetterStoneAgeItems;
import net.mrhitech.bsa.common.item.UnfiredSherdItem;

import java.util.ArrayList;

public enum AddSherdsModifier implements ItemStackModifier.SingleInstance<AddSherdsModifier> {
    INSTANCE;
    
    @Override
    public AddSherdsModifier instance() {
        return INSTANCE;
    }
    
    @Override
    public ItemStack apply(ItemStack stack, ItemStack input) {
        ArrayList<ItemStack> ingredients = Lists.newArrayList(RecipeHelpers.getCraftingInput());
        
        Item[] sherds = new Item[4];
        
        int currSherd = 0;
        for (ItemStack currentIngredient : ingredients) {
            if (Helpers.isItem(currentIngredient, BetterStoneAgeTags.Items.UNFIRED_SHERDS)) {
                sherds[currSherd] = currentIngredient.getItem();
                ++currSherd;
                if (currSherd >= 4) {
                    break;
                }
            }
        }
        
        DecoratedPotBlockEntity.Decorations decorations = new DecoratedPotBlockEntity.Decorations(
                cookedVersion(sherds[0]),
                cookedVersion(sherds[1]),
                cookedVersion(sherds[2]),
                cookedVersion(sherds[3])
        );
        
        return createDecoratedPotItem(stack, decorations);
    }
    
    public static ItemStack createDecoratedPotItem(ItemStack stack, DecoratedPotBlockEntity.Decorations pDecorations) {
        CompoundTag compoundtag = pDecorations.save(new CompoundTag());
        BlockItem.setBlockEntityData(stack, BlockEntityType.DECORATED_POT, compoundtag);
        return stack;
    }
    
    public Item cookedVersion(Item item) {
        if (item instanceof UnfiredSherdItem unfiredSherd) {
            return unfiredSherd.getPattern().getCookedSherd();
        }
        return item;
    }
}
