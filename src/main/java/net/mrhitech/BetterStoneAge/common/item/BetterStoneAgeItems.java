package net.mrhitech.BetterStoneAge.common.item;

import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.config.TFCConfig;
import net.dries007.tfc.util.Helpers;
import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.mrhitech.BetterStoneAge.BetterStoneAge;
import net.dries007.tfc.common.blocks.rock.RockCategory;

import java.util.Map;
import net.dries007.tfc.common.items.JugItem;
import net.mrhitech.BetterStoneAge.common.blocks.BetterStoneAgeBlocks;

public class BetterStoneAgeItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, BetterStoneAge.MOD_ID);

    public static final Map<DyeColor, RegistryObject<Item>> UNFIRED_CERAMIC_JUGS = Helpers.mapOfKeys(DyeColor.class, color ->
            ITEMS.register("ceramic/jug/unfired/" + color, () -> new Item(new Item.Properties())));
    public static final Map<DyeColor, RegistryObject<Item>> GLAZED_CERAMIC_JUGS = Helpers.mapOfKeys(DyeColor.class, color ->
            ITEMS.register("ceramic/jug/glazed/" + color, () -> new JugItem(new Item.Properties(), TFCConfig.SERVER.jugCapacity, TFCTags.Fluids.USABLE_IN_JUG)));
    public static final Map<DyeColor, RegistryObject<Item>> GLAZED_POTS = Helpers.mapOfKeys(DyeColor.class, color ->
            ITEMS.register("ceramic/pot/glazed/" + color, () -> new PotItem(new Item.Properties(), BetterStoneAgeBlocks.GLAZED_POTS.get(color).get())));
    public static final Map<RockCategory, RegistryObject<Item>> MULTITOOL_HEADS = Helpers.mapOfKeys(RockCategory.class, rock_category ->
            ITEMS.register("stone/multitool_head/" + rock_category.toString().toLowerCase(), () -> new Item(new Item.Properties())));

    public static final RegistryObject<Item> SINEW = ITEMS.register("sinew", () -> new BlockItem(BetterStoneAgeBlocks.SINEW.get(), new Item.Properties()));
    public static final RegistryObject<Item> DRIED_SINEW = ITEMS.register("dried_sinew", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> POUNDED_SINEW = ITEMS.register("pounded_sinew", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SINEW_STRING = ITEMS.register("sinew_string", () -> new Item(new Item.Properties()));


    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }

}
