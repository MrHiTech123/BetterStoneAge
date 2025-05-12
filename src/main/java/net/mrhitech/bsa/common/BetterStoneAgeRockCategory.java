package net.mrhitech.bsa.common;

import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.items.HammerItem;
import net.dries007.tfc.common.items.JavelinItem;
import net.dries007.tfc.common.items.TFCHoeItem;
import net.dries007.tfc.common.items.ToolItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.Tier;

import java.util.Locale;
import java.util.function.Function;

public enum BetterStoneAgeRockCategory {
    FLINT(BetterStoneAgeTiers.FLINT),
    AMETHYST(BetterStoneAgeTiers.AMETHYST),
    EMERALD(BetterStoneAgeTiers.EMERALD),
    KIMBERLITE(BetterStoneAgeTiers.KIMBERLITE),
    OPAL(BetterStoneAgeTiers.OPAL),
    PYRITE(BetterStoneAgeTiers.PYRITE),
    RUBY(BetterStoneAgeTiers.RUBY),
    SAPPHIRE(BetterStoneAgeTiers.SAPPHIRE),
    TOPAZ(BetterStoneAgeTiers.TOPAZ),
    OBSIDIAN(BetterStoneAgeTiers.OBSIDIAN);
    
    private final Tier tier;
    
    BetterStoneAgeRockCategory(Tier tier) {
        this.tier = tier;
    }
    
    public Tier getTier() {
        return tier;
    }
    
    public String getSerializedName() {
        return name().toLowerCase(Locale.ROOT);
    }
    
    public enum ItemType
    {
        AXE(rock -> new AxeItem(rock.getTier(), ToolItem.calculateVanillaAttackDamage(1.5F, rock.getTier()), -3.2F, properties())),
        AXE_HEAD,
        HAMMER(rock -> new HammerItem(rock.getTier(), ToolItem.calculateVanillaAttackDamage(1f, rock.getTier()), -3.0F, properties())),
        HAMMER_HEAD,
        HOE(rock -> new TFCHoeItem(rock.getTier(), -1, -3.0f, properties())),
        HOE_HEAD,
        JAVELIN(rock -> new JavelinItem(rock.getTier(), ToolItem.calculateVanillaAttackDamage(0.7f, rock.getTier()), 1.5f * rock.getTier().getAttackDamageBonus(), -2.2F, properties(), "stone")),
        JAVELIN_HEAD,
        KNIFE(rock -> new ToolItem(rock.getTier(), ToolItem.calculateVanillaAttackDamage(0.6f, rock.getTier()), -2.0F, TFCTags.Blocks.MINEABLE_WITH_KNIFE, properties())),
        KNIFE_HEAD,
        SHOVEL(rock -> new ShovelItem(rock.getTier(), ToolItem.calculateVanillaAttackDamage(0.875F, rock.getTier()), -3.0F, properties())),
        SHOVEL_HEAD,
        MULTITOOL_HEAD;

        public static Item.Properties properties()
        {
            return new Item.Properties();
        }

        private final Function<BetterStoneAgeRockCategory, Item> itemFactory;

        ItemType()
        {
            this(rock -> new Item(properties()));
        }

        ItemType(Function<BetterStoneAgeRockCategory, Item> itemFactory)
        {
            this.itemFactory = itemFactory;
        }

        public Item create(BetterStoneAgeRockCategory category)
        {
            return itemFactory.apply(category);
        }
        
        public String getSerializedName() {
            return name().toLowerCase(Locale.ROOT);
        }
    }
}
