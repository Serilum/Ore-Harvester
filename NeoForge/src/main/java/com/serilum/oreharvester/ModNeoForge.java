package com.serilum.oreharvester;

import com.natamus.collective.check.RegisterMod;
import com.natamus.collective.check.ShouldLoadCheck;
import com.serilum.oreharvester.neoforge.config.IntegrateNeoForgeConfig;
import com.serilum.oreharvester.neoforge.events.NeoForgeOreMineEvents;
import com.serilum.oreharvester.neoforge.events.NeoForgeWorldEvents;
import com.serilum.oreharvester.util.Reference;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod(Reference.MOD_ID)
public class ModNeoForge {
	
	public ModNeoForge(IEventBus modEventBus) {
		if (!ShouldLoadCheck.shouldLoad(Reference.MOD_ID)) {
			return;
		}

		modEventBus.addListener(this::loadComplete);

		setGlobalConstants();
		ModCommon.init();

		IntegrateNeoForgeConfig.registerScreen(ModLoadingContext.get());

		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}

	private void loadComplete(final FMLLoadCompleteEvent event) {
		NeoForge.EVENT_BUS.register(NeoForgeOreMineEvents.class);
		NeoForge.EVENT_BUS.register(NeoForgeWorldEvents.class);
	}

	private static void setGlobalConstants() {

	}
}