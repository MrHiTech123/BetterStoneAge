package net.mrhitech.bsa.common.item;

import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.TFCTiers;
import net.dries007.tfc.common.items.*;
import net.dries007.tfc.config.TFCConfig;
import net.dries007.tfc.util.Helpers;
import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.mrhitech.bsa.BetterStoneAge;
import net.dries007.tfc.common.blocks.rock.RockCategory;

import java.util.Map;
import java.util.function.Supplier;

import net.mrhitech.bsa.common.BetterStoneAgeRockCategory;
import net.mrhitech.bsa.common.SherdPattern;
import net.mrhitech.bsa.common.blocks.BetterStoneAgeBlocks;
import net.mrhitech.bsa.common.food.BetterStoneAgeFoods;

public class BetterStoneAgeItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, BetterStoneAge.MOD_ID);

    public static final Map<DyeColor, RegistryObject<Item>> UNFIRED_JUGS = Helpers.mapOfKeys(DyeColor.class, color ->
            register("ceramic/jug/unfired/" + color, () -> new Item(new Item.Properties())));
    public static final Map<DyeColor, RegistryObject<Item>> GLAZED_JUGS = Helpers.mapOfKeys(DyeColor.class, color ->
            register("ceramic/jug/glazed/" + color, () -> new JugItem(new Item.Properties().stacksTo(1), TFCConfig.SERVER.jugCapacity, TFCTags.Fluids.USABLE_IN_JUG)));
    public static final Map<DyeColor, RegistryObject<Item>> UNFIRED_POTS = Helpers.mapOfKeys(DyeColor.class, color -> 
            register("ceramic/pot/unfired/" + color, () -> new Item(new Item.Properties())));
    public static final Map<DyeColor, RegistryObject<Item>> GLAZED_POTS = Helpers.mapOfKeys(DyeColor.class, color ->
            register("ceramic/pot/glazed/" + color, () -> new PotItem(new Item.Properties(), BetterStoneAgeBlocks.GLAZED_POTS.get(color).get())));
    public static final Map<RockCategory, RegistryObject<Item>> MULTITOOL_HEADS = Helpers.mapOfKeys(RockCategory.class, rock_category ->
            register("stone/multitool_head/" + rock_category.toString().toLowerCase(), () -> new Item(new Item.Properties())));


    public static final RegistryObject<Item> CRUSHED_BARLEY_GRAIN = register("food/crushed_barley_grain", () -> new Item(new Item.Properties().food(BetterStoneAgeFoods.CRUSHED_BARLEY_GRAIN)));
    public static final RegistryObject<Item> CRUSHED_OAT_GRAIN = register("food/crushed_oat_grain", () -> new Item(new Item.Properties().food(BetterStoneAgeFoods.CRUSHED_OAT_GRAIN)));
    public static final RegistryObject<Item> CRUSHED_RYE_GRAIN = register("food/crushed_rye_grain", () -> new Item(new Item.Properties().food(BetterStoneAgeFoods.CRUSHED_RYE_GRAIN)));
    public static final RegistryObject<Item> CRUSHED_WHEAT_GRAIN = register("food/crushed_wheat_grain", () -> new Item(new Item.Properties().food(BetterStoneAgeFoods.CRUSHED_WHEAT_GRAIN)));

    public static final RegistryObject<Item> COARSE_BARLEY_FLOUR = register("food/coarse_barley_flour", () -> new Item(new Item.Properties().food(BetterStoneAgeFoods.COARSE_BARLEY_FLOUR)));
    public static final RegistryObject<Item> COARSE_MAIZE_FLOUR = register("food/coarse_maize_flour", () -> new Item(new Item.Properties().food(BetterStoneAgeFoods.COARSE_MAIZE_FLOUR)));
    public static final RegistryObject<Item> COARSE_OAT_FLOUR = register("food/coarse_oat_flour", () -> new Item(new Item.Properties().food(BetterStoneAgeFoods.COARSE_OAT_FLOUR)));
    public static final RegistryObject<Item> COARSE_RICE_FLOUR = register("food/coarse_rice_flour", () -> new Item(new Item.Properties().food(BetterStoneAgeFoods.COARSE_RICE_FLOUR)));
    public static final RegistryObject<Item> COARSE_RYE_FLOUR = register("food/coarse_rye_flour", () -> new Item(new Item.Properties().food(BetterStoneAgeFoods.COARSE_RYE_FLOUR)));
    public static final RegistryObject<Item> COARSE_WHEAT_FLOUR = register("food/coarse_wheat_flour", () -> new Item(new Item.Properties().food(BetterStoneAgeFoods.COARSE_WHEAT_FLOUR)));

    public static final RegistryObject<Item> PORRIDGE = register("food/porridge", () -> new Item(new Item.Properties().food(BetterStoneAgeFoods.PORRIDGE)));

    public static final RegistryObject<Item> SINEW = register("sinew", () -> new BlockItem(BetterStoneAgeBlocks.SINEW.get(), new Item.Properties()));
    public static final RegistryObject<Item> DRIED_SINEW = register("dried_sinew", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> POUNDED_SINEW = register("pounded_sinew", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SINEW_STRING = register("sinew_string", () -> new Item(new Item.Properties()));
    
    public static final RegistryObject<Item> BONE_FISHING_ROD = register("bone/fishing_rod", () -> new TFCFishingRodItem(new Item.Properties().rarity(Rarity.COMMON).defaultDurability(20), TFCTiers.SEDIMENTARY));
    public static final RegistryObject<Item> BONE_FISH_HOOK = register("bone/fish_hook", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SABERTOOTH_FANG = register("sabertooth_fang", () -> new Item(new Item.Properties()));
    
    public static final Map<BetterStoneAgeRockCategory, Map<BetterStoneAgeRockCategory.ItemType, RegistryObject<Item>>> STONE = Helpers.mapOfKeys(BetterStoneAgeRockCategory.class, rockCategory ->
        Helpers.mapOfKeys(BetterStoneAgeRockCategory.ItemType.class, itemType -> 
            register("stone/" + itemType.getSerializedName() + "/" + rockCategory.getSerializedName(),
                    () -> itemType.create(rockCategory)
            )
        )
    );
    
    public static final RegistryObject<Item> STONE_ARROWHEAD = register("stone/arrowhead", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FLINT_ARROWHEAD = register("stone/arrowhead/flint", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BONE_ARROWHEAD = register("bone/arrowhead", () -> new Item(new Item.Properties()));
    
    public static final RegistryObject<Item> HIDE_DOOR = register("hide_door", () -> new DoubleHighBlockItem(BetterStoneAgeBlocks.HIDE_DOOR.get(), new Item.Properties()));
    
    public static final RegistryObject<Item> CLAY_DUST = register("dust/clay", () -> new Item(new Item.Properties()));
    
    public static final RegistryObject<Item> UNFIRED_DECORATED_POT = register("ceramic/unfired_decorated_pot", () -> new UnfiredDecoratedPotItem(new Item.Properties()));
    public static final Map<SherdPattern, RegistryObject<Item>> UNFIRED_SHERDS = Helpers.mapOfKeys(SherdPattern.class, (pattern) -> 
        register("ceramic/sherd/unfired/" + pattern.getSerializedName(), () -> new Item(new Item.Properties())));
    public static final RegistryObject<Item> FIRED_BLANK_SHERD = register("ceramic/sherd/fired/blank", () -> new Item(new Item.Properties()));
    
    public static final RegistryObject<Item> OBSIDIAN = register("obsidian", () -> new Item(new Item.Properties()));
    
    public static <T extends Item> RegistryObject<T> register(String name, Supplier<T> supplier) {
        return ITEMS.register(name, supplier);
    }
    
    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }

}
