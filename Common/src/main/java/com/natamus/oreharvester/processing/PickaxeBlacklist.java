package com.natamus.oreharvester.processing;

import com.natamus.collective.services.Services;
import com.natamus.oreharvester.data.Constants;
import com.natamus.oreharvester.data.Variables;
import com.natamus.oreharvester.util.Reference;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class PickaxeBlacklist {
	public static void attemptProcessingPickaxeBlacklist(Level level) {
		if (!Variables.processedPickaxeBlacklist) {
			try {
				setupPickaxeBlacklist(level);
				Variables.processedPickaxeBlacklist = true;
			} catch (IOException ex) {
				Constants.logger.warn("[" + Reference.NAME + "] Something went wrong setting up the pickaxe blacklist file.");
			}
		}
	}

	public static void setupPickaxeBlacklist(Level level) throws IOException {
		Registry<Item> itemRegistry = level.registryAccess().registryOrThrow(Registries.ITEM);
		List<String> blacklist = new ArrayList<String>();

		PrintWriter writer = null;
		if (!Constants.configDir.isDirectory() || !Constants.blacklistFile.isFile()) {
			boolean ignored = Constants.configDir.mkdirs();
			writer = new PrintWriter(Constants.configPath + File.separator + "harvestable_pickaxe_blacklist.txt", StandardCharsets.UTF_8);
		}
		else {
			String blcontent = new String(Files.readAllBytes(Paths.get(Constants.configPath + File.separator + "harvestable_pickaxe_blacklist.txt")));
			for (String pickaxerl : blcontent.split("," )) {
				String name = pickaxerl.replace("\n", "").trim();
				if (name.startsWith("//")) {
					continue;
				}
				if (name.startsWith("!")) {
					blacklist.add(name.replace("!", ""));
				}
			}
		}

		if (writer != null) {
			writer.println("// To disable a certain pickaxe from being able to harvest ore veins, add an exclamation mark (!) in front of the line,");
		}

		for (Item item : itemRegistry) {
			if (Services.TOOLFUNCTIONS.isPickaxe(new ItemStack(item))) {
				ResourceLocation rl = itemRegistry.getKey(item);
				if (rl == null) {
					continue;
				}

				String name = rl.toString();

				if (writer != null) {
					writer.println(name + ",");
				}

				if (!blacklist.contains(name)) {
					Variables.allowedPickaxes.add(item);
				}
			}
		}

		if (writer != null) {
			writer.close();
		}
	}
}
