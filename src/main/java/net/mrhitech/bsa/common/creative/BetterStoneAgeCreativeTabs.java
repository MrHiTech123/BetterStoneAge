package net.mrhitech.bsa.common.creative;

import net.dries007.tfc.common.TFCCreativeTabs;
import net.dries007.tfc.common.blocks.rock.RockCategory;
import net.minecraft.world.item.DyeColor;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.mrhitech.bsa.common.BetterStoneAgeRockCategory;
import net.mrhitech.bsa.common.SherdPattern;
import net.mrhitech.bsa.common.item.BetterStoneAgeItems;

public class BetterStoneAgeCreativeTabs {
    public static void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == TFCCreativeTabs.EARTH.tab().getKey()) {
            for (DyeColor color : DyeColor.values()) {
                event.accept(BetterStoneAgeItems.UNFIRED_JUGS.get(color));
                event.accept(BetterStoneAgeItems.GLAZED_JUGS.get(color));
                event.accept(BetterStoneAgeItems.UNFIRED_POTS.get(color));
                event.accept(BetterStoneAgeItems.GLAZED_POTS.get(color));
            }
            event.accept(BetterStoneAgeItems.BONE_FISH_HOOK);
            event.accept(BetterStoneAgeItems.BONE_FISHING_ROD);
            event.accept(BetterStoneAgeItems.BONE_ARROWHEAD);
            event.accept(BetterStoneAgeItems.SABERTOOTH_FANG);
            
            event.accept(BetterStoneAgeItems.CLAY_DUST);
            
            event.accept(BetterStoneAgeItems.UNFIRED_DECORATED_POT);
            for (SherdPattern pattern : SherdPattern.values()) {
                event.accept(BetterStoneAgeItems.UNFIRED_SHERDS.get(pattern));
            }
            event.accept(BetterStoneAgeItems.FIRED_BLANK_SHERD);
            event.accept(BetterStoneAgeItems.OBSIDIAN);
            
        }
        if (event.getTabKey() == TFCCreativeTabs.WOOD.tab().getKey()) {
            event.accept(BetterStoneAgeItems.HIDE_DOOR);
        }
        if (event.getTabKey() == TFCCreativeTabs.MISC.tab().getKey()) {
            event.accept(BetterStoneAgeItems.SINEW);
            event.accept(BetterStoneAgeItems.DRIED_SINEW);
            event.accept(BetterStoneAgeItems.POUNDED_SINEW);
            event.accept(BetterStoneAgeItems.SINEW_STRING);
        }
        if (event.getTabKey() == TFCCreativeTabs.FOOD.tab().getKey()) {
            event.accept(BetterStoneAgeItems.CRUSHED_BARLEY_GRAIN);
            event.accept(BetterStoneAgeItems.CRUSHED_OAT_GRAIN);
            event.accept(BetterStoneAgeItems.CRUSHED_RYE_GRAIN);
            event.accept(BetterStoneAgeItems.CRUSHED_WHEAT_GRAIN);
            
            event.accept(BetterStoneAgeItems.COARSE_BARLEY_FLOUR);
            event.accept(BetterStoneAgeItems.COARSE_MAIZE_FLOUR);
            event.accept(BetterStoneAgeItems.COARSE_OAT_FLOUR);
            event.accept(BetterStoneAgeItems.COARSE_RICE_FLOUR);
            event.accept(BetterStoneAgeItems.COARSE_RYE_FLOUR);
            event.accept(BetterStoneAgeItems.COARSE_WHEAT_FLOUR);
            
            event.accept(BetterStoneAgeItems.PORRIDGE);
            
        }
        if (event.getTabKey() == TFCCreativeTabs.ROCKS.tab().getKey()) {
            for (RockCategory rock_category : RockCategory.values()) {
                event.accept(BetterStoneAgeItems.MULTITOOL_HEADS.get(rock_category));
            }
            
            for (BetterStoneAgeRockCategory category : BetterStoneAgeRockCategory.values()) {
                for (BetterStoneAgeRockCategory.ItemType type : BetterStoneAgeRockCategory.ItemType.values()) {
                    event.accept(BetterStoneAgeItems.STONE.get(category).get(type));
                }
            }
            
            event.accept(BetterStoneAgeItems.STONE_ARROWHEAD);
            event.accept(BetterStoneAgeItems.FLINT_ARROWHEAD);
            
            
        }
    }
}
