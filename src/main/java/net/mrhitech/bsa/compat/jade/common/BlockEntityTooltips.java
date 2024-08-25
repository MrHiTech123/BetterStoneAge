package net.mrhitech.bsa.compat.jade.common;

import net.dries007.tfc.common.blockentities.TickCounterBlockEntity;
import net.dries007.tfc.compat.jade.common.BlockEntityTooltip;
import net.dries007.tfc.compat.jade.common.RegisterCallback;
import net.dries007.tfc.util.calendar.Calendars;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.mrhitech.bsa.common.blocks.devices.DryingSinewBlock;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class BlockEntityTooltips {
    public static void register(RegisterCallback<BlockEntityTooltip, Block> callback) {
        System.out.println("Registering sinew");
        callback.register("drying_sinew", DRYING_SINEW, DryingSinewBlock.class);
    }
    
    public static final BlockEntityTooltip DRYING_SINEW = (level, state, pos, entity, tooltip) -> {
        if (entity instanceof TickCounterBlockEntity counter && state.getBlock() instanceof DryingSinewBlock) {
            if (state.getValue(DryingSinewBlock.DRIED)) {
                tooltip.accept(Component.translatable("tfc.jade.dried_mud_bricks"));
            }
            else {
                if (level.isRainingAt(entity.getBlockPos().above())) {
                    tooltip.accept(Component.translatable("tfc.jade.raining_mud_bricks"));
                }
                else {
                    timeLeft(level, tooltip, DryingSinewBlock.ticks - counter.getTicksSinceUpdate(), Component.translatable("tfc.jade.mud_bricks_nearly_done"));
                }
            }
        }
        
        System.out.println("Getting sinew tooltip");
        
    };
    
    public static void timeLeft(Level level, Consumer<Component> tooltip, long ticks)
    {
        timeLeft(level, tooltip, ticks, null);
    }
    
    public static void timeLeft(Level level, Consumer<Component> tooltip, long ticks, @Nullable Component ifNegative)
    {
        if (ticks > 0)
        {
            tooltip.accept(Component.translatable("tfc.jade.time_left", Calendars.get(level).getTimeDelta(ticks)));
        }
        else if (ifNegative != null)
        {
            tooltip.accept(ifNegative);
        }
    }
}
