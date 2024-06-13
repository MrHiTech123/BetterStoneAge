package net.mrhitech.BetterStoneAge.common.item;

import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.TFCTiers;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.items.*;
import net.dries007.tfc.config.TFCConfig;
import net.dries007.tfc.util.Helpers;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Tier;
import net.dries007.tfc.util.Metal;
import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.mrhitech.BetterStoneAge.BetterStoneAge;
import net.dries007.tfc.common.blocks.rock.RockCategory;

import java.util.Map;

import net.mrhitech.BetterStoneAge.common.BetterStoneAgeTiers;
import net.mrhitech.BetterStoneAge.common.blocks.BetterStoneAgeBlocks;
import net.mrhitech.BetterStoneAge.common.food.BetterStoneAgeFoods;

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


    public static final RegistryObject<Item> CRUSHED_BARLEY_GRAIN = ITEMS.register("food/crushed_barley_grain", () -> new Item(new Item.Properties().food(BetterStoneAgeFoods.CRUSHED_BARLEY_GRAIN)));
    public static final RegistryObject<Item> CRUSHED_OAT_GRAIN = ITEMS.register("food/crushed_oat_grain", () -> new Item(new Item.Properties().food(BetterStoneAgeFoods.CRUSHED_OAT_GRAIN)));
    public static final RegistryObject<Item> CRUSHED_RYE_GRAIN = ITEMS.register("food/crushed_rye_grain", () -> new Item(new Item.Properties().food(BetterStoneAgeFoods.CRUSHED_RYE_GRAIN)));
    public static final RegistryObject<Item> CRUSHED_WHEAT_GRAIN = ITEMS.register("food/crushed_wheat_grain", () -> new Item(new Item.Properties().food(BetterStoneAgeFoods.CRUSHED_WHEAT_GRAIN)));

    public static final RegistryObject<Item> COARSE_BARLEY_FLOUR = ITEMS.register("food/coarse_barley_flour", () -> new Item(new Item.Properties().food(BetterStoneAgeFoods.COARSE_BARLEY_FLOUR)));
    public static final RegistryObject<Item> COARSE_MAIZE_FLOUR = ITEMS.register("food/coarse_maize_flour", () -> new Item(new Item.Properties().food(BetterStoneAgeFoods.COARSE_MAIZE_FLOUR)));
    public static final RegistryObject<Item> COARSE_OAT_FLOUR = ITEMS.register("food/coarse_oat_flour", () -> new Item(new Item.Properties().food(BetterStoneAgeFoods.COARSE_OAT_FLOUR)));
    public static final RegistryObject<Item> COARSE_RICE_FLOUR = ITEMS.register("food/coarse_rice_flour", () -> new Item(new Item.Properties().food(BetterStoneAgeFoods.COARSE_RICE_FLOUR)));
    public static final RegistryObject<Item> COARSE_RYE_FLOUR = ITEMS.register("food/coarse_rye_flour", () -> new Item(new Item.Properties().food(BetterStoneAgeFoods.COARSE_RYE_FLOUR)));
    public static final RegistryObject<Item> COARSE_WHEAT_FLOUR = ITEMS.register("food/coarse_wheat_flour", () -> new Item(new Item.Properties().food(BetterStoneAgeFoods.COARSE_WHEAT_FLOUR)));

    public static final RegistryObject<Item> PORRIDGE = ITEMS.register("food/porridge", () -> new Item(new Item.Properties().food(BetterStoneAgeFoods.PORRIDGE)));

    public static final RegistryObject<Item> SINEW = ITEMS.register("sinew", () -> new BlockItem(BetterStoneAgeBlocks.SINEW.get(), new Item.Properties()));
    public static final RegistryObject<Item> DRIED_SINEW = ITEMS.register("dried_sinew", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> POUNDED_SINEW = ITEMS.register("pounded_sinew", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SINEW_STRING = ITEMS.register("sinew_string", () -> new Item(new Item.Properties()));
    
    public static final RegistryObject<Item> BONE_FISHING_ROD = ITEMS.register("bone/fishing_rod", () -> new TFCFishingRodItem(new Item.Properties().rarity(Rarity.COMMON).defaultDurability(20), TFCTiers.SEDIMENTARY));
    public static final RegistryObject<Item> BONE_FISH_HOOK = ITEMS.register("bone/fish_hook", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SABERTOOTH_FANG = ITEMS.register("sabertooth_fang", () -> new Item(new Item.Properties()));
    
    public static final RegistryObject<Item> FLINT_AXE_HEAD = ITEMS.register("stone/axe_head/flint", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FLINT_HAMMER_HEAD = ITEMS.register("stone/hammer_head/flint", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FLINT_HOE_HEAD = ITEMS.register("stone/hoe_head/flint", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FLINT_JAVELIN_HEAD = ITEMS.register("stone/javelin_head/flint", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FLINT_KNIFE_HEAD = ITEMS.register("stone/knife_head/flint", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FLINT_SHOVEL_HEAD = ITEMS.register("stone/shovel_head/flint", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FLINT_MULTITOOL_HEAD = ITEMS.register("stone/multitool_head/flint", () -> new Item(new Item.Properties()));
    
    public static final RegistryObject<Item> FLINT_AXE = ITEMS.register("stone/axe/flint", () -> new AxeItem(BetterStoneAgeTiers.FLINT, ToolItem.calculateVanillaAttackDamage(1.5F, BetterStoneAgeTiers.FLINT), -3.2F, new Item.Properties()));
    public static final RegistryObject<Item> FLINT_HAMMER = ITEMS.register("stone/hammer/flint", () -> new HammerItem(BetterStoneAgeTiers.FLINT, ToolItem.calculateVanillaAttackDamage(1F, BetterStoneAgeTiers.FLINT), -3.0F, new Item.Properties()));
    public static final RegistryObject<Item> FLINT_HOE = ITEMS.register("stone/hoe/flint", () -> new HoeItem(BetterStoneAgeTiers.FLINT, -1, -3.0F, new Item.Properties()));
    public static final RegistryObject<Item> FLINT_JAVELIN = ITEMS.register("stone/javelin/flint", () -> new JavelinItem(BetterStoneAgeTiers.FLINT, ToolItem.calculateVanillaAttackDamage(0.7F, BetterStoneAgeTiers.FLINT), 1.5F * BetterStoneAgeTiers.FLINT.getAttackDamageBonus(), -2.2F, new Item.Properties(), "flint"));
    public static final RegistryObject<Item> FLINT_KNIFE = ITEMS.register("stone/knife/flint", () -> new ToolItem(BetterStoneAgeTiers.FLINT, ToolItem.calculateVanillaAttackDamage(0.6F, BetterStoneAgeTiers.FLINT), -2.0F, TFCTags.Blocks.MINEABLE_WITH_KNIFE, new Item.Properties()));
    public static final RegistryObject<Item> FLINT_SHOVEL = ITEMS.register("stone/shovel/flint", () -> new ShovelItem(BetterStoneAgeTiers.FLINT, ToolItem.calculateVanillaAttackDamage(0.0875F, BetterStoneAgeTiers.FLINT), -3.0F, new Item.Properties()));
    
    
    
    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }

}
