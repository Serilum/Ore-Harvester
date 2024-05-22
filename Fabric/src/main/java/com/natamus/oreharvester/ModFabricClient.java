package com.natamus.oreharvester;

import com.natamus.collective.fabric.callbacks.CollectiveClientEvents;
import com.natamus.oreharvester.events.WorldEvents;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.multiplayer.ClientLevel;

public class ModFabricClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() { 
		registerEvents();
	}
	
	private void registerEvents() {
		CollectiveClientEvents.CLIENT_WORLD_LOAD.register((ClientLevel clientLevel) -> {
			WorldEvents.onWorldLoad(clientLevel);
		});
	}
}
