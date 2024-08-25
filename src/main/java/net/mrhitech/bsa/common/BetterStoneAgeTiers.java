package net.mrhitech.bsa.common;

import net.dries007.tfc.common.TFCTiers;

import java.util.List;
import net.dries007.tfc.common.TFCTags.Blocks;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.ToolTier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.TierSortingRegistry;
import org.jetbrains.annotations.Nullable;

public class BetterStoneAgeTiers {
    private static Tier register(String name, Tier before, @Nullable Tier after, TagKey<Block> tag, int level, int uses, float speed, float damage, int enchantmentValue) {
        return register(name, List.of(before), after == null ? List.of() : List.of(after), tag, level, uses, speed, damage, enchantmentValue);
    }
    
    private static Tier register(String name, List<Object> before, List<Object> after, TagKey<Block> tag, int level, int uses, float speed, float damage, int enchantmentValue) {
        Tier tier = new ToolTier(name, level, uses, speed, damage, enchantmentValue, tag, () -> {
            return Ingredient.EMPTY;
        });
        if (!Helpers.BOOTSTRAP_ENVIRONMENT) {
            TierSortingRegistry.registerTier(tier, Helpers.identifier(name), before, after);
        }
        
        return tier;
    }
    
    public static final Tier FLINT = register("flint", TFCTiers.IGNEOUS_EXTRUSIVE, TFCTiers.BISMUTH_BRONZE, Blocks.NEEDS_STONE_TOOL, 0, 120, 4.9f, 2.5f, 5);
}
