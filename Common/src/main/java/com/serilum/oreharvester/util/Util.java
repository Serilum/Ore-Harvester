package com.serilum.oreharvester.util;

import com.natamus.collective.functions.BlockFunctions;
import com.natamus.collective.functions.BlockPosFunctions;
import com.natamus.collective.services.Services;
import com.serilum.oreharvester.config.ConfigHandler;
import com.serilum.oreharvester.data.Variables;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

public class Util {
	public static boolean isOre(BlockState blockState) {
		return isOre(blockState, blockState.getBlock());
	}
	public static boolean isOre(Block block) {
		return isOre(block.defaultBlockState(), block);
	}
	public static boolean isOre(BlockState blockState, Block block) {
		if (blockState.hasBlockEntity() || Variables.blacklistedOreBlocks.contains(block)) {
			return false;
		}
		if (Variables.whitelistedOreBlocks.contains(block)) {
			return true;
		}
		return Services.BLOCKTAGS.isOre(blockState, ConfigHandler.enableFuzzyOreSearch) || block.equals(Blocks.ANCIENT_DEBRIS);
	}

	public static boolean isSameOre(Level level, Block block, Block otherBlock) {
		if (block.equals(otherBlock)) {
			return true;
		}
		if (!getOreTypeName(level, block).equals(getOreTypeName(level, otherBlock))) {
			return false;
		}
		return isOre(otherBlock);
	}

	private static String getOreTypeName(Level level, Block block) {
		Registry<Block> blockRegistry = level.registryAccess().registryOrThrow(Registries.BLOCK);
		return blockRegistry.getKey(block).toString().replace("deepslate_", "");
	}

	public static List<BlockState> getDistinctBlockStates(Level level, List<BlockPos> blockPositions) {
		List<BlockState> distinctBlockStates = new ArrayList<>();
		for (BlockPos blockPos : blockPositions) {
			BlockState blockState = level.getBlockState(blockPos);
			if (!distinctBlockStates.contains(blockState)) {
				distinctBlockStates.add(blockState);
			}
		}
		return distinctBlockStates;
	}

	public static boolean wantsToHarvestVein(Player player) {
		if (ConfigHandler.oreHarvestWithoutSneak) {
			return !player.isCrouching();
		}
		return player.isCrouching();
	}

	public static boolean isAllowedPickaxe(Item item) {
		return Variables.allowedPickaxes.contains(item) || Variables.whitelistedPickaxes.contains(item);
	}

	public static boolean canHarvestVeinWith(Player player, BlockState blockState) {
		ItemStack handStack = player.getItemInHand(InteractionHand.MAIN_HAND);
		if (!isAllowedPickaxe(handStack.getItem())) {
			return false;
		}
		if (!blockState.requiresCorrectToolForDrops()) {
			return true;
		}
		return handStack.isCorrectToolForDrops(blockState);
	}

	public static boolean isInIdList(String idList, String id) {
		String idPath = id.substring(id.indexOf(':') + 1);

		for (String rawEntry : idList.split(",")) {
			String entry = rawEntry.strip().toLowerCase(Locale.ROOT);
			if (entry.isEmpty()) {
				continue;
			}

			String comparedId = idPath;
			if (entry.contains(":")) {
				comparedId = id;
			}

			if (comparedId.matches(wildcardToRegex(entry))) {
				return true;
			}
		}
		return false;
	}

	private static String wildcardToRegex(String entry) {
		return Pattern.quote(entry).replace("*", "\\E.*\\Q");
	}

	public static List<Item> getOreDropItems(Level level, BlockState blockState, ItemStack itemStack, Player player, Vec3 origin) {
		List<Item> oreDropItems = new ArrayList<>();
		for (ItemStack dropStack : BlockFunctions.getBlockDrops(level, blockState, itemStack, player, origin)) {
			if (!oreDropItems.contains(dropStack.getItem())) {
				oreDropItems.add(dropStack.getItem());
			}
		}
		return oreDropItems;
	}


	public static List<BlockPos> getOresNextToEachOther(Level level, BlockPos startPos, Block block) {
		return getBlocksNextToEachOther(level, startPos, block, 20, ConfigHandler.maxOresPerVein);
	}

	public static List<BlockPos> getBlocksNextToEachOther(Level level, BlockPos startPos, Block block, int maxDistance, int maxBlocks) {
		Set<BlockPos> checkedBlocks = new HashSet<>();
		List<BlockPos> blocksAround = new ArrayList<BlockPos>();
		if (isSameOre(level, level.getBlockState(startPos).getBlock(), block)) {
			blocksAround.add(startPos);
			checkedBlocks.add(startPos);
		}

		HashMap<BlockPos, Integer> rgnbcount = new HashMap<>();
		rgnbcount.put(startPos.immutable(), 0);
		recursiveGetNextBlocks(level, startPos, startPos, block, blocksAround, checkedBlocks, rgnbcount, maxDistance, maxBlocks);
		return blocksAround;
	}

	private static void recursiveGetNextBlocks(Level level, BlockPos startPos, BlockPos pos, Block block, List<BlockPos> blocksAround, Set<BlockPos> checkedBlocks, HashMap<BlockPos, Integer> rgnbcount, int maxDistance, int maxBlocks) {

		int rgnbc = rgnbcount.get(startPos);
		if (rgnbc <= 100) {
			rgnbcount.put(startPos, rgnbc + 1);

			for (BlockPos pba : BlockPos.betweenClosed(pos.getX()-1, pos.getY()-1, pos.getZ()-1, pos.getX()+1, pos.getY()+1, pos.getZ()+1)) {
				pba = pba.immutable();
				if (!checkedBlocks.contains(pba)) {
					checkedBlocks.add(pba);
					if (isSameOre(level, level.getBlockState(pba).getBlock(), block) && !blocksAround.contains(pba)) {
						boolean isAllowed = false;

						List<BlockPos> connected = BlockPosFunctions.getBlocksAround(pba, true);
						for (BlockPos cp : connected) {
							BlockState cpState = level.getBlockState(cp);
							if (isSameOre(level, cpState.getBlock(), block) || cpState.isAir()) {
								isAllowed = true;
								break;
							}
						}

						if (!isAllowed) {
							continue;
						}

						if (blocksAround.size() >= maxBlocks) {
							return;
						}

						blocksAround.add(pba);
						if (BlockPosFunctions.withinDistance(startPos, pba, maxDistance)) {
							recursiveGetNextBlocks(level, startPos, pba, block, blocksAround, checkedBlocks, rgnbcount, maxDistance, maxBlocks);
						}
					}
				}
			}

		}
	}
}