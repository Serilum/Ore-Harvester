package com.serilum.oreharvester.events;

import com.mojang.datafixers.util.Pair;
import com.natamus.collective.functions.BlockFunctions;
import com.natamus.collective.functions.ItemFunctions;
import com.natamus.collective.services.Services;
import com.serilum.oreharvester.config.ConfigHandler;
import com.serilum.oreharvester.data.HarvestAttempt;
import com.serilum.oreharvester.data.Variables;
import com.serilum.oreharvester.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
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
		if (level.isClientSide || Variables.isHarvestingVein) {
			return true;
		}

		Pair<Level, Player> attemptKey = new Pair<Level, Player>(level, player);
		HarvestAttempt attempt = Variables.harvestAttempts.remove(attemptKey);

		boolean isOreHarvest;
		if (attempt != null && attempt.pos.equals(blockPos) && isRecent(attempt, player)) {
			isOreHarvest = attempt.isOreHarvest;
		}
		else {
			isOreHarvest = Util.wantsToHarvestVein(player);
		}

		if (!isOreHarvest) {
			return true;
		}

		Block block = blockState.getBlock();
		if (!Util.isOre(block)) {
			return true;
		}

		if (!Util.canHarvestVeinWith(player, blockState)) {
			return true;
		}

		ItemStack handStack = player.getItemInHand(InteractionHand.MAIN_HAND);
		List<BlockPos> oresAround = Util.getOresNextToEachOther(level, blockPos, block);

		int oreCount = oresAround.size();
		if (oreCount < 0) {
			return true;
		}

		if (ConfigHandler.dropOresAtFirstBrokenBlock) {
			BlockPos immutableBlockPos = blockPos.immutable();
			for (BlockState oreState : Util.getDistinctBlockStates(level, oresAround)) {
				for (Item oreItem : Util.getOreDropItems(level, oreState, handStack, player, Vec3.atCenterOf(blockPos))) {
					if (!Variables.lastDropLocations.containsKey(oreItem)) {
						Variables.lastDropLocations.put(oreItem, new CopyOnWriteArrayList<BlockPos>());
					}

					CopyOnWriteArrayList<BlockPos> dropLocations = Variables.lastDropLocations.get(oreItem);
					if (!dropLocations.contains(immutableBlockPos)) {
						dropLocations.add(immutableBlockPos);
					}
				}
			}
			Variables.lastAction.put(immutableBlockPos, new Date());
		}

		Variables.isHarvestingVein = true;
		try {
			if (!isBreakAllowed(level, player, blockPos, blockState)) {
				return false;
			}

			harvestOres(level, (ServerPlayer)player, blockPos, oresAround, handStack);
		}
		finally {
			Variables.isHarvestingVein = false;
		}

		return false;
	}

	private static void harvestOres(Level level, ServerPlayer serverPlayer, BlockPos firstOrePos, List<BlockPos> oresAround, ItemStack handStack) {
		int durabilityLoseCount = (int)Math.ceil(1.0 / ConfigHandler.loseDurabilityModifier);
		int durabilityStartCount = -1;

		for (BlockPos orePos : oresAround) {
			if (!level.mayInteract(serverPlayer, orePos)) {
				continue;
			}

			BlockState oreState = level.getBlockState(orePos);
			if (!orePos.equals(firstOrePos) && !isBreakAllowed(level, serverPlayer, orePos, oreState)) {
				continue;
			}

			if (!level.getBlockState(orePos).equals(oreState)) {
				continue;
			}

			if (serverPlayer.isCreative()) {
				level.removeBlock(orePos, false);
			}
			else {
				BlockFunctions.dropBlock(level, orePos, serverPlayer, handStack);
			}

			if (ConfigHandler.triggerBlockBreakEventPerHarvestedOre) {
				Services.EVENTTRIGGER.triggerAfterBlockBreakEvent(level, serverPlayer, orePos, oreState, null);
			}

			if (!serverPlayer.isCreative()) {
				serverPlayer.awardStat(Stats.BLOCK_MINED.get(oreState.getBlock()));

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
					serverPlayer.causeFoodExhaustion(0.025F * (float)ConfigHandler.increaseExhaustionModifier);
				}
			}

			if (handStack.isEmpty()) {
				break;
			}
		}
	}

	private static boolean isBreakAllowed(Level level, Player player, BlockPos pos, BlockState state) {
		if (!ConfigHandler.triggerBlockBreakEventPerHarvestedOre) {
			return true;
		}
		return Services.EVENTTRIGGER.triggerBlockBreakEvent(level, player, pos, state);
	}

	public static float onHarvestBreakSpeed(Level level, Player player, float digSpeed, BlockState state) {
		if (!ConfigHandler.increaseHarvestingTimePerOre) {
			return digSpeed;
		}

		Block block = state.getBlock();
		if (!Util.isOre(block)) {
			return digSpeed;
		}

		if (!Util.canHarvestVeinWith(player, state)) {
			return digSpeed;
		}

		BlockPos blockPos = null;

		HitResult hitResult = player.pick(20.0D, 0.0F, false);
		if (hitResult.getType() == HitResult.Type.BLOCK) {
			blockPos = ((BlockHitResult)hitResult).getBlockPos();
		}

		if (blockPos == null) {
			return digSpeed;
		}

		HarvestAttempt attempt = getOrStartHarvestAttempt(level, player, blockPos, block);
		if (!attempt.isOreHarvest || attempt.oreCount <= 0) {
			return digSpeed;
		}

		return digSpeed/(1+(attempt.oreCount * (float)ConfigHandler.increasedHarvestingTimePerOreModifier));
	}

	private static HarvestAttempt getOrStartHarvestAttempt(Level level, Player player, BlockPos blockPos, Block block) {
		Pair<Level, Player> attemptKey = new Pair<Level, Player>(level, player);

		HarvestAttempt attempt = Variables.harvestAttempts.get(attemptKey);
		if (attempt != null && attempt.pos.equals(blockPos) && isRecent(attempt, player)) {
			attempt.lastSeenTick = player.tickCount;
			return attempt;
		}

		boolean isOreHarvest = Util.wantsToHarvestVein(player);

		int oreCount = -1;
		if (isOreHarvest) {
			oreCount = Util.getOresNextToEachOther(level, blockPos, block).size();
		}

		attempt = new HarvestAttempt(blockPos.immutable(), isOreHarvest, oreCount, player.tickCount);
		Variables.harvestAttempts.keySet().removeIf(key -> key.getSecond().isRemoved());
		Variables.harvestAttempts.put(attemptKey, attempt);
		return attempt;
	}

	private static boolean isRecent(HarvestAttempt attempt, Player player) {
		return player.tickCount - attempt.lastSeenTick <= 2;
	}
}