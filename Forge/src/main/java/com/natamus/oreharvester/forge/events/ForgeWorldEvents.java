package com.natamus.oreharvester.forge.events;

import com.natamus.collective.functions.WorldFunctions;
import com.natamus.oreharvester.events.WorldEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.bus.BusGroup;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;

import java.lang.invoke.MethodHandles;

public class ForgeWorldEvents {
	public static void registerEventsInBus() {
		BusGroup.DEFAULT.register(MethodHandles.lookup(), ForgeWorldEvents.class);
	}

	@SubscribeEvent
	public static void onWorldLoad(LevelEvent.Load e) {
		Level level = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getLevel());
		if (level == null) {
			return;
		}

		WorldEvents.onWorldLoad((ServerLevel)level);
	}

	@SubscribeEvent
	public static void onScaffoldingItem(EntityJoinLevelEvent e) {
		WorldEvents.onItemEntityJoinWorld(e.getLevel(), e.getEntity());
	}
}