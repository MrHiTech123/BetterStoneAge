package net.mrhitech.bsa.common;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.mrhitech.bsa.BetterStoneAge;

public class BetterStoneAgeTags {
    public static class Items {
        public static final TagKey<Item> UNFIRED_SHERDS = create("ceramic/unfired_sherds");
        
        private static TagKey<Item> create(String modId, String itemId) {
            return TagKey.create(Registries.ITEM, new ResourceLocation(modId, itemId));
        }
        private static TagKey<Item> create(String id) {
            return create(BetterStoneAge.MOD_ID, id);
        }
    }
}
