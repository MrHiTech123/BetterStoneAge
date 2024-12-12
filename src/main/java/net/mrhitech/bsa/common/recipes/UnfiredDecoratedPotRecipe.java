package net.mrhitech.bsa.common.recipes;

import net.dries007.tfc.util.Helpers;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import net.mrhitech.bsa.common.BetterStoneAgeTags;
import net.mrhitech.bsa.common.SherdPattern;
import net.mrhitech.bsa.common.item.BetterStoneAgeItems;
import net.mrhitech.bsa.common.item.UnfiredSherdItem;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class UnfiredDecoratedPotRecipe extends CustomRecipe {
    public UnfiredDecoratedPotRecipe(ResourceLocation pId, CraftingBookCategory pCategory) {
        super(pId, pCategory);
    }
    
    public static final Map<Integer, int[]> sherdLocationsInContainerOfSize = new HashMap<>() {
        {
            put(4, new int[]{0, 1, 2, 3, -1});
            put(9, new int[]{1, 3, 5, 7, -1});
        }
    };
    
    public boolean matches(CraftingContainer pContainer, Level pLevel) {
        int size = pContainer.getContainerSize();
        
        int[] whereTheSherdsAre = sherdLocationsInContainerOfSize.get(size);
        
        int currSherd = 0;
        for(int i = 0; i < size; ++i) {
            ItemStack stack = pContainer.getItem(i);
            if (i == whereTheSherdsAre[currSherd]) {
                if (!Helpers.isItem(stack, BetterStoneAgeTags.Items.UNFIRED_SHERDS)) {
                    return false;
                }
                ++currSherd;
            }
            else {
                if (!stack.isEmpty()) {
                    return false;
                }
            }
            
        }
        return true;
    }
    
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return pWidth >= 2 && pHeight >= 2;
    }
    
    @Override
    public @NotNull ItemStack assemble(CraftingContainer pContainer, RegistryAccess pRegistryAccess) {
        
        int size = pContainer.getContainerSize();
        int[] decorationLocations = sherdLocationsInContainerOfSize.get(size);
        
        DecoratedPotBlockEntity.Decorations decorations = new DecoratedPotBlockEntity.Decorations(
            cookedVersion(pContainer.getItem(decorationLocations[0]).getItem()), 
            cookedVersion(pContainer.getItem(decorationLocations[1]).getItem()), 
            cookedVersion(pContainer.getItem(decorationLocations[2]).getItem()), 
            cookedVersion(pContainer.getItem(decorationLocations[3]).getItem())
        );
        return createDecoratedPotItem(decorations);
    }
    
    public Item cookedVersion(Item item) {
        if (item instanceof UnfiredSherdItem unfiredSherd) {
            return unfiredSherd.getPattern().getCookedSherd();
        }
        return item;
    }
    
    public static ItemStack createDecoratedPotItem(DecoratedPotBlockEntity.Decorations pDecorations) {
        ItemStack toReturn = new ItemStack(BetterStoneAgeItems.UNFIRED_DECORATED_POT.get());
        CompoundTag compoundtag = pDecorations.save(new CompoundTag());
        BlockItem.setBlockEntityData(toReturn, BlockEntityType.DECORATED_POT, compoundtag);
        return toReturn;
    }
    
    public @NotNull RecipeSerializer<?> getSerializer() {
        return BetterStoneAgeRecipeSerializers.CRAFTING_UNFIRED_DECORATED_POT.get();
    }
}
