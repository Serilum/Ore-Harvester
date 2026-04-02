package com.natamus.oreharvester.data;

import com.mojang.logging.LogUtils;
import com.natamus.collective.functions.DataFunctions;
import org.slf4j.Logger;

import java.io.File;

public class Constants {
	public static final Logger logger = LogUtils.getLogger();

	public static final String configPath = DataFunctions.getConfigDirectory() + File.separator + "oreharvester";
	public static final File configDir = new File(configPath);
	public static final File blacklistFile = new File(configPath + File.separator + "harvestable_pickaxe_blacklist.txt");
}
