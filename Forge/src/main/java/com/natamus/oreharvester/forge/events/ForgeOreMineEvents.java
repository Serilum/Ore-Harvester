package com.natamus.oreharvester.forge.events;

import com.natamus.collective.functions.WorldFunctions;
import com.natamus.oreharvester.events.OreMineEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class ForgeOreMineEvents {
	@SubscribeEvent
	public void onOreHarvest(BlockEvent.BreakEvent e) {
		Level level = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getLevel());
		if (level == null) {
			return;
		}
		
		OreMineEvents.onOreHarvest(level, e.getPlayer(), e.getPos(), e.getState(), null);
	}

	@SubscribeEvent
	public void onHarvestBreakSpeed(PlayerEvent.BreakSpeed e) {
		Player player = e.getEntity();
		Level level = player.level;

		float originalSpeed = e.getOriginalSpeed();
		float newSpeed = OreMineEvents.onHarvestBreakSpeed(level, player, originalSpeed, e.getState());

		if (originalSpeed != newSpeed) {
			e.setNewSpeed(newSpeed);
		}
	}
}