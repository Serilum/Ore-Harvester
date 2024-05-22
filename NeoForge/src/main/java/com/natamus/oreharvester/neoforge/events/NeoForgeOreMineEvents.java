package com.natamus.oreharvester.neoforge.events;

import com.natamus.collective.functions.WorldFunctions;
import com.natamus.oreharvester.events.OreMineEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

@Mod.EventBusSubscriber
public class NeoForgeOreMineEvents {
	@SubscribeEvent
	public static void onOreHarvest(BlockEvent.BreakEvent e) {
		Level level = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getLevel());
		if (level == null) {
			return;
		}
		
		OreMineEvents.onOreHarvest(level, e.getPlayer(), e.getPos(), e.getState(), null);
	}

	@SubscribeEvent
	public static void onHarvestBreakSpeed(PlayerEvent.BreakSpeed e) {
		Player player = e.getEntity();
		Level level = player.level();

		float originalSpeed = e.getOriginalSpeed();
		float newSpeed = OreMineEvents.onHarvestBreakSpeed(level, player, originalSpeed, e.getState());

		if (originalSpeed != newSpeed) {
			e.setNewSpeed(newSpeed);
		}
	}
}