package com.natamus.oreharvester;

import com.natamus.collective.check.RegisterMod;
import com.natamus.collective.fabric.callbacks.CollectivePlayerEvents;
import com.natamus.oreharvester.events.OreMineEvents;
import com.natamus.oreharvester.events.WorldEvents;
import com.natamus.oreharvester.util.Reference;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class ModFabric implements ModInitializer {

	@Override
	public void onInitialize() {
		setGlobalConstants();
		ModCommon.init();

		loadEvents();

		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}

	private void loadEvents() {
		ServerWorldEvents.LOAD.register((MinecraftServer server, ServerLevel level) -> {
			WorldEvents.onWorldLoad(level);
		});

		PlayerBlockBreakEvents.BEFORE.register((level, player, pos, state, entity) -> {
			return OreMineEvents.onOreHarvest(level, player, pos, state, entity);
		});

		CollectivePlayerEvents.ON_PLAYER_DIG_SPEED_CALC.register((Level level, Player player, float digSpeed, BlockState state) -> {
			return OreMineEvents.onHarvestBreakSpeed(level, player, digSpeed, state);
		});

		ServerEntityEvents.ENTITY_LOAD.register((Entity entity, ServerLevel serverLevel) -> {
			WorldEvents.onItemEntityJoinWorld(serverLevel, entity);
		});
	}

	private static void setGlobalConstants() {

	}
}
