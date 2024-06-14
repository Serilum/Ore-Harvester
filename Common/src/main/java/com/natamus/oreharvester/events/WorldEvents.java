package com.natamus.oreharvester.events;

import com.natamus.oreharvester.config.ConfigHandler;
import com.natamus.oreharvester.data.Variables;
import com.natamus.oreharvester.processing.PickaxeBlacklist;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

import java.util.Date;

public class WorldEvents {
	public static void onWorldLoad(Level level) {
		PickaxeBlacklist.attemptProcessingPickaxeBlacklist(level);
	}

	public static void onItemEntityJoinWorld(Level world, Entity entity) {
		if (!ConfigHandler.dropOresAtFirstBrokenBlock) {
			return;
		}

		if (world.isClientSide) {
			return;
		}

		if (!(entity instanceof ItemEntity)) {
			return;
		}

		ItemEntity ie = (ItemEntity)entity;
		Item item = ie.getItem().getItem();
		if (!Variables.lastDropLocations.containsKey(item)) {
			return;
		}

		Date now = new Date();

		BlockPos blockPos = entity.blockPosition();
		BlockPos lowBlockPos = new BlockPos(blockPos.getX(), 1, blockPos.getZ());
		for (BlockPos lastDropPos : Variables.lastDropLocations.get(item)) {
			if (Variables.lastAction.containsKey(lastDropPos)) {
				Date lastdate = Variables.lastAction.get(lastDropPos);
				long ms = (now.getTime()-lastdate.getTime());
				if (ms > 2000) {
					Variables.lastDropLocations.get(item).remove(lastDropPos);
					Variables.lastAction.remove(lastDropPos);
					continue;
				}
			}

			if (lowBlockPos.closerThan(new BlockPos(lastDropPos.getX(), 1, lastDropPos.getZ()), 20)) {
				entity.teleportTo(lastDropPos.getX(), lastDropPos.getY()+1, lastDropPos.getZ());
				Variables.lastAction.put(lastDropPos.immutable(), now);
			}
		}
	}
}