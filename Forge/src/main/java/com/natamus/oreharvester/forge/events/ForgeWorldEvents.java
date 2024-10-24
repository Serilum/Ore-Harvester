package com.natamus.oreharvester.forge.events;

import com.natamus.collective.functions.WorldFunctions;
import com.natamus.oreharvester.events.WorldEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class ForgeWorldEvents {
	@SubscribeEvent
	public void onWorldLoad(LevelEvent.Load e) {
		Level level = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getLevel());
		if (level == null) {
			return;
		}

		WorldEvents.onWorldLoad((ServerLevel)level);
	}

	@SubscribeEvent
	public void onScaffoldingItem(EntityJoinLevelEvent e) {
		WorldEvents.onItemEntityJoinWorld(e.getLevel(), e.getEntity());
	}
}