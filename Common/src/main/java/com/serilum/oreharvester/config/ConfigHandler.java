package com.serilum.oreharvester.config;

import com.natamus.collective.config.DuskConfig;
import com.serilum.oreharvester.util.Reference;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ConfigHandler extends DuskConfig {
	public static HashMap<String, List<String>> configMetaData = new HashMap<String, List<String>>();

	@Entry public static boolean oreHarvestWithoutSneak = false;
	@Entry public static boolean dropOresAtFirstBrokenBlock = true;
	@Entry public static boolean enableFuzzyOreSearch = true;
	@Entry public static String oreBlockWhitelist = "";
	@Entry public static String oreBlockBlacklist = "";
	@Entry public static String pickaxeWhitelist = "easy_emerald:*_paxel,piercingpaxels:*_paxel,randomisfits:*_hammer,randomisfits:*_multitool,tetra:modular_double";
	@Entry(min = 1, max = 10000) public static int maxOresPerVein = 1000;
	@Entry public static boolean triggerBlockBreakEventPerHarvestedOre = true;
	@Entry public static boolean loseDurabilityPerHarvestedOre = true;
	@Entry(min = 0.001, max = 1.0) public static double loseDurabilityModifier = 1.0;
	@Entry public static boolean increaseExhaustionPerHarvestedOre = true;
	@Entry(min = 0.001, max = 1.0) public static double increaseExhaustionModifier = 1.0;
	@Entry public static boolean increaseHarvestingTimePerOre = true;
	@Entry(min = 0.01, max = 10.0) public static double increasedHarvestingTimePerOreModifier = 0.2;

	public static void initConfig() {
		configMetaData.put("oreHarvestWithoutSneak", Arrays.asList(
			"If enabled, ore harvesting works when not holding the sneak button. If disabled it's reversed, and only works when sneaking."
		));
		configMetaData.put("dropOresAtFirstBrokenBlock", Arrays.asList(
			"Whether all ore drops in a mined vein should drop at the first broken block's position."
		));
		configMetaData.put("enableFuzzyOreSearch", Arrays.asList(
			"With fuzzy search enabled, it uses a block's raw name to determine whether it's an ore block. Improves compatibility with other mods that have not set a block tag, but does create a small chance of a false positive."
		));
		configMetaData.put("oreBlockWhitelist", Arrays.asList(
			"A comma separated list of block ids that are harvested as ore veins too, also when they're not detected as an ore. Use * as a wildcard. An id without a namespace matches blocks of every mod. For example 'minecraft:glowstone,create:*crimsite'. Applies after rejoining the world."
		));
		configMetaData.put("oreBlockBlacklist", Arrays.asList(
			"A comma separated list of block ids that are never harvested as ore veins, also when they're detected as an ore. Same format as oreBlockWhitelist, for example 'minecraft:ancient_debris,*redstone_ore'. Applies after rejoining the world."
		));
		configMetaData.put("pickaxeWhitelist", Arrays.asList(
			"A comma separated list of item ids that can harvest ore veins too, for modded tools that are not detected as a pickaxe, like paxels and multitools. A listed tool still needs to be able to mine the ore. Use * as a wildcard. An id without a namespace matches items of every mod. Applies after rejoining the world."
		));
		configMetaData.put("maxOresPerVein", Arrays.asList(
			"The maximum amount of ore blocks that are harvested at once in a single vein, including the broken block."
		));
		configMetaData.put("triggerBlockBreakEventPerHarvestedOre", Arrays.asList(
			"If enabled, every harvested ore in a vein triggers a block break event, as if broken by hand. This lets other mods see each ore, like skill mods that give experience for mining or claim mods that protect an area. An ore is skipped when another mod cancels its event."
		));
		configMetaData.put("loseDurabilityPerHarvestedOre", Arrays.asList(
			"If enabled, for every ore harvested, the held pickaxe loses durability."
		));
		configMetaData.put("loseDurabilityModifier", Arrays.asList(
			"Here you can set how much durability harvesting an ore vein should take from the pickaxe. For example if set to 0.1, this means that every 10 ore take 1 durability."
		));
		configMetaData.put("increaseExhaustionPerHarvestedOre", Arrays.asList(
			"If enabled, players' exhaustion level increases 0.005 per harvested ore (Minecraft's default per broken block) * increaseExhaustionModifier."
		));
		configMetaData.put("increaseExhaustionModifier", Arrays.asList(
			"This determines how much exhaustion should be added to the player per harvested ore. By default 0.005 * 1.0."
		));
		configMetaData.put("increaseHarvestingTimePerOre", Arrays.asList(
			"If enabled, harvesting time will increase per existing ore in a vein. The amount is determined by 'increasedHarvestingTimePerOreModifier'."
		));
		configMetaData.put("increasedHarvestingTimePerOreModifier", Arrays.asList(
			"How much longer it takes to harvest an ore vein with 'increaseHarvestingTimePerOre' enabled. The actual speed is: newSpeed = originalSpeed / (1 + (logCount * increasedHarvestingTimePerOreModifier))."
		));

		DuskConfig.init(Reference.NAME, Reference.MOD_ID, ConfigHandler.class);
	}
}