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
    public static final Tier FLINT = register("flint", TFCTiers.IGNEOUS_EXTRUSIVE, TFCTiers.BISMUTH_BRONZE, Blocks.NEEDS_STONE_TOOL, 0, 120, 4.9f, 2.5f, 5);
    
    public static final Tier AMETHYST = register("amethyst", TFCTiers.IGNEOUS_EXTRUSIVE, FLINT, Blocks.NEEDS_STONE_TOOL, 0, 75, 4.7f, 2.0f, 5);
    public static final Tier EMERALD = register("emerald", TFCTiers.IGNEOUS_EXTRUSIVE, FLINT, Blocks.NEEDS_STONE_TOOL, 0, 65, 4.4f, 2.0f, 5);
    public static final Tier KIMBERLITE = register("kimberlite", TFCTiers.IGNEOUS_EXTRUSIVE, FLINT, Blocks.NEEDS_STONE_TOOL, 0, 70, 4.4f, 2.0f, 5);
    public static final Tier OPAL = register("opal", TFCTiers.IGNEOUS_EXTRUSIVE, FLINT, Blocks.NEEDS_STONE_TOOL, 0, 80, 4.7f, 2.0f, 5);
    public static final Tier PYRITE = register("pyrite", TFCTiers.IGNEOUS_EXTRUSIVE, FLINT, Blocks.NEEDS_STONE_TOOL, 0, 60, 4.4f, 2.0f, 5);
    public static final Tier RUBY = register("ruby", TFCTiers.IGNEOUS_EXTRUSIVE, FLINT, Blocks.NEEDS_STONE_TOOL, 0, 65, 4.4f, 2.0f, 5);
    public static final Tier SAPPHIRE = register("sapphire", TFCTiers.IGNEOUS_EXTRUSIVE, FLINT, Blocks.NEEDS_STONE_TOOL, 0, 65, 4.4f, 2.0f, 5);
    public static final Tier TOPAZ = register("topaz", TFCTiers.IGNEOUS_EXTRUSIVE, FLINT, Blocks.NEEDS_STONE_TOOL, 0, 65, 4.4f, 2.0f, 5);
    public static final Tier OBSIDIAN = register("obsidian", TFCTiers.IGNEOUS_EXTRUSIVE, FLINT, Blocks.NEEDS_STONE_TOOL, 0, 36, 13.0f, 4.0f, 5);
    
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
}
