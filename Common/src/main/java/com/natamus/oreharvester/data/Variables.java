package com.natamus.oreharvester.data;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Variables {
    public static boolean processedPickaxeBlacklist = false;
	public static List<Item> allowedPickaxes = new ArrayList<Item>();

	public static final HashMap<Pair<Level, Player>, Pair<Date, Integer>> harvestSpeedCache = new HashMap<Pair<Level, Player>, Pair<Date, Integer>>();

	public static final HashMap<Item, CopyOnWriteArrayList<BlockPos>> lastDropLocations = new HashMap<Item, CopyOnWriteArrayList<BlockPos>>();
	public static final HashMap<BlockPos, Date> lastAction = new HashMap<BlockPos, Date>();
}
