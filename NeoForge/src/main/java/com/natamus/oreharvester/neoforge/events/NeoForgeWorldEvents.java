package com.natamus.oreharvester.neoforge.events;

import com.natamus.collective.functions.WorldFunctions;
import com.natamus.oreharvester.events.WorldEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.level.LevelEvent;

@EventBusSubscriber
public class NeoForgeWorldEvents {
	@SubscribeEvent
	public static void onScaffoldingItem(EntityJoinLevelEvent e) {
		WorldEvents.onItemEntityJoinWorld(e.getLevel(), e.getEntity());
	}

	@SubscribeEvent
	public static void onWorldLoad(LevelEvent.Load e) {
		Level level = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getLevel());
		if (level == null) {
			return;
		}

		WorldEvents.onWorldLoad((ServerLevel)level);
	}
}