package com.natamus.oreharvester.config;

import com.natamus.collective.config.DuskConfig;
import com.natamus.oreharvester.util.Reference;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ConfigHandler extends DuskConfig {
	public static HashMap<String, List<String>> configMetaData = new HashMap<String, List<String>>();

	@Entry public static boolean oreHarvestWithoutSneak = false;
	@Entry public static boolean dropOresAtFirstBrokenBlock = true;
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