package net.mrhitech.bsa.common.recipes.outputs;

import net.dries007.tfc.common.recipes.outputs.ItemStackModifier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public enum CopySherdsModifier implements ItemStackModifier.SingleInstance<CopySherdsModifier> {
    INSTANCE;
    
    public ItemStack apply(ItemStack stack, ItemStack input) {
        
        if (stack.getTag() == null) {
            stack.setTag(new CompoundTag());
        }
        CompoundTag stackTag = stack.getTag();
        
        if (input.getTag() != null && input.getTag().contains("BlockEntityTag")) {
            stackTag.put("BlockEntityTag", input.getTag().getCompound("BlockEntityTag"));
        }
        
        
        return stack;
    }
    
    public CopySherdsModifier instance() {
        return INSTANCE;
    }
    
}
