package com.serilum.oreharvester.processing;

import com.serilum.oreharvester.config.ConfigHandler;
import com.serilum.oreharvester.data.Variables;
import com.serilum.oreharvester.util.Util;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.HashSet;
import java.util.Set;

public class OreBlockLists {
	public static void processOreBlockLists(Level level) {
		Registry<Block> blockRegistry = level.registryAccess().lookupOrThrow(Registries.BLOCK);

		Set<Block> whitelistedOreBlocks = new HashSet<>();
		Set<Block> blacklistedOreBlocks = new HashSet<>();
		for (Block block : blockRegistry) {
			Identifier rl = blockRegistry.getKey(block);
			if (rl == null) {
				continue;
			}

			String blockId = rl.toString();
			if (Util.isInIdList(ConfigHandler.oreBlockWhitelist, blockId)) {
				whitelistedOreBlocks.add(block);
			}
			if (Util.isInIdList(ConfigHandler.oreBlockBlacklist, blockId)) {
				blacklistedOreBlocks.add(block);
			}
		}

		Variables.whitelistedOreBlocks = whitelistedOreBlocks;
		Variables.blacklistedOreBlocks = blacklistedOreBlocks;
	}
}
