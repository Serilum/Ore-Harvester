package com.natamus.oreharvester.events;

import com.mojang.datafixers.util.Pair;
import com.natamus.collective.functions.BlockFunctions;
import com.natamus.collective.functions.ItemFunctions;
import com.natamus.oreharvester.config.ConfigHandler;
import com.natamus.oreharvester.data.Variables;
import com.natamus.oreharvester.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class OreMineEvents {
	public static boolean onOreHarvest(Level level, Player player, BlockPos blockPos, BlockState blockState, BlockEntity blockEntity) {
		if (level.isClientSide) {
			return true;
		}

		Pair<Level, Player> cachePair = new Pair<Level, Player>(level, player);
		if (!Variables.harvestSpeedCache.containsKey(cachePair)) {
			if (ConfigHandler.oreHarvestWithoutSneak) {
				if (player.isCrouching()) {
					return true;
				}
			} else {
				if (!player.isCrouching()) {
					return true;
				}
			}
		}
		else {
			Variables.harvestSpeedCache.remove(cachePair);
		}

		Block block = blockState.getBlock();
		if (!Util.isOre(block)) {
			return true;
		}
		
		ItemStack handStack = player.getItemInHand(InteractionHand.MAIN_HAND);
		if (!Variables.allowedPickaxes.contains(handStack.getItem())) {
			return true;
		}

		List<BlockPos> oresAround = Util.getOresNextToEachOther(level, blockPos, block);

		int oreCount = oresAround.size();
		if (oreCount < 0) {
			return true;
		}

		if (ConfigHandler.dropOresAtFirstBrokenBlock) {
			Item oreItem = Util.getOreDrop(level, blockState, block, handStack, player, Vec3.atCenterOf(blockPos));
			if (!Variables.lastDropLocations.containsKey(oreItem)) {
				Variables.lastDropLocations.put(oreItem, new CopyOnWriteArrayList<BlockPos>());
			}

			BlockPos immutableBlockPos = blockPos.immutable();
			Variables.lastDropLocations.get(oreItem).add(immutableBlockPos);
			Variables.lastAction.put(immutableBlockPos, new Date());
		}
		
		int durabilityLoseCount = (int)Math.ceil(1.0 / ConfigHandler.loseDurabilityModifier);
		int durabilityStartCount = -1;

		ServerPlayer serverPlayer = (ServerPlayer)player;

		for (BlockPos orePos : oresAround) {
			BlockState logstate = level.getBlockState(orePos);
			Block log = logstate.getBlock();

			BlockFunctions.dropBlock(level, orePos, player, handStack);
			//ForgeEventFactory.onEntityDestroyBlock(player, orePos, logstate);

			if (!player.isCreative()) {
				if (ConfigHandler.loseDurabilityPerHarvestedOre) {
					if (durabilityStartCount == -1) {
						durabilityStartCount = durabilityLoseCount;
						ItemFunctions.itemHurtBreakAndEvent(handStack, serverPlayer, InteractionHand.MAIN_HAND, 1);
					}
					else {
						durabilityLoseCount -= 1;

						if (durabilityLoseCount == 0) {
							ItemFunctions.itemHurtBreakAndEvent(handStack, serverPlayer, InteractionHand.MAIN_HAND, 1);
							durabilityLoseCount = durabilityStartCount;
						}
					}
				}
				if (ConfigHandler.increaseExhaustionPerHarvestedOre) {
					player.causeFoodExhaustion(0.025F * (float)ConfigHandler.increaseExhaustionModifier);
				}
			}
		}

		return false;
	}

	public static float onHarvestBreakSpeed(Level level, Player player, float digSpeed, BlockState state) {
		if (!ConfigHandler.increaseHarvestingTimePerOre) {
			return digSpeed;
		}

		Block block = state.getBlock();
		if (!Util.isOre(block)) {
			return digSpeed;
		}

		if (ConfigHandler.oreHarvestWithoutSneak) {
			if (player.isCrouching()) {
				return digSpeed;
			}
		}
		else {
			if (!player.isCrouching()) {
				return digSpeed;
			}
		}

		ItemStack handStack = player.getItemInHand(InteractionHand.MAIN_HAND);
		if (!Variables.allowedPickaxes.contains(handStack.getItem())) {
			return digSpeed;
		}

		int oreCount = -1;

		Date now = new Date();
		Pair<Level, Player> keyPair = new Pair<Level, Player>(level, player);
		if (Variables.harvestSpeedCache.containsKey(keyPair)) {
			Pair<Date, Integer> valuePair = Variables.harvestSpeedCache.get(keyPair);
			long ms = (now.getTime()-valuePair.getFirst().getTime());

			if (ms < 1000) {
				oreCount = valuePair.getSecond();
			}
			else {
				Variables.harvestSpeedCache.remove(keyPair);
			}
		}

		BlockPos blockPos = null;

		HitResult hitResult = player.pick(20.0D, 0.0F, false);
		if (hitResult.getType() == HitResult.Type.BLOCK) {
			blockPos = ((BlockHitResult)hitResult).getBlockPos();
		}

		if (blockPos == null) {
			return digSpeed;
		}

		boolean recheck = false;
		if (oreCount < 0) {
			List<BlockPos> oresAround = Util.getOresNextToEachOther(level, blockPos, block);

			oreCount = oresAround.size();
			if (oreCount == 0) {
				return digSpeed;
			}

			Variables.harvestSpeedCache.put(keyPair, new Pair<Date, Integer>(now, oreCount));
			recheck = true;
		}

		return digSpeed/(1+(oreCount * (float)ConfigHandler.increasedHarvestingTimePerOreModifier));
	}
}