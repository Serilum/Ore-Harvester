package com.serilum.oreharvester.data;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Variables {
	public static boolean processedPickaxeBlacklist = false;
	public static boolean isHarvestingVein = false;
	public static List<Item> allowedPickaxes = new ArrayList<Item>();
	public static Set<Item> whitelistedPickaxes = new HashSet<>();
	public static Set<Block> whitelistedOreBlocks = new HashSet<>();
	public static Set<Block> blacklistedOreBlocks = new HashSet<>();

	public static final ConcurrentHashMap<Pair<Level, Player>, HarvestAttempt> harvestAttempts = new ConcurrentHashMap<>();

	public static final HashMap<Item, CopyOnWriteArrayList<BlockPos>> lastDropLocations = new HashMap<Item, CopyOnWriteArrayList<BlockPos>>();
	public static final HashMap<BlockPos, Date> lastAction = new HashMap<BlockPos, Date>();
}
