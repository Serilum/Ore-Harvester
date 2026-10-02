package com.serilum.oreharvester;

import com.natamus.collective.fabric.callbacks.CollectiveClientEvents;
import com.serilum.oreharvester.events.WorldEvents;
import net.fabricmc.api.ClientModInitializer;
import com.serilum.oreharvester.util.Reference;
import com.natamus.collective.check.ShouldLoadCheck;
import net.minecraft.client.multiplayer.ClientLevel;

public class ModFabricClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() { 
		if (!ShouldLoadCheck.shouldLoad(Reference.MOD_ID)) {
			return;
		}

		registerEvents();
	}
	
	private void registerEvents() {
		CollectiveClientEvents.CLIENT_WORLD_LOAD.register((ClientLevel clientLevel) -> {
			WorldEvents.onWorldLoad(clientLevel);
		});
	}
}
