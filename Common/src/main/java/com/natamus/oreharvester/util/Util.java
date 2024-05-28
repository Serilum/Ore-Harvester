package com.natamus.oreharvester.util;

import com.natamus.collective.functions.BlockFunctions;
import com.natamus.collective.functions.BlockPosFunctions;
import com.natamus.collective.services.Services;
import com.natamus.oreharvester.config.ConfigHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Util {
	public static boolean isOre(BlockState blockState) {
		return isOre(blockState, blockState.getBlock());
	}
	public static boolean isOre(Block block) {
		return isOre(block.defaultBlockState(), block);
	}
	public static boolean isOre(BlockState blockState, Block block) {
		return Services.BLOCKTAGS.isOre(blockState, ConfigHandler.enableFuzzyOreSearch) || block.equals(Blocks.ANCIENT_DEBRIS);
	}

	public static @Nullable Item getOreDrop(Level level, BlockState blockState, Block block, ItemStack itemStack, Player player, Vec3 origin) {
		List<ItemStack> possibleDrops = BlockFunctions.getBlockDrops(level, blockState, itemStack, player, origin);
		if (possibleDrops.size() == 0) {
			return null;
		}

		return possibleDrops.get(0).getItem();
	}


	private static final HashMap<BlockPos, Integer> rgnbcount = new HashMap<BlockPos, Integer>();

	public static List<BlockPos> getOresNextToEachOther(Level level, BlockPos startPos, Block block) {
		return getBlocksNextToEachOther(level, startPos, block, 20);
	}

    public static List<BlockPos> getBlocksNextToEachOther(Level level, BlockPos startPos, Block block, int maxDistance) {
        List<BlockPos> checkedBlocks = new ArrayList<BlockPos>();
        List<BlockPos> blocksAround = new ArrayList<BlockPos>();
        if (level.getBlockState(startPos).getBlock().equals(block)) {
            blocksAround.add(startPos);
            checkedBlocks.add(startPos);
        }

        rgnbcount.put(startPos.immutable(), 0);
        recursiveGetNextBlocks(level, startPos, startPos, block, blocksAround, checkedBlocks, maxDistance);
        return blocksAround;
    }

    private static void recursiveGetNextBlocks(Level level, BlockPos startPos, BlockPos pos, Block block, List<BlockPos> blocksAround, List<BlockPos> checkedBlocks, int maxDistance) {

        int rgnbc = rgnbcount.get(startPos);
        if (rgnbc <= 100) {
            rgnbcount.put(startPos, rgnbc + 1);

			for (BlockPos pba : BlockPos.betweenClosed(pos.getX()-1, pos.getY()-1, pos.getZ()-1, pos.getX()+1, pos.getY()+1, pos.getZ()+1)) {
				pba = pba.immutable();
				if (!checkedBlocks.contains(pba)) {
					checkedBlocks.add(pba);
					if (level.getBlockState(pba).getBlock().equals(block) && !blocksAround.contains(pba)) {
						boolean isAllowed = false;

						List<BlockPos> connected = BlockPosFunctions.getBlocksAround(pba, true);
						for (BlockPos cp : connected) {
							Block cpBlock = level.getBlockState(cp).getBlock();
							if (cpBlock.equals(block) || cpBlock.equals(Blocks.AIR)) {
								isAllowed = true;
								break;
							}
						}

						if (!isAllowed) {
							continue;
						}

						blocksAround.add(pba);
						if (BlockPosFunctions.withinDistance(startPos, pba, maxDistance)) {
							recursiveGetNextBlocks(level, startPos, pba, block, blocksAround, checkedBlocks, maxDistance);
						}
					}
				}
			}

        }
    }
}