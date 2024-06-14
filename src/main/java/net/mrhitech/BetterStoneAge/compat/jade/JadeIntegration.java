package net.mrhitech.BetterStoneAge.compat.jade;

import net.dries007.tfc.compat.jade.common.BlockEntityTooltip;
import net.dries007.tfc.compat.jade.common.EntityTooltip;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Block;
import net.mrhitech.BetterStoneAge.compat.jade.common.BlockEntityTooltips;
import snownee.jade.api.*;
import snownee.jade.api.config.IPluginConfig;

@WailaPlugin
public class JadeIntegration implements IWailaPlugin {
    
    @Override
    public void registerClient(IWailaClientRegistration registry) {
        System.out.println("Registering tooltips for blockentities");
        BlockEntityTooltips.register((name, tooltip, block) -> register(registry, name, tooltip, block));
    }
    
    private static void register(IWailaClientRegistration registry, ResourceLocation name, BlockEntityTooltip blockEntityTooltip, Class<? extends Block> block)
    {
        registry.registerBlockComponent(new IBlockComponentProvider() {
            @Override
            public void appendTooltip(ITooltip tooltip, BlockAccessor access, IPluginConfig config)
            {
                blockEntityTooltip.display(access.getLevel(), access.getBlockState(), access.getPosition(), access.getBlockEntity(), tooltip::add);
            }
            
            @Override
            public ResourceLocation getUid()
            {
                return name;
            }
        }, block);
    }
    
    private static void register(IWailaClientRegistration registry, ResourceLocation name, EntityTooltip entityTooltip, Class<? extends Entity> entityClass)
    {
        registry.registerEntityComponent(new IEntityComponentProvider() {
            @Override
            public void appendTooltip(ITooltip tooltip, EntityAccessor access, IPluginConfig config)
            {
                entityTooltip.display(access.getLevel(), access.getEntity(), tooltip::add);
            }
            
            @Override
            public ResourceLocation getUid()
            {
                return name;
            }
        }, entityClass);
    }
}
