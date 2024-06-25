<h2>Ore Harvester</h2>
<p><a href="https://github.com/Serilum/Ore-Harvester"><img src="https://serilum.com/assets/data/logo/ore-harvester.png"></a></p><h2>Download</h2>
<p>You can download Ore Harvester on CurseForge and Modrinth:</p><p>&nbsp;&nbsp;CurseForge: &nbsp;&nbsp;<a href="https://curseforge.com/minecraft/mc-mods/ore-harvester">https://curseforge.com/minecraft/mc-mods/ore-harvester</a><br>&nbsp;&nbsp;Modrinth: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="https://modrinth.com/mod/ore-harvester">https://modrinth.com/mod/ore-harvester</a></p>
<h2>Issue Tracker</h2>
<p>To keep a better overview of all mods, the issue tracker is located in a separate repository.<br>&nbsp;&nbsp;For issues, ideas, suggestions or anything else, please follow this link:</p>
<p>&nbsp;&nbsp;&nbsp;&nbsp;-> <a href="https://serilum.com/url/issue-tracker">Issue Tracker</a></p>
<h2>Pull Requests</h2>
<p>Because of the way mod loader files are bundled into one jar, some extra information is needed to do a PR.<br>&nbsp;&nbsp;A wiki page entry about it is available here:</p>
<p>&nbsp;&nbsp;&nbsp;&nbsp;-> <a href="https://serilum.com/url/pull-requests">Pull Request Information</a></p>
<h2>Mod Description</h2>
<p style="text-align:center"><a href="https://serilum.com/" rel="nofollow"><img src="https://github.com/Serilum/.cdn/raw/main/description/header/header.png" alt="" width="838" height="400"></a></p>
<p style="text-align:center"><a href="https://curseforge.com/members/serilum/projects" rel="nofollow"><img src="https://raw.githubusercontent.com/Serilum/.data-workflow/main/badges/svg/curseforge.svg" width="200"></a> <a href="https://modrinth.com/user/Serilum" rel="nofollow"><img src="https://raw.githubusercontent.com/Serilum/.data-workflow/main/badges/svg/modrinth.svg" width="200"></a> <a href="https://patreon.com/serilum" rel="nofollow"><img src="https://raw.githubusercontent.com/Serilum/.data-workflow/main/badges/svg/patreon.svg" width="200"></a> <a href="https://youtube.com/@serilum" rel="nofollow"><img src="https://raw.githubusercontent.com/Serilum/.data-workflow/main/badges/svg/youtube.svg" width="200"></a></p>
<p><strong><span style="font-size:24px">Requires the library mod&nbsp;<a style="font-size:24px" href="https://curseforge.com/minecraft/mc-mods/collective" rel="nofollow">Collective</a>.</span></strong><strong>&nbsp;<br><br> &nbsp; &nbsp;This mod is part of <span style="color:#008000"><a style="color:#008000" href="https://curseforge.com/minecraft/modpacks/the-vanilla-experience" rel="nofollow">The Vanilla Experience</a></span>.</strong><br><span style="font-size:18px">Ore Harvester allows players to mine entire ore veins instantly, by breaking one of the blocks in it while crouching. The time it takes to mine is increased by the amount of ore that's in the vein. All ore items will be dropped at the initial broken block's position. It is compatible with Silk Touch and any other enchantments. Diagionally connected blocks are also broken, as long as it's visible with an air block.<br><br>The mod has various config options to edit the behaviour. There is also a blacklist file available where you can specify which pickaxes are allowed to use the mod's feature.<br><br>Developed to work alongside my <a style="font-size:18px" href="https://curseforge.com/minecraft/mc-mods/tree-harvester" rel="nofollow">Tree Harvester</a> mod.<br></span><br><br><strong><span style="font-size:20px">Configurable:</span> <span style="color:#008000;font-size:14px"><a style="color:#008000" href="https://github.com/Serilum/.information/wiki/how-to-configure-mods" rel="nofollow">(&nbsp;how do I configure?&nbsp;)</a></span><br></strong><span style="font-size:12px"><strong>oreHarvestWithoutSneak</strong>&nbsp;(default = false): If enabled, ore harvesting works when not holding the sneak button. If disabled it's reversed, and only works when sneaking.</span><br><span style="font-size:12px"><strong>dropOresAtFirstBrokenBlock</strong>&nbsp;(default = true): Whether all ore drops in a mined vein should drop at the first broken block's position.<br></span><strong>enableFuzzyOreSearch</strong> (default = true): With fuzzy search enabled, it uses a block's raw name to determine whether it's an ore block. Improves compatibility with other mods that have not set a block tag, but does create a small chance of a false positive.<br><span style="font-size:12px"><strong>loseDurabilityPerHarvestedOre</strong>&nbsp;(default = true): If enabled, for every ore harvested, the held pickaxe loses durability.</span><br><span style="font-size:12px"><strong>loseDurabilityModifier</strong>&nbsp;(default = 1.0, min 0.001, max 1.0): Here you can set how much durability harvesting an ore vein should take from the pickaxe. For example if set to 0.1, this means that every 10 ore take 1 durability.</span><br><span style="font-size:12px"><strong>increaseExhaustionPerHarvestedOre</strong>&nbsp;(default = true): If enabled, players' exhaustion level increases 0.005 per harvested ore (Minecraft's default per broken block) * increaseExhaustionModifier.</span><br><span style="font-size:12px"><strong>increaseExhaustionModifier</strong>&nbsp;(default = 1.0, min 0.001, max 1.0): This determines how much exhaustion should be added to the player per harvested ore. By default 0.005 * 1.0.</span><br><span style="font-size:12px"><strong>increaseHarvestingTimePerOre</strong>&nbsp;(default = true): If enabled, harvesting time will increase per existing ore in a vein. The amount is determined by 'increasedHarvestingTimePerOreModifier'.</span><br><span style="font-size:12px"><strong>increasedHarvestingTimePerOreModifier</strong>&nbsp;(default = 0.2, min 0.01, max 10.0): How much longer it takes to harvest an ore vein with 'increaseHarvestingTimePerOre' enabled. The actual speed is: newSpeed = originalSpeed / (1 + (logCount * increasedHarvestingTimePerOreModifier)).</span><br><br></p>
<p><br><span style="font-size:24px"><strong>Pickaxe Blacklist Feature:</strong></span><br>It's possible to only allow certain pickaxes to harvest ore veins. On first load of the mod a blacklist file is generated. It is located at <strong><em>./config/oreharvester/harvestable_pickaxe_blacklist.txt</em></strong>. This works for modded pickaxe items as well.<br><picture><img src="https://github.com/Serilum/.cdn/raw/main/projects/ore-harvester/a.png" width="994" height="622"></picture><br><br><br><span style="font-size:36px"><strong>Some GIFs:</strong></span><br><span style="font-size:14px">Mine any ore in a vein, and all blocks in it are broken:</span></p>
<div class="spoiler">
<p><picture><img src="https://github.com/Serilum/.cdn/raw/main/projects/ore-harvester/b.gif"></picture></p>
</div>
<p>&nbsp;<br><span style="font-size:14px">Compatible with Silk Touch and any other enchantments:</span></p>
<div class="spoiler">
<p><picture><img src="https://github.com/Serilum/.cdn/raw/main/projects/ore-harvester/c.gif"></picture></p>
</div>
<p>&nbsp;<br>------------------<br><br><span style="font-size:24px"><strong>You may freely use this mod in any modpack, as long as the download remains hosted within the CurseForge or Modrinth ecosystem.</strong></span><br><br><span style="font-size:18px"><a style="font-size:18px;color:#008000" href="https://serilum.com/" rel="nofollow">Serilum.com</a> contains an overview and more information on all mods available.</span><br><br><span style="font-size:14px">Comments are disabled as I'm unable to keep track of all the separate pages on each mod.</span><span style="font-size:14px"><br>For issues, ideas, suggestions or anything else there is the&nbsp;<a style="font-size:14px;color:#008000" href="https://github.com/Serilum/.issue-tracker" rel="nofollow">Github repo</a>. Thanks!</span><span style="font-size:6px"><br><br></span></p>
<p style="text-align:center"><a href="https://serilum.com/donate" rel="nofollow"><img src="https://github.com/Serilum/.cdn/raw/main/description/projects/support.svg" alt="" width="320"></a></p>