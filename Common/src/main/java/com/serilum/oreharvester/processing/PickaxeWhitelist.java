package com.serilum.oreharvester.processing;

import com.natamus.collective.functions.ConfigFunctions;
import com.serilum.oreharvester.config.ConfigHandler;
import com.serilum.oreharvester.data.Variables;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

import java.util.HashSet;
import java.util.Set;

public class PickaxeWhitelist {
	public static void processPickaxeWhitelist(Level level) {
		Registry<Item> itemRegistry = level.registryAccess().registryOrThrow(Registries.ITEM);

		Set<Item> whitelistedPickaxes = new HashSet<>();
		for (Item item : itemRegistry) {
			ResourceLocation rl = itemRegistry.getKey(item);
			if (rl == null) {
				continue;
			}

			if (ConfigFunctions.isInIdList(ConfigHandler.pickaxeWhitelist, rl.toString())) {
				whitelistedPickaxes.add(item);
			}
		}

		Variables.whitelistedPickaxes = whitelistedPickaxes;
	}
}
